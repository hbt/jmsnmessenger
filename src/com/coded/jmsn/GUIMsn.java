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
 * GUIMsn.java
 * est le coeur du programme JMSN.
 * affichage de contacts (par pseudo, emails)
 * boiteVocale
 * changement de statut
 * gestion contacts (block complet, block partiel, parler en offline, unblock, ajout
 * delete)
 * changement pseudo
 * reception de messages
 * logger complet (session ID, cookie, authentification cookie, IP etc)
 * envoie de fichiers, reception de fichiers
 * envoie de messages à tous, envoie de fichiers à tous
 * affichage d'informations sur un contact
 * Date: 07/01/2006
 * @author Hassen Ben Tanfous
 */

//imports
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import com.coded.jmsn.log.*;
import com.coded.jmsn.msg.*;
import com.coded.jmsn.outils.*;
import com.coded.jmsn.send.*;
import rath.msnm.*;
import rath.msnm.entity.*;
import rath.msnm.event.*;
import rath.msnm.ftp.*;
import rath.msnm.msg.*;

public class GUIMsn extends JFrame implements GUIComposants, ActionListener {

    private Container container;

    private JMenuBar barre;
    private JMenu menuFichier,
            menuContacts,
            menuActions;

    //menu fichier
    private JMenuItem itemExit;

    private JMenu menuStatut;
    private JMenuItem itemOnline,
            itemBusy,
            itemBrb,
            itemAway,
            itemPhone,
            itemLunch,
            itemOffline;
    private JMenu menuPseudo;
    private JMenuItem itemChangerPseudo;

    //menu contacts
    private JMenuItem itemAdd,
            itemRemove;

    private JMenu menuBlock;
    private JMenuItem itemBlockComplet,
            itemBlockPartiel;

    private JMenu menuUnblock;
    private JMenuItem itemUnblockComplet,
            itemUnblockPartiel;

    private JMenuItem itemContactsAfficher;

    //menu actions
    private JMenuItem itemSendMessage,
            itemSendFile;

    private JMenu menuAutoActions;
    private JMenuItem itemKeywordAction;

    //fin menu

    private JLabel lblPseudo,
            lblListeOnline,
            lblListeOffline,
            lblLogger;

//    private Vector vecPseudo; //pseudo + status

//    private JComboBox comboStatus; //pseudo + status

    private JScrollPane scrollListeOnline,
            scrollListeOffline;

    private JList listeOnline,
            listeOffline;

    private DefaultListModel listeOnlineEmail,
            listeOnlinePseudo,
            listeOfflineEmail,
            listeOfflinePseudo;

    private JRadioButton radioEmail, //affichage par email
            radioPseudo; //affichage par pseudo

    private JPopupMenu menuContactOnline, //menu de right-click sur liste
            menuContactOffline;

    private JMenuItem itemShortMessage, //raccourci via la liste de contacts
            itemShortBlockOnline,
            itemShortUnblockOnline,
            itemShortRemoveOnline,
            itemShortBlockOffline,
            itemShortUnblockOffline,
            itemShortRemoveOffline;

    private JTextPane afficherInfosContacts; //menu: contacts/afficherInfos;

    private MSNMessenger msn;

    private Messagerie msg;

    private String tempEmail; //utilisée pour stocker le mail temporaire de chaque sélection

    private Vector vecBlockPartiel; //utilisée pour garder la liste des contacts bloquer partiellement

    private Vector vecChat; //vecteur contenant les fenêtres chat

    private JFileChooser jfcReceiver; //receveur de fichiers lors d'un envoie

    private MyLogger logger;

    private JTextField txtPseudo;

    private HashMap hashCommandes; //contient les commandes originales

    private BoiteVocale boiteVocale; //messagerie automatique
//
//    private GUISendFiles envoieFichiers;
//    private GUISendMessage envoieMessages;

    public GUIMsn() {
        instancierComponsants();
        configurerComposants();
    }

    public GUIMsn(MSNMessenger messenger) {
        this();
        this.msn = messenger;

        msn.addMsnListener(new MyMsnListener());
    }

    public void instancierComponsants() {
        container = getContentPane();

        barre = new JMenuBar();

        //menu fichier
        menuFichier = new JMenu("Fichier");
        itemExit = new JMenuItem("Exit");

        menuStatut = new JMenu("Mon statut");
        itemOnline = new JMenuItem("Online");
        itemBusy = new JMenuItem("Busy");
        itemBrb = new JMenuItem("Be right back");
        itemAway = new JMenuItem("Away");
        itemPhone = new JMenuItem("On the phone");
        itemLunch = new JMenuItem("Out to lunch");
        itemOffline = new JMenuItem("Appear offline");

        menuPseudo = new JMenu("Pseudo");
        itemChangerPseudo = new JMenuItem("Changer pseudo");

        //menu contacts
        menuContacts = new JMenu("Contacts");
        itemAdd = new JMenuItem("Add contact");
        itemRemove = new JMenuItem("Remove contact");

        menuBlock = new JMenu("Block contact");
        itemBlockComplet = new JMenuItem("Complètement");
        itemBlockPartiel = new JMenuItem("Partiellement");

        menuUnblock = new JMenu("Unblock");
        itemUnblockComplet = new JMenuItem("Complètement");
        itemUnblockPartiel = new JMenuItem("Partiellement");

        itemContactsAfficher = new JMenuItem("Afficher informations");

        //menu actions
        menuActions = new JMenu("Actions");

        itemSendMessage = new JMenuItem("Envoyer un message");
        itemSendFile = new JMenuItem("Envoyer un fichier");

        menuAutoActions = new JMenu("AUTOMATIC ACTIONS");
//        itemSimpleAction = new JMenuItem("Simple Réponse");
        itemKeywordAction = new JMenuItem("Action par mot-clé");

        lblPseudo = new JLabel("Pseudo:");

        lblListeOnline = new JLabel("Online");
        lblListeOffline = new JLabel("Offline");

        lblLogger = new JLabel("Logger");

        //pseudo + status
//        vecPseudo = new Vector(1 + MsnConstantes.tabPersoStatus.length);

//        comboStatus = new JComboBox(vecPseudo);

        listeOnlinePseudo = new DefaultListModel();
        listeOnlineEmail = new DefaultListModel();

        listeOfflinePseudo = new DefaultListModel();
        listeOfflineEmail = new DefaultListModel();

        listeOnline = new JList(listeOnlinePseudo);
        listeOffline = new JList(listeOfflinePseudo);

        scrollListeOnline = new JScrollPane(listeOnline);
        scrollListeOffline = new JScrollPane(listeOffline);

        afficherInfosContacts = new JTextPane();

        radioEmail = new JRadioButton("Par Email");
        radioPseudo = new JRadioButton("Par Pseudo");

        menuContactOnline = new JPopupMenu();
        menuContactOffline = new JPopupMenu();

        itemShortMessage = new JMenuItem();
        itemShortBlockOnline = new JMenuItem();
        itemShortUnblockOnline = new JMenuItem();
        itemShortRemoveOnline = new JMenuItem();
        itemShortBlockOffline = new JMenuItem();
        itemShortUnblockOffline = new JMenuItem();
        itemShortRemoveOffline = new JMenuItem();

        vecBlockPartiel = new Vector(20);
        vecChat = new Vector(15);

        jfcReceiver = new JFileChooser();

        logger = new MyLogger();

        txtPseudo = new JTextField();

        hashCommandes = new HashMap();

        boiteVocale = new BoiteVocale();
    }

