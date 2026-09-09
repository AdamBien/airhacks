<%--
 % $Id: missingformdata.jsp,v 1.5.4.1 2001/03/15 00:40:14 brydon Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
--%>

<%--
 % Report the missing fields to the user after a form validation
 % fails.
--%>

<%@ page import="com.sun.j2ee.blueprints.petstore.control.web.MissingFormDataException" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<p>

<%
  MissingFormDataException error =
    (MissingFormDataException)request.getAttribute("missingFormData");
  Collection missingFields = null;
  if (error != null)
    missingFields = error.getMissingFields();
%>

<font size="5" color="red"><%= error.getMessage() %></font>
<p>
  Please return to the previous page using the back button on your
  browser and ensure that the following fields are properly entered
  and re-submit the form.
<ul>

<%
  if (missingFields != null) {
    Iterator it = missingFields.iterator();
    while (it.hasNext()) {
      String item = (String)it.next();
%>

    <li><%= item %></li>

<%   } %>
<% } %>

</ul>
