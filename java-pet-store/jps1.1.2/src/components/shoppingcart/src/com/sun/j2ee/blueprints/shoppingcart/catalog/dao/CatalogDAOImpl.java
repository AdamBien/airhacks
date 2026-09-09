/*
 * $Id: CatalogDAOImpl.java,v 1.1.2.4 2001/04/13 22:50:26 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits r?erv?.
 */

package com.sun.j2ee.blueprints.shoppingcart.catalog.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.naming.NamingException;

import com.sun.j2ee.blueprints.shoppingcart.util.JNDINames;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.ListChunk;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Category;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Product;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Item;
import com.sun.j2ee.blueprints.shoppingcart.util.DatabaseNames;

import com.sun.j2ee.blueprints.shoppingcart.catalog.exceptions.CatalogDAOSysException;

import com.sun.j2ee.blueprints.util.tracer.Debug;

/**
 * This class implements CatalogDAO for oracle, sybase and cloudscape DBs.
 * This class encapsulates all the SQL calls made by Catalog EJB.
 * This layer maps the relational data stored in the database to
 * the objects needed by Catalog EJB.
*/
public class CatalogDAOImpl implements CatalogDAO {

    private Connection dbConnection;
    private DataSource datasource;

    public CatalogDAOImpl()  throws CatalogDAOSysException {
        try {
            InitialContext ic = new InitialContext();
            datasource = (DataSource) ic.lookup(JNDINames.ESTORE_DATASOURCE);
        } catch (NamingException ne) {
            throw new CatalogDAOSysException("NamingException while looking " +
                            "up DB context  : " + ne.getMessage());
        }
    }

    public Category getCategory(String categoryId, Locale locale) throws
                                    CatalogDAOSysException {
        String qstr = "select catid, name, descn from "
          + DatabaseNames.getTableName(DatabaseNames.CATEGORY_TABLE, locale)
          + " where catid = '" + categoryId + "'";

        Category cat = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            getDBConnection();
            stmt = dbConnection.createStatement();
            rs = stmt.executeQuery(qstr);
            while (rs.next()) {
                int i = 1;
                String catid = rs.getString(i++).trim();
                String name = rs.getString(i++);
                String descn = rs.getString(i++);
                cat = new Category(catid, name, descn);
            }
        } catch(SQLException se) {
            throw new CatalogDAOSysException("SQLException while getting " +
                        "Category " + categoryId + " : " + se.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection();
        }
        return cat;
    }

    public ListChunk getCategories(int startIndex, int count,
                            Locale locale) throws CatalogDAOSysException {
        String qstr = "select catid, name, descn from " +
            DatabaseNames.getTableName(DatabaseNames.CATEGORY_TABLE, locale) +
            " order by name";

        int localCount = 0; // used to count the number of elems in list

        int num =
            getCount(DatabaseNames.getTableName(DatabaseNames.CATEGORY_TABLE,
                    locale));

        ArrayList al = new ArrayList();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            getDBConnection();
            stmt = dbConnection.createStatement();
            rs = stmt.executeQuery(qstr);

            // skip initial rows as specified by the startIndex parameter.
            while (startIndex-- > 0 && rs.next());

            // Now get data as requested.
            while (count-- > 0 && rs.next()) {
                int i = 1;
                String catid = rs.getString(i++).trim();
                String name = rs.getString(i++);
                String descn = rs.getString(i++);
                Category cat = new Category(catid, name, descn);
                al.add(cat);
                localCount++;
            }
        } catch(SQLException se) {
            throw new CatalogDAOSysException("SQLException while getting " +
                        "multiple categories : " + se.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection();
        }
        ListChunk rl = new ListChunk(num, al, startIndex, localCount);
        return rl;
    }

