
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

/*
 * Created on 2004-6-2
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author junli_du
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.io.IOException;


public class Test {
    public static void main(String[] args) {
        String userName = "c0d3dm1nd@gmail.com";
        String password = "marinescommand";

        final MSNMessenger msn = new MSNMessenger(userName, password);
        msn.setInitialStatus(UserStatus.ONLINE);
        msn.addMsnListener(new MsnAdapter() {
                public void progressTyping(SwitchboardSession ss,
                    MsnFriend friend, String typingUser) {
                    System.out.println("Typing on " + friend.getLoginName());
                }

                public void instantMessageReceived(SwitchboardSession ss,
                    MsnFriend friend, MimeMessage mime) {
                    System.out.println("*** MimeMessage from " +
                        friend.getLoginName());

                    String msg = mime.getMessage();

                    if (msg.equalsIgnoreCase("logout")) {
                     System.out.println("logout");
                        msn.logout();
                    }

                    System.out.println(mime.getMessage());
                    System.out.println("*****************************");
                }
            });
        msn.login();

        String strMsnMsg = "\u6D63\u72B2\u30BD\u951B\u5B5EMSN!";
        MimeMessage msnMsg = new MimeMessage(strMsnMsg);

        String toLoginName = "c0d3dm1nd@hotmail.com";
        //MsnFriend friend = new MsnFriend(toLoginName);

        try {
            msn.doCall(toLoginName);
        } catch (IOException e) {
            System.out.println("doCall:");
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000); //\u93C8€\u6FC2\u85C9\u6E6A\u59DD\u3086\u6E41\u93B5€\u5BE4\u60F0\u7E5C,\u7455\u4F77\u7B09SwitchboardSession\u9352\u6D98\u7F13\u7039\u89C4\u69D7\u6FB6\u8FAB\u89E6
        } catch (InterruptedException e) {
            System.out.println("thread:");
            e.printStackTrace();
        }

        SwitchboardSession ss = null;

        try {
            ss = msn.doCallWait(toLoginName);
        } catch (InterruptedException e) {
            System.out.println("session docallwait InterruptedException:");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("session docallwait IOException:");
            e.printStackTrace();
        }

        try {
            while (ss == null) {
                ss = msn.doCallWait(toLoginName);
            }
        } catch (InterruptedException e) {
            System.out.println("session docallwait InterruptedException:");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("session docallwait:");
            e.printStackTrace();
        }

        if (ss == null) {
            System.out.println("SwitchboardSession error");

            return;
        }

        MimeMessage msg = new MimeMessage();
        msg.setKind(MimeMessage.KIND_MESSAGE);
        msg.setMessage(strMsnMsg);

        try {
            ss.sendInstantMessage(msg);
        } catch (IOException e) {
            System.out.println("sendInstantMessage:");
            e.printStackTrace();
        }

        //msn.sendMessage(toLoginName,msg);
        //msn.logout();
    }
}
