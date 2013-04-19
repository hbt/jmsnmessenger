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
 * GUIChat.java
 * represente une fenetre chat lorsqu'une conversation est demarre
 * Date: 10/01/2006
 * @author Hassen Ben Tanfous
 */

//imports
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import com.coded.jmsn.msg.*;
import rath.msnm.*;
import rath.msnm.msg.*;
import rath.msnm.event.MsnAdapter;
import rath.msnm.entity.MsnFriend;

public class GUIChat extends Thread implements GUIComposants, ActionListener {
    private JFrame frame;
    private Container container;

    private JLabel lblTitre; //pseudo + email

    private JButton boutonInvite, //invite un contact
            boutonSendFiles, //envoie des fichiers
            boutonBlock,
            boutonUnblock;

    private JEditorPane editeur; //contenu de la conversation
    private JScrollPane scrollEditeur;

    private JTextField txt;

    private MSNMessenger msn;

    private String email,
            pseudo;

    private MimeMessage message; //configuration message (Color etc)

    private SwitchboardSession session; //current session

    private Messagerie msg;

    private Vector vecChat; //vecteur de conversations chat

    private JFileChooser jfcSendFiles;

    private JLabel lblProgression;

    /**
     *
     * @param messenger MSNMessenger
     */
    public GUIChat(MSNMessenger messenger) {
        this.msn = messenger;
        instancierComponsants();
        configurerComposants();
    }

    /**
     *
     * @param messenger MSNMessenger
     * @param email String
     * @param pseudo String
     */
    public GUIChat(MSNMessenger messenger, String email, String pseudo) {
        this(messenger);
        this.email = email;
        this.pseudo = pseudo;
    }

    /**
     *
     * @param messenger MSNMessenger
     * @param email String
     * @param pseudo String
     * @param ss SwitchboardSession
     */
    public GUIChat(MSNMessenger messenger, String email, String pseudo,
                   SwitchboardSession ss) {
        this(messenger, email, pseudo);
        this.session = ss;
    }

    /**
     *
     * @param messenger MSNMessenger
     * @param email String
     * @param pseudo String
     * @param ss SwitchboardSession
     * @param mimeMessage MimeMessage
     */
    public GUIChat(MSNMessenger messenger, String email, String pseudo,
                   SwitchboardSession ss, MimeMessage mimeMessage) {
        this(messenger, email, pseudo, ss);
        mimeMessage.setFontColor(Color.BLACK);
        this.message = mimeMessage;
    }

    public void run() {
        msn.addMsnListener(new MyMsnListener());

        lblTitre.setText(pseudo + " <" + email + ">");

        //setEnabled (true) or (false) the buttons block and unblock
        boolean trouve = false;
        BuddyList bl = msn.getBuddyGroup().getBlockList();
        for (int i = 0; i < bl.size() && !trouve; i++) {
            if (bl.get(i).getLoginName().equalsIgnoreCase(email)) {
                boutonBlock.setEnabled(false);
                trouve = true;
            }
        }

        if (!trouve) {
            boutonUnblock.setEnabled(false);
        }
    }

    public void instancierComponsants() {
        frame = new JFrame(MsnConstantes.TITRE);
        container = frame.getContentPane();

        lblTitre = new JLabel();
        boutonInvite = new JButton("Invite");
        boutonSendFiles = new JButton("Send files");
        boutonBlock = new JButton("Block");
        boutonUnblock = new JButton("Unblock");

        editeur = new JEditorPane();
        scrollEditeur = new JScrollPane(editeur);

        txt = new JTextField();

        message = new MimeMessage();

        vecChat = new Vector(15);

        jfcSendFiles = new JFileChooser();

        lblProgression = new JLabel();
    }

    public void configurerComposants() {
        msg.titre = MsnConstantes.TITRE;

        //permet de fermer la fenetre de chat avec le bouton ESC
        AbstractAction fermer = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        };

