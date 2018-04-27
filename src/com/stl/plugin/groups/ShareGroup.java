package com.stl.plugin.groups;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;

public class ShareGroup extends DefaultActionGroup {

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setVisible(true);
        e.getPresentation().setEnabled(true);
    }

}
