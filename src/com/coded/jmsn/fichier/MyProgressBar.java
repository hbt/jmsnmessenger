package com.coded.jmsn.fichier;

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
 * MyProgressBar.java
 * permet d'afficher une barre de progression lors d'un transfert de fichier
 * avec le nom du fichier et la valeur en pourcentage qu'il est possible d'ajouter
 * Date: 13/01/2006
 * @author Hassen Ben Tanfous
 */

//imports
import javax.swing.*;
import rath.msnm.ftp.*;

public class MyProgressBar extends Thread {
    private VolatileTransfer transfert;
    private JProgressBar progress;
    private JFrame frame;
    private JLabel lblName;

    /**
     *
     */
    public MyProgressBar() {
        progress = new JProgressBar();
        lblName = new JLabel();
        frame = new JFrame();

        progress.setMaximum(100);
        progress.setBounds(10, 30, 100, 20);

        lblName.setBounds(10, 10, 100, 20);

        frame.getContentPane().setLayout(null);
        frame.getContentPane().add(progress);
        frame.getContentPane().add(lblName);

        frame.setSize(150, 100);
        frame.setLocation(200, 200);
        frame.setVisible(true);
    }

    /**
     *
     * @param trans VolatileTransfer
     */
    public void setTransfert(VolatileTransfer trans) {
        this.transfert = trans;
        lblName.setText(transfert.getFilename());
    }

    public void run() {
        while (transfert.getCommitPercent() != 100) {
            progress.setValue(transfert.getCommitPercent());
        }

        frame.setVisible(false);
        progress.setValue(0);
        stop();
    }
}
