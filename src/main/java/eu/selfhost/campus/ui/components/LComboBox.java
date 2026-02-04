package eu.selfhost.campus.ui.components;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.googlecode.lanterna.gui2.ComboBox;
import com.googlecode.lanterna.gui2.Interactable;

import eu.selfhost.campus.ui.api.IModifyable;

public class LComboBox extends ComboBox<String> implements IModifyable {

    private Supplier<String> getter = null;

    boolean initialRefrsh = true;

    boolean inRefresh = false;

    private Supplier<String[]> listGetter = null;

    private Consumer<Interactable> onModified = null;

    private Consumer<String> setter = null;

    public LComboBox() {
        addListener((selectedIndex, previousSelection, changedByUserInteraction) -> {
            updateModel();
        });
    }

    @Override
    protected void afterLeaveFocus(FocusChangeDirection direction, Interactable nextInFocus) {
        updateModel();
    }

    @SuppressWarnings("hiding")
    public void bindArray(Supplier<String> getter, Supplier<String[]> listGetter,
            Consumer<String> setter) {
        setGetter(getter);
        setListGetter(listGetter);
        setSetter(setter);
    }

    @SuppressWarnings("hiding")
    public void bindList(Supplier<String> getter, Supplier<List<String>> listGetter,
            Consumer<String> setter) {
        setGetter(getter);
        setListGetter(() -> listGetter.get().toArray(new String[0]));
        setSetter(setter);
    }

    @Override
    public Supplier<String> getGetter() {
        return getter;
    }

    public Supplier<String[]> getListGetter() {
        return listGetter;
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
        if (inRefresh) {
            return;
        }
        inRefresh = true;
        try {
            if (getter != null) {
                String text = getter.get();
                if (initialRefrsh) {
                    initialRefrsh = false;
                    String[] items = listGetter.get();
                    clearItems();
                    for (String item : items) {
                        addItem(item);
                    }
                } else {
                    boolean modified = false;
                    String[] items = listGetter.get();
                    if (getItemCount() == items.length) {
                        for (int i = 0; i < getItemCount(); i++) {
                            if (!Objects.equals(getItem(i), items[i])) {
                                modified = true;
                            }
                        }
                    } else {
                        modified = true;
                    }
                    if (modified) {
                        clearItems();
                        for (String item : items) {
                            addItem(item);
                        }
                        setSelectedItem(text);
                        return;
                    }
                }
                if (Objects.equals(getText(), text == null ? "" : text)) {
                    // no modifications
                    return;
                }
                setSelectedItem(text);
            }
        } finally {
            inRefresh = false;
        }
    }

    @Override
    public void setGetter(Supplier<String> detter) {
        this.getter = detter;
    }

    public void setListGetter(Supplier<String[]> listGetter) {
        this.listGetter = listGetter;
    }

    @Override
    public void setOnModified(Consumer<Interactable> onModified) {
        this.onModified = onModified;
    }

    public void setSetter(Consumer<String> setter) {
        this.setter = setter;
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