    public Product getProduct(String productId, Locale locale) throws
                                        CatalogDAOSysException {

        String qstr =
            "select productid, name, descn " +
            "from " +
            DatabaseNames.getTableName(DatabaseNames.PRODUCT_TABLE, locale) +
            " where " +
            "productid='" + productId + "'";
        Debug.println("Query String is:" + qstr);

        Product product = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            getDBConnection();
            stmt = dbConnection.createStatement();
            rs = stmt.executeQuery(qstr);
            while (rs.next()) {
                int i = 1;
                String productid = rs.getString(i++).trim();
                String name = rs.getString(i++);
                String descn = rs.getString(i++);
                product = new Product(productid, name, descn);
            }
        } catch(SQLException se) {
            throw new CatalogDAOSysException("SQLException while getting " +
                        "product " + productId + " : " + se.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection();
        }
        return product;
    }

    public ListChunk getProducts(String categoryId, int startIndex,
                int count, Locale locale) throws CatalogDAOSysException {

        int localCount = 0 ;
        String qstr =
            "select productid, name, descn " +
            "from " +
            DatabaseNames.getTableName(DatabaseNames.PRODUCT_TABLE, locale) +
            " where category =  " + "'" + categoryId + "' " + " order by name";
        Debug.println("Query String is:" + qstr);

        int num =
            getCount(DatabaseNames.getTableName(DatabaseNames.PRODUCT_TABLE,
                locale) + " where category =  " + "'" + categoryId + "' ");

        ArrayList al = new ArrayList();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            getDBConnection();
            stmt = dbConnection.createStatement();
            rs = stmt.executeQuery(qstr);
            HashMap table = new HashMap();

            // skip initial rows as specified by the startIndex parameter.
            while (startIndex-- > 0 && rs.next());

            // Now get data as requested.
            while (count-- > 0 && rs.next()) {
                int i = 1;
                String productid = rs.getString(i++).trim();
                String name = rs.getString(i++).trim();
                String descn = rs.getString(i++).trim();

                Product product = null;
                if (table.get(productid) == null) {
                    product = new Product(productid, name, descn);
                    table.put(productid, product);
                    al.add(product);
                    localCount++;
                }
            }
        } catch(SQLException se) {
            throw new CatalogDAOSysException("SQLException while getting " +
                        "multiple products for cat " + categoryId + " : " +
                        se.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection();
        }
        ListChunk rl = new ListChunk(num, al, startIndex, localCount);
        return rl;
    }

    public Item getItem(String itemId, Locale locale) throws
                                        CatalogDAOSysException {

        String qstr =
            "select itemid, listprice, unitcost, " +
            "attr1, attr2, a.productid, name, descn " +
            "from " +
            DatabaseNames.getTableName(DatabaseNames.ITEM_TABLE, locale) +
            " a, " +
            DatabaseNames.getTableName(DatabaseNames.PRODUCT_TABLE, locale) +
            " b where " + "a.itemid = '" + itemId + "' order by name";
        Debug.println("Query String is:" + qstr);

        Item item = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            getDBConnection();
            stmt = dbConnection.createStatement();
            rs = stmt.executeQuery(qstr);
            while (rs.next()) {
                int i = 1;
                String itemid = rs.getString(i++).trim();
                double listprice = rs.getDouble(i++);
                double unitcost = rs.getDouble(i++);
                String attr1 = rs.getString(i++);
                String attr2 = rs.getString(i++);
                String productid = rs.getString(i++).trim();
                String name = rs.getString(i++);
                String descn = rs.getString(i++);
                if ((attr2 != null) && !attr2.trim().equals("")) {
                    attr1 += ", " + attr2;
                }
                item = new Item(productid, itemid, attr1, listprice, unitcost);
            }
        } catch(SQLException se) {
            throw new CatalogDAOSysException("SQLException while getting " +
                        "Iten " + itemId + " : " + se.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection();
        }
        return item;
    }

