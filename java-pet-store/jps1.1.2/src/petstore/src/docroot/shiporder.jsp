<%--
 % $Id: shiporder.jsp,v 1.10.4.4 2001/04/06 22:01:21 gmurray Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
--%>


<%@ page import="com.sun.j2ee.blueprints.shoppingcart.catalog.model.Product" %>
<%@ page import="com.sun.j2ee.blueprints.shoppingcart.catalog.model.Item" %>
<%@ page import="com.sun.j2ee.blueprints.customer.order.model.LineItem" %>
<%@ page import="com.sun.j2ee.blueprints.petstore.util.JSPUtil" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Iterator" %>

<p>

<jsp:useBean
  id="catalog"
  class="com.sun.j2ee.blueprints.petstore.control.web.CatalogWebImpl"
  scope="application"
/>

<jsp:useBean
  id="order"
  class="com.sun.j2ee.blueprints.petstore.control.web.OrderWebImpl"
  scope="request">
<%
 order.init(request);
%>
</jsp:useBean>

<%
if (order.getOrderId() != -1){
%>
    <p>
      <font size="5">Date:</font>
      <%= order.getOrderDate().getFullDateString(java.util.Locale.US) %>
    <p>
      <font size="5">Account:</font><%= order.getUserId() %>
    <p>
      <font size="5">Order Information:</font>
    <p>
      <font size="3">Order ID :</font><%= order.getOrderId() %>
    <p>
    <table bgcolor="#336666">
      <tr background="../images/bkg-topbar.gif" border="0">
        <th><font size="3" color="white">Item ID</font></th>
        <th><font size="3" color="white">Product Name</font></th>
        <th><font size="3" color="white">Unit Price</font></th>
        <th><font size="3" color="white">Quantity</font></th>
        <th><font size="3" color="white">Total Cost</font></th>
      </tr>

<%
  Collection lineItems = order.getLineItems();
  Iterator it = lineItems.iterator();
  while (it.hasNext()) {
    LineItem lineItem = (LineItem) it.next();
    Item item = catalog.getItem(lineItem.getItemNo().trim(), Locale.US);
    Product product = catalog.getProduct(item.getProductId(), Locale.US);
    // For each line item in the order -
%>

      <tr bgcolor="#eeebcc">
        <td><%= item.getItemId() %></td>
        <td>
          <a href="productdetails?item_id=<%= item.getItemId() %>">
            <%= item.getAttribute() %>
            <%= product.getName() %>
         </a>
        </td>
        <td>
          <%= JSPUtil.formatCurrency(lineItem.getUnitPrice()) %>
        </td>
        <td>
          <%= lineItem.getQty() %>
        </td>
        <td>
          <%= JSPUtil.formatCurrency(lineItem.getUnitPrice() *
          lineItem.getQty()) %>
        </td>
      </tr>
<%}%>


<%-- End of line items --%>

      <tr bgcolor="#336666">
        <td><font size="3" color="white">Total:</font></td>
        <td></td>
        <td></td>
        <td></td>
        <td>
          <font size="3" color="white">
            <%=JSPUtil.formatCurrency(order.getTotalPrice()) %>
          </font>
        </td>
      </tr>
    </table>

    <p>
      <font size="5">Shipping Information:</font>
    <p>
      <font size="3">Shipping Address:</font>

    <table border="0">
      <tr>
        <td colspan="3">
          <%= order.getShipToAddr().getStreetName1() %>
        </td>
      </tr>
      <tr>
        <td colspan="3">
          <%= order.getShipToAddr().getStreetName2() %>
        </td>
      </tr>
      <tr>
        <td>
          <%= order.getShipToAddr().getCity() %> ,
        </td>
        <td>
          <%= order.getShipToAddr().getState() %>
        </td>
        <td>
          <%= order.getShipToAddr().getZipCode() %>
        </td>
      </tr>
    </table>

    <p>
      <font size="3">Carrier:</font>
      <%= order.getCarrier() %>
    <p>
      <font size="5">Billing Information:</font>
    <p>
      <font size="3">Bill To Address:</font>

    <table border="0">
      <tr>
        <td colspan="3">
          <%= order.getBillToFirstName() %>
          <%= order.getBillToLastName() %>
        </td>
      </tr>
      <tr>
        <td colspan="3">
          <%= order.getBillToAddr().getStreetName1() %>
        </td>
      </tr>
      <tr>
        <td colspan="3">
          <%= order.getBillToAddr().getStreetName2() %>
        </td>
      </tr>
      <tr>
        <td>
          <%= order.getBillToAddr().getCity() %>,
        </td>
        <td>
          <%= order.getBillToAddr().getState() %>
        </td>
        <td>
          <%= order.getBillToAddr().getZipCode() %>
        </td>
      </tr>
    </table>

    <p>
      <font size="3">Credit Card Information:</font>
    <p>
    <table border="0">
      <tr>
        <td>Type:</td>
        <td>
          <%= order.getCreditCard().getCardType() %>
        </td>
      </tr>
      <tr>
        <td>Number:</td>
        <td>
          <%= order.getCreditCard().getCardNo() %>
        </td>
      </tr>
      <tr>
        <td>Expiration Date:</td>
        <td>
          <%= order.getCreditCard().getExpiryDateString() %>
        </td></tr>
    </table>

    <p>
      <font size="5">Status:</font> <%= order.getStatus() %>

<% } %>
