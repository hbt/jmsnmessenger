package com.coded.jmsn.outils;

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
 * Outils.java
 * rassemblent quelques methodes utilisees par les classes pour certains traitements
 * Date: 13/01/2006
 * @author Hassen Ben Tanfous
 */

public class Outils {

    /**
     * formate le nom afin d'enlever les "%20" dans le pseudo
     * @deprecated utiliser la méthode getFormattedFriendlyName
     * @param str String
     * @return String
     */
    public static String formatFriendlyName(String str) {
        StringBuffer strbuff = new StringBuffer(str);
        while (strbuff.indexOf("%20") != -1) {
            strbuff.insert(strbuff.indexOf("%20"), " ");
            strbuff.delete(strbuff.indexOf("%20"), strbuff.indexOf("%20") + 3);
        }
        return strbuff.toString();
    }

    /**
     * remplit avec des espaces le reste de la chaine afin d'être uniforme
     * @param chaine String
     * @param nbEspaces int
     * @return String
     */
    public static String remplirEspaces(String str, int nbEspaces) {
        while (str.length() < nbEspaces) {
            str += " ";
        }
        return str;
    }
}
