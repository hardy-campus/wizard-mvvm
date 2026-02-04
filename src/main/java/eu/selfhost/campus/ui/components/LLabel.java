package eu.selfhost.campus.ui.components;

import java.util.function.Supplier;

import com.googlecode.lanterna.gui2.Label;

import eu.selfhost.campus.ui.api.IRefreshable;

public class LLabel extends Label implements IRefreshable {

    private Supplier<String> getter = null;

    public LLabel(String text) {
        super(text);
    }

    @SuppressWarnings("hiding")
    public void bind(Supplier<String> getter) {
        setGetter(getter);
    }

    @Override
    public Supplier<String> getGetter() {
        return getter;
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
}