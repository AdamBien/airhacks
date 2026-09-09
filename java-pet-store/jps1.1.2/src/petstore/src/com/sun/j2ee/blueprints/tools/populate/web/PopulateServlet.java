
package com.sun.j2ee.blueprints.tools.populate.web;

import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.j2ee.blueprints.tools.populate.web.PopulateBean;

import com.sun.j2ee.blueprints.util.tracer.Debug;

public class PopulateServlet extends HttpServlet {

    private String currentURL = null;
    private String petstoreURL = null;
    private ArrayList requiredTables;
    private ArrayList optionalTables;
    private String ESTORE_DATASOURCE =
        "java:comp/env/jdbc/EstoreDataSource";
    private ServletContext context;

    public void init(ServletConfig cfg) throws ServletException{
        super.init(cfg);
        this.context = cfg.getServletContext();
        requiredTables = new ArrayList();
        optionalTables = new ArrayList();
        requiredTables.add("ACCOUNT");
        requiredTables.add("BANNERDATA");
        requiredTables.add("CATEGORY");
        requiredTables.add("INVENTORY");
        requiredTables.add("ITEM");
        requiredTables.add("LINEITEM");
        requiredTables.add("ORDERS");
        requiredTables.add("ORDERSTATUS");
        requiredTables.add("PRODUCT");
        requiredTables.add("PROFILE");
        requiredTables.add("SIGNON");
        requiredTables.add("SUPPLIER");
        optionalTables.add("CATEGORY_JA");
        optionalTables.add("ITEM_JA");
        optionalTables.add("PRODUCT_JA");
    }

    public  void doGet(HttpServletRequest request, HttpServletResponse  response)
        throws ServletException, IOException {
        String contextRoot  = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        if (currentURL == null)  currentURL = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI();
        if (petstoreURL == null)  petstoreURL = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        HttpSession session = request.getSession();
        response.setContentType("text/html;charset=8859_1");

        Connection dbConnection = null;
        String command = request.getParameter("command");
        String forwardPage = request.getParameter("forward");
        String redirectPage = request.getParameter("redirect");

        String cleanRedirect = null;
        /* decode the parameters of the url redirect
           Normatlly this could be done using the HttpUtils.parseQuerryString
           but since it is deprecated as of the Servlet spec 2.3 we will
           decode the hexadecimal noation mannually
         */
        if ((redirectPage != null) && redirectPage.indexOf('%') != -1) {
            cleanRedirect = "";
            int index = 0;
            while (index< redirectPage.length()) {
                char target = redirectPage.charAt(index);
                if (target == '%' && index +3 < redirectPage.length()) {
                    // covert %XX hex to an intger
                    target = (char)Integer.parseInt(redirectPage.substring(index+1, index+3), 16);
                    index+=3;
                } else {
                    index++;
                }
                cleanRedirect += target;

            }
        }
        try {
            if (command == null) {
                dbConnection = checkConnection(request, response);
                if (dbConnection != null) {
                    showTables(request, response, dbConnection);
                    closeConnection(dbConnection);
                }
            } else if (command.equals("installTables")) {
                dbConnection = checkConnection(request, response);
                String databaseName = request.getParameter("database_name");
                if (dbConnection != null && databaseName != null) {
                    boolean success =  installTables(response, dbConnection, databaseName, false);
                    if (success) {
                        showTables(request, response, dbConnection);
                        closeConnection(dbConnection);
                    } else {
                        closeConnection(dbConnection);
                        return;
                    }
                } else {
                    closeConnection(dbConnection);
                    context.getRequestDispatcher("/populate_db_failed.jsp").forward(request, response);
                }
            }  else if (command.equals("chooseTables")) {
                dbConnection = checkConnection(request, response);
                chooseTables(request, response, dbConnection);
                closeConnection(dbConnection);
            }  else if (command.equals("checkTables")) {
                dbConnection = checkConnection(request, response);
                if ((dbConnection != null) && (forwardPage != null || cleanRedirect != null)) {
                    boolean pass = checkTables(response, dbConnection);
                    if (pass && forwardPage != null) {
                        closeConnection(dbConnection);
                        context.getRequestDispatcher(forwardPage).forward(request,response);
                    } else if (pass && cleanRedirect != null){
                        closeConnection(dbConnection);
                        response.sendRedirect(contextRoot + cleanRedirect);
                    } else {
                        showTables(request, response, dbConnection);
                        closeConnection(dbConnection);
                    }
                } else {
                    closeConnection(dbConnection);
                    return;
                }
            } else if (command.equals("checkJapaneseTables")) {
                dbConnection = checkConnection(request, response);
                if ((dbConnection != null) && (forwardPage != null || cleanRedirect != null)) {
                    boolean pass = checkJapaneseTables(response, dbConnection);
                    if (pass && forwardPage != null) {
                        closeConnection(dbConnection);
                        context.getRequestDispatcher(forwardPage).forward(request,response);
                    } else if (pass && cleanRedirect != null){
                        closeConnection(dbConnection);
                        response.sendRedirect(contextRoot + cleanRedirect);
                    } else {
                        showTables(request, response, dbConnection);
                        closeConnection(dbConnection);
                    }
                } else {
                    closeConnection(dbConnection);
                    return;
                }
            }
       } catch (Exception ex) {
            Debug.println("PopulateServlet caught: " + ex);

            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<body bgcolor=white>");
            out.println("<font size=+4 color=red>PopulateServlet Error:</font><br>" + ex);
            ex.printStackTrace(out);
            out.println("</body></html>");
        } finally {
            closeConnection(dbConnection);
        }
     }

