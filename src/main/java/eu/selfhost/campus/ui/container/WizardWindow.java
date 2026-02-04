package eu.selfhost.campus.ui.container;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.WindowListenerAdapter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import eu.selfhost.campus.ui.api.IValidatable;
import eu.selfhost.campus.ui.util.Util;

public class WizardWindow extends BasicWindow {

    protected final AtomicBoolean cancelled = new AtomicBoolean(false);

    protected final WizardContentPanel wizardContentPanel;

    public WizardWindow(IValidatable config, String title, WizardPage... pages) {

        super(title);

        wizardContentPanel = new WizardContentPanel(pages) {

            @Override
            protected void closeWindow(boolean lcancel) {
                cancelled.set(lcancel);
                if (lcancel) {
                    WizardWindow.this.close();
                    return;
                }
                List<String> errors = config.validate();
                boolean done = errors.size() == 0;
                if (!done) {
                    done = Util.errorMsgbox((WindowBasedTextGUI) getTextGUI(), errors);
                }
                if (done) {
                    WizardWindow.this.close();
                    return;
                }
            }

        };
        setComponent(wizardContentPanel);
        addWindowListener(new WindowListenerAdapter() {
            @Override
            public void onUnhandledInput(Window basePane, KeyStroke keyStroke,
                    AtomicBoolean hasBeenHandled) {
                if (keyStroke.getKeyType() == KeyType.Escape) {
                    cancelled.set(true);
                    basePane.close();
                    hasBeenHandled.set(true);
                }
                super.onUnhandledInput(basePane, keyStroke, hasBeenHandled);
            }
        });

        wizardContentPanel.setOnNewPage(this::onNewPage);

        wizardContentPanel.start();

        setHints(Arrays.asList(Window.Hint.EXPANDED));

        setTitle(pages[0].getTitle());
    }

    protected void onNewPage(WizardPage page) {
        setTitle(page.getTitle());
    }

    public boolean isCancelled() {
        return cancelled.get();
    }

    public void setOnNewPage(Consumer<WizardPage> onNewPage) {
        if (onNewPage == null) {
            wizardContentPanel.setOnNewPage(this::onNewPage);
        } else {
            wizardContentPanel.setOnNewPage(page -> {
                onNewPage.accept(page);
                WizardWindow.this.onNewPage(page);
            });
        }
    }
}
