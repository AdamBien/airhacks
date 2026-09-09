/*
 * $Id: Calendar.java,v 1.3.4.6 2001/04/06 22:00:49 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits r?erv?.
 */

package com.sun.j2ee.blueprints.customer.util;

import java.util.Date;
import java.text.DateFormat;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import java.util.Locale;

/**
 * This class represents a calender
 */
public class Calendar extends Object implements java.io.Serializable{

    public static final int MONTH = java.util.Calendar.MONTH;
    public static final int DATE = java.util.Calendar.DATE;
    public static final int YEAR = java.util.Calendar.YEAR;

    private int month;
    private int day;
    private int year;

    private Calendar(int year, int month, int day){
        this.month = month;
        this.day= day;
        this.year = year;
    }

    public static Calendar getInstance(){
        java.util.Calendar c = java.util.Calendar.getInstance();
        int m = c.get(java.util.Calendar.MONTH);
        int d = c.get(java.util.Calendar.DATE);
        int y = c.get(java.util.Calendar.YEAR);
        return new Calendar(y,m,d);
    }

    public int getMonth(){
        return month;
    }

    public int getDay(){
        return day;
    }

    public int getYear(){
        return year;
    }

    public void set(int year, int month, int day){
        this.month = month;
        this.day = day;
        this.year = year;
    }

    public void set(int target, int value){
        switch (target){
            case java.util.Calendar.MONTH :
                this.month = value;
                break;
            case java.util.Calendar.YEAR :
                this.year = value;
                break;
            case java.util.Calendar.DATE :
                this.day = value;
                break;
        }
    }

    public int get(int target){
        switch (target){
            case java.util.Calendar.MONTH : return this.month;
            case java.util.Calendar.YEAR : return this.year;
            case java.util.Calendar.DATE : return this.day;
            default: return -1;
        }

    }

    public void setTime(java.util.Date date){
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        this.day = c.get(java.util.Calendar.DATE);
        this.month = c.get(java.util.Calendar.MONTH);
        this.year = c.get(java.util.Calendar.YEAR);
    }

    public void clear(){
        this.day = -1;
        this.month = -1;
        this.year = -1;
    }

   /**
    * @return the date encoded in the format  mm/yyyy
    */
    public String getExpiryDateString(){
        return ((month > 10)? "0" : "") + month + "/" + ((year > 10)? "0" : "") + year;
    }

   /**
    * @return the date encoded in the format  based on locale
    */
    public String getFullDateString(Locale locale){
        java.util.Calendar c = java.util.Calendar.getInstance(locale);
        c.set(year,month,day);
        Date d = c.getTime();
        DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT,locale);
        return df.format(d);
    }

   /**
    * @return the date encoded in the format  mm/dd/yyyy
    */
    public String getFullDateString(){
        return ((month + 1 < 10)? "0" : "") + (month + 1) + "/" + ((day < 10)? "0" : "") + day + "/" + ((year < 10)? "0" : "") + year;
    }

    /**
     * @return the date encoded in the JDBC format, {d 'yyyy-mm-dd'}
     */

    public String getJDBCDateString() {
        return "{d '" + ((year<10) ? "0" : "") + year + "-"
            + ((month+1 < 10) ? "0" : "") + (month+1) + "-"
            + ((day<10) ? "0" : "") + day + "'}";
    }

    public String toString(){
        return "[Year=" + year + ", Month=" + month + ", Day=" + day + "]";
    }

    public String getCloudscapeDateString(){
        return year + "-" + ((month + 1 < 10)? "0" : "") + (month + 1) + "-" + ((day < 10)? "0" : "") + day;
    }

    public Element toXml(Document doc, String id) {
        Element root = doc.createElement("Date");
        if (id != null)
            root.setAttribute("Id", id);

        Element node = doc.createElement("Month");
        node.appendChild(doc.createTextNode(String.valueOf(month)));
        root.appendChild(node);

        node = doc.createElement("Day");
        node.appendChild(doc.createTextNode(String.valueOf(day)));
        root.appendChild(node);

        node = doc.createElement("Year");
        node.appendChild(doc.createTextNode(String.valueOf(year)));
        root.appendChild(node);

        return root;
    }
}
