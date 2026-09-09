<%--
 % $Id: pendingorders.jsp,v 1.2.4.1 2001/03/14 23:56:44 brydon Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
--%>

<%--
 % Generates a list of pending orders in XML
 %
 % Currently this file is only accessed from the Microsoft
 % interoperatility demo.
--%>

<%--
<%@ page contentType="text/xml" %>
--%>

<jsp:useBean
  id="manageorders"
  class="com.sun.j2ee.blueprints.petstoreadmin.control.web.ManageOrdersBean"
  scope="page"
/>

<jsp:getProperty name="manageorders" property="pendingOrdersXML" />
