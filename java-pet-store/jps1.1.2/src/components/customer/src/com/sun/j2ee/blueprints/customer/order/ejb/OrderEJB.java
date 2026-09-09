/*
 * $Id: OrderEJB.java,v 1.15.4.12 2001/04/14 00:39:24 lblair Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.order.ejb;

import java.util.Collection;
import java.util.Locale;
import java.util.Properties;
import java.rmi.RemoteException;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import com.sun.j2ee.blueprints.customer.util.EJBUtil;
import com.sun.j2ee.blueprints.customer.util.Calendar;
import com.sun.j2ee.blueprints.customer.util.Address;
import com.sun.j2ee.blueprints.customer.util.CreditCard;
import com.sun.j2ee.blueprints.customer.order.model.LineItem;
import com.sun.j2ee.blueprints.customer.order.model.OrderModel;
import com.sun.j2ee.blueprints.customer.order.model.MutableOrderModel;

import com.sun.j2ee.blueprints.customer.order.dao.OrderDAO;
import com.sun.j2ee.blueprints.customer.order.dao.OrderDAOFactory;
import com.sun.j2ee.blueprints.customer.account.ejb.Account;
import com.sun.j2ee.blueprints.customer.account.ejb.AccountHome;

import com.sun.j2ee.blueprints.customer.order.exceptions.OrderAppException;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAOSysException;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAOAppException;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAODBUpdateException;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAODupKeyException;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAOFinderException;

import com.sun.j2ee.blueprints.util.tracer.Debug;

/**
 * Entity Bean implementation of Order EJB.Uses
 * the Account EJB to get account info associated
 * with an order.
 *
 * @see Account
 */
public class OrderEJB implements EntityBean {

    private MutableOrderModel orderDetails;
    private EntityContext context;
    private transient OrderDAO orderDao;

    public OrderEJB() {}

    public Integer ejbCreate (Collection lineItems, Address shipToAddr,
                              Address billToAddr, String shipToFirstName,
                              String shipToLastName, String billToFirstName,
                              String billToLastName, CreditCard chargeCard,
                              String carrier, String userId,
                              double totalPrice, Locale locale)  throws CreateException,
                                                  OrderAppException {

        // set the instance data
        this.orderDetails = new MutableOrderModel(-1, lineItems, shipToAddr,
                                           billToAddr, shipToFirstName,
                                           shipToLastName, billToFirstName,
                                           billToLastName, chargeCard,
                                           carrier, userId,
                                           Calendar.getInstance(),
                                           Order.PENDING, totalPrice, locale);
        try {
            OrderDAO dao = getDAO();
            int id = dao.create(this.orderDetails);
            this.orderDetails.setOrderId(id);
            return(new Integer(id));
        } catch (OrderDAODBUpdateException se) {
            context.setRollbackOnly();
            throw new CreateException (se.getMessage());
        } catch (OrderDAOAppException oapp) {
            throw new OrderAppException(oapp.getMessage());
        } catch (OrderDAOSysException osys) {
            throw new EJBException(osys.getMessage());
        }
    }

    public void ejbRemove() throws RemoveException {

        try {
            OrderDAO dao = getDAO();
            dao.remove(((Integer)context.getPrimaryKey()).intValue());
        } catch (OrderDAODBUpdateException se) {
            context.setRollbackOnly();
            throw new RemoveException (se.getMessage());
        } catch (OrderDAOSysException osys) {
            throw new EJBException(osys.getMessage());
        }
    }

    public void setEntityContext(EntityContext ec) {
        context = ec;
    }

    public void ejbLoad() {
        try{
            OrderDAO dao = getDAO();
            Integer id = (Integer)context.getPrimaryKey();
            this.orderDetails = dao.load(id.intValue());
        } catch (OrderDAOFinderException se) {
            throw new EJBException (se.getMessage());
        } catch (OrderDAOSysException ss) {
            throw new EJBException (ss.getMessage());
        }
    }

    public void ejbStore() {
        try{
            OrderDAO dao = getDAO();
            dao.store(this.orderDetails);
        } catch (OrderDAOSysException se) {
            throw new EJBException (se.getMessage());
        } catch (OrderDAOAppException sa) {
            throw new EJBException (sa.getMessage());
        }
    }

    /**
     * @param  key is the orderID for a particular order
     */
    public Integer ejbFindByPrimaryKey(Integer key) throws FinderException {
        try{
            OrderDAO dao = getDAO();
            Integer findReturn = dao.findByPrimaryKey(key.intValue());
            this.orderDetails = dao.load(key.intValue());
            return(findReturn);
        } catch (OrderDAOFinderException se) {
            throw new FinderException ("SQL Exception in find by primary key");
        } catch (OrderDAOSysException ss) {
            throw new EJBException (ss.getMessage());
        }
    }

    /**
     * @return  a Collection of references to all the order EJBs
     *          for the userId.
     */
    public Collection ejbFindUserOrders( String userId )
        throws FinderException {

        try{
            OrderDAO dao = getDAO();
            return(dao.findUserOrders(userId));
        } catch (OrderDAOFinderException se) {
            throw new FinderException
                ("SQL Exception in finding collection of primary keys");
        } catch (OrderDAOSysException ss) {
            throw new EJBException (ss.getMessage());
        }
    }

    public void unsetEntityContext() {}

    public void ejbActivate() {
    }

    public void ejbPassivate() {
      this.orderDao = null;
    }

    public void ejbPostCreate(Collection lineItems, Address shipToAddr,
                              Address billToAddr, String shipToFirstName,
                              String shipToLastName, String billToFirstName,
                              String billToLastName, CreditCard chargeCard,
        String carrier, String userId,double totalPrice, Locale locale){}

    /**
     * @return  the OrderModel containing the details
     *           of an order
     */
    public OrderModel getDetails() {
        return(new OrderModel(orderDetails.getOrderId(),
                              orderDetails.getLineItems(),
                              orderDetails.getShipToAddr(),
                              orderDetails.getBillToAddr(),
                              orderDetails.getShipToFirstName(),
                              orderDetails.getShipToLastName(),
                              orderDetails.getBillToFirstName(),
                              orderDetails.getBillToLastName(),
                              orderDetails.getCreditCard(),
                              orderDetails.getCarrier(),
                              orderDetails.getUserId(),
                              orderDetails.getOrderDate(),
                              orderDetails.getStatus(),
                              orderDetails.getTotalPrice(),
                              orderDetails.getLocale()));
    }

    /**
     * @return  the Account EJB associated with this order
     */
    public Account getAccount() throws FinderException {
        try {
            AccountHome acctHome = EJBUtil.getAccountHome();
            return acctHome.findByPrimaryKey(this.orderDetails.getUserId());
        } catch (FinderException fe) {
            // Account mysterious disappeared..
            Debug.print(fe);
            throw new FinderException(fe.getMessage());
        } catch (NamingException ne) {
            // Account mysterious disappeared..
            Debug.print(ne);
            throw new EJBException(ne);
        } catch (RemoteException re) {
            Debug.print(re);
            throw new EJBException(re);
        }
    }

    private OrderDAO getDAO() throws OrderDAOSysException {
        if(orderDao == null) {
            orderDao = OrderDAOFactory.getDAO();
        }
        return orderDao;
    }
}