    private Connection checkConnection(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Connection dbConnection = null;
        try {
            dbConnection = getDBConnection();
        } catch (IOException iox) {
            dbConnection =  null;
        }
        if (dbConnection != null) {
            boolean closed = true;
            try {
                closed = dbConnection.isClosed();
            } catch (SQLException se) {
                dbConnection =  null;
            }
            if (!closed) {
                return dbConnection;
            } else {
                context.getRequestDispatcher("/populate_db_failed.jsp").forward(request, response);
            }
        } else {
          context.getRequestDispatcher("/populate_db_failed.jsp").forward(request, response);
        }
        return null;
    }

    private void chooseTables (HttpServletRequest request, HttpServletResponse response, Connection dbConnection)
                    throws IOException, ServletException {
        String databaseName = null;
        try {
            databaseName = dbConnection.getMetaData().getDatabaseProductName();
        } catch (java.sql.SQLException se) {
        }
        PopulateBean popBean = new PopulateBean();
        request.setAttribute("populateBean", popBean);
        popBean.setDatabaseProductName(databaseName);
        context.getRequestDispatcher("/populate_install.jsp").forward(request, response);
    }


    private void showTables(HttpServletRequest request, HttpServletResponse response, Connection dbConnection)
                throws IOException, ServletException {
        HashMap installedTables = getTables(dbConnection);
        Iterator it = requiredTables.iterator();
        boolean pass = true;
        HashMap tables = new HashMap();
        while (it.hasNext()) {
            String tableName = (String)it.next();
            String passed = (installedTables.containsKey(tableName))? "true" : "false";
            tables.put(tableName, passed);
            if (!installedTables.containsKey(tableName)) pass = false;
        }

        boolean optionalPass = true;
        HashMap optionalTablesMap = new HashMap();
        Iterator it2 = optionalTables.iterator();
        while (it2.hasNext()) {
            String tableName = (String)it2.next();
            String passed = (installedTables.containsKey(tableName))? "true" : "false";
            optionalTablesMap.put(tableName, passed);
            if (!installedTables.containsKey(tableName)) optionalPass = false;
        }
        String databaseName = null;
        try {
            databaseName = dbConnection.getMetaData().getDatabaseProductName();
        } catch (java.sql.SQLException se) {
        }
        PopulateBean popBean = new PopulateBean();
        request.setAttribute("populateBean", popBean);
        popBean.setRequiredTables(tables);
        popBean.setOptionalTables(optionalTablesMap);
        popBean.setDatabaseProductName(databaseName);
        popBean.setDatabaseInstallationValid(pass);
        popBean.setOptionalDatabaseInstallationValid(optionalPass);
        context.getRequestDispatcher("/populate.jsp").forward(request, response);
    }

    private boolean checkJapaneseTables(HttpServletResponse response, Connection dbConnection) {
        HashMap installedTables = getTables(dbConnection);
        Iterator it = requiredTables.iterator();
        boolean pass = true;
        while (it.hasNext()) {
            String tableName = (String)it.next();
            String passed = (installedTables.containsKey(tableName))? "yes" : "no";
                        if (!installedTables.containsKey(tableName)) pass = false;
        }
        Iterator it2 = optionalTables.iterator();
        while (it2.hasNext()) {
            String tableName = (String)it2.next();
            String passed = (installedTables.containsKey(tableName))? "yes" : "no";
                        if (!installedTables.containsKey(tableName)) pass = false;
        }
        return pass;
    }

    private boolean checkTables(HttpServletResponse response, Connection dbConnection) {
        HashMap installedTables = getTables(dbConnection);
        Iterator it = requiredTables.iterator();
        boolean pass = true;
        while (it.hasNext()) {
            String tableName = (String)it.next();
            String passed = (installedTables.containsKey(tableName))? "yes" : "no";
                        if (!installedTables.containsKey(tableName)) pass = false;
        }
        return pass;
    }

