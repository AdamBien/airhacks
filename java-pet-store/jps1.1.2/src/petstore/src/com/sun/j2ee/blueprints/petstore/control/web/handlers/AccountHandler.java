/*
 * $Id: AccountHandler.java,v 1.7.4.8 2001/04/03 21:38:59 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits r?erv?.
 */

package com.sun.j2ee.blueprints.petstore.control.web.handlers;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

import com.sun.j2ee.blueprints.customer.util.Address;
import com.sun.j2ee.blueprints.petstore.control.event.EStoreEvent;

import com.sun.j2ee.blueprints.petstore.control.event.AccountEvent;
import com.sun.j2ee.blueprints.customer.util.ContactInformation;
import com.sun.j2ee.blueprints.personalization.profilemgr.model.ExplicitInformation;
import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreEventException;
import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;
import com.sun.j2ee.blueprints.petstore.util.JSPUtil;

import com.sun.j2ee.blueprints.petstore.control.web.ModelManager;
import com.sun.j2ee.blueprints.petstore.control.web.CustomerWebImpl;
import com.sun.j2ee.blueprints.petstore.control.web.MissingFormDataException;

/**
 * Account Handler
 *
*/
public class AccountHandler extends RequestHandlerSupport {

    public EStoreEvent processRequest(HttpServletRequest request)
        throws EStoreEventException {
        Debug.println("Started creation of an Account Event");
        String action = request.getParameter("action");
        Debug.println("AccountHandler (web): action=" + action);
        if (action == null) {
            return null;
        } else if (action.equals("createAccount")) {
            return createNewAccountEvent(request);
        } else if (action.equals("updateAccount")) {
            return createUpdateAccountEvent(request);
        }
        return null;
    }

    private EStoreEvent createNewAccountEvent(HttpServletRequest request){
        Debug.println("Creating new Account and Profile");
        Debug.println("Getting Account Information");
        Locale currentLocale = JSPUtil.getLocale(request.getSession());
        ArrayList missingFields = null;
        AccountEvent event = new AccountEvent();

        String userId = request.getParameter("user_name").trim();
        if (currentLocale.equals(Locale.JAPAN)) userId = JSPUtil.convertJISEncoding(userId);
        if (userId.equals("")) {
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("User ID");
        }
        ModelManager mm = (ModelManager)request.getSession().getAttribute(WebKeys.ModelManagerKey);
        CustomerWebImpl customer = mm.getCustomerWebImpl();
        customer.setUserId(userId);
        String password = request.getParameter("password").trim();
        // convert the encoding to unicode if it is coming from a Japanese page
        if (currentLocale.equals(Locale.JAPAN)) password = JSPUtil.convertJISEncoding(password);
        if (password.equals("")) {
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("Password");
        }
        String email = request.getParameter("user_email").trim();
        if (currentLocale.equals(Locale.JAPAN)) email = JSPUtil.convertJISEncoding(email);

        if (email.equals("")) {
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("E-Mail Address");
        }

        Debug.println("Getting Profile Information");
        String language = request.getParameter("language").trim();
        // set the locale here
        Locale locale = JSPUtil.getLocaleFromLanguage(language);
        request.getSession().setAttribute(WebKeys.LanguageKey, locale);
        Debug.println("Account Handler set language to: " + language);
        if (language.equals("")) {
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("Language Preference");
        }
        String favorite = request.getParameter("favorite_category").trim();
        if (favorite.equals("")) {
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("Favorite Category");
        }
        String myListOpt = request.getParameter("myList_on");
        if (myListOpt == null)
          myListOpt = "false";
        else
          myListOpt = "true";
        String bannerOpt = request.getParameter("banners_on");
        if (bannerOpt == null)
           bannerOpt = "false";
        else
           bannerOpt = "true";
        if (missingFields != null) {
            MissingFormDataException ex = new MissingFormDataException("Missing Address Data", missingFields);
            request.setAttribute(WebKeys.MissingFormDataKey, ex);
            return null;
        }
        String status = "OK";
        ContactInformation contactInformation = extractContactInformation(request);
        if (contactInformation == null) return null;
        // copy shipping address if the same as billing address
        if (request.getParameter("ship_to_billing_address") != null) {
            request.getSession().setAttribute(WebKeys.ShippingContactInfoKey,
                                 contactInformation);
            request.getSession().setAttribute(WebKeys.ShippingAddressRequiredKey, "false");
        } else {
            request.getSession().setAttribute(WebKeys.ShippingAddressRequiredKey, "true");
        }
        ExplicitInformation explicitInformation = new ExplicitInformation(language,
                        favorite, (myListOpt.equals("true")? true : false),
                        (bannerOpt.equals("true")? true : false) );
        event.setInfo(userId,  password, status, contactInformation, explicitInformation);
        return event;
    }

