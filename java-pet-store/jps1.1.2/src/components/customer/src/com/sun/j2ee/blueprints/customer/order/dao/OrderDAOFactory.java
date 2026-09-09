/*
 * $Id: OrderDAOFactory.java,v 1.1.2.2 2001/03/13 00:52:41 lblair Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.order.dao;


import javax.naming.NamingException;
import javax.naming.InitialContext;

import com.sun.j2ee.blueprints.customer.util.JNDINames;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAOSysException;
import com.sun.j2ee.blueprints.util.tracer.Debug;

public class OrderDAOFactory {

    /**
     * This method instantiates a particular subclass implementing
     * the abstract methods based on the information obtained from the
     * deployment descriptor
     */
    public static OrderDAO getDAO() throws OrderDAOSysException {

        OrderDAO orderDao = null;
        try {
            InitialContext ic = new InitialContext();
            String className = (String) ic.lookup(JNDINames.ORDER_DAO_CLASS);
            orderDao = (OrderDAO) Class.forName(className).newInstance();
        } catch (NamingException ne) {
            throw new OrderDAOSysException("OrderDAOFactory.getDAO: " +
                "NamingException while getting DAO type : \n"+ne.getMessage());
        } catch (Exception se) {
            throw new OrderDAOSysException("OrderDAOFactory.getDAO: " +
                "Exception while getting DAO type : \n" + se.getMessage());
        }
        return orderDao;
    }

}
