package com.stl.plugin.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.refactoring.ui.InfoDialog;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static com.intellij.util.ui.UIUtil.DEFAULT_HGAP;
import static com.intellij.util.ui.UIUtil.DEFAULT_VGAP;

public class IPSetupDialog extends InfoDialog {


    private JProgressBar progressBar;

    public IPSetupDialog(Project project) {
        super("Abc", project);
        setOKButtonText("Cancel");
        setTitle("Initiating 'Share The Load'");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JLabel information = new JLabel("Attempting to connect to establish connection...");

        progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setVisible(true);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBag bag = new GridBag().setDefaultInsets(JBUI.insets(0, 6, DEFAULT_VGAP, DEFAULT_HGAP)).setDefaultAnchor(GridBagConstraints.LINE_START).setDefaultFill(GridBagConstraints.HORIZONTAL);
        panel.add(information, bag.nextLine().next());
        panel.add(progressBar, bag.nextLine().next());

        return panel;
    }

    public void setProgress(int i) {
        progressBar.setValue(i);
    }
}