    public ListChunk getItems(String productId, int startIndex, int count,
                                Locale locale) throws CatalogDAOSysException {

        int localCount = 0;
        String qstr =
            "select itemid, listprice, unitcost, " +
            "attr1, attr2, a.productid, name, descn " +
            "from " +
            DatabaseNames.getTableName(DatabaseNames.ITEM_TABLE, locale) +
            " a, " +
            DatabaseNames.getTableName(DatabaseNames.PRODUCT_TABLE, locale) +
            " b where " + "a.productid = '" + productId +
            "' and a.productid = b.productid "+ " order by name";
        Debug.println("Query String is:" + qstr);

        int num =
            getCount(DatabaseNames.getTableName(DatabaseNames.ITEM_TABLE,
                locale) + " a, " +
                    DatabaseNames.getTableName(DatabaseNames.PRODUCT_TABLE,
                        locale) + " b where a.productid = '" + productId +
                            "' and a.productid = b.productid");
        ArrayList al = new ArrayList();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            getDBConnection();
            stmt = dbConnection.createStatement();
            rs = stmt.executeQuery(qstr);
            HashMap table = new HashMap();

            // skip initial rows as specified by the startIndex parameter.
            while (startIndex-- > 0 && rs.next());

            // Now get data as requested.
            while (count-- > 0 && rs.next()) {
                int i = 1;
                String itemid = rs.getString(i++).trim();
                double listprice = rs.getDouble(i++);
                double unitcost = rs.getDouble(i++);
                String attr1 = rs.getString(i++);
                String attr2 = rs.getString(i++);
                String productid = rs.getString(i++).trim();
                String name = rs.getString(i++);
                String descn = rs.getString(i++);
                if ((attr2 != null) && !attr2.trim().equals("")) {
                    attr1 += ", " + attr2;
                }
                Item item = new Item(productid, itemid, attr1, listprice,
                                                    unitcost);
                al.add(item);
                localCount++;
            }
        } catch(SQLException se) {
            throw new CatalogDAOSysException("SQLException while getting " +
                        "items for prod " + productId + " : " + se.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection();
        }
        ListChunk rl = new ListChunk(num, al, startIndex, localCount);
        return rl;
    }

