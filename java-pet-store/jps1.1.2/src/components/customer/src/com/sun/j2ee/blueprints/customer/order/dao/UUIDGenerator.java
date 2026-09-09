/*
 * $Id: UUIDGenerator.java,v 1.1.2.1 2001/03/12 09:15:35 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.order.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAOSysException;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAODBUpdateException;

/**
 * This class is used to generate unique primary
 * keys for OrderEJB. Used by the OrderDAOCS.java
 *
 * @see OrderDAOCS
 */
public class UUIDGenerator implements java.io.Serializable {

    /**
     * This method gets the next sequence number
     * and updates the sequence number. A database
     * is used to store the sequence number.
     *
     * @return  the next sequence number
     */
    public static int nextSeqNum(Connection dbConnection) throws
                             OrderDAODBUpdateException, OrderDAOSysException {

        int seqNum = 0;

        Statement s = null;
        ResultSet rs = null;
        try {
            s = dbConnection.createStatement();
            rs = s.executeQuery(
                            "SELECT seqnum FROM sequence for update");
            if(rs.next())
                seqNum = rs.getInt(1);
            int resultCount = s.executeUpdate(
                                "update sequence set seqnum = seqnum + 1");
            if ( resultCount != 1 )
                throw new OrderDAODBUpdateException("Error while updating sequence");
        } catch(SQLException se) {
               throw new OrderDAOSysException(
                        "SQL Exception while updating sequence : \n" + se);
        }
        return (seqNum);
    }
}
