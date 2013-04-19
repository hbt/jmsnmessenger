/*
 * @(#)FileProgress.java
 *
 * Copyright (c) 2002, Jang-Ho Hwang
 * All rights reserved.
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
 * 	3. Neither the name of the Jang-Ho Hwang nor the names of its contributors
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
 *    $Id: FileProgress.java,v 1.2 2002/03/13 09:40:39 xrath Exp $
 */
//package rath.jmsn.file;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

import rath.msnm.msg.MimeMessage;

import rath.msnm.ftp.VolatileTransfer;
//import rath.jmsn.ToolBox;
//import rath.jmsn.ui.DialogAppender;
//
//import rath.jmsn.util.Msg;
/**
 *
 * @author Jang-Ho Hwang, rath@linuxkorea.co.kr
 * @version $Id: FileProgress.java,v 1.2 2002/03/13 09:40:39 xrath Exp $
 */
public class FileProgress extends JProgressBar implements ActionListener
{
        private VolatileTransfer transfer;
//        private DialogAppender appender;
        private Timer timer = null;
        private int oldValue;

        public FileProgress( )
        {
                super( 0, 100 );

//                setFont( ToolBox.FONT );
                setBorder( BorderFactory.createLineBorder( Color.black, 1 ) );

//                this.appender = appender;
                this.timer = new Timer( 500, this );
                setStringPainted( true );

                setPreferredSize( new Dimension(80, 20) );
        }

        public void setTransfer( VolatileTransfer transfer )
        {
                this.transfer = transfer;
                this.timer.start();
                setToolTipText( transfer.getFilename() );
        }

        public void actionPerformed( ActionEvent e )
        {
                int percent = transfer.getCommitPercent();
                if( oldValue != percent )
                {
                        oldValue = percent;
                        setValue( percent );
                        setString( percent + "%" );

                        if( percent==100 )
                        {
                                timer.stop();
                                processEnded();
                        }
                }
        }

        private void processEnded()
        {
                final Component me = this;
                SwingUtilities.invokeLater( new Runnable() {
                        public void run()
                        {
                                JPanel panel = (JPanel)getParent();
                                if( panel!=null )
                                {
                                        panel.remove( me );
                                        panel.doLayout();
                                        panel.validate();
                                        panel.repaint();
                                }
                        }
                });

                MimeMessage msg = new MimeMessage();
//                msg.setMessage( Msg.get("file.transfer.complete", transfer.getFilename()) );
//                appender.appendMessage( msg );
        }

}
