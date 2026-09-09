/*
 * $Id: SignOnEJB.java,v 1.1.2.15 2001/04/14 00:39:25 lblair Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.signon.ejb;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.DuplicateKeyException;

import com.sun.j2ee.blueprints.signon.util.DatabaseNames;
import com.sun.j2ee.blueprints.signon.dao.SignOnDAO;
import com.sun.j2ee.blueprints.signon.dao.SignOnDAOFactory;
import com.sun.j2ee.blueprints.signon.model.SignOnModel;
import com.sun.j2ee.blueprints.signon.model.MutableSignOnModel;

import com.sun.j2ee.blueprints.signon.exceptions.SignOnDAOSysException;
import com.sun.j2ee.blueprints.signon.exceptions.SignOnDAOAppException;
import com.sun.j2ee.blueprints.signon.exceptions.SignOnDAODBUpdateException;
import com.sun.j2ee.blueprints.signon.exceptions.SignOnDAODupKeyException;
import com.sun.j2ee.blueprints.signon.exceptions.SignOnDAOFinderException;
import com.sun.j2ee.blueprints.signon.exceptions.SignOnAppException;
import com.sun.j2ee.blueprints.signon.exceptions.SignOnAppLongIdException;
import com.sun.j2ee.blueprints.signon.exceptions.SignOnAppInvalidCharException;

/**
 * Implementation of signon as an Entity Bean
 */
public class SignOnEJB implements EntityBean {

    private MutableSignOnModel user;
    private transient SignOnDAO signonDao;
    private EntityContext context;

    public SignOnEJB() {}

    public String ejbCreate (String userName, String passWord)
           throws CreateException, DuplicateKeyException, SignOnAppException {

        // check the input data
        if(userName.length() > DatabaseNames.MAX_USERID_LENGTH)
            throw new SignOnAppLongIdException("User ID cant be more than " +
                       DatabaseNames.MAX_USERID_LENGTH + " chars long");
        if(passWord.length() > DatabaseNames.MAX_PASSWD_LENGTH)
            throw new SignOnAppLongIdException("Password cant be more than " +
                       DatabaseNames.MAX_PASSWD_LENGTH + " chars long");

        if( (userName.indexOf('%') != -1) ||
            (userName.indexOf('*') != -1) )
            throw new SignOnAppInvalidCharException("The user Id cannot " +
                                            "have '%' or '*' characters");

        this.user = new MutableSignOnModel(userName, passWord);

        try {
            SignOnDAO dao = getDAO();
            dao.create(this.user);
        } catch (SignOnDAODBUpdateException se) {
            context.setRollbackOnly();
            throw new CreateException("SignOnEJB: create failed : " +
                                            se.getMessage());
        } catch (SignOnDAODupKeyException sf) {
            throw new DuplicateKeyException("SignOnEJB: DupKey Exception : " +
                                            sf.getMessage());
        } catch (SignOnDAOAppException sg) {
            throw new SignOnAppException("SignOnEJB: user error : " +
                                            sg.getMessage());
        } catch (SignOnDAOSysException sh) {
            throw new EJBException("SignOnEJB: create failed : " +
                                            sh.getMessage());
        }
        return (userName);
    }

    /**
     * A post create method for this EJB object.
     * @param userName    a string which represents the id of this user
     * @param passWord    a password for this user
     * @throws          <code>CreateException</code> is thrown if the
     *                  userName could not be created for this user
     * @throws          <code>DuplicateKeyException</code> is thrown if the
     *                  userName already exists
     */
    public void ejbPostCreate(String userName, String passWord)
        throws CreateException, DuplicateKeyException, SignOnAppException {
    }

    public void ejbRemove() throws RemoveException {
        try{
            SignOnDAO dao = getDAO();
            dao.remove((String)context.getPrimaryKey());
        } catch (SignOnDAODBUpdateException se) {
            context.setRollbackOnly();
            throw new RemoveException(se.getMessage());
        } catch (SignOnDAOSysException ss) {
            throw new EJBException(ss.getMessage());
        }
    }

    public void setEntityContext(EntityContext ec) {
        context = ec;
    }

    public void ejbLoad() {
        SignOnDAO dao;
        try{
            dao = getDAO();
            this.user = dao.load((String)context.getPrimaryKey());
        } catch (SignOnDAOSysException se) {
            throw new EJBException (se.getMessage());
        } catch (SignOnDAOFinderException ss) {
            throw new EJBException (ss.getMessage());
        }
    }

    public void ejbStore() {
        try{
            SignOnDAO dao = getDAO();
            dao.store(this.user);
        } catch (SignOnDAOSysException se) {
            throw new EJBException (se.getMessage());
        } catch (SignOnDAOAppException ss) {
            throw new EJBException (ss.getMessage());
        }
    }

    public String ejbFindByPrimaryKey (String key) throws FinderException {

        try{
            SignOnDAO dao = getDAO();
            String usr = dao.findByPrimaryKey(key);
            this.user = dao.load(usr);
            return(usr);
        } catch (SignOnDAOFinderException se) {
            throw new FinderException (se.getMessage());
        } catch (SignOnDAOSysException ss) {
            throw new EJBException (ss.getMessage());
        }
    }

    public void unsetEntityContext() {}

    public void ejbActivate() {
    }

    public void ejbPassivate() {
      this.signonDao = null;
    }

    // business methods

    /**
     * @return  the SignOnModel containing the
     * signon data details
     */
    public SignOnModel getDetails() {
        return(new SignOnModel(this.user.getUserName(),
                               this.user.getPassWord()));
    }

    /**
     * update a users password
     */
    public void updatePassWord(String passWord) {
         this.user.setPassWord(passWord);
    }

    private SignOnDAO getDAO() throws SignOnDAOSysException {
        if(signonDao == null) {
            signonDao = SignOnDAOFactory.getDAO();
        }
        return signonDao;
    }
}
