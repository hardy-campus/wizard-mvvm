/**
 * 
 */
package eu.selfhost.campus.ui.api;

import java.io.IOException;
import java.util.function.Consumer;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.TerminalScreen;

import eu.selfhost.campus.ui.container.WizardPage;
import eu.selfhost.campus.ui.container.WizardWindow;
import eu.selfhost.campus.ui.util.Util;

public class Wizards {

    public static boolean runUI(IValidatable mainBean, String title, WizardPage... pages)
            throws IOException {
        return runUI(mainBean, title, null, pages);
    }

    public static boolean runUI(IValidatable mainBean, String title, Consumer<WizardPage> onNewPage,
            WizardPage... pages) throws IOException {

        try (TerminalScreen screen = Util.start()) {

            final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            textGUI.setTheme(null);

            final WizardWindow window = new WizardWindow(mainBean, title, pages);
            Util.setupTerminal(screen, window, title);

            window.setOnNewPage(onNewPage);

            textGUI.addWindowAndWait(window);

            return !window.isCancelled();
        }
    }
}
