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
 * GUISendMessage.java
 * permet d'envoyer un message à plusieurs contacts à la fois.
 * permet d'envoyer un message à un contact via son e-mail
 * Date: 13/01/2006
 * @author Hassen Ben Tanfous
 */

//imports
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import com.coded.jmsn.*;
import rath.msnm.*;
import rath.msnm.msg.*;

public class GUISendMessage extends GUISend implements ActionListener {

    //partie contact
    private JTextArea txt1;
    private JLabel lblMessage1;
    private JScrollPane scroll1;

    //partie other
    private JTextArea txt2;
    private JLabel lblMessage2;
    private JScrollPane scroll2;

    private MSNMessenger msn;

    private MimeMessage message;

    private Vector vecChat;

    /**
     *
     * @param messenger MSNMessenger
     */
    public GUISendMessage(MSNMessenger messenger) {
        this.msn = messenger;
        instancierComposants();
        configurerComposants();
    }

    public void instancierComposants() {
        super.instancierComponsants();

        //partie contacts
        txt1 = new JTextArea();
        lblMessage1 = new JLabel("Message:");
        scroll1 = new JScrollPane(txt1);

        //partie other
        txt2 = new JTextArea();
        lblMessage2 = new JLabel("Message:");
        scroll2 = new JScrollPane(txt2);

        message = new MimeMessage();

    }

    public void configurerComposants() {
        super.configurerComposants();

        //liste
        loaderContacts();

        message.setFontColor(Color.black);

        ajouterComposant(panelContact, lblMessage1, 220, 10, 100, 20);
        ajouterComposant(panelContact, scroll1, 220, 40, 200, 200);

        //panel other
        ajouterComposant(panelOther, lblMessage2, 220, 10, 100, 20);
        ajouterComposant(panelOther, scroll2, 220, 40, 200, 200);

        //
        tabPane.addTab("Contacts", panelContact);
        tabPane.addTab("Autres", panelOther);

        ajouterComposant(container, tabPane, 0, 0, 450, 300);
        ajouterComposant(container, boutonSend, 250, 310, 100, 30);

        boutonSend.addActionListener(this);

        setSize(450, 375);
        setLocation(200, 0);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        SwitchboardSession session;
        GUIChat temp;
        if (e.getSource() == boutonSend) {
            if (!txt1.getText().equalsIgnoreCase("")) {
                message.setMessage(txt1.getText());
                int[] tabEmail = liste.getSelectedIndices(); //email sélectionne

                for (int i = 0; i < tabEmail.length; i++) {
                    try {
                        session = msn.findSwitchboardSession(listeEmail.
                                get(tabEmail[i]).toString());
                        while (session == null) {
                            session = msn.doCallWait(listeEmail.
                                    get(tabEmail[i]).toString());
                        }

                        session.sendInstantMessage(message);
                        //pour démarrer une fenetre après l'envoie
                        //ajouter la conversation au vecChat de GUIMSN
//                        temp = new GUIChat(msn,
//                                msn.getLoginName(),
//                                msn.getLoginName(), session);
//
//                        temp.append(txt1.getText());
                        setVisible(false);

                    } catch (InterruptedException ex) {
                    } catch (IOException ex) {
                    }
                }
                com.coded.jmsn.msg.Messagerie.msgi("Message envoyé",
                        MsnConstantes.TITRE);
            } else if (!txt2.getText().equalsIgnoreCase("")) {
                message.setMessage(txt2.getText());
                try {
                    session = msn.doCallWait(txtEmail.getText());
                    while (session == null) {
                        session = msn.doCallWait(txtEmail.getText());
                    }

                    session.sendInstantMessage(message);
                    //temp = new GUIChat(msn, msn.getLoginName(), msn.getLoginName(), session);
                    //temp.append(txt2.getText());


                    setVisible(false);
                } catch (InterruptedException ex1) {
                } catch (IOException ex1) {
                }
            }
        }
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
}
