/*
 * $Id: OrderDAO.java,v 1.1.2.1 2001/03/12 09:15:33 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.order.dao;

import java.util.Collection;

import com.sun.j2ee.blueprints.customer.util.Address;
import com.sun.j2ee.blueprints.customer.util.Calendar;
import com.sun.j2ee.blueprints.customer.util.CreditCard;

import com.sun.j2ee.blueprints.customer.order.model.MutableOrderModel;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAOSysException;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAOAppException;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAODBUpdateException;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAODupKeyException;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAOFinderException;

/**
 * This class is the interface that has to be implemented for each database
 * types.
 */
public interface OrderDAO {

    // methods to create / load / store / remove orders

    public abstract int create(MutableOrderModel details) throws
                                         OrderDAOSysException,
                                         OrderDAODBUpdateException,
                                         OrderDAOAppException;
    public MutableOrderModel load(int orderId) throws OrderDAOFinderException,
                              OrderDAOSysException;
    public abstract void store(MutableOrderModel details) throws
                                        OrderDAOSysException,
                                        OrderDAOAppException,
                                        OrderDAODBUpdateException;
    public void remove(int orderId) throws OrderDAODBUpdateException,
                                OrderDAOSysException;

    public Integer findByPrimaryKey(int key) throws OrderDAOSysException,
                                             OrderDAOFinderException;
    public Collection findUserOrders(String id) throws OrderDAOFinderException,
                                              OrderDAOSysException;
}
