package dev.jugapi.opoflow;

import dev.jugapi.opoflow.config.AppConfig;
import dev.jugapi.opoflow.exception.AppConfigurationException;
import dev.jugapi.opoflow.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {

        try {
            AppConfig config = new AppConfig();
            ConsoleUI ui = config.setup();
            ui.start();
        } catch (
                AppConfigurationException e) {
            System.err.println(e.getMessage());
        }

    }
}
