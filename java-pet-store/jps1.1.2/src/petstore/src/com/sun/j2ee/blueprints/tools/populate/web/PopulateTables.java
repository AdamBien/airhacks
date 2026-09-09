package com.sun.j2ee.blueprints.tools.populate.web;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.DriverManager;

import java.io.PrintWriter;
import java.util.Vector;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;
import java.io.ByteArrayOutputStream;

import com.sun.j2ee.blueprints.util.tracer.Debug;


public class PopulateTables {

    private static String loadFile(URL url, String encoding){
        InputStream is;
        String returnString = new String("");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
                is = url.openConnection().getInputStream();
                long total =0;
                byte [] buffer = new byte[1024];
                while (true){
                    int nRead = is.read(buffer,0,buffer.length);
                    total += nRead;
                    if (nRead <=0) break;
                    bos.write(buffer,0, nRead);
                }
                is.close();
                bos.close();

        }catch (IOException e){
                System.out.print ("Error Reading file: " + e + "\n");
        }catch (Exception e){
            System.out.print ("Error Reading: " + e + "\n");
        }
        try{
            byte[] bytes = bos.toByteArray();
            if (encoding != null) returnString = new String(bytes,0, bytes.length, encoding);
            else returnString = new String(bytes);
        }catch (UnsupportedEncodingException enex){
            Debug.println("Unable to Convert Source File");
        }
        return returnString;
    }

    public static void populate(URL url, String encoding, Connection con, PrintWriter out, boolean verbose) {
        if (verbose) {
            out.println("<br>Loading " + url);
            out.flush();
        }
        String text = loadFile(url, encoding);
        if (verbose) {
            out.println("<br>Connection is " + con);
            out.flush();
        }
        StringTokenizer strTok = new StringTokenizer(text, ";");
        while (strTok.hasMoreTokens()) {
            String querry = strTok.nextToken().trim();
            if (!querry.equals("") && !querry.startsWith("connect") ) {
                if (verbose) {
                    out.println("<br>Querry=" + querry);
                    out.flush();
                }
                try {

                    Vector targetStrings = new Vector();
                    String processedQuerry = "";
                    String VALUES = "VALUES";
                    int startIndex = -1;
                    // get working string
                    int valueStart = querry.toUpperCase().indexOf(VALUES);
                    if (valueStart != -1) {
                           startIndex = querry.indexOf("(", valueStart + VALUES.length());
                           if (startIndex < querry.length()) processedQuerry = querry.substring(0, startIndex);
                    }
                    // find all the target strings
                    if (startIndex != -1) {
                        int index = startIndex;
                        int literalStart = -1;
                        while (index < querry.length()) {
                            if (querry.charAt(index) == '\'') {
                                if (literalStart == -1 && index + 1 < querry.length()) {
                                    literalStart = index +1;
                                } else {
                                    String targetString = querry.substring(literalStart, index);
                                    targetStrings.addElement(targetString);
                                    literalStart = -1;
                                    processedQuerry += "?";
                                    index++;
                                }
                            }
                            if (literalStart == -1 && querry.charAt(index) == ')') {
                                break;
                            } else if (index < querry.length() && literalStart == -1) {
                                processedQuerry += querry.charAt(index);
                            }
                            index++;

                        }
                        processedQuerry += ")";
                        if (verbose) {
                            out.println("<br>Processed Querry=" + processedQuerry);
                            out.flush();
                        }
                        if (con != null) {
                            PreparedStatement stmt = con.prepareStatement(processedQuerry);
                            for (int loop=0; loop < targetStrings.size(); loop++) {
                                String arg = (String)targetStrings.elementAt(loop);
                                stmt.setString(loop +1, arg);
                           }
                           int resultCount = stmt.executeUpdate();
                           stmt.close();
                           if (verbose) {
                               out.println("<br>" + resultCount + " row(s) updated/inserted/deleted");
                               out.flush();
                           }
                       }
                    } else {
                        if (con != null) {
                            PreparedStatement stmt = con.prepareStatement(querry);
                            int resultCount = stmt.executeUpdate();
                            stmt.close();
                            if (verbose) {
                                out.println("<br>" + resultCount + " row(s) updated/inserted/deleted");
                                out.flush();
                            }
                       }

                    }
               } catch (SQLException ex) {
                   if (verbose) {
                       out.println("<br>PopulateTables Caught : " + ex);
                       out.flush();
                   }
               }
            }
        }
    }

}
