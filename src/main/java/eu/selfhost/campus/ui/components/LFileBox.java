package eu.selfhost.campus.ui.components;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Interactable;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.FileDialogBuilder;

import eu.selfhost.campus.ui.api.IModifyable;
import eu.selfhost.campus.ui.container.WizardPage;

public class LFileBox extends Panel implements IModifyable {

    private LTextBox filename = new LTextBox();

    private Button openFile = new Button(">");

    public LFileBox() {
        super(new GridLayout(2).setHorizontalSpacing(0).setLeftMarginSize(0).setRightMarginSize(0));
        addComponent(filename.setLayoutData(GridLayout.createHorizontallyFilledLayoutData()));
        addComponent(openFile);

        openFile.addListener(this::onTriggered);
        openFile.setRenderer(new Button.FlatButtonRenderer());
        openFile.setLabel(""
                + getThemeDefinition().getCharacter("POPUP", Symbols.TRIANGLE_DOWN_POINTING_BLACK));
    }

    protected void onTriggered(Button button) {
        File input = new FileDialogBuilder().setTitle("Open File").setDescription("Choose a file")
                .setActionLabel("Open").build().showDialog((WindowBasedTextGUI) getTextGUI());
        if (input != null) {
            filename.setText(input.toString());
        }
    }

    public void bind(Supplier<File> getter, Consumer<File> setter) {
        filename.setGetter(WizardPage.convertFile(getter));
        filename.setSetter(WizardPage.convertFile(setter));
    }

    @Override
    public Supplier<String> getGetter() {
        return filename.getGetter();
    }

    @Override
    public Consumer<Interactable> getOnModified() {
        return filename.getOnModified();
    }

    public Consumer<String> getSetter() {
        return filename.getSetter();
    }

    @Override
    public void refresh() {
        filename.refresh();
    }

    @Override
    public void setGetter(Supplier<String> getter) {
        filename.setGetter(getter);
    }

    @Override
    public void setOnModified(Consumer<Interactable> onModified) {
        filename.setOnModified(onModified);
    }

    public void setSetter(Consumer<String> setter) {
        filename.setSetter(setter);
    }
}