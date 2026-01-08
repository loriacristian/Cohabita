package it.unical.ingsw.cohabita.application.command;

import it.unical.ingsw.cohabita.ui.view.LoginController;
import it.unical.ingsw.cohabita.ui.view.RegistrazioneController;

public class RegistrazioneCommand implements Command {

    private final RegistrazioneController controller;

    public RegistrazioneCommand(RegistrazioneController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        controller.onRegistrati();
    }
}