    public ListChunk searchProducts(Collection keyWords, int startIndex,
                    int count, Locale locale) throws CatalogDAOSysException {
        int num = 0, i;
        int localCount = 0;
        String qstr1 =
            "select itemid, listprice, unitcost, " +
            "attr1, a.productid, name, descn " +
            "from " +
            DatabaseNames.getTableName(DatabaseNames.ITEM_TABLE, locale) +
            " a, " +
            DatabaseNames.getTableName(DatabaseNames.PRODUCT_TABLE, locale) +
            " b where " + "a.productid = b.productid and " + "b.productid in ";
        String qstr = "";
        qstr += qstr1;
        qstr += getQueryStr (keyWords, locale);
        ArrayList al = new ArrayList();
        ArrayList pids = new ArrayList();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap table = new HashMap();
        try {
            getDBConnection();
            // Use a modified prepared statement to allow for proper lookup of Japanese items
            stmt =createPreparedStatement(dbConnection, qstr);
            rs = stmt.executeQuery();
            while(rs.next()) {

                // get the query results
                i = 1;
                String itemid = rs.getString(i++).trim();
                double listprice = rs.getDouble(i++);
                double unitcost = rs.getDouble(i++);
                String attr1 = rs.getString(i++);
                String productid = rs.getString(i++).trim();
                String name = rs.getString(i++);
                String descn = rs.getString(i++);

                // dont send back the same product twice
                if(pids.contains(productid))
                    continue;

                Product product = null;
                product = new Product(productid, name, descn);
                pids.add(productid);
                table.put(productid, product);
                // update total count of unique products that satisfy search
                num++;
            }
            for(i=0; i<pids.size(); i++) {

                // skip initial rows
                if(startIndex-- > 0)
                    continue;

                // If list if full, dont send more prods
                if(count == localCount)
                    break;

                // Get the product from table and put it on the ListChunk
                al.add(table.get(pids.get(i)));
                localCount++;
            }
        } catch(SQLException se) {
            throw new CatalogDAOSysException("SQLException while searching : "+
                       se.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection();
        }
        ListChunk rl = new ListChunk(num, al, startIndex, localCount);
        return rl;
    }

    private String getQueryStr(Collection keyWords, Locale locale) {
        String qstr = new String();

        if (keyWords.size() < 1)
            return qstr;

        qstr = " ( SELECT productid from " +
            DatabaseNames.getTableName(DatabaseNames.PRODUCT_TABLE, locale)+
                " WHERE ( ( lower(name) like ";
        for (Iterator it = keyWords.iterator() ; it.hasNext() ; ) {
            String keyword = ((String)it.next()).toLowerCase();
            qstr += "'%" + keyword + "%'";
            if ((keyWords.size() > 1) && it.hasNext())
                qstr += " OR lower(name) like ";
        }
        qstr += ") OR ( lower(category) like ";
        for (Iterator it = keyWords.iterator() ; it.hasNext() ; ) {
            qstr += "'%" + ((String)it.next()).toLowerCase() + "%'";
            if ((keyWords.size() > 1) && it.hasNext())
                qstr += " OR lower(category) like ";
        }
        qstr += ") ) ) ";
        return qstr;
    }

    private int getCount(String tableEtAl) throws CatalogDAOSysException {
        String qstr = "select COUNT(*) from " +  tableEtAl;
        int count;

        Statement stmt = null;
        ResultSet rs = null;
        try {
            getDBConnection();
            stmt = dbConnection.createStatement();
            rs = stmt.executeQuery(qstr);
            rs.next();
            count = rs.getInt(1);
        } catch(SQLException se) {
            throw new CatalogDAOSysException("SQLException while getting " +
                        "COUNT : " + se.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection();
        }
        return count;
    }

    private void getDBConnection() throws CatalogDAOSysException {
        try {
            dbConnection =  datasource.getConnection();
        } catch (SQLException se) {
            throw new CatalogDAOSysException("SQLException while getting " +
                            "DB connection  : " + se.getMessage());
        }
    }

    private void closeStatement(Statement stmt) throws CatalogDAOSysException {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch(SQLException se) {
            throw new CatalogDAOSysException("SQLException while closing " +
                            "statement  : " + se.getMessage());
        }
    }

    private void closeResultSet(ResultSet result) throws CatalogDAOSysException {
        try {
            if (result != null) {
                result.close();
            }
        } catch(SQLException se) {
            throw new CatalogDAOSysException("SQLException while closing " +
                            "result  : " + se.getMessage());
        }
    }

    private void closeConnection() throws CatalogDAOSysException {
        try {
            if (dbConnection != null && !dbConnection.isClosed()) {
                dbConnection.close();
            }
        } catch(SQLException se) {
            throw new CatalogDAOSysException("SQLException while closing " +
                            "DB connection  : " + se.getMessage());
        }
    }

    /**
     * This method allows us to create a prepared search statement that will be friendly
     * To Japanese in cloudscape and other databases.
     * Basically we use a prepared statement that contants '?' where Japanese characters
     * may occur and then we use the stmt.setString(index, "search string")
     *
     * This technique should not affect the English searchs.
     *
    */

    private PreparedStatement createPreparedStatement(Connection con, String querry)
            throws SQLException {
        ArrayList targetStrings = new ArrayList();
        String processedQuerry = "";
        int startIndex = 0;
        if (startIndex != -1) {
            int index = startIndex;
            int literalStart = -1;
            while (index < querry.length()) {
                if (querry.charAt(index) == '\'') {
                    if (literalStart == -1 && index + 1 < querry.length()) {
                        literalStart = index +1;
                    } else {
                        String targetString = querry.substring(literalStart, index);
                        targetStrings.add(targetString);
                        literalStart = -1;
                        processedQuerry += "?";
                        index++;
                    }
                }
               if (index < querry.length() && literalStart == -1) {
                    processedQuerry += querry.charAt(index);
                }
                index++;
            }
            PreparedStatement stmt = con.prepareStatement(processedQuerry);
            Iterator it = targetStrings.iterator();
            int counter =1;
            while (it.hasNext()) {
                String arg = (String)it.next();
                stmt.setString(counter++, arg);
            }
            return stmt;
        } else {
            PreparedStatement stmt = con.prepareStatement(querry);
            return stmt;
        }
    }
}
