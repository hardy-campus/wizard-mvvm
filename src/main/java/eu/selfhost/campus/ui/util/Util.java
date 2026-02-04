package eu.selfhost.campus.ui.util;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.ExtendedTerminal;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;

import eu.selfhost.campus.ui.api.Description;

public class Util {

    /** logger for this class. */
    private static Logger logger = LoggerFactory.getLogger(Util.class);

    public static void close(Screen screen) {
        if (screen != null) {
            try {
                screen.close();
            } catch (IOException e) {
                logger.error("screen close failed: {}", e.toString());
                logger.debug("Ext.Err.: {}", e);
            }
        }
    }

    public static boolean errorMsgbox(final WindowBasedTextGUI _textGUI, List<String> errors) {
        List<String> lines = new ArrayList<>(errors.size() * 2);
        lines.add(0, "The following problems were found:");
        for (String error : errors) {
            lines.addAll(split(error, 70));
        }

        MessageDialogButton btn = MessageDialog.showMessageDialog(_textGUI, "Error",
                String.join("\n", lines), MessageDialogButton.Retry, MessageDialogButton.Ignore);

        return btn == MessageDialogButton.Ignore;
    }

    public static String extractDescription(Object beanObject) {
        final Description desc = beanObject.getClass().getAnnotation(Description.class);
        if (desc != null && desc.description() != null) {
            return desc.description();
        }
        return "";
    }

    public static String extractTitle(Object beanObject) {
        final Description desc = beanObject.getClass().getAnnotation(Description.class);
        if (desc != null && desc.title() != null) {
            return desc.title();
        }
        return "";
    }

    public static boolean msgbox(String title, String msg) {

        try (TerminalScreen screen = start()) {

            final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            textGUI.setTheme(null);

            MessageDialog.showMessageDialog(textGUI, title, msg, MessageDialogButton.OK);

            return true;

        } catch (IOException e) {
            logger.error("msgbox {} failed: {}", title, e.toString());
            logger.debug("Ext.Err.: {}", e);
            return false;
        }
    }

    /**
     * @param screen
     * @param window
     * @throws IOException
     */
    public static void setupTerminal(TerminalScreen screen, final BasicWindow window)
            throws IOException {
        @SuppressWarnings("resource")
        Terminal terminal = screen.getTerminal();
        window.setFixedSize(screen.getTerminalSize().withRelative(-5, -5));
        terminal.addResizeListener((_terminal, newSize) -> {
            window.setFixedSize(screen.getTerminalSize().withRelative(-5, -5));
        });
        if (terminal instanceof SwingTerminal) {
            SwingTerminal swing = (SwingTerminal) terminal;
            swing.setFont(Font.getFont("Lucida Console"));
        }
        if (terminal instanceof ExtendedTerminal) {
            ((ExtendedTerminal) terminal).setTitle("Example Wizard");
        }
    }

    public static List<String> split(String input, int maxCol) {
        List<String> result = new ArrayList<>();
        String[] items = input.split(" ");
        String line = "*";
        for (String item : items) {
            String l = line + " " + item;
            if (l.length() > maxCol) {
                result.add(line);
                line = "  " + item;
            } else {
                line = l;
            }
        }
        return result;
    }

    public static TerminalScreen start() throws IOException {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        terminalFactory.setPreferTerminalEmulator(false);
        TerminalScreen screen = terminalFactory.createScreen();
        screen.startScreen();
        return screen;
    }

}
