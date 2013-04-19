package com.coded.jmsn.msg;

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
 * Messagerie.java
 * affichage des messages
 * Date: 05/01/2006
 * @author Hassen Ben Tanfous
 */

//imports
import javax.swing.*;

public class Messagerie {
    public static String titre;

    /**
     *
     * @param titre String
     */
    public Messagerie(String titre) {
        this.titre = titre;
    }

    /**
     * message d'erreur
     * @param message String
     */
    public static void msge(String message) {
        msg(message, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * message plain
     * @param message String
     */
    public static void msg(String message) {
        msg(message, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * message information
     * @param message String
     */
    public static void msgi(String message) {
        msg(message, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * message question
     * @param message String
     */
    public static void msgq(String message) {
        msg(message, JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * message warning
     * @param message String
     */
    public static void msgw(String message) {
        msg(message, JOptionPane.WARNING_MESSAGE);
    }

    /**
     *
     * @param msg String
     * @param type int
     */
    public static void msg(String msg, int type) {
        JOptionPane.showMessageDialog(null, msg, titre, type);
    }

    /**
     *
     * @param msg String
     * @param titre String
     * @param type int
     */
    public static void msg(String msg, String titre, int type) {
        JOptionPane.showMessageDialog(null, msg, titre, type);
    }

    /**
     *
     * @param msg String
     * @param titre String
     */
    public static void msge(String msg, String titre) {
        msg(msg, titre, JOptionPane.ERROR_MESSAGE);
    }

    /**
     *
     * @param msg String
     * @param titre String
     */
    public static void msg(String msg, String titre) {
        msg(msg, titre, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     *
     * @param msg String
     * @param titre String
     */
    public static void msgi(String msg, String titre) {
        msg(msg, titre, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     *
     * @param msg String
     * @param titre String
     */
    public static void msgq(String msg, String titre) {
        msg(msg, titre, JOptionPane.QUESTION_MESSAGE);
    }

    /**
     *
     * @param msg String
     * @param titre String
     */
    public static void msgw(String msg, String titre) {
        msg(msg, titre, JOptionPane.WARNING_MESSAGE);
    }

    /**
     *
     * @param msg String
     */
    public static void msgOnly(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }
}
