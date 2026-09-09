/*
 * $Id: CreditCard.java,v 1.3.4.3 2001/03/15 00:47:23 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.util;

import org.w3c.dom.Element;
import org.w3c.dom.Document;

import com.sun.j2ee.blueprints.customer.util.Calendar;
import java.util.StringTokenizer;
import com.sun.j2ee.blueprints.util.tracer.Debug;

/**
 * This class represents a credit card
 */
public class CreditCard implements java.io.Serializable {

    private String cardNo = "";
    private String cardType = "";
    private Calendar expiryDate;

    /**
     * default constructor
     */
    public CreditCard (){
        cardNo = new String();
        cardType = new String();
        expiryDate = Calendar.getInstance();
    }

    /**
     * @param  expiryDateString is mm/dd/yyyy
     */
    public CreditCard (String cardNo, String cardType, Calendar expiryDate){
        this.cardNo = cardNo;
        this.cardType = cardType;
        this.expiryDate = expiryDate;
    }

    /**
     * @param  expiryDateString is mm/dd/yyyy
     */
    public CreditCard (String cardNo, String cardType, String expiryDateString){
        this.cardNo = cardNo;
        this.cardType = cardType;
        expiryDate = getCreditCardExpiryDate(expiryDateString);
        int month = 0;
        int year = 0;
    }

    // get methods for the instance variables

    public String getCardNo() {
        return cardNo;
    }

    public String getCardType() {
        return cardType;
    }

    public String getExpiryMonthString() {
        if (expiryDate == null) {
            return "";
        } else {
            int month = getExpiryDate().get(java.util.Calendar.MONTH) + 1;
            return ((month < 10) ? "0" : "")  +  month + "";
        }
    }

    public String getExpiryYearString() {
        return (expiryDate == null) ? "" :
            getExpiryDate().get(java.util.Calendar.YEAR) + "";
    }

    public Calendar getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param  expiryDateString is mm/dd/yyyy
     */
    private Calendar getCreditCardExpiryDate(String expiryDateString){

        Calendar expiryDate = Calendar.getInstance();
        expiryDate.clear();
        int month = 0;
        int year = 0;

        try{
            if (expiryDateString != null){
                StringTokenizer strTok = new StringTokenizer(expiryDateString, "/");

                if (strTok.countTokens() == 0){
                    throw new Exception("CreditCard Date Format Error: " +
                                        expiryDateString);
                } else if (strTok.countTokens() == 2){

                    month = Integer.parseInt(strTok.nextToken());
                    year = Integer.parseInt(strTok.nextToken());
                    expiryDate.set(Calendar.MONTH, (month - 1));
                    expiryDate.set(Calendar.YEAR, year);

                } else {

                    month = Integer.parseInt(strTok.nextToken());
                    int  day = Integer.parseInt(strTok.nextToken());
                    year = Integer.parseInt(strTok.nextToken());
                    expiryDate.set(year, (month - 1), day);
                }

                return expiryDate;
            }

        } catch(Throwable e) {
            Debug.println("Credit Card: Error Parsing date: " + e);
            Debug.print(e);
        }
        return null;
    }

    public String getExpiryDateString(){
        int year = expiryDate.get(java.util.Calendar.YEAR);
        int month = expiryDate.get(java.util.Calendar.MONTH) + 1 ;
        return ((month < 10) ? "0" : "")  + month + "/" + ((year < 10) ? "0" : "") + year;
    }

    public String toString(){
        return "[Card Type=" + cardType + ", Card Number=" + cardNo +
            ", Expiration Date="  + expiryDate + "]";
    }

    public Element toXml(Document doc, String id) {
        Element root = doc.createElement("CreditCard");
        if (id != null)
            root.setAttribute("Id", id);
        Element node = doc.createElement("CardNumber");
        node.appendChild(doc.createTextNode(cardNo));
        root.appendChild(node);
        node = doc.createElement("CardType");
        node.appendChild(doc.createTextNode(cardType));
        root.appendChild(node);
        root.appendChild(expiryDate.toXml(doc, "ExpiryDate"));
        return root;
    }
}