    private EStoreEvent createUpdateAccountEvent(HttpServletRequest request){
        Debug.println("Updating existing Account and Profile");
        Debug.println("Getting Account Information");
        Locale currentLocale = JSPUtil.getLocale(request.getSession());
        ArrayList missingFields = null;
        AccountEvent event = new AccountEvent();

        String userId = request.getParameter("user_name").trim();
        if (userId.equals("")) {
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("User ID");
        }
        if (currentLocale.equals(Locale.JAPAN)) userId = JSPUtil.convertJISEncoding(userId);
        Debug.println("createUpdateAccountEvent: userId=" + userId);
        String email = request.getParameter("user_email").trim();
        if (email.equals("")) {
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("E-Mail Address");
        }
        Debug.println("createUpdateAccountEvent: email=" + email);
        Debug.println("Getting Profile Information");
        String language = request.getParameter("language").trim();
        if (language.equals("")) {
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("Language Preference");
        }
        Debug.println("createUpdateAccountEvent: language=" + language);
        // set the locale here
        Locale locale = JSPUtil.getLocaleFromLanguage(language);
        request.getSession().setAttribute(WebKeys.LanguageKey, locale);
        Debug.println("Account Handler set language to: " + language);
        String favorite = request.getParameter("favorite_category").trim();
        if (favorite.equals("")) {
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("Favorite Category");
        }
        Debug.println("createUpdateAccountEvent: favorite=" + favorite);

        String myListOpt = request.getParameter("myList_on");
        if (myListOpt == null)
          myListOpt = "false";
        else
          myListOpt = "true";
        Debug.println("createUpdateAccountEvent: myListOpt=" + myListOpt);

        String bannerOpt = request.getParameter("banners_on");
        if (bannerOpt == null)
           bannerOpt = "false";
        else
           bannerOpt = "true";
        Debug.println("createUpdateAccountEvent: bannerOpt=" + bannerOpt);

        if (missingFields != null) {
            MissingFormDataException ex = new MissingFormDataException("Missing Address Data", missingFields);
            request.setAttribute(WebKeys.MissingFormDataKey, ex);
            return null;
        }
        ContactInformation contactInformation = extractContactInformation(request);
        if (contactInformation == null) return null;
        Debug.println("createUpdateAccountEvent: contactInformation=" + contactInformation.toString());
        // copy shipping address if the same as billing address
        if (request.getParameter("ship_to_billing_address") != null) {
            request.getSession().setAttribute(WebKeys.ShippingContactInfoKey,
                                 contactInformation);
            request.getSession().setAttribute(WebKeys.ShippingAddressRequiredKey, "false");
        } else {
            request.getSession().setAttribute(WebKeys.ShippingAddressRequiredKey, "true");
        }
        ExplicitInformation explicitInformation = new ExplicitInformation(language,
                        favorite, (myListOpt.equals("true")? true : false),
                        (bannerOpt.equals("true")? true : false) );
        Debug.println("createUpdateAccountEvent: explicitInformation=" + explicitInformation.toString());
        event.setInfo(userId, contactInformation, explicitInformation);
        return event;
    }

    /** parse address form and generate a ContactInformation object */
    private ContactInformation extractContactInformation(HttpServletRequest request) {
        Locale currentLocale = JSPUtil.getLocale(request.getSession());
        ArrayList missingFields = null;
        String familyName =  request.getParameter("family_name").trim();
        // convert the encoding to unicode if it is coming from a Japanese page
        if (currentLocale.equals(Locale.JAPAN)) familyName = JSPUtil.convertJISEncoding(familyName);
        if (familyName.equals("")) {
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("Last Name");
        }
        String givenName =  request.getParameter("given_name").trim();
        // convert the encoding to unicode if it is coming from a Japanese page
        if (currentLocale.equals(Locale.JAPAN)) givenName = JSPUtil.convertJISEncoding(givenName);
        if (givenName.equals("")) {
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("First Name");
        }
        String address1 = request.getParameter("address_1").trim();
        // convert the encoding to unicode if it is coming from a Japanese page
        if (currentLocale.equals(Locale.JAPAN)) address1 = JSPUtil.convertJISEncoding(address1);
        if (address1.equals("")){
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("Street Address");
        }
        String address2 = request.getParameter("address_2").trim();
        // convert the encoding to unicode if it is coming from a Japanese page
        if (currentLocale.equals(Locale.JAPAN)) address2 = JSPUtil.convertJISEncoding(address2);
        String city =   request.getParameter("city").trim();
        // convert the encoding to unicode if it is coming from a Japanese page
        if (currentLocale.equals(Locale.JAPAN)) city = JSPUtil.convertJISEncoding(city);
        if (city.equals("")){
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("City");
        }
        String stateOrProvince = request.getParameter("state_or_province").trim();
        // convert the encoding to unicode if it is coming from a Japanese page
        if (currentLocale.equals(Locale.JAPAN)) stateOrProvince = JSPUtil.convertJISEncoding(stateOrProvince);
        if (stateOrProvince.equals("")) {
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("State or Province");
        }
        String postalCode = request.getParameter("postal_code").trim();
        // convert the encoding to unicode if it is coming from a Japanese page
        if (currentLocale.equals(Locale.JAPAN)) postalCode = JSPUtil.convertJISEncoding(postalCode);

        if (postalCode.equals("")){
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("Postal Code");
        }
        String country = request.getParameter("country").trim();
        String telephone = request.getParameter("telephone_number").trim();
        if (currentLocale.equals(Locale.JAPAN)) telephone = JSPUtil.convertJISEncoding(telephone);
        if (telephone.equals("")){
            if (missingFields == null) {
                missingFields = new ArrayList();
            }
            missingFields.add("Telephone Number");
        }
        String email = request.getParameter("user_email");
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

