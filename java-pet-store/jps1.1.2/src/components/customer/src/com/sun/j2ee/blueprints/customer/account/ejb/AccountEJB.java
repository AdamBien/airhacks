/*
 * $Id: AccountEJB.java,v 1.9.4.12 2001/04/14 00:39:23 lblair Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.account.ejb;

import java.rmi.RemoteException;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.DuplicateKeyException;

import com.sun.j2ee.blueprints.customer.account.dao.AccountDAO;
import com.sun.j2ee.blueprints.customer.account.dao.AccountDAOFactory;
import com.sun.j2ee.blueprints.customer.util.ContactInformation;
import com.sun.j2ee.blueprints.customer.util.DatabaseNames;
import com.sun.j2ee.blueprints.customer.account.model.AccountModel;

import com.sun.j2ee.blueprints.customer.account.exceptions.AccountDAOSysException;
import com.sun.j2ee.blueprints.customer.account.exceptions.AccountDAOAppException;
import com.sun.j2ee.blueprints.customer.account.exceptions.AccountDAODBUpdateException;
import com.sun.j2ee.blueprints.customer.account.exceptions.AccountDAOFinderException;
import com.sun.j2ee.blueprints.customer.account.exceptions.AccountDAODupKeyException;
import com.sun.j2ee.blueprints.customer.account.exceptions.AccountAppException;
import com.sun.j2ee.blueprints.customer.account.exceptions.AccountAppLongIdException;
import com.sun.j2ee.blueprints.customer.account.exceptions.AccountAppInvalidCharException;

import com.sun.j2ee.blueprints.util.tracer.Debug;

/**
 * Implementation of account as an Entity Bean
 */
public class AccountEJB implements EntityBean {

    private AccountModel accountDetails;
    private EntityContext context;
    private transient AccountDAO accountDao;

    public AccountEJB() {}

    public String ejbCreate (String userId, String password, String status,
           ContactInformation info)
           throws DuplicateKeyException,CreateException,
                  AccountAppLongIdException, AccountAppInvalidCharException,
                  AccountAppException {

        // check the input data
        if(userId.length() > DatabaseNames.MAX_USERID_LENGTH)
            throw new AccountAppLongIdException("User ID cant be more than " +
                       DatabaseNames.MAX_USERID_LENGTH + " chars long");
        if(password.length() > DatabaseNames.MAX_PASSWD_LENGTH)
            throw new AccountAppLongIdException("Password cant be more than " +
                       DatabaseNames.MAX_PASSWD_LENGTH + " chars long");

        if( (userId.indexOf('%') != -1) ||
            (userId.indexOf('*') != -1) )
            throw new AccountAppInvalidCharException("The user Id cannot " +
                                            "have '%' or '*' characters");

        // set the instance data
        this.accountDetails = new AccountModel(userId, status, info);

        try {
            AccountDAO dao = getDAO();
            dao.create(this.accountDetails);
            return (userId);
        } catch(AccountDAODBUpdateException se) {
            context.setRollbackOnly();
            throw new CreateException (se.getMessage());
        } catch (AccountDAODupKeyException acd) {
            throw new DuplicateKeyException(acd.getMessage());
        } catch (AccountDAOAppException aca) {
            throw new AccountAppException(aca.getMessage());
        } catch (AccountDAOSysException acs) {
            throw new EJBException(acs.getMessage());
        }
    }

    public void ejbRemove() throws RemoveException {
        try{
            AccountDAO dao = getDAO();
            dao.remove((String)context.getPrimaryKey());
        } catch (AccountDAODBUpdateException se) {
            context.setRollbackOnly();
            throw new RemoveException (se.getMessage());
        } catch (AccountDAOSysException acs) {
            throw new EJBException(acs.getMessage());
        }
    }

    public void setEntityContext(EntityContext ec) {
        context = ec;
    }

    public void ejbLoad() {
        AccountDAO dao;
        try{
            dao = getDAO();
            this.accountDetails = dao.load((String)context.getPrimaryKey());
        } catch (AccountDAOFinderException se) {
            throw new EJBException (se.getMessage());
        } catch (AccountDAOSysException acs) {
            throw new EJBException(acs.getMessage());
        }
    }

    public void ejbStore() {
        try{
            AccountDAO dao = getDAO();
            dao.store(this.accountDetails);
        } catch (AccountDAOAppException se) {
            throw new EJBException (se.getMessage());
        } catch (AccountDAOSysException acs) {
            throw new EJBException(acs.getMessage());
        }
    }

    public String ejbFindByPrimaryKey (String key) throws FinderException {
        try{
            AccountDAO dao = getDAO();
            String userId = dao.findByPrimaryKey(key);
            this.accountDetails = dao.load(userId);
            return(userId);
        } catch (AccountDAOFinderException se) {
            throw new FinderException (se.getMessage());
        } catch (AccountDAOSysException acs) {
            throw new EJBException(acs.getMessage());
        }
    }

    public void unsetEntityContext() {}

    public void ejbActivate() {
    }

    public void ejbPassivate() {
        this.accountDao = null;
    }

    public void ejbPostCreate(String userId, String password, String status,
                              ContactInformation info)
        throws DuplicateKeyException,CreateException {
    }

    // business methods

    public void changeContactInformation(ContactInformation info) {
        String uid = accountDetails.getUserId();
        String stat = accountDetails.getStatus();
        this.accountDetails = new AccountModel(uid, stat, info);
    }

    public AccountModel getDetails() {
        return(this.accountDetails);
    }

    private AccountDAO getDAO() throws AccountDAOSysException {
        if(accountDao == null) {
           accountDao = AccountDAOFactory.getDAO();
        }
        return accountDao;
    }
}
