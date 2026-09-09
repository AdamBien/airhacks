/*
 * $Id: AdminClientControllerEJB.java,v 1.4.4.6 2001/03/14 23:56:41 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstoreadmin.control.ejb;

import javax.rmi.PortableRemoteObject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

import com.sun.j2ee.blueprints.petstoreadmin.control.util.JNDINames;
import com.sun.j2ee.blueprints.petstoreadmin.control.ejb.AdminOrderDAO;
import com.sun.j2ee.blueprints.petstoreadmin.control.ejb.AdminOrderDAOException;

public class AdminClientControllerEJB implements SessionBean {

    private SessionContext sc;
    private AdminOrderDAO admDAO = null;
    private String admin_user = null;

    public AdminClientControllerEJB() {}

    public void ejbCreate() throws CreateException {
        try {
            InitialContext ic = new InitialContext();
            admin_user = (String) ic.lookup(JNDINames.ADMIN_USER_NAME);
        } catch (NamingException ne) {
            throw new EJBException("AdminOrderDAO:  NamingException while getting admin user id : \n" + ne.getMessage());
        } catch (Exception se) {
            throw new EJBException("AdminOrderDAO:  Exception while getting admin user id : \n" + se.getMessage());
        }
    }

    public void setSessionContext(SessionContext sc) {
        this.sc = sc;
    }

    public void ejbRemove() {}

    public void ejbActivate() {}

    public void ejbPassivate() {}

    private void getAdminDAO() {
        try {
            admDAO = new AdminOrderDAO();
        } catch(AdminOrderDAOException ae) {
            throw new EJBException(ae);
        }
    }

    public Collection getPendingOrders(Locale locale) {

        Collection orderColl = null;

        try {
            if(admDAO == null)
                getAdminDAO();
            orderColl = admDAO.getAllPendingOrders(locale);
        } catch(AdminOrderDAOException ae) {
            throw new EJBException("ACCEJB: getOrders Exception" + ae);
        }
        return(orderColl);
    }

    public void setOrdersStatus(ArrayList ordersList) {

        try{
            String caller = sc.getCallerPrincipal().getName();
            if(caller.equals(admin_user)) {
                if(admDAO == null)
                    getAdminDAO();
                admDAO.setNewStatus(ordersList);
            } else {
                throw new EJBException("Caller is not an Administrator!");
            }
        } catch (AdminOrderDAOException ae) {
            throw new EJBException("AdminOrders: AdminOrderDAOException" + ae);
        }
    }
}
