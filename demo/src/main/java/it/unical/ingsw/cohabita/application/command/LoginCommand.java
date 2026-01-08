package it.unical.ingsw.cohabita.application.command;

import it.unical.ingsw.cohabita.ui.view.LoginController;

public class LoginCommand implements Command {

    private final LoginController controller;

    public LoginCommand(LoginController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        controller.onLogin();
    }
}
