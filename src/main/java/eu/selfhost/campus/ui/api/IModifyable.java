package eu.selfhost.campus.ui.api;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.googlecode.lanterna.gui2.Interactable;

public interface IModifyable extends IRefreshable {

    @Override
    Supplier<String> getGetter();

    Consumer<Interactable> getOnModified();

    @Override
    void refresh();

    // void bind(Supplier<String> getter, Consumer<String> setter);

    @Override
    void setGetter(Supplier<String> getter);

    void setOnModified(Consumer<Interactable> onModified);
}
