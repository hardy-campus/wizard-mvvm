package eu.selfhost.campus.ui.api;

import java.util.function.Supplier;

public interface IRefreshable {

    Supplier<String> getGetter();

    void refresh();

    void setGetter(Supplier<String> getter);
}
