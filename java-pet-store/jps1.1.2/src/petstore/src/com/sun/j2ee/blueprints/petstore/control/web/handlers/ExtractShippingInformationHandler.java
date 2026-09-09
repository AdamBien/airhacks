/*
 * $Id: ExtractShippingInformationHandler.java,v 1.1.4.3 2001/03/15 03:50:36 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.web.handlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Locale;

import com.sun.j2ee.blueprints.petstore.util.JSPUtil;
import com.sun.j2ee.blueprints.petstore.control.event.EStoreEvent;
import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreEventException;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;
import com.sun.j2ee.blueprints.customer.util.ContactInformation;
import com.sun.j2ee.blueprints.customer.util.Calendar;
import com.sun.j2ee.blueprints.customer.util.CreditCard;
import com.sun.j2ee.blueprints.customer.util.Address;
import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.petstore.control.web.MissingFormDataException;
/**
 * This class is the base interface to request handlers on the
 * web tier.
 *
*/
public class ExtractShippingInformationHandler extends RequestHandlerSupport {

    public EStoreEvent processRequest(HttpServletRequest request)  throws EStoreEventException {
        Debug.println("Extracting Shipping Information");
        ContactInformation contactInformation = extractContactInformation(request);
        if (contactInformation != null ) request.getSession().setAttribute(WebKeys.ShippingContactInfoKey, contactInformation);
        return null;
    }

    /** parse address form and generate a ContactInformation object */
    private ContactInformation extractContactInformation(HttpServletRequest request) {
        Locale currentLocale = JSPUtil.getLocale(request.getSession());
        ArrayList missingFields = null;
        String familyName =  request.getParameter("family_name").trim();
        if (currentLocale.equals(Locale.JAPAN)) familyName = JSPUtil.convertJISEncoding(familyName);
        if (familyName.equals("")) {
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("Last Name");
        }
        String givenName =  request.getParameter("given_name").trim();
        if (currentLocale.equals(Locale.JAPAN)) givenName = JSPUtil.convertJISEncoding(givenName);
        if (givenName.equals("")) {
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("First Name");
        }
        String address1 = request.getParameter("address_1").trim();
        if (currentLocale.equals(Locale.JAPAN)) address1 = JSPUtil.convertJISEncoding(address1);
        if (address1.equals("")){
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("Street Address");
        }
        String address2 = request.getParameter("address_2").trim();
        if (currentLocale.equals(Locale.JAPAN)) address2 = JSPUtil.convertJISEncoding(address2);
        String city =   request.getParameter("city").trim();
        if (city.equals("")){
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("City");
        }
        String stateOrProvince = request.getParameter("state_or_province").trim();
        if (currentLocale.equals(Locale.JAPAN)) stateOrProvince = JSPUtil.convertJISEncoding(stateOrProvince);
        if (stateOrProvince.equals("")) {
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("State or Province");
        }
        String postalCode = request.getParameter("postal_code").trim();
        if (currentLocale.equals(Locale.JAPAN)) postalCode = JSPUtil.convertJISEncoding(postalCode);
        if (postalCode.equals("")){
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("Postal Code");
        }
        String country = request.getParameter("country").trim();
        if (currentLocale.equals(Locale.JAPAN)) country = JSPUtil.convertJISEncoding(country);
        String telephone = request.getParameter("telephone_number").trim();
        if (currentLocale.equals(Locale.JAPAN)) telephone = JSPUtil.convertJISEncoding(telephone);
        if (telephone.equals("")){
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("Telephone Number");
        }
        String email = request.getParameter("user_email");
        if (currentLocale.equals(Locale.JAPAN)) email = JSPUtil.convertJISEncoding(email);
        if (missingFields != null) {
            MissingFormDataException ex = new MissingFormDataException("Missing Address Data", missingFields);
            request.setAttribute(WebKeys.MissingFormDataKey, ex);
            return null;
        }
        Address address = new Address(address1, address2, city,
                                      stateOrProvince, postalCode,country);
        return new ContactInformation(familyName, givenName, telephone,
                                      email, address);
    }
}
