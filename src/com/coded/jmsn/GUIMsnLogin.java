package com.coded.jmsn;

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
 * GUIMsnLogin.java
 * permet de logger l'utilisateur sur son compte MSN
 * Date: 07/01/2006
 * @author Hassen Ben Tanfous

 */

//imports
import java.awt.*;
import javax.swing.*;
import rath.msnm.*;
import java.awt.event.*;
import com.coded.jmsn.msg.*;
import rath.msnm.event.*;
import rath.msnm.entity.MsnFriend;

public class GUIMsnLogin implements GUIComposants {
    //partie connexion
    private JFrame frameConnexion;
    private Container containerConnexion;

    private JButton boutonConnexion;
    private JLabel lblInfosConnexion;

    //partie login
    private JFrame frameLogin;
    private Container containerLogin;

    private JButton boutonLogin;
    private JLabel lblUsername,
            lblPasswd;
    private JTextField txtUsername;
    private JPasswordField txtPasswd;

    private JComboBox comboStatus;

    private MSNMessenger msn;

    private Messagerie msg;


    public GUIMsnLogin() {
        instancierComponsants();
        configurerComposants();
    }

    public void instancierComponsants() {
        //partie connexion
        frameConnexion = new JFrame();
        containerConnexion = frameConnexion.getContentPane();

        boutonConnexion = new JButton("Connexion");
        lblInfosConnexion = new JLabel("Connexion en cours...");

        //partie login
        frameLogin = new JFrame();
        containerLogin = frameLogin.getContentPane();

        boutonLogin = new JButton("Login");
        lblUsername = new JLabel("Username:");
        lblPasswd = new JLabel("Password:");

        txtUsername = new JTextField();
        txtPasswd = new JPasswordField();

        comboStatus = new JComboBox(MsnConstantes.tabPersoStatus);
    }

    public void configurerComposants() {
        msg.titre = MsnConstantes.TITRE;

        //partie connexion
        containerConnexion.setLayout(null);

        boutonConnexion.addActionListener(alConnexion);
        lblInfosConnexion.setVisible(false);

        ajouterComposant(containerConnexion, boutonConnexion, 100, 50, 200, 40);
        ajouterComposant(containerConnexion, lblInfosConnexion, 100, 50, 200,
                         40);

        //partie login
        containerLogin.setLayout(null);
        boutonLogin.addActionListener(alLogin);
        txtPasswd.addActionListener(alLogin);

        ajouterComposant(containerLogin, lblUsername, 10, 10, 100, 20);
        ajouterComposant(containerLogin, txtUsername, 120, 12, 200, 20);
        ajouterComposant(containerLogin, lblPasswd, 10, 40, 100, 20);
        ajouterComposant(containerLogin, txtPasswd, 120, 42, 200, 20);
        ajouterComposant(containerLogin, comboStatus, 120, 72, 200, 20);
        ajouterComposant(containerLogin, boutonLogin, 120, 120, 200, 40);

        frameLogin.setTitle(MsnConstantes.TITRE + " MSN LOGIN");
        frameLogin.setSize(400, 250);
        frameLogin.setLocation(350, 200);
        frameLogin.setVisible(false);

        frameConnexion.setTitle(MsnConstantes.TITRE);
        frameConnexion.setSize(400, 200);
        frameConnexion.setLocation(300, 200);
        frameConnexion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameConnexion.setVisible(true);
    }

    /**
     * actionListener pour les composants de la connexion
     */
    private ActionListener alConnexion = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == boutonConnexion) {
                frameLogin.setVisible(true);
            }
        }
    };

    private ActionListener alLogin = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == boutonLogin) {
                if (txtUsername.getText() == null) {
                    msg.msge("Vous devez entrer un nom d'utilisateur");
                } else {
                    //création messenger
                    msn = new MSNMessenger(txtUsername.getText(),
                                           getPassword(txtPasswd.getPassword()));
                    //status initial
                    msn.setInitialStatus(MsnConstantes.tabRathStatus[
                                         comboStatus.
                                         getSelectedIndex()]);
                    msn.addMsnListener(new MsnLoginListener());

                    try {
                        msn.login();
                    } catch (Exception ex) {
                        msg.msge("Impossible de vous connecter avec " +
                                 txtUsername.getText());
                        txtPasswd.setText("");
                        lblInfosConnexion.setVisible(false);
                        boutonConnexion.setVisible(true);
                    }

                    //login en cours
                    frameLogin.setVisible(false);
                    boutonConnexion.setVisible(false);
                    lblInfosConnexion.setVisible(true);
                }
            } else if (e.getSource() == txtPasswd) {
                if (txtUsername.getText() == null) {
                   msg.msge("Vous devez entrer un nom d'utilisateur");
               } else {
                   //création messenger
                   msn = new MSNMessenger(txtUsername.getText(),
                                          getPassword(txtPasswd.getPassword()));
                   //status initial
                   msn.setInitialStatus(MsnConstantes.tabRathStatus[
                                        comboStatus.
                                        getSelectedIndex()]);
                   msn.addMsnListener(new MsnLoginListener());

                   try {
                       msn.login();
                   } catch (Exception ex) {
                       msg.msge("Impossible de vous connecter avec " +
                                txtUsername.getText());
                       txtPasswd.setText("");
                       lblInfosConnexion.setVisible(false);
                       boutonConnexion.setVisible(true);
                   }

                   //login en cours
                   frameLogin.setVisible(false);
                   boutonConnexion.setVisible(false);
                   lblInfosConnexion.setVisible(true);
               }
            }
        }
    };

    public void ajouterComposant(Container c, Component comp, int x, int y,
                                 int x1, int y1) {
        comp.setBounds(x, y, x1, y1);
        c.add(comp);
    }

    private String getPassword(char[] tabPasswd) {
        String passwd = "";
        for (int i = 0; i < tabPasswd.length; i++) {
            passwd += tabPasswd[i];
        }
        return passwd;
    }

    public JFrame getFrame () {
        return this.frameLogin;
    }


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
     * MsnLoginListener.java
     * gère les evenements lors du login de l'utilisateur sur son compte MSN personnel
     * Date: 07/01/2006
     * @author Hassen Ben Tanfous
     */
    private class MsnLoginListener extends MsnAdapter {

        public void loginComplete(MsnFriend own) {
            lblInfosConnexion.setText(
                    "Connecté avec succès ...");
            msn.removeMsnListener(this);
            GUIMsn main = new GUIMsn(msn);
            main.getLogger().println("Connecté avec succès sous " +
                                     msn.getOwner().getLoginName());
            frameConnexion.setVisible(false);
        }

        public void loginError(String header) {
            msg.msge("Impossible de vous connecter avec " +
                     txtUsername.getText());
            txtPasswd.setText("");
            lblInfosConnexion.setVisible(false);
            boutonConnexion.setVisible(true);
        }
    }
}
