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
 * GUISend.java
 * Base d'envoie utilisée par GUISendMessage et GUISendFiles pour l'interface graphique minimale
 * Date: 13/01/2006
 * @author Hassen Ben Tanfous
 */

//imports
import java.awt.*;

import javax.swing.*;

import com.coded.jmsn.*;

public class GUISend extends JFrame implements GUIComposants {

    Container container;
    JTabbedPane tabPane;

    JPanel panelContact,
            panelOther;

    JList liste;
    JScrollPane scrollListe;
    DefaultListModel listeEmail;

    JButton boutonSend;
    JLabel lblContacts;

    //panel2
    JLabel lblEmail;
    JTextField txtEmail;

    public void instancierComponsants() {
        container = getContentPane();
        tabPane = new JTabbedPane();

        panelContact = new JPanel();
        panelOther = new JPanel();

        listeEmail = new DefaultListModel();
        liste = new JList(listeEmail);
        scrollListe = new JScrollPane(liste);

        boutonSend = new JButton("Send");
        lblContacts = new JLabel("Contacts");

        lblEmail = new JLabel("Email: ");
        txtEmail = new JTextField();
    }

    public void configurerComposants() {
        setTitle(MsnConstantes.TITRE);

        container.setLayout(null);
        panelContact.setLayout(null);
        panelOther.setLayout(null);

        //panel contact
        ajouterComposant(panelContact, scrollListe, 10, 10, 200, 290);

        //panel autre
        ajouterComposant(panelOther, lblEmail, 10, 10, 100, 20);
        ajouterComposant(panelOther, txtEmail, 10, 34, 150, 20);

        boutonSend.setBackground(Color.red);

        setResizable(false);
    }

    public void ajouterComposant(Container c, Component comp, int x, int y,
                                 int x1, int y1) {
        comp.setBounds(x, y, x1, y1);
        c.add(comp);
    }
}
