/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.dma.ais.virtualnet.transponder.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import dk.dma.ais.virtualnet.common.table.TargetTableEntry;

import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SelectTargetDialog extends JDialog implements ActionListener, ListSelectionListener {

    private static final long serialVersionUID = 1L;

    private Integer selectedTarget;
    private final SelectTargetList list = new SelectTargetList();
    private final JButton selectButton = new JButton("Select");
    private final JButton cancelButton = new JButton("Cancel");

    public SelectTargetDialog(JFrame parent, List<TargetTableEntry> targets) {
        super(parent, "Select target", true);
        setResizable(false);
        setSize(300, 400);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        getContentPane().setLayout(new BorderLayout());
        
        JPanel btnPanel = new JPanel();
        selectButton.setPreferredSize(new Dimension(75, 28));
        selectButton.setEnabled(false);
        selectButton.addActionListener(this);
        btnPanel.add(selectButton);
        cancelButton.setPreferredSize(new Dimension(75, 28));
        cancelButton.addActionListener(this);
        btnPanel.add(cancelButton);
        getContentPane().add(btnPanel, BorderLayout.SOUTH);
        
        list.addListSelectionListener(this);
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        listScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        getContentPane().add(listScroller, BorderLayout.CENTER);

        getContentPane().add(list.getFilterField(), BorderLayout.NORTH);
        list.getFilterField().setPreferredSize(new Dimension(294, 24));
        
        for (TargetTableEntry target : targets) {
            list.addTarget(target);
        }

        // Hitting the escape key should simulate clicking "Cancel"
        ActionListener escAction = new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                cancelButton.doClick();
            }};
        getRootPane().registerKeyboardAction(escAction,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        // Hitting the enter key should simulate clicking "Select"
        ActionListener enterAction = new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                selectButton.doClick();
            }};
        getRootPane().registerKeyboardAction(enterAction,
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public Integer getSelectedTarget() {
        return selectedTarget;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            selectedTarget = null;
            this.setVisible(false);
        } else if (e.getSource() == selectButton) {
            if (list.getSelectedIndex() >= 0) {
                this.selectedTarget = list.getSelectedValue().getMmsi();
                this.setVisible(false);
            }
        }
        
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == list) {
            selectButton.setEnabled(list.getSelectedIndex() >= 0);
        }
        
    }
}
