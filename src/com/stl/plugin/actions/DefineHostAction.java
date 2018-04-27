package com.stl.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.stl.plugin.dialogs.IPSetupDialog;
import com.stl.plugin.main.Client;

import static com.stl.plugin.main.ShareTheLoad.*;

public class DefineHostAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        String ip = Messages.showInputDialog("Please Provide the IP of the Host", "Host IP", Messages.getQuestionIcon(), "", new InputValidator() {
            @Override
            public boolean checkInput(String input) {
                return input.matches("\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}\\b");
            }

            @Override
            public boolean canClose(String input) {
                return input.matches("\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}\\b");
            }
        });
        String pass = Messages.showInputDialog("Please Provide the password of the Host", "Password", Messages.getQuestionIcon(), "", new InputValidator() {
            @Override
            public boolean checkInput(String s) {
                return s.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
            }

            @Override
            public boolean canClose(String s) {
                return s.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
            }
        });
        config_define(IPADDR, ip);
        IPSetupDialog d = new IPSetupDialog(e.getProject());
        d.show();
        assert pass != null;
        String hashedpw = hashpw(pass);
        d.setProgress(50);
        Client guest = new Client(ip, hashedpw);
        d.close(DialogWrapper.CLOSE_EXIT_CODE);
    }


}
