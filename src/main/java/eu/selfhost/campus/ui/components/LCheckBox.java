package eu.selfhost.campus.ui.components;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.googlecode.lanterna.gui2.CheckBox;
import com.googlecode.lanterna.gui2.Interactable;

import eu.selfhost.campus.ui.api.IModifyable;

public class LCheckBox extends CheckBox implements IModifyable {

    final String[] booleanValues;

    private Supplier<String> getter = null;

    private Consumer<Interactable> onModified = null;

    private Consumer<String> setter = null;

    public LCheckBox(String... booleanValues) {
        this.booleanValues = booleanValues == null || booleanValues.length < 2
                ? new String[] { "true", "false" }
                : booleanValues;
        addListener((boolean checked) -> {
            updateModel();
        });
    }

    @Override
    protected void afterLeaveFocus(FocusChangeDirection direction, Interactable nextInFocus) {
        updateModel();
    }

    // @Override
    @SuppressWarnings("hiding")
    public void bind(Supplier<String> getter, Consumer<String> setter) {
        setGetter(getter);
        setSetter(setter);
    }

    @Override
    public Supplier<String> getGetter() {
        return getter;
    }

    @Override
    public Consumer<Interactable> getOnModified() {
        return onModified;
    }

    public Consumer<String> getSetter() {
        return setter;
    }

    public String getText() {
        if (this.isChecked()) {
            return booleanValues[0];
        }
        return booleanValues[1];
    }

    @Override
    public void refresh() {
        if (getter != null) {
            String text = getter.get();
            setText(text == null ? "" : text);
        }
    }

    @Override
    public void setGetter(Supplier<String> getter) {
        this.getter = getter;
    }

    @Override
    public void setOnModified(Consumer<Interactable> onModified) {
        this.onModified = onModified;
    }

    public void setSetter(Consumer<String> setter) {
        this.setter = setter;
    }

    public void setText(String text) {
        if (booleanValues[0].equalsIgnoreCase(text)) {
            setChecked(true);
        } else {
            setChecked(false);
        }
    }

    private void updateModel() {
        if (Objects.equals(getText(), getter.get())) {
            // no modifications
            return;
        }
        if (setter != null) {
            setter.accept(getText());
        }
        if (onModified != null) {
            onModified.accept(this);
        }
    }
}