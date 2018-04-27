package com.stl.plugin.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.stl.plugin.main.Host;

import java.io.IOException;

import static com.stl.plugin.main.ShareTheLoad.*;

public class ExternalizeWorkspaceAction extends ToggleAction {

    @Override
    public boolean isSelected(AnActionEvent anActionEvent) {
        try {
            return (boolean) config_fetch(ENABLED);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        } catch (ClassCastException e) {
            return Boolean.parseBoolean(config_fetch(ENABLED).toString());
        }
//        return MemoryViewManager.getInstance().isAutoUpdateModeEnabled();
    }


    @Override
    public void setSelected(AnActionEvent e, boolean flag) {
        Project project = e.getProject();
        if (project != null) {
//            MemoryViewManager.getInstance().setAutoUpdate(b);
            config_define(ENABLED, flag);
            if (flag) {
                String password = Messages.showInputDialog("Please Define a password for external connections. (Must have number, upper and lowercase letter and symbol)",
                        "Password Definition", AllIcons.General.PasswordLock, "", new InputValidator() {
                            @Override
                            public boolean checkInput(String s) {
                                return s.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
                            }

                            @Override
                            public boolean canClose(String s) {
                                return s.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
                            }
                        });
                assert password != null;
                hashpw(password);
                try {
                    Host host = Host.getInstance();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
