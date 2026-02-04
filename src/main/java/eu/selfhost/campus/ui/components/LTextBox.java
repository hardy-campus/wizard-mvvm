package eu.selfhost.campus.ui.components;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.googlecode.lanterna.gui2.Interactable;
import com.googlecode.lanterna.gui2.TextBox;

import eu.selfhost.campus.ui.api.IModifyable;

public class LTextBox extends TextBox implements IModifyable {

    private Supplier<String> getter = null;

    private Consumer<Interactable> onModified = null;

    private Consumer<String> setter = null;

    @Override
    protected void afterLeaveFocus(FocusChangeDirection direction, Interactable nextInFocus) {
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

    @Override
    public void refresh() {
        if (getter != null) {
            String text = getter.get();
            setText(text == null ? "" : text);
        }
    }

    @Override
    public void setGetter(Supplier<String> detter) {
        this.getter = detter;
    }

    @Override
    public void setOnModified(Consumer<Interactable> onModified) {
        this.onModified = onModified;
    }

    public void setSetter(Consumer<String> setter) {
        this.setter = setter;
    }
}