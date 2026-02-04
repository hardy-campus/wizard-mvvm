package eu.selfhost.campus.ui.example;

import java.util.regex.Pattern;

import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;

import eu.selfhost.campus.ui.components.LCheckBox;
import eu.selfhost.campus.ui.components.LComboBox;
import eu.selfhost.campus.ui.components.LTextBox;
import eu.selfhost.campus.ui.container.WizardPage;
import eu.selfhost.campus.ui.example.Example.Type;
import eu.selfhost.campus.ui.util.Util;

public class ExamplePage extends WizardPage {

    final LTextBox intValue = new LTextBox();

    final LCheckBox someOption = new LCheckBox();

    final LTextBox textValue = new LTextBox();

    final LTextBox file = new LTextBox();

    final LComboBox type = new LComboBox();

    public ExamplePage(Example bean) {
        super(new GridLayout(2).setVerticalSpacing(1).setTopMarginSize(1).setBottomMarginSize(1),
                bean);

        String beanTitle = Util.extractTitle(bean);
        setTitle(beanTitle);

        String beanDesc = Util.extractDescription(bean);
        setDescription(beanDesc);

        GridLayout gridLayout = (GridLayout) getLayoutManager();
        gridLayout.setHorizontalSpacing(3);

        someOption.setLabel("Some Option");
        addComponent(someOption);
        addField(someOption);
        someOption.setLayoutData(createLabelLayout());
        someOption.bind(convertBoolean(bean::isSomeOption), convertBoolean(bean::setSomeOption));
        addComponent(createGreedySpace());

        Label lbl = new Label("Type");
        lbl.setLayoutData(createLabelLayout());
        addComponent(lbl);
        addComponent(type);
        addField(type);
        type.setLayoutData(createComboBoxLayout());
        type.bindArray(convertEnum(bean::getType), bean::getTypeList,
                convertEnum(Type.class, bean::setType));

        addComponent(new Label("Text Value"));
        addComponent(textValue);
        addField(textValue);
        textValue.setLayoutData(createTextBoxLayout());
        textValue.bind(bean::getTextValue, bean::setTextValue);

        addComponent(new Label("File"));
        addComponent(file);
        addField(file);
        file.setLayoutData(createTextBoxLayout());
        file.bind(convertFile(bean::getFile), convertFile(bean::setFile));

        addComponent(new Label("Int Value"));
        intValue.setValidationPattern(Pattern.compile("[0-9]*"));
        addComponent(intValue);
        addField(intValue);
        intValue.setLayoutData(createTextBoxLayout());
        intValue.bind(convertInt(bean::getIntValue), convertInt(bean::setIntValue));

        refresh();
    }

    @Override
    public void refresh() {
        super.refresh();
        boolean fl = ((Example) bean).isSomeOption();
        type.setEnabled(!fl);
        textValue.setEnabled(!fl);
        intValue.setEnabled(!fl);
    }
}