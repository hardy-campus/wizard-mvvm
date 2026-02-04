package eu.selfhost.campus.ui.container;

import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.LinearLayout.Alignment;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Separator;
import com.googlecode.lanterna.input.KeyStroke;

public class WizardContentPanel extends Panel {

    /** logger for this class. */
    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(WizardContentPanel.class);

    final Button cancel = new Button("Cancel")
            .setLayoutData(LinearLayout.createLayoutData(Alignment.End));

    final Panel content;

    final Label errorLabel = new Label("")
            .setLayoutData(GridLayout.createHorizontallyFilledLayoutData());

    final EmptySpace filler = new EmptySpace().setLayoutData(GridLayout.createLayoutData(
            GridLayout.Alignment.FILL, GridLayout.Alignment.BEGINNING, false, true));

    final Panel footer = new Panel(new LinearLayout(Direction.HORIZONTAL))
            .setLayoutData(GridLayout.createHorizontallyFilledLayoutData());

    final Label header = new Label("")
            .setLayoutData(GridLayout.createHorizontallyFilledLayoutData());

    final Button next = new Button("Next>")
            .setLayoutData(LinearLayout.createLayoutData(Alignment.End));

    Consumer<WizardPage> onNewPage;

    int page = 0;

    int pagePosition = 3;

    final WizardPage[] pages;

    final Button prev = new Button("<Prev")
            .setLayoutData(LinearLayout.createLayoutData(Alignment.End));

    final Separator sep1 = new Separator(Direction.HORIZONTAL)
            .setLayoutData(GridLayout.createHorizontallyFilledLayoutData());

    final Separator sep2 = new Separator(Direction.HORIZONTAL)
            .setLayoutData(GridLayout.createHorizontallyFilledLayoutData());

    WizardContentPanel(WizardPage... content) {
        super(new GridLayout(1));

        pages = content;
        this.content = content[0].setLayoutData(GridLayout.createHorizontallyFilledLayoutData());

        header.setLabelWidth(80);
        header.setPreferredSize(new TerminalSize(80, 2));
        header.setText(content[0].getDescription());
        header.setLayoutData(GridLayout.createHorizontallyFilledLayoutData());

        errorLabel.setLabelWidth(80);
        errorLabel.setPreferredSize(new TerminalSize(80, 2));
        errorLabel.setForegroundColor(TextColor.ANSI.RED);
        errorLabel.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL,
                GridLayout.Alignment.BEGINNING, true, false));

        addComponent(new EmptySpace(TerminalSize.ONE));
        addComponent(header);
        addComponent(errorLabel);

        addComponent(sep1);

        pagePosition = getChildCount();
        addComponent(content[0]);

        addComponent(filler);
        addComponent(sep2);

        footer.addComponent(prev);
        footer.addComponent(next);
        footer.addComponent(new EmptySpace(new TerminalSize(2, 1)));
        footer.addComponent(cancel);

        footer.setLayoutData(GridLayout.createHorizontallyEndAlignedLayoutData(1));
        addComponent(footer);

        for (WizardPage lpage : pages) {
            lpage.setOnErrors(errors -> {
                String errorMsg = errors.size() > 0 ? " * " + String.join("\n * ", errors) : "";
                errorLabel.setText(errorMsg);
                onErrors(errors);
            });
        }
        content[0].refresh();

        prev.addListener(this::prev);
        next.addListener(this::next);
        cancel.addListener(this::cancel);
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public boolean handleInput(KeyStroke keyStroke) {
        if (keyStroke.isAltDown()) {
            switch (keyStroke.getCharacter()) {
                case 'F':
                case 'f':
                    if (page == pages.length - 1) {
                        next(next);
                    }
                    break;
                case 'N':
                case 'n':
                    if (page < pages.length - 1) {
                        next(next);
                    }
                    break;
                case 'P':
                case 'p':
                    prev(prev);
                    break;
                case 'C':
                case 'c':
                    cancel(cancel);
                    break;
            }
        }
        return super.handleInput(keyStroke);
    }

    private void cancel(Button button1) {
        closeWindow(true);
    }

    protected void closeWindow(boolean cancellled) {
        // TODO in derived classes
    }

    public Consumer<WizardPage> getOnNewPage() {
        return onNewPage;
    }

    private void next(Button button1) {
        page++;
        if (page >= pages.length) {
            page = pages.length - 1;
            closeWindow(false);
            return;
        }
        switchPage(pages[page - 1], pages[page]);
    }

    private void onErrors(List<String> errors) {
        next.setEnabled(errors.size() == 0);
    }

    private void prev(Button button1) {
        page--;
        if (page < 0) {
            page = 0;
            return;
        }
        switchPage(pages[page + 1], pages[page]);
    }

    public void setOnNewPage(Consumer<WizardPage> onNewPage) {
        this.onNewPage = onNewPage;
    }

    @Override
    public synchronized Panel setSize(TerminalSize size) {
        Panel result = super.setSize(size);
        // logger.info("set label width: {}", size.getColumns() - 5);
        header.setLabelWidth(size.getColumns() - 5);
        header.setText(header.getText());
        header.invalidate();
        errorLabel.setLabelWidth(size.getColumns() - 5);
        errorLabel.setText(errorLabel.getText());
        errorLabel.invalidate();
        return result;
    }

    public void start() {
        switchPage(null, pages[page]);
    }

    private void switchPage(WizardPage oldPage, WizardPage newPage) {
        if (oldPage != null) {
            removeComponent(oldPage);
        }
        addComponent(pagePosition, newPage);
        newPage.setLayoutData(GridLayout.createHorizontallyFilledLayoutData());
        invalidate();

        newPage.initialFocus();
        newPage.refresh();

        next.setLabel(page == pages.length - 1 ? "Finish" : "Next");

        header.setText(newPage.getDescription());
        if (onNewPage != null) {
            onNewPage.accept(newPage);
        }
    }
}