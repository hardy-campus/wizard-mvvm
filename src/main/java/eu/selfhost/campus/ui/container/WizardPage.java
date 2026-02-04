package eu.selfhost.campus.ui.container;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.GridLayout.Alignment;
import com.googlecode.lanterna.gui2.Interactable;
import com.googlecode.lanterna.gui2.LayoutData;
import com.googlecode.lanterna.gui2.Panel;

import eu.selfhost.campus.ui.api.IModifyable;
import eu.selfhost.campus.ui.api.IRefreshable;
import eu.selfhost.campus.ui.api.IValidatable;

public class WizardPage extends Panel {

    public static Consumer<String> convertBoolean(Consumer<Boolean> booleanConsumer) {
        return (input) -> {
            booleanConsumer.accept(input != null && input.equalsIgnoreCase("true"));
        };
    }

    public static Supplier<String> convertBoolean(Supplier<Boolean> booleanSupplier) {
        return () -> {
            Boolean value = booleanSupplier.get();
            return value != null && value ? "true" : "false";
        };
    }

    public static <E extends Enum<E>> Consumer<String> convertEnum(Class<E> clazz,
            Consumer<E> enumConsumer) {
        return (input) -> {
            enumConsumer.accept(input == null ? null : toEnum(clazz, input));
        };
    }

    public static <E extends Enum<E>> Supplier<String> convertEnum(Supplier<E> enumSupplier) {
        return () -> {
            E _enum = enumSupplier.get();
            return _enum == null ? "" : _enum.toString();
        };
    }

    public static Consumer<String> convertFile(Consumer<File> fileConsumer) {
        return (input) -> {
            fileConsumer.accept(input == null || input.isEmpty() ? null : new File(input));
        };
    }

    public static Supplier<String> convertFile(Supplier<File> fileSupplier) {
        return () -> {
            File file = fileSupplier.get();
            return file == null ? null : file.toString();
        };
    }

    public static Consumer<String> convertInt(Consumer<Integer> intConsumer) {
        return (input) -> {
            intConsumer.accept(input == null || input.isEmpty() ? 0 : Integer.parseInt(input));
        };
    }

    public static Supplier<String> convertInt(Supplier<Integer> intSupplier) {
        return () -> {
            Integer value = intSupplier.get();
            return value == null ? null : "" + value;
        };
    }

    protected static <E extends Enum<E>> E toEnum(Class<E> enumClass, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Enum.valueOf(enumClass, value);
    }

    protected final IValidatable bean;

    String description;

    List<IRefreshable> fields = new ArrayList<>();

    Consumer<List<String>> onErrors;

    String title;

    public WizardPage(GridLayout gridLayout, IValidatable bean) {
        super(gridLayout);
        this.bean = bean;
    }

    protected void addField(IModifyable field) {
        fields.add(field);
        field.setOnModified(f -> {
            refresh();
        });
    }

    protected void addField(IRefreshable field) {
        fields.add(field);
    }

    public void commitValues() {
        // implement in derived class
    }

    protected LayoutData createComboBoxLayout() {
        return GridLayout.createLayoutData(Alignment.BEGINNING, Alignment.CENTER);
    }

    protected EmptySpace createGreedySpace() {
        return new EmptySpace(TerminalSize.ONE)
                .setLayoutData(GridLayout.createHorizontallyFilledLayoutData());
    }

    protected LayoutData createLabelLayout() {
        return GridLayout.createLayoutData(Alignment.BEGINNING, Alignment.CENTER);
    }

    protected LayoutData createTextBoxLayout() {
        return GridLayout.createHorizontallyFilledLayoutData();
    }

    protected LayoutData createTextBoxLayout(int horizontalSpan) {
        return GridLayout.createHorizontallyFilledLayoutData(horizontalSpan);
    }

    public String getDescription() {
        return description;
    }

    public Consumer<List<String>> getOnErrors() {
        return onErrors;
    }

    public String getTitle() {
        return title;
    }

    public void initialFocus() {
        Interactable i = fields.stream().filter(f -> f instanceof Interactable)
                .map(f -> (Interactable) f).findFirst().orElse(null);
        if (i != null) {
            i.takeFocus();
        }
    }

    public void next() {
        if (!validate()) {
            return;
        }
    }

    public void refresh() {
        List<String> errors = bean.validate();
        if (onErrors != null) {
            onErrors.accept(errors);
        }
        for (IRefreshable field : fields) {
            field.refresh();
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOnErrors(Consumer<List<String>> onErrors) {
        this.onErrors = onErrors;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean validate() {
        return bean.validate().size() == 0;
    }
}