    public void configurerComposants() {
        msg.titre = MsnConstantes.TITRE;

        container.setLayout(null);
        //menu Fichier
        itemExit.setToolTipText("Ferme JMSN");
        itemExit.addActionListener(new EcouteursMenuFichier());

        itemOnline.addActionListener(new EcouteursMenuFichier());
        itemBusy.addActionListener(new EcouteursMenuFichier());
        itemBrb.addActionListener(new EcouteursMenuFichier());
        itemAway.addActionListener(new EcouteursMenuFichier());
        itemPhone.addActionListener(new EcouteursMenuFichier());
        itemLunch.addActionListener(new EcouteursMenuFichier());
        itemOffline.addActionListener(new EcouteursMenuFichier());

        menuStatut.add(itemOnline);
        menuStatut.add(itemBusy);
        menuStatut.add(itemBrb);
        menuStatut.add(itemAway);
        menuStatut.add(itemPhone);
        menuStatut.add(itemLunch);
        menuStatut.add(itemOffline);

        itemChangerPseudo.addActionListener(new EcouteursMenuFichier());
        menuPseudo.add(itemChangerPseudo);

        menuFichier.addSeparator();
        menuFichier.add(menuStatut);
        menuFichier.addSeparator();
        menuFichier.add(menuPseudo);
        menuFichier.add(itemExit);

        //menu contacts
        itemAdd.setToolTipText("Ajoute un contact");
        itemAdd.addActionListener(new EcouteursMenuContacts());
        menuContacts.add(itemAdd);
        itemRemove.setToolTipText("Retire un contact");
        itemRemove.addActionListener(new EcouteursMenuContacts());
        menuContacts.add(itemRemove);

        menuContacts.addSeparator();

        itemBlockComplet.setToolTipText(
                "Le contact ne vous voit plus, mais vous pouvez lui parler en offline");
        itemBlockComplet.addActionListener(new EcouteursMenuContacts());
        itemBlockPartiel.setToolTipText(
                "Le contact vous voit mais ne peut pas vous parler");
        itemBlockPartiel.addActionListener(new EcouteursMenuContacts());
        menuBlock.add(itemBlockComplet);
        menuBlock.add(itemBlockPartiel);

        menuContacts.add(menuBlock);
        menuContacts.addSeparator();

        itemUnblockComplet.setToolTipText("Débloquer le contact");
        itemUnblockComplet.addActionListener(new EcouteursMenuContacts());
        itemUnblockPartiel.setToolTipText(
                "Débloquer le contact pour qu'il puisse vous parler");
        itemUnblockPartiel.addActionListener(new EcouteursMenuContacts());
        menuUnblock.add(itemUnblockComplet);
        menuUnblock.add(itemUnblockPartiel);

        menuContacts.add(menuUnblock);
        menuContacts.addSeparator();

        itemContactsAfficher.setToolTipText(
                "Affiche des informations sur un contact");
        itemContactsAfficher.addActionListener(new EcouteursMenuContacts());
        menuContacts.add(itemContactsAfficher);

        //menu actions
        itemSendMessage.addActionListener(new EcouteursMenuActions());
        menuActions.add(itemSendMessage);
        itemSendFile.addActionListener(new EcouteursMenuActions());
        menuActions.add(itemSendFile);

        menuActions.addSeparator();

//        itemSimpleAction.setToolTipText("Envoie un message lorsque " +
//                                        "un contact démarre une fenêtre");
//        itemSimpleAction.addActionListener(new EcouteursMenuActions());
        itemKeywordAction.setToolTipText(
                "<html>Exécute une action par mot clé <br>" +
                "EX: lorsqu'un contact dit <b>Salut</b> une action s'éxecute <b>reponse ou fichier</b>");
        itemKeywordAction.addActionListener(new EcouteursMenuActions());

//        menuAutoActions.add(itemSimpleAction);
        menuAutoActions.add(itemKeywordAction);
        menuActions.add(menuAutoActions);

        barre.add(menuFichier);
        barre.add(menuContacts);
        barre.add(menuActions);

        //combo status et pseudo
//        comboStatus.addActionListener(this);
        txtPseudo.setEditable(false);

        //radio
        //par pseudo
        radioPseudo.setSelected(true);
        //par email
        radioEmail.setSelected(false);

        radioEmail.addActionListener(this);
        radioPseudo.addActionListener(this);

        //menu popupmenu de liste
        itemShortMessage.addActionListener(new EcouteursPopListe());
        itemShortBlockOnline.addActionListener(new EcouteursPopListe());
        itemShortUnblockOnline.addActionListener(new EcouteursPopListe());
        itemShortRemoveOnline.addActionListener(new EcouteursPopListe());
        itemShortBlockOffline.addActionListener(new EcouteursPopListe());
        itemShortUnblockOffline.addActionListener(new EcouteursPopListe());
        itemShortRemoveOffline.addActionListener(new EcouteursPopListe());

        menuContactOnline.add(itemShortMessage);
        menuContactOnline.addSeparator();
        menuContactOnline.add(itemShortBlockOnline);
        menuContactOnline.add(itemShortUnblockOnline);
        menuContactOnline.addSeparator();
        menuContactOnline.add(itemShortRemoveOnline);

        menuContactOffline.add(itemShortBlockOffline);
        menuContactOffline.add(itemShortUnblockOffline);
        menuContactOffline.addSeparator();
        menuContactOffline.add(itemShortRemoveOffline);

        //liste
        listeOnline.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listeOffline.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listeOnline.add(menuContactOnline);
        listeOffline.add(menuContactOffline);

        listeOffline.addMouseListener(alListeOffline);
        listeOnline.addMouseListener(alListeOnline);

        afficherInfosContacts.setFont(new Font("Arial", Font.PLAIN, 11));
        afficherInfosContacts.setEnabled(false);

        //logger
        ajouterComposant(container, logger.getPanel(), 720, 100, 275, 600);

        ajouterComposant(container, barre, 10, 10, 200, 20);
        ajouterComposant(container, lblPseudo, 10, 50, 100, 20);
//        ajouterComposant(container, comboStatus, 100, 50, 200, 20);
        ajouterComposant(container, txtPseudo, 100, 50, 200, 20);
        ajouterComposant(container, lblListeOnline, 50, 80, 100, 20);
        ajouterComposant(container, scrollListeOnline, 50, 100, 250, 600);

        ajouterComposant(container, radioEmail, 330, 350, 100, 20);
        ajouterComposant(container, radioPseudo, 330, 390, 100, 20);

        ajouterComposant(container, lblListeOffline, 450, 80, 100, 20);
        ajouterComposant(container, scrollListeOffline, 450, 100, 250, 600);

        setTitle(MsnConstantes.TITRE);
        setExtendedState(MAXIMIZED_BOTH);
        setSize (500, 500);
        setLocation (0, 0);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void ajouterComposant(Container c, Component comp, int x, int y,
                                 int x1, int y1) {
        comp.setBounds(x, y, x1, y1);
        c.add(comp);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == radioEmail) {
            radioPseudo.setSelected(false);
            //permutation
            listeOnline.setModel(listeOnlineEmail);
            listeOffline.setModel(listeOfflineEmail);
        } else if (e.getSource() == radioPseudo) {
            radioEmail.setSelected(false);
            //permutation
            listeOnline.setModel(listeOnlinePseudo);
            listeOffline.setModel(listeOfflinePseudo);
        }
//        else if (e.getSource() == comboStatus) {
//            if (comboStatus.getSelectedIndex() ==
//                comboStatus.getItemCount() - 1) {
//                String pseudo = JOptionPane.showInputDialog(null,
//                        "Entrez votre nouveau pseudo",
//                        MsnConstantes.TITRE,
//                        JOptionPane.QUESTION_MESSAGE);
//                if (pseudo != null) {
//                    try {
//                        msn.setMyFriendlyName(pseudo);
//                        comboStatus.setSelectedIndex(0);
//                        vecPseudo.set(0, pseudo);
//                        logger.println("Confirmation de votre pseudo " + pseudo);
//                    } catch (IOException ex) {
//                        msg.msge("Impossible de changer votre pseudo");
//                    }
//                }
//            if (comboStatus.getSelectedIndex() !=
//                comboStatus.getItemCount() - 1 &&
//                comboStatus.getSelectedIndex() != 0) {
//                //ni le premier, ni le dernier
//                try {
//                    msn.setMyStatus(MsnConstantes.tabRathStatus[comboStatus.
//                                    getSelectedIndex() - 1]);
//                    comboStatus.setSelectedIndex(0);
//
//                    logger.println("Votre nouveau statut est: " +
//                                   MsnConstantes.tabPersoStatus[comboStatus.
//                                   getSelectedIndex() - 1]);
//                } catch (IOException ex1) {
//                    msg.msge("Impossible de changer le statut");
//                }
//            }
//        }
    }

