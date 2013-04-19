import rath.msnm.MSNMessenger;
import rath.msnm.SwitchboardSession;
import rath.msnm.UserStatus;
import rath.msnm.entity.MsnFriend;
import rath.msnm.event.MsnAdapter;
import rath.msnm.msg.MimeMessage;
import java.awt.*;
import rath.msnm.ftp.*;
import java.io.*;
import java.util.*;
import rath.msnm.ftp.*;
import rath.msnm.entity.*;
import rath.msnm.msg.*;
import rath.msnm.*;
import rath.msnm.*;
import rath.msnm.GroupList;
import rath.msnm.BuddyList;
import rath.msnm.msg.*;
import java.awt.event.ComponentAdapter;
import javax.swing.*;
import java.awt.*;
import javax.swing.tree.*;


public class MsnTreeTest extends JFrame {

    private static MSNMessenger msn;
    public static String coded = "c0d3dm1nd@hotmail.com";
    public static String prog = "hbtprogrammer@hotmail.com";
    public static String gmail = "c0d3dm1nd@gmail.com";

    private static SwitchboardSession session;

    Container container;
    DefaultMutableTreeNode worker,
            eleve,
            prof;
    JTree tree;
    JScrollPane scroll;

    public MsnTreeTest() {
        String[] tab = {"hello", "test", "blabla"};

        container = getContentPane();
        container.setLayout(null);

        eleve = new DefaultMutableTreeNode("MSN");

        worker = new DefaultMutableTreeNode("Worker");
        prof = new DefaultMutableTreeNode("Profs");

        for (int i = 0; i < tab.length; i++) {
            worker.add(new DefaultMutableTreeNode(tab[i]));
            prof.add(new DefaultMutableTreeNode(tab[i]));
        }
//        worker.add(new DefaultMutableTreeNode("hello world2"));

        eleve.add(worker);
        eleve.add(prof);

        tree = new JTree(eleve);

        scroll = new JScrollPane(tree);
        scroll.setBounds(10, 10, 100, 100);

        container.add(scroll);

        setSize(300, 300);
        setLocation(200, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

//        worker.add(n);

    }

    public static void main(String[] args) {
        //        BuddyTree buddy;
        Properties prop;
        msn = new MSNMessenger
              ("c0d3dm1nd@gmail.com", "marinescommand");

        msn.setInitialStatus(UserStatus.ONLINE);
        msn.addMsnListener(new MSNAdapter(msn));
        msn.login();

        System.out.println
                ("Waiting for the response....");

        MsnTreeTest test = new MsnTreeTest();
        try {
            Thread.currentThread().sleep(6000);
        } catch (InterruptedException ex) {
        }
        if (msn.isLoggedIn()) {
            BuddyList bl = msn.getBuddyGroup().getForwardList();
            for (int i = 0; i < bl.size(); i++) {
                if (!bl.get(i).getStatus().equalsIgnoreCase(UserStatus.OFFLINE)) {
                    System.out.println(bl.get(i).getLoginName());
                } else {
                    System.out.println("User offline: " +
                                       bl.get(i).getLoginName());
                }
            }
        } else {
            System.out.println("terminator");
        }

    }

    public void run() {
        msn.logout();
        System.out.println("MSN Logout OK");
    }

}
