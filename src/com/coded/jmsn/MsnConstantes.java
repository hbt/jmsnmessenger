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
 * MsnConstantes.java
 * rassemble toutes les constantes utilisées pour JMSN
 * Date: 04/01/2006
 * @author Hassen Ben Tanfous
 */

//imports
import rath.msnm.*;

public interface MsnConstantes {

    /** titre de l'application */
    static String TITRE = "JMSN par Hassen Ben Tanfous";

    //status msn messenger personnalisés
    static String[] tabPersoStatus = {"Online", "Busy", "Brb", "Away",
                                     "On the phone", "Out to lunch",
                                     "Appear offline"};

    //status msn messenger from rath
    static String[] tabRathStatus = {UserStatus.ONLINE, UserStatus.BUSY,
                                    UserStatus.BE_RIGHT_BACK,
                                    UserStatus.IDLE, UserStatus.ON_THE_PHONE,
                                    UserStatus.ON_THE_LUNCH,
                                    UserStatus.OFFLINE};

}
