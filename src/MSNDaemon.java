

import java.awt.*;
import java.io.*;
import java.util.*;

import rath.msnm.*;
import rath.msnm.entity.*;
import rath.msnm.event.*;
import rath.msnm.ftp.*;
import rath.msnm.msg.*;


public class MSNDaemon extends Thread {


    private static MSNMessenger msn;
    public static String coded = "c0d3dm1nd@hotmail.com";
    public static String prog = "hbtprogrammer@hotmail.com";
    public static String gmail = "c0d3dm1nd@gmail.com";

    private static SwitchboardSession session;

    public static void main(String[] args) {
//        BuddyTree buddy;
        Properties prop;
        msn = new MSNMessenger
              (gmail, "marinescommand");

        msn.setInitialStatus(UserStatus.ONLINE);
        msn.addMsnListener(new MSNAdapter(msn));
        msn.login();
        System.out.println
                ("Waiting for the response....");
//\u6355\u6349Ctrl+C\u7684\u8F93\u5165\u4EE5\u4FBF\u6CE8\u9500MSN\u7684\u767B\u5F55

        Runtime.getRuntime().
                addShutdownHook(new MSNDaemon());

//       LocalCopy local = msn.getLocalCopy();
//       File root = local.getHomeDirectory();
//       root = new File("D:\\transfert", gmail);
//       String downdir = local.getProperty("download.dir");
//       if (downdir == null) {
//           File dir = new File (root, "files");
//           dir.mkdirs();
//           downdir = dir.getAbsolutePath();
//           local.setProperty("download.dir", downdir);
//       } else {
//           System.out.println (downdir);
//       }
//
//       new File (downdir).mkdirs();


//        BuddyList bl2 = msn.getBuddyGroup().getForwardList();
//        for (int j = 0; j < bl2.size(); j++) {
//            if (!bl2.get(j).getStatus().equalsIgnoreCase(UserStatus.
//                    OFFLINE)) {
//                System.out.println("ICI " + bl2.get(j).getLoginName());
//            }
//        }

        try {
            msn.doCall(coded);
            session = msn.doCallWait(coded);
            if (session == null) {
                while (session == null) {
                    session = msn.doCallWait(coded);
                    System.out.println("une fois");
                }

                BuddyList bl2 = msn.getBuddyGroup().getForwardList();
                for (int j = 0; j < bl2.size(); j++) {
                    System.out.println(bl2.get(j).getStatus());
                    System.out.println(bl2.get(j).getLoginName());
                }

                System.out.println(session.getMsnFriend().getLoginName());
                //envoie fichiers
                msn.sendFileRequest(session.getMsnFriend().getLoginName(),
                                    new File("D:\\test2.mpg"), session);

                //images
//                PhotoFormatter img = new PhotoFormatter();
//                img.resize(new File("D:\\tara.jpeg"));

//                "user.photo";


//                GroupList gl = msn.getBuddyGroup().;
                BuddyList bl = msn.getBuddyGroup().getForwardList();
                for (int i = 0; i < bl.size(); i++) {
                    MsnFriend temp = bl.get(i);
                    if (!temp.getStatus().equals(UserStatus.OFFLINE)) {
                        session = msn.doCallWait(temp.getLoginName());
                        while (session == null) {
                            session = msn.doCallWait(temp.getLoginName());
                        }

                        //envoie messages
                        session.sendInstantMessage(new MimeMessage("fuck"));

//                        session.sendFileRequest(to, ftm);

//                        session.sendFileRequest(, new FileTra);
//                        session.sendFileRequest(new ToS);

                        session.sendInstantMessage(new MimeMessage(
                                "double fuck"));

                    }
                    System.out.println(temp.getLoginName());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            /** @todo Gérer cette exception */
        }

    }

    public static void print(String msg) {
        System.out.println(msg);
    }

    /**
     * \u7528\u6237\u4E2D\u6B62\u7A0B\u5E8F\u6267\u884C
     */
    public void run() {
        msn.logout();
        System.out.println("MSN Logout OK");
    }
}


/**
 * MSN\u6D88\u606F\u4E8B\u4EF6\u5904\u7406\u7C7B
 * @author Liudong
 */
class MSNAdapter extends MsnAdapter {

    MSNMessenger messenger;
    String coded = "c0d3dm1nd@hotmail.com";
    SwitchboardSession laSession;

    public void print(String msg) {
        System.out.println(msg);
    }

    public void listOnline(MsnFriend friend) {
        System.out.println(friend.getLoginName());
    }

    public MSNAdapter(MSNMessenger messenger) {

        this.messenger = messenger;
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException ex1) {
//        }
//        try {
//            laSession = this.messenger.doCallWait(coded);
//        } catch (InterruptedException ex2) {
//        } catch (IOException ex2) {
//        }
//        if (laSession == null) {
//            while (laSession == null) {
//                try {
//                    laSession = this.messenger.doCallWait(coded);
//                } catch (InterruptedException ex3) {
//                } catch (IOException ex3) {
//                }
//                System.out.println("une fois");
//            }
//        }
    }

    /**
     * \u67D0\u4EBA\u6B63\u5728\u8F93\u5165\u4FE1\u606F
     */
    public void progressTyping(
            SwitchboardSession ss,
            MsnFriend friend,
            String typingUser) {
        System.out.println
                (friend.getLoginName() +
                 "\u6B63\u5728\u8F93\u5165\u4FE1\u606F..." + "\n" +
                 friend.getCode() + "\n" + friend.getFormattedFriendlyName() +
                 "\n" + friend.getOldStatus() + "\n" + friend.getStatus() +
                 "\n" + friend.getAccessValue() + "\n" +
                 friend.getGroupIndex() +
                 "\n" + friend.getLoginName() + "\n" + friend.INVISIBLE +
                 "\n");
    }

    /**
     * \u6536\u5230\u6D88\u606F\u7684\u65F6\u5019\u6267\u884C\u8BE5\u65B9\u6CD5
     */
    public void instantMessageReceived(
            SwitchboardSession ss,
            MsnFriend friend,
            MimeMessage mime) {
//        this.laSession = ss;
        System.out.print(friend.getFriendlyName() + " - > ");
        System.out.println(mime.getMessage());

        try {
//\u53D1\u9001\u76F8\u540C\u7684\u56DE\u590D\u4FE1\u606F\u7ED9\u53D1\u9001\u8005
            MimeMessage mime3 = new MimeMessage();
            mime3.setFontColor(Color.BLUE);
            mime3.setKind(MimeMessage.KIND_MESSAGE);

            MimeMessage mime2 = new MimeMessage();
            mime2.setKind(MimeMessage.KIND_TYPING_USER);
            mime2.setEffectCode(MimeMessage.STR_FILE_TRANSFER);

            mime2.setFontName("Courier new");
            mime2.setMessage("hello world. Voici ma message automatique. Copier la ligne au complet de l'option pour y acceder:\n 1. Laisser un message \n 2. say hello world");
            mime2.setFontColor(Color.RED);

            mime2.setKind(MimeMessage.KIND_MESSAGE);
            if (mime.getMessage().equals("1. Laisser un message")) {
                mime3.setMessage("message enregistre " +
                                 friend.getFriendlyName());
//                messenger.sendMessage(friend.getLoginName(), mime3);
                ss.sendInstantMessage(mime3);
            } else if (mime.getMessage().equals("2. say hello world")) {
//                messenger.sendFileRequest(friend.getLoginName(),
//                                          new File("D:\\test.mpg"), laSession);
//                messenger.sendFileRequest(friend.getLoginName(),
//                                          new File("D:\\test.mpg"), ss);


//                messenger.sendFileRequest(friend.getLoginName(),
//                                          new File("D:\\test.mpg"),
//                                          ss.getSessionId());
            }

            messenger.sendMessage
                    (friend.getLoginName(), mime2);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * \u767B\u5F55\u6210\u529F\u540E\u6267\u884C\u8BE5\u65B9\u6CD5
     */
    public void loginComplete(MsnFriend own) {
        System.out.println
                (own.getLoginName() + " Login OK");
        System.out.println(own.getFriendlyName());

//
//
//        BuddyList bl2 = messenger.getBuddyGroup().getForwardList();
//        for (int j = 0; j < bl2.size(); j++) {
//            System.out.println(bl2.get(j).getOldStatus());
//            try {
//                messenger.doCall(bl2.get(j).getLoginName());
//            try {
//                laSession = messenger.doCallWait(bl2.get(j).getLoginName());
//                if (laSession == null) {
//                    while (laSession != null) {
//                        messenger.doCallWait(bl2.get(j).getLoginName());
//                    }
//                }
//            } catch (InterruptedException ex1) {
//            } catch (IOException ex1) {
//            }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//            System.out.println(bl2.get(j).getStatus());
//            System.out.println(bl2.get(j).getLoginName());
//        }

    }

    /**
     * \u767B\u5F55\u5931\u8D25\u540E\u6267\u884C\u8BE5\u65B9\u6CD5
     */
    public void loginError(String header) {
        System.out.println
                ("Login Failed: " + header);
    }

    /**
     * \u597D\u53CB\u79BB\u7EBF\u65F6\u6267\u884C\u8BE5\u65B9\u6CD5
     */
    public void userOffline(String loginName) {
        System.out.println
                ("USER " + loginName + " Logout.");
    }

    /**
     * \u597D\u53CB\u4E0A\u7EBF\u65F6\u6267\u884C\u8BE5\u65B9\u6CD5
     */
    public void userOnline(MsnFriend friend) {
        System.out.println
                ("USER " + friend.getFriendlyName() + " Login.");
    }

    /**
     * \u6709\u4EBA\u52A0\u6211\u4E3A\u597D\u53CB\u65F6\u6267\u884C
     */
    public void whoAddedMe(MsnFriend friend) {
        System.out.println
                ("USER " + friend.getLoginName() + " Addme.");
        try {
            messenger.addFriend(friend.getLoginName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * \u6709\u4EBA\u628A\u6211\u4ECE\u597D\u53CB\u5217\u8868\u4E2D\u5220\u9664\u65F6\u6267\u884C
     */
    public void whoRemovedMe(MsnFriend friend) {
        System.out.println
                ("USER " + friend.getLoginName() + " Remove me.");
        try {
            messenger.removeFriend(friend.getLoginName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchboardSessionStarted(SwitchboardSession ss) {
        //dès que quelqu'un ouvre une fenetre sur toi
        System.out.println("je demarre la session");
//        this.laSession = ss;
//        //ss.getMsnFriend().getLoginName()
//        try {
//            ss.sendInstantMessage(new MimeMessage("je suis le premier"));
//            messenger.sendFileRequest(ss.getMsnFriend().getLoginName(),
//                                      new File("D:\\test.mpg"), laSession);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }

    public void whoJoinSession(SwitchboardSession ss, final MsnFriend join) {

    }

    public void filePosted(SwitchboardSession ss, int cookie, String filename,
                           int filesize) {
        System.out.println(filename);
    }


    public void fileSendAccepted(SwitchboardSession ss, int cookie) {

//        try {
//            messenger.sendFileRequest(ss.getMsnFriend().getLoginName(),
//                                      new File("D:\\test.mpg"), ss);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
        print("file accepte");
    }

    public void fileSendRejected(SwitchboardSession ss, int cookie,
                                 String reason) {
        print("file rejete");
    }

    public void fileSendStarted(VolatileTransferServer server) {
        print("jenvoie");
    }

    public void fileSendEnded(VolatileTransferServer server) {
        print("envoie fini");
    }

    public void fileReceiveStarted(VolatileDownloader downloader) {
//        TestingProgress t = new TestingProgress();
//        t.setTransfert(downloader);
//        t.start();
//        int oldValue = 0;
//        VolatileTransfer transfer = downloader;
//        while (transfer.getCommitPercent() != 100) {
//            System.out.println(transfer.getCommitPercent());
//        }
//
//        int percent = transfer.getCommitPercent();
//        if (oldValue != percent) {
//            oldValue = percent;
//            System.out.println(percent);
//            System.out.println(percent + "%");
//
//            if (percent == 100) {
//                timer.stop();
//                processEnded();
//            }
//        }

//        File fichier = new File(downloader.getFile().getName());
//        System.out.println ("NOM: " + fichier.getAbsolutePath());
//
//        while (fichier.length() != downloader.getFile().length()) {
//            System.out.println ("taille " + fichier.length());
//        }
//        System.out.println (downloader.getCommitPercent());
//        System.out.println (downloader.getFile().length());
//        System.out.println (downloader.getFileSize());
//        System.out.println (downloader.getName());
//        while (downloader.getFileSize() != downloader.getfile
        print("je recois");
    }

    public void fileSendError(VolatileTransferServer server, Throwable e) {
        System.out.println("erreur pour envoie");
    }


    public void fileReceiveError(VolatileDownloader downloader, Throwable e) {
        System.out.println("erreur pour fichier recu");
    }

    public void whoPartSession(SwitchboardSession ss, MsnFriend part) {
        //bon quand je ferme, ne pas utiliser abandon
        print(part.getLoginName() + " est parti session");
    }

    public void switchboardSessionEnded(SwitchboardSession ss) {
        //ferme la fenetre
        print(ss.getMsnFriend().getLoginName() + " session  ended");
    }

    public void switchboardSessionAbandon(SwitchboardSession ss,
                                          String targetName) {
        print(ss.getMsnFriend().getLoginName() + " abandon session");
    }


    public static void copyFile(File src, File dest) throws IOException {
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(dest);

        java.nio.channels.FileChannel channelSrc = fis.getChannel();
        java.nio.channels.FileChannel channelDest = fos.getChannel();

        channelSrc.transferTo(0, channelSrc.size(), channelDest);

        fis.close();
        fos.close();
    }

    public void renameNotify(MsnFriend friend) {
        print("rename");
    }

    public void buddyListModified() {
        print("modified");
    }

    public void listAdd(MsnFriend friend) {
        print("listadd");
    }
}
