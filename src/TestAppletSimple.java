

import javax.swing.*;
import com.coded.jmsn.*;
import rath.msnm.MSNMessenger;

public class TestAppletSimple extends JApplet {

    public void init() {
        GUIMsnLogin login = new GUIMsnLogin();
    }

    public static void main(String[] args) {
        new TestAppletSimple();
    }
}