    /**
     * mouselister pour gerer les evenements relies a la souris sur la liste
     * online
     */
    private MouseListener alListeOnline = new MouseListener() {
        public void mouseClicked(MouseEvent e) {
            itemShortMessage.setEnabled(true);
            itemShortBlockOnline.setEnabled(true);
            itemShortUnblockOnline.setEnabled(true);

            boolean trouve = false;

            if (SwingUtilities.isRightMouseButton(e) &&
                !listeOnline.isSelectionEmpty()) {

                tempEmail = listeOnlineEmail.get(listeOnline.
                                                 getSelectedIndex()).toString();

                //permet à l'utilisateur d'accéder au menu via le bouton droit
                itemShortMessage.setText("Send a message to " + tempEmail);
                itemShortBlockOnline.setText("Block " + tempEmail);
                itemShortUnblockOnline.setText("Unblock " + tempEmail);
                itemShortRemoveOnline.setText("Delete " + tempEmail);

                //si le contact est lui-même
                if (tempEmail.equalsIgnoreCase(msn.getLoginName())) {
                    itemShortMessage.setEnabled(false);
                }

                //parcous la block liste et permute les boutons block et unblock
                BuddyList blockListe = msn.getBuddyGroup().getBlockList();
                for (int i = 0; i < blockListe.size() && !trouve; i++) {
                    if (blockListe.get(i).getLoginName().equalsIgnoreCase(
                            tempEmail)) {
                        itemShortBlockOnline.setEnabled(false);
                        itemShortMessage.setEnabled(false);
                        trouve = true;
                    }
                }

                if (!trouve) {
                    itemShortUnblockOnline.setEnabled(false);
                }

                menuContactOnline.show(listeOnline, e.getX(), e.getY());
            } else if (SwingUtilities.isLeftMouseButton(e) &&
                       e.getClickCount() == 2 && !listeOnline.isSelectionEmpty()) {
                //démarrage d'une fenetre chat
                String email = listeOnlineEmail.get(listeOnline.
                        getSelectedIndex()).
                               toString();
                String pseudo = listeOnlinePseudo.get(listeOnline.
                        getSelectedIndex()).toString();
                try {
                    SwitchboardSession session = msn.doCallWait(email);
                    while (session == null) {
                        session = msn.doCallWait(email);
                    }
                    GUIChat temp = new GUIChat(msn, email, pseudo, session);
                    temp.start();
                    vecChat.add(temp);
                } catch (IOException ex) {
                } catch (InterruptedException ex) {
                    /** @todo Gérer cette exception */
                }
            } else if (SwingUtilities.isLeftMouseButton(e) &&
                       e.getClickCount() == 1 && !listeOnline.isSelectionEmpty()) {
                //affichage des informations sur le contact dans le Logger
                MsnFriend tempFriend = msn.getBuddyGroup().getForwardList().get(
                        listeOnlineEmail.get(listeOnline.getSelectedIndex()).
                        toString());
                if (tempFriend != null) {
                    logger.println("Access value: " + tempFriend.getAccessValue());
                    logger.println("Code: " + tempFriend.getCode());
                    logger.println("Pseudo: " + tempFriend.getFriendlyName());
                    logger.println("Group Index: " + tempFriend.getGroupIndex());
                    logger.println("Login name: " + tempFriend.getLoginName());
                    logger.println("Old Status: " + tempFriend.getOldStatus());
                    logger.println("Status: " + tempFriend.getStatus());
                }
            }
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }
    };

