package eu.selfhost.campus.bin;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.selfhost.campus.ui.api.Wizards;
import eu.selfhost.campus.ui.example.Example;
import eu.selfhost.campus.ui.example.ExamplePage;

public class WizardTester {

    /** logger for this class. */
    private static Logger logger = LoggerFactory.getLogger(WizardTester.class);

    public static void main(String... args) {
        try {
            if (run()) {
                System.err.println("User finished wizard");
            } else {
                System.err.println("User cancelled wizard");
            }
        } catch (IOException e) {
            logger.error("failed to run wizard: {}", e.toString());
            logger.debug("Ext.Err.: ", e);
        }
    }

    public static boolean run() throws IOException {

        Example example = new Example();
        ExamplePage examplePage = new ExamplePage(example);

        return Wizards.runUI(example, "Example", examplePage);
    }
}
