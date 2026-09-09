<%--
 % $Id: confirmshippingdata.jsp,v 1.5.4.1 2001/03/15 00:40:12 brydon Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
--%>



<%--
 % Displays the details of the information collected from the user
 % regarding processing of their order.
--%>

<%@ page import="com.sun.j2ee.blueprints.customer.util.ContactInformation" %>
<%@ page import="com.sun.j2ee.blueprints.petstore.util.WebKeys" %>

<%
ContactInformation billTo = (ContactInformation)session.getAttribute(WebKeys.BillingContactInfoKey);
ContactInformation shipTo = (ContactInformation)session.getAttribute(WebKeys.ShippingContactInfoKey);
%>


<p>

  Please confirm that the following data is correct and press

  the <b>Continue</b> button to ship the order.

<p>

  <font size="3" color="black">Billing Address</font>



<table border="0">

  <tr>

    <td>

      <%=billTo.getGivenName()%>

      <%=billTo.getFamilyName()%>

    </td>

  </tr>

  <tr>

    <td><%=billTo.getAddress().getStreetName1()%></td>

  </tr>

  <tr>

    <td><%=billTo.getAddress().getStreetName2()%></td>

  </tr>

  <tr>

    <td>

      <%=billTo.getAddress().getCity()%>,

      <%=billTo.getAddress().getState()%>&nbsp;&nbsp;

      <%=billTo.getAddress().getZipCode()%>

    </td>

  </tr>

</table>

<p>

  <font size="3" color="black">Shipping Address</font>

<table border="0">

  <tr>

    <td>

      <%=shipTo.getGivenName()%>

      <%=shipTo.getFamilyName()%>

    </td>

  </tr>

  <tr>

    <td><%=shipTo.getAddress().getStreetName1()%></td>

  </tr>

  <tr>

    <td><%=shipTo.getAddress().getStreetName2()%></td>

  </tr>

  <tr>

    <td>

      <%=shipTo.getAddress().getCity()%>,

      <%=shipTo.getAddress().getState()%>&nbsp;&nbsp;

      <%=shipTo.getAddress().getZipCode()%>

    </td>

  </tr>

</table>

<p>

  <a href="commitorder"><img src="../images/button_cont.gif" alt="Continue" border="0"></a>

<p>



