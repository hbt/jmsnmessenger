package com.coded.jmsn.send;

/**
 * Copyright (C) 2006 Hassen Ben Tanfous
 * All right reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 	1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 	2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 	3. Neither the name of the Hassen Ben Tanfous nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * GUISendFiles.java
 * permet d'envoyer des fichiers à plusieurs contacts de votre liste
 * ou à un contact via e-mail. Peut envoyer plusieurs fichiers à la fois
 * Date: 13/01/2006
 * @author Hassen Ben Tanfous
 */

//imports
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

import com.coded.jmsn.*;
import rath.msnm.*;

public class GUISendFiles extends GUISend implements ActionListener {

    //panel contact
    private JFileChooser jfc1;

    private JButton boutonSelectFiles1;
    private JLabel lblSelection1;

    //panel other
    private JFileChooser jfc2;
    private JButton boutonSelectFiles2;
    private JLabel lblSelection2;

    private MSNMessenger msn;

    private File[] tabFiles;

    private int i = 0; //initialisé à cause de l'actionPerformed

    /**
     *
     * @param messenger MSNMessenger
     */
    public GUISendFiles(MSNMessenger messenger) {
        this.msn = messenger;
        this.instancierComposants();
        this.configurerComposants();
    }

    public void instancierComposants() {
        super.instancierComponsants();

        //partie contacts
        boutonSelectFiles1 = new JButton("Selectionner");
        lblSelection1 = new JLabel("Sélection de fichiers");

        jfc1 = new JFileChooser();

        //partie other
        boutonSelectFiles2 = new JButton("Selectionner");
        lblSelection2 = new JLabel(lblSelection1.getText());

        jfc2 = new JFileChooser();
    }

    public void configurerComposants() {
        super.configurerComposants();

        loaderContacts();

        //partie contacts
        boutonSelectFiles1.addActionListener(this);
        jfc1.setDialogTitle(MsnConstantes.TITRE);
        jfc1.setApproveButtonText("Select");
        jfc1.setMultiSelectionEnabled(true);

        ajouterComposant(panelContact, lblSelection1, 220, 10, 200, 20);
        ajouterComposant(panelContact, boutonSelectFiles1, 220, 40, 200, 40);

        //partie others
        boutonSelectFiles2.addActionListener(this);
        jfc1.setDialogTitle(MsnConstantes.TITRE);
        jfc1.setApproveButtonText("Select");
        jfc1.setMultiSelectionEnabled(true);

        ajouterComposant(panelOther, lblSelection2, 220, 10, 200, 20);
        ajouterComposant(panelOther, boutonSelectFiles2, 220, 40, 200, 40);

        boutonSend.addActionListener(this);

        tabPane.addTab("Contacts", panelContact);
        tabPane.addTab("Autres", panelOther);

        ajouterComposant(container, tabPane, 0, 0, 450, 300);
        ajouterComposant(container, boutonSend, 250, 310, 100, 30);

        boutonSend.addActionListener(this);

        setSize(450, 375);
        setLocation(200, 0);
        setVisible(true);

    }

    /**
     * charge la liste de contacts dans le vecteur
     */
    private void loaderContacts() {
        BuddyList bl = msn.getBuddyGroup().getForwardList();
        for (int i = 0; i < bl.size(); i++) {
            if (!bl.get(i).getStatus().equalsIgnoreCase(UserStatus.OFFLINE)) {
                listeEmail.addElement(bl.get(i).getLoginName());
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        int choix;

        if (e.getSource() == boutonSend) {
            SwitchboardSession session = null;

            if (liste.getSelectedIndices().length != 0) {
                int[] tabEmail = liste.getSelectedIndices();

                for (; i < liste.getSelectedIndices().length; i++) {

                    //verification session existante
                    session = msn.findSwitchboardSession(listeEmail.get(
                            tabEmail[i]).toString());

                    try {
                        //creation session
                        while (session == null) {
                            session = msn.doCallWait(listeEmail.get(
                                    tabEmail[i]).toString());
                        }
                    } catch (InterruptedException ex) {
                    } catch (IOException ex) {
                    }

                    for (int k = 0; k < tabFiles.length; k++) {
                        try {
                            //envoie de fichiers
                            msn.sendFileRequest(session.getMsnFriend().
                                                getLoginName(),
                                                tabFiles[k], session);
                        } catch (IOException ex1) {
                        }
                    }
                }

                setVisible(false);
            } else {
                try {
                    session = msn.findSwitchboardSession(txtEmail.getText());
                    while (session == null) {
                        session = msn.doCallWait(txtEmail.getText());
                    }
                    for (int i = 0; i < tabFiles.length; i++) {
                        msn.sendFileRequest(txtEmail.getText(), tabFiles[i],
                                            session);
                    }

                    setVisible(false);
                } catch (InterruptedException ex2) {
                } catch (IOException ex2) {
                }
            }

        } else if (e.getSource() == boutonSelectFiles1) {
            //panel contacts
            choix = jfc1.showOpenDialog(null);
            if (choix == jfc1.APPROVE_OPTION) {
                tabFiles = jfc1.getSelectedFiles();
            }
        } else if (e.getSource() == boutonSelectFiles2) {
            //panel others
            choix = jfc2.showOpenDialog(null);
            if (choix == jfc2.APPROVE_OPTION) {
                tabFiles = jfc2.getSelectedFiles();
            }
        }
    }
}
