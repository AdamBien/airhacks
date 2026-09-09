<%--
 % $Id: manageorders.jsp,v 1.4.4.5 2001/03/20 01:06:00 gmurray Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
--%>

<%--
 % Page that enables an order manager to move "pending" orders to
 % either "approved" or "denied" based on credit card verification
--%>

<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.sun.j2ee.blueprints.petstore.util.JSPUtil" %>
<%@ page import="com.sun.j2ee.blueprints.customer.order.model.OrderModel" %>

<html>
   <head>
      <title>Manage Orders</title>
   </head>
   <body bgcolor="white" text="#000000" link="#0000EE" vlink="#551A8B" alink="#FF0000">
      <h1>Customer Order Management</h1>
      <h3>Verify customer credit card information and if valid, then
      submit the Pending ("P") order(s) to either Approved("A") or
          Denied ("D").</h3>
   <hr width="100%">

<jsp:useBean
    id="manageorders"
    class="com.sun.j2ee.blueprints.petstoreadmin.control.web.ManageOrdersBean"
    scope="session"
/>

<%
  String callerId = request.getUserPrincipal().getName();
  if(callerId.equals("jps_admin")) {
%>

    <form action="AdminRequestProcessor" method="post">
    <input type="hidden" name="currentScreen" value="updateorders">
    <input type="hidden" name="action" value="updateOrders">
<%
    Collection pendingOrderIds = manageorders.getPendingOrders(java.util.Locale.US);
    Collection jPendingOrderIds = manageorders.getPendingOrders(java.util.Locale.JAPAN);

    if (pendingOrderIds != null|| jPendingOrderIds != null) {
        if (pendingOrderIds != null) {
%>
    <br><font size="+2">US Orders</font>
    <br>
    <br>
   <table border="0" bgcolor="#336666">
    <tr>
     <td><font color="white" size="3">Order Id</font></td>
     <td><font color="white" size="3">User Id</font></td>
     <td><font color="white" size="3">Credit Card Number</font></td>
     <td><font color="white" size="3">Expires MM/YYYY</font></td>
     <td><font color="white" size="3">Order Total</font></td>
     <td><font color="white" size="3">Order Status</font></td>
     <td><font color="white" size="3">Update</font></td>
    </tr>

<%
    Iterator it = null;
    if (pendingOrderIds != null) it = pendingOrderIds.iterator();
    while ((it != null) && it.hasNext()) {
      OrderModel anOrder = (OrderModel)it.next();
   // For each pending order, create a table row
%>

   <tr bgcolor="#eeebcc">
      <td><%= anOrder.getOrderId() %></td>
      <td><%= anOrder.getUserId() %></td>
      <td><%= anOrder.getCreditCard().getCardNo() %></td>
      <td><%= anOrder.getCreditCard().getExpiryMonthString() %>
          <%= anOrder.getCreditCard().getExpiryYearString() %>
      </td>
      <td align="right"><%= JSPUtil.formatCurrency(anOrder.getTotalPrice()) %></td>
      <td align="center">
          <select name="<%= "status_"+ anOrder.getOrderId() %>" >
             <option value="pending" selected><%= anOrder.getStatus() %></option>
             <option value="denied">D</option>
             <option value="approved">A</option>
          </select>
      </td>
      <td align="center"><input type=checkbox name="<%= "order_"+ anOrder.getOrderId() %>"  value="false">
      </td>
   </tr>
  <% } %>

  </table>

<% } %>
<!-- Show Japanese orders !-->
<%
    if (jPendingOrderIds != null) {
    Iterator it2  = null;
      it2 = jPendingOrderIds.iterator();
 %>
   <br>
   <br><font size="+2">Japanese Orders</font>
   <br>
   <br>
   <table border="0" bgcolor="#336666">
    <tr>
    <td><font color="white" size="3">Order Id</font></td>
    <td><font color="white" size="3">User Id</font></td>
    <td><font color="white" size="3">Credit Card Number</font></td>
    <td><font color="white" size="3">Expires MM/YYYY</font></td>
    <td><font color="white" size="3">Order Total</font></td>
    <td><font color="white" size="3">Order Status</font></td>
    <td><font color="white" size="3">Update</font></td>
   </tr>
<%
    while ((it2 != null) && it2.hasNext()) {
      OrderModel anOrder = (OrderModel)it2.next();
   // For each pending order, create a table row
%>


   <tr bgcolor="#eeebcc">
      <td><%= anOrder.getOrderId() %></td>
      <td><%= anOrder.getUserId() %></td>
      <td><%= anOrder.getCreditCard().getCardNo() %></td>
      <td><%= anOrder.getCreditCard().getExpiryMonthString() %>
          <%= anOrder.getCreditCard().getExpiryYearString() %>
      </td>
      <td align="right"><%= JSPUtil.formatPlainCurrency(anOrder.getTotalPrice()) %> yen</td>
      <td align="center">
          <select name="<%= "status_"+ anOrder.getOrderId() %>" >
             <option value="pending" selected><%= anOrder.getStatus() %></option>
             <option value="denied">D</option>
             <option value="approved">A</option>
          </select>
      </td>
      <td align="center"><input type=checkbox name="<%= "order_"+ anOrder.getOrderId() %>"  value="false">
      </td>
   </tr>

  <% } %>


  </table>
<% } %>
  <br><br>
  <input type="image" border="0" src="images/button_submit.gif" name="button">
  </form>
<%   } else { %>

  <font size="5" color="green">
    There are no "Pending" orders at this time - thank you
  </font>

<%   }
   } else {
%>
    <font size="5" color="green">
    You are not authorised to update the status of orders.
  </font>
<%
   }
%>

   </body>
</html>