    private boolean installTables(HttpServletResponse response, Connection dbConnection, String databaseName,  boolean verbose)
                throws IOException {
        if (databaseName == null) {
            try {
                databaseName = dbConnection.getMetaData().getDatabaseProductName();
            } catch (java.sql.SQLException se) {}
       }
       if (databaseName == null) {
          PrintWriter out = response.getWriter();
          out.println("<html>");
          out.println("<body bgcolor=white>");
          out.println("<br>Unable to obtain the database name");
          out.println("</html>");
          return false;
       }
       if (databaseName.equals("DBMS:cloudscape")) {
           return installCloudscapeTables(response, dbConnection, verbose);
       } else  if (databaseName.equals("Oracle")) {
           return installOracleTables(response, dbConnection, verbose);
       }  else  if (databaseName.equals("SQL Server")) {
           return installSybaseTables(response, dbConnection, verbose);
       } else {
           PrintWriter out = response.getWriter();
          out.println("<html>");
          out.println("<body bgcolor=white>");
          out.println("<br>Error: Uknown Database: " + databaseName);
          out.println("</html>");
       }
       return false;
    }

    private boolean installOracleTables(HttpServletResponse response, Connection dbConnection, boolean verbose)
                throws IOException {
        URL oracleEnglish = null;
        try {
            oracleEnglish = context.getResource("/WEB-INF/sql/Oracle.sql");
            PopulateTables.populate(oracleEnglish, "ASCII", dbConnection, null, verbose);
        } catch (java.net.MalformedURLException ex) {
            Debug.println("PopulateServlet: Population Error:  caught malformed URL exception: " + ex);
        }
        return true;
    }

    private boolean installSybaseTables(HttpServletResponse response, Connection dbConnection, boolean verbose)
                throws IOException {
        URL sybaseEnglish = null;
        try {
            sybaseEnglish = context.getResource("/WEB-INF/sql/Sybase.sql");
            PopulateTables.populate(sybaseEnglish, "ASCII", dbConnection, null, verbose);
        } catch (java.net.MalformedURLException ex) {
            Debug.println("PopulateServlet: Population Error: caught malformed URL exception: " + ex);
        }
        return true;
    }

    private boolean installCloudscapeTables(HttpServletResponse response, Connection dbConnection, boolean verbose)
                throws IOException {
        URL cloudscapeEnglish = null;
        URL cloudscapeJapenseTables = null;
        URL cloudscapeJapanese = null;
        try {
            cloudscapeEnglish = context.getResource("/WEB-INF/sql/cloudscape.sql");
            PopulateTables.populate(cloudscapeEnglish, "ASCII", dbConnection, null, verbose);
            cloudscapeJapenseTables = context.getResource("/WEB-INF/sql/cloudscape_tables.sql");
            PopulateTables.populate(cloudscapeJapenseTables, "ASCII", dbConnection, null, verbose);
            cloudscapeJapanese = context.getResource("/WEB-INF/sql/cloudscape_ja.sql");
            PopulateTables.populate(cloudscapeJapanese, "SJIS", dbConnection, null, verbose);
        } catch (java.net.MalformedURLException ex) {
            Debug.println("PopulateServlet: Population Error: caught malformed URL exception: " + ex);
        }
        return true;
    }

    private Connection getDBConnection() throws IOException {
        DataSource datasource = null;
        try {
            InitialContext ic = new InitialContext();
            datasource = (DataSource) ic.lookup(ESTORE_DATASOURCE);
        } catch (NamingException ne) {
            throw new IOException("NamingException while looking " +
                            "up DB context  : " + ne.getMessage());
        }
        try {
            Connection dbConnection = null;
            if (datasource != null) dbConnection = datasource.getConnection();
            return dbConnection;
        } catch (SQLException se) {
            throw new IOException("SQLException while getting " +
                            "DB connection  : " + se.getMessage());
        }
    }

    private void closeConnection(Connection dbConnection) {
        try {
            if (dbConnection != null && !dbConnection.isClosed()) {
                dbConnection.close();
            }
        } catch(SQLException se) {
            Debug.println("SQLException while closing " +
                            "DB connection  : " + se.getMessage());
        } finally {
            dbConnection = null;
        }
    }

    private HashMap getTables(Connection dbConnection) {
        try {

            HashMap tables = new HashMap();
            DatabaseMetaData metaData = dbConnection.getMetaData();
            ResultSet rs = metaData.getTables(null, null, null, new String[] {"TABLE"});
            while (rs.next()) {
                tables.put(rs.getString(3).toUpperCase(), "");
            }
            return tables;
        } catch (java.sql.SQLException sqx) {}
        return null;
    }


}
