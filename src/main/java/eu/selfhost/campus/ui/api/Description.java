package eu.selfhost.campus.ui.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Simple description annotation, which annotates a view model with title and description.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {

    String description();

    String title();
}
