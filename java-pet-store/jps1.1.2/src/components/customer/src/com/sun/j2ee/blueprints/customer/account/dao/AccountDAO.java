/*
 * $Id: AccountDAO.java,v 1.1.2.1 2001/03/12 09:15:31 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.account.dao;

import com.sun.j2ee.blueprints.customer.account.exceptions.AccountDAOSysException;
import com.sun.j2ee.blueprints.customer.account.exceptions.AccountDAOAppException;
import com.sun.j2ee.blueprints.customer.account.exceptions.AccountDAODBUpdateException;
import com.sun.j2ee.blueprints.customer.account.exceptions.AccountDAOFinderException;
import com.sun.j2ee.blueprints.customer.account.exceptions.AccountDAODupKeyException;

import com.sun.j2ee.blueprints.customer.account.model.AccountModel;
import com.sun.j2ee.blueprints.customer.util.ContactInformation;
import com.sun.j2ee.blueprints.customer.util.Address;

/**
 * This is the interface for Account DAO.
 */
public interface AccountDAO {

    public void create(AccountModel details) throws AccountDAOSysException,
                                AccountDAODupKeyException,
                                AccountDAODBUpdateException,
                                AccountDAOAppException;
    public AccountModel load(String id) throws   AccountDAOSysException,
                                AccountDAOFinderException;
    public void store(AccountModel details) throws AccountDAODBUpdateException,
                                AccountDAOAppException,
                                AccountDAOSysException;
    public void remove(String id) throws AccountDAODBUpdateException,
                                AccountDAOSysException;
    public String findByPrimaryKey(String id) throws AccountDAOFinderException,
                                            AccountDAOSysException;
}
