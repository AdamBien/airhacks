<%--
 % $Id: shiporder.jsp,v 1.4.4.4 2001/04/06 22:01:21 gmurray Exp $
 % Copyright 2000 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2000 Sun Microsystems, Inc. Tous droits r?serv?s. 
--%>
<%@ page contentType="text/html;charset=SJIS" %>
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
      <font size="5">注文日:</font>
      <%= order.getOrderDate().getFullDateString(java.util.Locale.JAPAN) %>
    <p>
      <font size="5">ユーザ ID:</font><%= order.getUserId() %>
    <p>
      <font size="5">注文情報:</font>
    <p>
      <font size="3">注文番号:</font><%= order.getOrderId() %>
    <p>
    <table bgcolor="#336666">
      <tr background="../images/bkg-topbar.gif" border="0">
	<th><font size="3" color="white">アイテムコード</font></th>
	<th><font size="3" color="white">アイテム名</font></th>
	<th><font size="3" color="white">単価</font></th>
	<th><font size="3" color="white">数量</font></th>
	<th><font size="3" color="white">合計</font></th>
      </tr>

<%
  Collection lineItems = order.getLineItems();
  Iterator it = lineItems.iterator(); 
  while (it.hasNext()) {
    LineItem lineItem = (LineItem) it.next();
    Item item = catalog.getItem(lineItem.getItemNo().trim(), Locale.JAPAN);
    Product product = catalog.getProduct(item.getProductId(), Locale.JAPAN);
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
	  <%= JSPUtil.formatCurrency(lineItem.getUnitPrice(), java.util.Locale.JAPAN) %>
	</td>
	<td>
	  <%= lineItem.getQty() %>
	</td>
	<td>
	  <%= JSPUtil.formatCurrency(lineItem.getUnitPrice() *
	  lineItem.getQty(), java.util.Locale.JAPAN) %>
	</td>
      </tr>
<%}%>


<%-- End of line items --%>
      <tr bgcolor="#336666">
	<td><font size="3" color="white">総合計:</font></td>
	<td></td>
	<td></td>
	<td></td>
	<td>
	  <font size="3" color="white">
	    <%=JSPUtil.formatCurrency(order.getTotalPrice(), java.util.Locale.JAPAN) %>
	  </font>
	</td>
      </tr>
    </table>

    <p>
      <font size="5">商品配送情報:</font>
    <p>
      <font size="3">商品配送先:</font>
      
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
      <font size="3">配送会社:</font>
      <%= order.getCarrier() %>
    <p>
      <font size="5">お支払い情報:</font>
    <p>
      <font size="3">請求書送付先:</font>

    <table border="0">
      <tr>
	<td colspan="3">
	  <%= order.getBillToLastName() %>
	  <%= order.getBillToFirstName() %> 
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
      <font size="3">クレジットカード情報:</font>
    <p>
    <table border="0">
      <tr>
	<td>クレジットカード種別:</td>
	<td>
	  <%= order.getCreditCard().getCardType() %>
	</td>
      </tr>
      <tr>
	<td>カード番号:</td>
	<td>
	  <%= order.getCreditCard().getCardNo() %>
	</td>
      </tr>
      <tr>
	<td>有効期限:</td>
	<td>
	  <%= order.getCreditCard().getExpiryDateString() %>
	</td></tr>
    </table>

    <p>
      <font size="5">ステータス:</font>
    <p>
      ステータス: <%= order.getStatus() %>

<% } %>