        frame.getRootPane().getActionMap().put("fermer", fermer);
        int stdMask = Toolkit.getDefaultToolkit().
                      getMenuShortcutKeyMask();
        InputMap im =
                frame.getRootPane().getInputMap(JComponent.
                                                WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "fermer");

        frame.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == e.VK_ESCAPE) {

                    for (int i = 0; i < vecChat.size(); i++) {
                        GUIChat temp = (GUIChat) vecChat.get(i);
                        if (temp.getEmail().equalsIgnoreCase(email)) {
                            vecChat.remove(i);
                            try {
                                session.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            frame.setVisible(false);
                        }
                    }

                }
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }

        });
        frame.addWindowListener(new WindowListener() {
            public void windowActivated(WindowEvent e) {
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                for (int i = 0; i < vecChat.size(); i++) {
                    GUIChat temp = (GUIChat) vecChat.get(i);
                    if (temp.getEmail().equalsIgnoreCase(email)) {
                        vecChat.remove(i);
                        try {
                            session.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        frame.setVisible(false);
                    }
                }
            }

            public void windowDeactivated(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowOpened(WindowEvent e) {
            }

        });

        frame.setSize(450, 490);
        frame.setLocation(200, 150);

        container.setLayout(null);

        scrollEditeur.setAutoscrolls(true);

        frame.setFocusable(false);
        boutonInvite.setFocusable(false);
        boutonSendFiles.setFocusable(false);
        boutonBlock.setFocusable(false);
        boutonUnblock.setFocusable(false);
        editeur.setFocusable(false);
        scrollEditeur.setFocusable(false);

        lblProgression.setFocusable(false);

        txt.setFocusable(true);
        txt.grabFocus();
        txt.requestFocus();

        editeur.setEditable(false);
//        editeur.setBackground(Color.BLACK);


        jfcSendFiles.setDialogTitle(MsnConstantes.TITRE + " envoie de fichiers");
        jfcSendFiles.setApproveButtonText("Envoyer");
        jfcSendFiles.setMultiSelectionEnabled(true);

        lblTitre.setText("Pseudo: ");

        boutonInvite.addActionListener(this);
        boutonSendFiles.addActionListener(this);
        boutonBlock.addActionListener(this);
        boutonUnblock.addActionListener(this);
        txt.addActionListener(this);

        boutonInvite.setToolTipText("Invite un autre contact à la conversation");
        boutonSendFiles.setToolTipText("Envoie plusieurs fichiers");
        boutonBlock.setToolTipText("Bloque le contact");
        boutonUnblock.setToolTipText("Débloque le contact");

        ajouterComposant(container, txt, 0, 370, 400, 60);
        ajouterComposant(container, lblTitre, 0, 0, frame.getWidth(), 20);
        ajouterComposant(container, boutonInvite, 0, 20, 100, 40);
        ajouterComposant(container, boutonSendFiles, 100, 20, 100, 40);
        ajouterComposant(container, boutonBlock, 200, 20, 100, 40);
        ajouterComposant(container, boutonUnblock, 300, 20, 100, 40);
        ajouterComposant(container, lblProgression, 0, 430, 400, 60);

        ajouterComposant(container, scrollEditeur, 0, 60, 400, 300);

        message.setFontColor(Color.BLACK);
        message.setKind(MimeMessage.KIND_MESSAGE);

        frame.setVisible(true);

    }

    public void ajouterComposant(Container c, Component comp, int x, int y,
                                 int x1, int y1) {
        comp.setBounds(x, y, x1, y1);
        c.add(comp);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boutonInvite) {
            //invite un contact à la conversation
            String email = JOptionPane.showInputDialog(null,
                    "Entrez l'e-mail de la personne à inviter",
                    MsnConstantes.TITRE, JOptionPane.QUESTION_MESSAGE);
            if (!session.isInFriend(email)) {
                try {
                    session.inviteFriend(email);
                    editeur.setText(editeur.getText() + "\n" + email +
                                    " fait parti de la conversation" + "\n\n");
                } catch (IOException ex2) {
                    msg.msge("Impossible d'inviter " + email);
                }
            }
        } else if (e.getSource() == boutonSendFiles) {
            //envoie de fichiers
            int choix = jfcSendFiles.showOpenDialog(null);
            if (choix == jfcSendFiles.APPROVE_OPTION) {
                File[] tabFiles = jfcSendFiles.getSelectedFiles();
                for (int i = 0; i < tabFiles.length; i++) {
                    if (tabFiles[i].isFile() && session.isInFriend(email)) {
                        try {
                            msn.sendFileRequest(email, tabFiles[i], session);
                        } catch (IOException ex5) {
                            msg.msge("Impossible d'envoyer le fichier " +
                                     tabFiles[i] + " à " + email);
                        }
                    }
                }
            }
        } else if (e.getSource() == boutonBlock) {
            try {
                msn.blockFriend(email);
                //permutation
                boutonUnblock.setEnabled(true);
                boutonBlock.setEnabled(false);
            } catch (IOException ex3) {
                msg.msge("Impossible de bloquer " + email);
            }
        } else if (e.getSource() == boutonUnblock) {
            try {
                msn.unBlockFriend(email);
                //permutation
                boutonBlock.setEnabled(true);
                boutonUnblock.setEnabled(false);
            } catch (IOException ex4) {
                msg.msge("Impossible de débloquer " + email);
            }
        } else if (e.getSource() == txt) {
            //msn.getOwner().getFormattedFriendlyName() ne fonctionne pas correctement
            editeur.setText(editeur.getText() +
                            msn.getLoginName() + ":" +
                            "\n" + txt.getText() + "\n\n");
            message.setMessage(txt.getText());
            txt.setText("");
            editeur.selectAll();

            if (session.isInFriend(email)) {
                try {
                    session.sendInstantMessage(message);
                } catch (IOException ex) {
                    msg.msge("Le message ne s'est pas rendu au destinataire");
                }
            } else {
                try {
//                    msn.doCall(email);
                    session = msn.doCallWait(email);
                    while (session == null) {
                        session = msn.doCallWait(email);
                    }
                    session.sendInstantMessage(message);
                } catch (IOException ex1) {
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    public String getID() {
        return session.getSessionId();
    }

    public String getEmail() {
        return email;
    }

    /**
     * vecteur avec toutes les conversations, session ID, les fenetres et les emails
     * @param vecChat Vector
     */
    public void setChatVector(Vector vecChat) {
        this.vecChat = vecChat;
    }

    public Vector getChatVector() {
        return vecChat;
    }

    /**
     * ajoute un message à l'éditeur
     * @param message String
     */
    public void append(String message) {
        editeur.setText(editeur.getText() + pseudo + ":" + "\n" + message +
                        "\n\n");
        editeur.selectAll();
        lblProgression.setText("");
    }

    public void setSession(SwitchboardSession ss) {
        this.session = ss;
    }

    public SwitchboardSession getSession() {
        return session;
    }

    public void closeSession() {
        try {
            this.session.close();
        } catch (IOException ex) {
        }
    }

    //fenetre visible
    public boolean isVisible() {
        return frame.isVisible();
    }

    /* Copyright (C) 2006 Hassen Ben Tanfous
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
     * MyMsnListener.java
     * @author Hassen Ben Tanfous
     */
    private class MyMsnListener extends MsnAdapter {
        /**
         *
         * @param ss SwitchboardSession
         * @param friend MsnFriend
         * @param typingUser String
         */
        public void progressTyping(SwitchboardSession ss, MsnFriend friend,
                                   String typingUser) {
            lblProgression.setText("");
            lblProgression.setText("<html>" + friend.getFormattedFriendlyName() +
                                   "<br>" +
                                   " est entrain de tapper un message </html>");
        }
    }
}
