package eu.selfhost.campus.ui.example;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.selfhost.campus.ui.api.Description;
import eu.selfhost.campus.ui.api.IValidatable;

@Description(title = "Next Example Configuration",
        description = "You can edit some test value and see simple validation, here too.")
public class Example2 implements IValidatable {

    public enum Type {
        TYPE1, TYPE2, TYPE3
    }

    /** logger for this class. */
    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(Example2.class);

    File file;

    int intValue;

    boolean someOption;

    String textValue;

    Type type = Type.TYPE1;

    public Example2() {
    }

    public File getFile() {
        return file;
    }

    public int getIntValue() {
        return intValue;
    }

    public String getTextValue() {
        return textValue;
    }

    public Type getType() {
        return type;
    }

    public String[] getTypeList() {
        return Stream.of(Type.values()).map(Type::toString).toArray(String[]::new);
    }

    public boolean isSomeOption() {
        return someOption;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public void setSomeOption(boolean someOption) {
        this.someOption = someOption;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public void setType(String type) {
        if (type == null || type.isEmpty()) {
            return;
        }
        this.type = Type.valueOf(type);
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (someOption) {
            errors.add("Some Option must not be true");
        }
        return errors;
    }
}