    /**
     * mouselister pour gerer les evenements relies a la souris sur la liste
     * offline
     */
    private MouseListener alListeOffline = new MouseListener() {
        public void mouseClicked(MouseEvent e) {
            itemShortBlockOffline.setEnabled(true);
            itemShortUnblockOffline.setEnabled(true);
            boolean trouve = false;

            if (SwingUtilities.isRightMouseButton(e) &&
                !listeOffline.isSelectionEmpty()) {

                tempEmail = listeOfflineEmail.get(listeOffline.
                                                  getSelectedIndex()).toString();

                //permet à l'utilisateur d'accéder au menu via le bouton droit
                itemShortBlockOffline.setText("Block " + tempEmail);
                itemShortUnblockOffline.setText("Unblock " + tempEmail);
                itemShortRemoveOffline.setText("Delete " + tempEmail);

                BuddyList blockListe = msn.getBuddyGroup().getBlockList();
                for (int i = 0; i < blockListe.size() && !trouve; i++) {
                    if (blockListe.get(i).getLoginName().equalsIgnoreCase(
                            tempEmail)) {
                        itemShortBlockOffline.setEnabled(false);
                        trouve = true;
                    }
                }

                if (!trouve) {
                    itemShortUnblockOffline.setEnabled(false);
                }

                menuContactOffline.show(listeOffline, e.getX(), e.getY());
            }
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }
    };

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
     * EcouteursPopListe.java
     * gère les evenements du menu pop (envoie de messages, block, unblock, delete)
     * Date: 05/01/2006
     * @author Hassen Ben Tanfous
     */
    private class EcouteursPopListe implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            boolean trouve;
            if (e.getSource() == itemShortMessage) {
                //démarrage d'une fenetre chat
                String email = listeOnlineEmail.get(listeOnline.
                        getSelectedIndex()).
                               toString();
                String pseudo = listeOnlinePseudo.get(listeOnline.
                        getSelectedIndex()).toString();
                logger.println("Démarrage de la conversation avec " + pseudo +
                               " <" + email + ">");
                try {
//                    msn.doCall(email);
                    SwitchboardSession session = msn.doCallWait(email);
                    while (session == null) {
                        session = msn.doCallWait(email);
                    }
                    GUIChat temp = new GUIChat(msn, email, pseudo, session);
                    temp.start();
                    vecChat.add(temp);
                    //affichage informations
                    logger.println("Conversation démarré avec " + pseudo + " <" +
                                   email + ">");
                    logger.println("Active count: " + session.activeCount());
                    logger.println("Cookie: " + session.getCookie());
                    logger.println("Friend count: " + session.getFriendCount());
                    logger.println("ServerName: " + session.getServerName());
                    logger.println("Session ID: " + session.getSessionId());
                    logger.println("Target: " + session.getTarget());
                    logger.println("TimeOut: " + session.getTimeout());
                } catch (IOException ex) {
                } catch (InterruptedException ex) {
                    /** @todo Gérer cette exception */
                }
            } else if (e.getSource() == itemShortBlockOnline) {
                //bloque contact liste online
                try {
                    msn.blockFriend(tempEmail);
                    logger.println(tempEmail +
                                   " est bloqué, vous pouvez lui parler en mode offline");
                } catch (IOException ex) {
                    msg.msge("Impossible de bloquer le contact " + tempEmail);
                }
            } else if (e.getSource() == itemShortUnblockOnline) {
                //unblock contact liste online
                try {
                    msn.unBlockFriend(tempEmail);
                    logger.println(tempEmail + " est débloque");
                } catch (IOException ex1) {
                    msg.msge("Impossible de débloquer le contact " + tempEmail);
                }
            } else if (e.getSource() == itemShortRemoveOnline) {
                trouve = false; //arrete la recherche quand trouve
                //remove contact liste online
                try {
                    msn.removeFriend(tempEmail);
                    logger.println(tempEmail + " a été retiré de votre liste");
                    for (int i = 0; i < listeOnlineEmail.size() && !trouve; i++) {
                        if (listeOnlineEmail.get(i).toString().equalsIgnoreCase(
                                tempEmail)) {
                            listeOnlineEmail.remove(i);
                            listeOnlinePseudo.remove(i);
                            trouve = true;
                        }
                    }
                } catch (IOException ex2) {
                    msg.msge("Impossible de supprimer le contact " + tempEmail);
                }

            } else if (e.getSource() == itemShortBlockOffline) {
                //block contact liste offline
                try {
                    msn.blockFriend(tempEmail);
                    logger.println(tempEmail + " est bloqué");
                } catch (IOException ex3) {
                    msg.msge("Impossible de bloquer le contact " + tempEmail);
                }
            } else if (e.getSource() == itemShortUnblockOffline) {
                //unblock contact liste offline
                try {
                    msn.unBlockFriend(tempEmail);
                    logger.println(tempEmail + " est débloque");
                } catch (IOException ex4) {
                    msg.msge("Impossible de débloquer le contact " + tempEmail);
                }
            } else if (e.getSource() == itemShortRemoveOffline) {
                trouve = false; //arrete la recherche quand la variable est trouve
                try {
                    msn.removeFriend(tempEmail);
                    logger.println(tempEmail + " a été retiré de votre liste");
                    for (int i = 0; i < listeOfflineEmail.size() && !trouve; i++) {
                        if (listeOfflineEmail.get(i).toString().
                            equalsIgnoreCase(tempEmail)) {
                            listeOfflineEmail.remove(i);
                            listeOfflinePseudo.remove(i);
                            trouve = true;
                        }
                    }
                } catch (IOException ex5) {
                    msg.msge("Impossible de supprimer le contact " + tempEmail);
                }
            }
        }
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

     * EcouteursMenuFichier.java
     * écoute les évènements sur les items dur menu fichier (exit, changement de statut,
     * changement de pseudo)
     * Date: 07/01/2006
     * @author Hassen Ben Tanfous
     */
    private class EcouteursMenuFichier implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == itemExit) {
                //fermeture du programme
                try {
                    msn.logout();
                } catch (Exception ee) {

                } finally {
                    System.exit(0);
                }
            } else if (e.getSource() == itemOnline) {
                try {
                    msn.setMyStatus(UserStatus.ONLINE);
                    logger.println("Changement de statut pour online");
                } catch (IOException ex) {
                    msg.msge("Impossible de changer de statut");
                }

            } else if (e.getSource() == itemBusy) {
                try {
                    msn.setMyStatus(UserStatus.BUSY);
                    logger.println("Changement de statut pour busy");
                } catch (IOException ex) {
                    msg.msge("Impossible de changer de statut");
                }
            } else if (e.getSource() == itemBrb) {
                try {
                    msn.setMyStatus(UserStatus.BE_RIGHT_BACK);
                    logger.println("Changement de statut pour brb");
                } catch (IOException ex) {
                    msg.msge("Impossible de changer de statut");
                }
            } else if (e.getSource() == itemAway) {
                try {
                    msn.setMyStatus(UserStatus.IDLE);
                    logger.println("Changement de statut pour away");
                } catch (IOException ex) {
                    msg.msge("Impossible de changer de statut");
                }
            } else if (e.getSource() == itemPhone) {
                try {
                    msn.setMyStatus(UserStatus.ON_THE_PHONE);
                    logger.println("Changement de statut pour on the phone");
                } catch (IOException ex) {
                    msg.msge("Impossible de changer de statut");
                }
            } else if (e.getSource() == itemLunch) {
                try {
                    msn.setMyStatus(UserStatus.ON_THE_LUNCH);
                    logger.println("Changement de statut pour out to lunch");
                } catch (IOException ex) {
                    msg.msge("Impossible de changer de statut");
                }
            } else if (e.getSource() == itemOffline) {
                try {
                    msn.setMyStatus(UserStatus.OFFLINE);
                    logger.println("Changement de statut pour appear offline");
                } catch (IOException ex) {
                    msg.msge("Impossible de changer de statut");
                }
            } else if (e.getSource() == itemChangerPseudo) {
                String pseudo = JOptionPane.showInputDialog(null,
                        "Entrez votre nouveau pseudo", MsnConstantes.TITRE,
                        JOptionPane.QUESTION_MESSAGE);
                if (pseudo != null) {
                    try {
                        msn.setMyFriendlyName(pseudo);
                    } catch (IOException ex1) {
                        msg.msge("Impossible de changer de pseudo");
                    }
                    txtPseudo.setText(pseudo);
                    logger.println("Pseudo modifié pour: " + pseudo);
                }
            }
        }
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
     * EcouteursMenuContacts.java
     * écoute les évènements sur les items dur menu contact
     * ajout de contact, block complet, partiel, unblock complet et partiel
     * remove contact
     * affichage d'informations
     * Date: 07/01/2006
     * @author Hassen Ben Tanfous
     */
    private class EcouteursMenuContacts implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String email;
            if (e.getSource() == itemAdd) {
                //ajout de contact
                email = JOptionPane.showInputDialog(null,
                        "Entrez l'e-mail de la personne à ajouter",
                        MsnConstantes.TITRE,
                        JOptionPane.QUESTION_MESSAGE);
                try {
                    msn.addFriend(email);
                    logger.println(email + " est ajouté à votre liste");
                } catch (IOException ex) {
                    msg.msge("Email invalide");
                }
            } else if (e.getSource() == itemRemove) {
                boolean trouve = false;
                //retire un contact
                email = JOptionPane.showInputDialog(null,
                        "Entrez l'email du contact à retirer",
                        MsnConstantes.TITRE, JOptionPane.QUESTION_MESSAGE);

                try {
                    msn.removeFriend(email);
                    logger.println(email + " est retiré de votre liste");
                    for (int i = 0; i < listeOnlineEmail.size() && !trouve; i++) {
                        if (listeOnlineEmail.get(i).toString().equalsIgnoreCase(
                                email)) {
                            listeOnlineEmail.remove(i);
                            listeOnlinePseudo.remove(i);
                            trouve = true;
                        }
                    }

                    if (!trouve) {
                        for (int i = 0; i < listeOfflineEmail.size() && !trouve;
                                     i++) {
                            if (listeOfflineEmail.get(i).toString().
                                equalsIgnoreCase(email)) {
                                listeOfflineEmail.remove(i);
                                listeOfflinePseudo.remove(i);
                                trouve = true;
                            }
                        }
                    }
                } catch (IOException ex1) {
                    msg.msge("Impossible de supprimer le contact " + email);
                }
            } else if (e.getSource() == itemBlockComplet) {
                email = JOptionPane.showInputDialog(null,
                        "Entrez l'e-mail du contact à bloquer complètement",
                        MsnConstantes.TITRE, JOptionPane.QUESTION_MESSAGE);
                try {
                    msn.blockFriend(email);
                    logger.println(tempEmail +
                                   " est bloqué, vous pouvez lui parler en mode offline");
                } catch (IOException ex2) {
                    msg.msge("Impossible de bloquer le contact " + email);
                }
            } else if (e.getSource() == itemBlockPartiel) {
                email = JOptionPane.showInputDialog(null,
                        "Entrez l'email du contact à bloquer partiellement",
                        MsnConstantes.TITRE, JOptionPane.QUESTION_MESSAGE);
                vecBlockPartiel.add(email);
                logger.println(email +
                               " est bloqué partiellement, le contact vous voit\n mais ne peut vous parler");
            } else if (e.getSource() == itemUnblockComplet) {
                email = JOptionPane.showInputDialog(null,
                        "Entrez l'e-mail du contact à débloquer complètement",
                        MsnConstantes.TITRE, JOptionPane.QUESTION_MESSAGE);
                try {
                    msn.unBlockFriend(email);
                    logger.println(tempEmail + " est débloque");
                } catch (IOException ex3) {
                    msg.msge("Impossible de débloquer le contact " + email);
                }
            } else if (e.getSource() == itemUnblockPartiel) {
                email = JOptionPane.showInputDialog(null,
                        "Entrez l'e-mail du contact à débloquer partiellement",
                        MsnConstantes.TITRE, JOptionPane.QUESTION_MESSAGE);
                for (int i = 0; i < vecBlockPartiel.size(); i++) {
                    if (vecBlockPartiel.get(i).toString().equalsIgnoreCase(
                            email)) {
                        vecBlockPartiel.remove(i);
                    }
                }
                logger.println(email + " est débloqué");
            } else if (e.getSource() == itemContactsAfficher) {
                final int TAILLE = 20;
                String contenu = "";
                email = JOptionPane.showInputDialog(null,
                        "Entrez l'e-mail du contact dont vous exigez des informations",
                        MsnConstantes.TITRE, JOptionPane.QUESTION_MESSAGE);

                MsnFriend tempFriend = msn.getBuddyGroup().getForwardList().get(
                        email);

                if (tempFriend != null) {
                    contenu += Outils.remplirEspaces("Access value: ", TAILLE) +
                            "\t" +
                            tempFriend.getAccessValue() + "\n";
                    contenu += Outils.remplirEspaces("Code: ", TAILLE) + "\t" +
                            tempFriend.getCode() + "\n";
                    contenu += Outils.remplirEspaces("FriendlyName: ", TAILLE) +
                            "\t" +
                            tempFriend.getFormattedFriendlyName() + "\n";
                    contenu += Outils.remplirEspaces("Group index: ", TAILLE) +
                            "\t" +
                            tempFriend.getGroupIndex() + "\n";
                    contenu += Outils.remplirEspaces("Login name: ", TAILLE) +
                            "\t" +
                            tempFriend.getLoginName() + "\n";
                    contenu += Outils.remplirEspaces("Old status: ", TAILLE) +
                            "\t" +
                            tempFriend.getOldStatus() + "\n";
                    contenu += Outils.remplirEspaces("Status: ", TAILLE) + "\t" +
                            tempFriend.getStatus();
                    afficherInfosContacts.setText(contenu);
                    JOptionPane.showMessageDialog(null, afficherInfosContacts,
                                                  MsnConstantes.TITRE,
                                                  JOptionPane.PLAIN_MESSAGE);

                } else {
                    msg.msge("Contact " + email + " introuvable");
                }
            }
        }
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
     * EcouteursMenuActions.java
     * gere les evenements sur les items du menu actions
     * Date: 13/01/2006
     * @author Hassen Ben Tanfous
     */
    private class EcouteursMenuActions implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == itemSendMessage) {
                new GUISendMessage(msn);
//                if (envoieMessages == null) {
//                    envoieMessages = new GUISendMessage(msn);
//                    envoieMessages.setVectorChat(vecChat);
//                } else {
//                    envoieMessages.setVisible(true);
//                    envoieMessages.setVectorChat(vecChat);
//                }

            } else if (e.getSource() == itemSendFile) {
//                if (envoieFichiers == null) {
//                    envoieFichiers = new GUISendFiles(msn);
//                } else {
//                    envoieFichiers.setVisible(true);
//                }
                new GUISendFiles(msn);

            } else if (e.getSource() == itemKeywordAction) {
                boiteVocale.setVisible(true);
            }
        }
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
     * BoiteVocale.java
     * permet d'afficher un message ou d'envoyer un fichier lorsque le contact
     * tappe un mot que vous avez configuré pour réagir.
     * Date: 12/01/2005
     * @author Hassen Ben Tanfous
     */
    private class BoiteVocale implements ActionListener {
        /** nom de fichier contenant les donnees automatique */
        public static final String FILENAME = "auto-answer.dat";

        private JFrame frame;
        private Container container;

        private JComboBox combo; //contient toutes les commandes

        private JButton boutonFlush, //flush une commandes
                boutonFlushAll; //flush toutes les commandes

        private JLabel lblMot, //premiere ligne
                lblReponse; //premiere ligne

        private JLabel lblMot2, //deuxieme ligne
                lblFichier; //deuxieme ligne

        private JTextField txtMot, //premiere ligne
                txtMot2, //deuxieme ligne
                txtReponse; //premiere ligne

        private JButton boutonSelectFiles; //selection de fichiers deuxieme ligne
        private JButton boutonAjouterFichier,
                boutonAjouterReponse;

        private JSeparator separateur1,
                separateur2;

        private JLabel lblInfos;

        private File fichier;

        private JFileChooser jfc;

        private JButton boutonSave;

        /**
         *
         */
        public BoiteVocale() {
            this.instancierComposants();
            this.configurerComposants();

            if (hashCommandes.size() == 0) {
                chargerAutomatiqueDonnees();
            }
        }

        public void instancierComposants() {
            frame = new JFrame(MsnConstantes.TITRE);
            container = frame.getContentPane();

            combo = new JComboBox();

            boutonFlush = new JButton("Flush");
            boutonFlushAll = new JButton("Flush-ALL");

            lblMot = new JLabel("Mot");
            lblReponse = new JLabel("Réponse");

            lblMot2 = new JLabel("Mot");
            lblFichier = new JLabel("Fichier");

            txtMot = new JTextField();
            txtMot2 = new JTextField();
            txtReponse = new JTextField();

            boutonSelectFiles = new JButton("Select");

            boutonAjouterFichier = new JButton("Ajouter");
            boutonAjouterReponse = new JButton("Ajouter");

            separateur1 = new JSeparator();
            separateur2 = new JSeparator();

            lblInfos = new JLabel(
                    "Ceci vous permet de réagir lorsqu'un de vos contact entre un mot");

            jfc = new JFileChooser();

            boutonSave = new JButton("Sauvegarder");
        }

        public void configurerComposants() {
            container.setLayout(null);

            boutonFlush.setToolTipText("Flush une commande");
            boutonFlush.addActionListener(this);
            boutonFlushAll.setToolTipText("Flush toutes les commandes");
            boutonFlushAll.addActionListener(this);

            boutonAjouterReponse.addActionListener(this);
            boutonAjouterFichier.addActionListener(this);
            boutonSelectFiles.setToolTipText(
                    "Sélectionnez les fichier à envoyer");
            boutonSelectFiles.addActionListener(this);

            boutonSave.setBackground(Color.red);
            boutonSave.addActionListener(this);
            boutonSave.setToolTipText("Sauvegarde les réponses");

            jfc.setDialogTitle(MsnConstantes.TITRE);
            jfc.setApproveButtonText("Select");
            jfc.setMultiSelectionEnabled(false);

            ajouterComposant(container, lblInfos, 2, 2, 450, 20);
            ajouterComposant(container, combo, 50, 25, 275, 30);
            ajouterComposant(container, boutonFlush, 350, 25, 100, 20);
            ajouterComposant(container, boutonFlushAll, 350, 50, 100, 20);
            ajouterComposant(container, separateur1, 0, 70, 500, 2);

            ajouterComposant(container, lblMot, 10, 80, 100, 20);
            ajouterComposant(container, txtMot, 10, 100, 150, 20);
            ajouterComposant(container, lblReponse, 180, 80, 100, 20);
            ajouterComposant(container, txtReponse, 180, 100, 150, 20);
            ajouterComposant(container, boutonAjouterReponse, 370, 100, 100, 20);

            ajouterComposant(container, separateur2, 0, 130, 500, 2);
            ajouterComposant(container, lblMot2, 10, 150, 100, 20);
            ajouterComposant(container, txtMot2, 10, 180, 150, 20);
            ajouterComposant(container, lblFichier, 180, 150, 100, 20);
            ajouterComposant(container, boutonSelectFiles, 180, 180, 150, 20);
            ajouterComposant(container, boutonAjouterFichier, 370, 180, 100, 20);

            ajouterComposant(container, boutonSave, 175, 240, 150, 20);

            frame.setResizable(false);
            frame.setSize(500, 300);
            frame.setLocation(200, 0);
            frame.setVisible(false);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == boutonAjouterReponse) {
                //ajoute le mot et la reponse
                hashCommandes.put(txtMot.getText(), txtReponse.getText());
                combo.addItem(txtMot.getText() + " : " + txtReponse.getText());

                txtMot.setText("");
                txtReponse.setText("");
            } else if (e.getSource() == boutonAjouterFichier) {
                //ajoute le mot et le fichier
                hashCommandes.put(txtMot2.getText(), fichier);
                combo.addItem(txtMot2.getText() + " : " + fichier.getName());

                txtMot2.setText("");
            } else if (e.getSource() == boutonSelectFiles) {
                //permet de selectionner le fichier
                int choix = jfc.showOpenDialog(null);
                if (choix == jfc.APPROVE_OPTION) {
                    fichier = jfc.getSelectedFile();
                }
            } else if (e.getSource() == boutonFlush) {
                //permet de flusher un mot
                int index = combo.getSelectedIndex();
                String key = combo.getItemAt(index).toString();

                key = key.substring(0, key.lastIndexOf(" : ")).trim();

                hashCommandes.remove(key);
                combo.removeItemAt(index);
            } else if (e.getSource() == boutonSave) {
                //permet de sauvergarder tous les mots
                frame.setVisible(false);
                ecrireAutomatiqueDonnees();
            } else if (e.getSource() == boutonFlushAll) {
                //efface tout
                hashCommandes.clear();
                combo.removeAllItems();
            }
        }

        /**
         * écrit les donnees dans un fichier
         */
        private void ecrireAutomatiqueDonnees() {
            String ligne;
            Object o;
            try {
                BufferedWriter ecrire = new BufferedWriter(new FileWriter(
                        FILENAME));
                Iterator keyIterator = hashCommandes.keySet().iterator();

                while (keyIterator.hasNext()) {
                    o = keyIterator.next();
                    ecrire.write(o.toString());
                    ecrire.newLine();
                    ecrire.write(hashCommandes.get(o.toString()).toString());
                    ecrire.newLine();
                    ecrire.newLine();
                }
                ecrire.close();
            } catch (IOException ex) {
            }
        }

        /**
         * charger les données dans la hashMap et le vecteur
         * les donnees s'affichent dans le comboBox
         */
        private void chargerAutomatiqueDonnees() {
            String key,
                    value;
            try {
                BufferedReader lire = new BufferedReader(new FileReader(
                        FILENAME));

                while ((key = lire.readLine()) != null) {
                    value = lire.readLine();
                    hashCommandes.put(key, value);
                    combo.addItem(key.toString() + " : " + value.toString());
                    lire.readLine();
                }
                lire.close();
            } catch (IOException ex) {
            }
        }

        public void setVisible(boolean etat) {
            frame.setVisible(etat);
        }
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
     * MyMsnListener.java
     * gère les evenements de msn messenger
     * Date: 07/01/2005
     * @author Hassen Ben Tanfous
     */
    public class MyMsnListener extends MsnAdapter {

        /**
         * est execute après le login, lorsque vous vous etes connecte au serveur
         * msn.hotmail.com, vous recevez un par un les contacts online
         * les ajoute a la liste online et les retire de la liste offline
         * @param friend MsnFriend
         */
        public void listOnline(MsnFriend friend) {
            boolean trouve = false;
            //pour chaque utilisateur = 1fois
            listeOnlineEmail.addElement(friend.getLoginName());
            listeOnlinePseudo.addElement(friend.getFormattedFriendlyName());

            for (int i = 0; i < listeOfflineEmail.size() && !trouve; i++) {
                if (friend.getLoginName().equalsIgnoreCase(listeOfflineEmail.
                        get(i).toString())) {
                    listeOfflineEmail.remove(i);
                    listeOfflinePseudo.remove(i);
                    trouve = true;
                }
            }
            logger.println(friend.getFormattedFriendlyName() + " est online");
        }

        /**
         * ajoute tous les contacts dans la liste offline
         * par défaut quand l'application démarre tout le monde est offline
         * et au fur et à mesure que quelqu'un se connecte, le contact
         * bouge d'une liste vers l'autre
         */
        public void buddyListModified() {

            //possibilité d'utiliser un comboBox comme dans MSN
            //pseudo (email) aucun changement du pseudo
            //bug avec la méthode msn.getOwner().getFriendlyName();
//            if (!(vecPseudo.size() > 1)) {
//                vecPseudo.add(msn.getLoginName());
//                comboStatus.setSelectedIndex(0);
//                for (int i = 0; i < MsnConstantes.tabPersoStatus.length; i++) {
//                    vecPseudo.add(MsnConstantes.tabPersoStatus[i]);
//                }
//                vecPseudo.add("CHANGER DE PSEUDO");
//            }
            //msn.getOwner().getFormattedFriendlyName() ne fonctionne pas correctement
//            txtPseudo.setText(msn.getLoginName());

            listeOfflineEmail.removeAllElements();
            listeOfflinePseudo.removeAllElements();

            logger.println("chargement de votre liste de contacts");

            //liste offline
            BuddyList bl = msn.getBuddyGroup().getForwardList();
            for (int i = 0; i < bl.size(); i++) {
                if (bl.get(i).getStatus().equalsIgnoreCase(UserStatus.OFFLINE)) {
                    listeOfflineEmail.addElement(bl.get(i).getLoginName());
                    listeOfflinePseudo.addElement(bl.get(i).
                                                  getFormattedFriendlyName());
                }
            }

            logger.println("Chargement complet");
        }

        /**
         * un utilisateur vient de se connecter ou vous a debloque
         * ou a changer son statut from Appear Offline to something else
         * ou a changer son pseudo
         * parcours la boucle des listeOnlineEmail pour voir si c un changement
         * de pseudo et modifie le contenu de la liste
         * sinon ajoute le contact à la liste online (first login)
         * et le retire de la liste offline
         * @param friend MsnFriend
         */
        public void userOnline(MsnFriend friend) {
            //changement de pseudo
            boolean pseudoChange = false;
            boolean userBouge = false; //bouge le user vers la liste online

            for (int i = 0; i < listeOnlineEmail.size() && !pseudoChange; i++) {
                if (listeOnlineEmail.get(i).toString().equalsIgnoreCase(friend.
                        getLoginName())) {
                    listeOnlinePseudo.set(i, friend.getFormattedFriendlyName());
                    pseudoChange = true;
                }
            }

            //le contact se connect pour la premiere fois
            if (!pseudoChange) {
                listeOnlineEmail.addElement(friend.getLoginName());
                listeOnlinePseudo.addElement(friend.
                                             getFormattedFriendlyName());
                for (int i = 0; i < listeOfflineEmail.size() && !userBouge; i++) {
                    if (listeOfflineEmail.get(i).toString().equalsIgnoreCase(
                            friend.getLoginName())) {
                        listeOfflineEmail.remove(i);
                        listeOfflinePseudo.remove(i);
                        userBouge = true;
                    }
                }
            }

            logger.println(friend.getFormattedFriendlyName() + " est online");

        }

        /**
         * L'utilisateur a disconnect ou vous a bloque
         * parcours la liste des mail online pour retirer l'utilisateur
         * et s'arrête des qu'il le trouve
         * @param loginName String
         */
        public void userOffline(String loginName) {
            boolean userBouge = false; //a bouge le user vers la  liste offline

            for (int i = 0; i < listeOnlineEmail.size() && !userBouge; i++) {
                if (listeOnlineEmail.get(i).toString().equalsIgnoreCase(
                        loginName)) {
                    listeOfflinePseudo.addElement(listeOnlinePseudo.get(i));
                    listeOfflineEmail.addElement(loginName);

                    logger.println(listeOnlinePseudo.get(i).toString() +
                                   " est offline");

                    listeOnlineEmail.remove(i);
                    listeOnlinePseudo.remove(i);
                    userBouge = true;
                }
            }
        }

        /**
         *
         * @param ss SwitchboardSession
         */
        public void switchboardSessionStarted(SwitchboardSession ss) {
            logger.println("Conversation démarré avec " +
                           ss.getMsnFriend().getFormattedFriendlyName() + " <" +
                           ss.getMsnFriend().getLoginName() + ">");
            logger.println("Active count: " + ss.activeCount());
            logger.println("Cookie: " + ss.getCookie());
            logger.println("Friend count: " + ss.getFriendCount());
            logger.println("ServerName: " + ss.getServerName());
            logger.println("Session ID: " + ss.getSessionId());
            logger.println("Target: " + ss.getTarget());
            logger.println("TimeOut: " + ss.getTimeout());
        }

        /**
         *
         * @param ss SwitchboardSession
         * @param friend MsnFriend
         * @param mime MimeMessage
         */
        public void instantMessageReceived(SwitchboardSession ss,
                                           MsnFriend friend, MimeMessage mime) {
            GUIChat temp;
            Object tempKey,
                    tempValue;
            File tempFichier;
            MimeMessage tempMsg = new MimeMessage();
            tempMsg.setFontColor(Color.black);

            boolean trouve = false;

            //verification pour block partiel
            for (int i = 0; i < vecBlockPartiel.size(); i++) {
                if (vecBlockPartiel.get(i).toString().equalsIgnoreCase(friend.
                        getLoginName())) {
                    try {
                        ss.close();
                    } catch (IOException ex) {
                    }
                    return ;
                }
            }

            //verification conversation existe deja
            for (int i = 0; i < vecChat.size() && !trouve; i++) {
                if (((GUIChat) vecChat.get(i)).getEmail().equalsIgnoreCase(
                        friend.getLoginName()) &&
                    ((GUIChat) vecChat.get(i)).isVisible()) {
                    temp = (GUIChat) vecChat.get(i);
                    temp.setSession(ss);
                    temp.append(mime.getMessage());
                    trouve = true;
                }
            }

            //conversation n'existe pas
            if (!trouve) {
                temp = new GUIChat(msn, friend.getLoginName(),
                                   friend.getFormattedFriendlyName(), ss, mime);
                temp.setChatVector(vecChat);
                temp.start();
                vecChat.add(temp);
                temp.append(mime.getMessage());
            }

            //vérification des mots pour affichage automatique
            Iterator keyIterator = hashCommandes.keySet().iterator();
            while (keyIterator.hasNext()) {
                tempKey = keyIterator.next();
                if (mime.getMessage().toLowerCase().indexOf(tempKey.toString().
                        toLowerCase()) != -1) {
                    tempValue = hashCommandes.get(tempKey);
                    try {
                        tempFichier = new File(tempValue.toString());
                        if (tempFichier.exists()) {
                            msn.sendFileRequest(friend.getLoginName(),
                                                tempFichier, ss);
                        } else {
                            tempMsg.setMessage(tempValue.toString());
                            ss.sendInstantMessage(tempMsg);
                            break;
                        }
                    } catch (Exception e) {
                        tempMsg.setMessage(tempValue.toString());
                        try {
                            ss.sendInstantMessage(tempMsg);
                            break;
                        } catch (IOException ex) {

                        }
                    } finally {
                        logger.println("Messagerie automatique pour " +
                                       tempKey.toString());
                        logger.println("Reponse: " + tempValue.toString());
                    }

                }
            }

        }

        /**
         *
         * @param ss SwitchboardSession
         * @param part MsnFriend
         */
        public void whoPartSession(SwitchboardSession ss, MsnFriend part) {
            logger.println(part.getFormattedFriendlyName() +
                           " est parti de la session ou /timeout");
        }

        /**
         *
         * @param ss SwitchboardSession
         */
        public void switchboardSessionEnded(SwitchboardSession ss) {
            logger.println(ss.getMsnFriend().getFormattedFriendlyName() +
                           " a fermé sa fenêtre ou/timeout");
        }

        //n'est jamais utlise lors d'un timeout ou d'une fermeture de fenetre
        public void switchboardSessionAbandon(SwitchboardSession ss,
                                              String targetName) {
            logger.println(targetName + " a abandonné la conversation");
        }

        /**
         *
         * @param downloader VolatileDownloader
         */
        public void fileReceiveStarted(VolatileDownloader downloader) {
            logger.println("Réception d'un fichier from " +
                           downloader.getHostAddress() + " via le port " +
                           downloader.getPort());
            logger.println("Nom du fichier: " + downloader.getFile().getName());
            logger.println("Taille du fichier: " + downloader.getFile().length());
            logger.println("Cookie: " + downloader.getCookie());
            logger.println("Authentification Cookie: " +
                           downloader.getAuthCookie());

            int choix = JOptionPane.showOptionDialog(null,
                    "Voulez-vous accepter " +
                    downloader.getFilename() +
                    " de taille " + downloader.getFile().length() +
                    "octets\nfrom " + downloader.getHostAddress() +
                    " via port: " + downloader.getPort(),
                    MsnConstantes.TITRE,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);

            switch (choix) {
            case JOptionPane.YES_OPTION:
                logger.println(
                        "Transfert en cours dans le répertoire où se trouve JMSN");

                //enlever les 3 lignes en commentaires pour avoir une progressBar
//                MyProgressBar barre = new MyProgressBar();
//                barre.setTransfert(downloader);
//                barre.start();
                break;
            case JOptionPane.NO_OPTION:
                downloader.close();
            }
        }

        /**
         *
         * @param downloader VolatileDownloader
         * @param e Throwable
         */
        public void fileReceiveError(VolatileDownloader downloader, Throwable e) {
            logger.println("Erreur lors de la réception du fichier " +
                           downloader.getFile().getName());

            msg.msge("Erreur lors de la réception du fichier " +
                     downloader.getFile().getName());
        }

        /**
         *
         * @param ss SwitchboardSession
         * @param cookie int
         */
        public void fileSendAccepted(SwitchboardSession ss, int cookie) {
            logger.println(ss.getMsnFriend().getFormattedFriendlyName() +
                           " a accepté le transfert de fichier");
        }

        /**
         *
         * @param ss SwitchboardSession
         * @param cookie int
         * @param reason String
         */
        public void fileSendRejected(SwitchboardSession ss, int cookie,
                                     String reason) {
            logger.println(ss.getMsnFriend().getFormattedFriendlyName() +
                           " a refusé le transfert de fichier");
        }

        /**
         *
         * @param server VolatileTransferServer
         */
        public void fileSendStarted(VolatileTransferServer server) {
            logger.println("transfert en cours du fichier " +
                           server.getFile().getName() + " de taille " +
                           server.getFile().length());
            logger.println("Port: " + server.getPort());
            logger.println("Cookie: " + server.getCookie());
            logger.println("Authentification cookie: " + server.getAuthCookie());
            logger.println("Receiver name: " + server.getReceiverName());

            //pour une progressBar
//            MyProgressBar barre = new MyProgressBar();
//            barre.setTransfert(server);
//            barre.start();
        }

        /**
         *
         * @param server VolatileTransferServer
         */
        public void fileSendEnded(VolatileTransferServer server) {
            logger.println("envoi terminé du fichier " +
                           server.getFile().getName() + " de taille " +
                           server.getFile().length());
            logger.println("Port: " + server.getPort());
            logger.println("Cookie: " + server.getCookie());
            logger.println("Authentification cookie: " + server.getAuthCookie());
            logger.println("Receiver name: " + server.getReceiverName());
        }
    }


    public Vector getChatVector() {
        return vecChat;
    }

    public MyLogger getLogger() {
        return logger;
    }

    public static void main (String[] args) {

    }
}
