<%--
 % $Id: carttable.jsp,v 1.5 2000/09/20 04:50:08 brydon Exp $
 % Copyright 2000 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2000 Sun Microsystems, Inc. Tous droits r?serv?s. 
--%>

<%--
 % Generate a tablular representation of the contents of the shopping
 % cart.
--%>
<%@ page contentType="text/html;charset=SJIS" %>
<%@ page import="com.sun.j2ee.blueprints.shoppingcart.cart.model.CartItem" %>
<%@ page import="com.sun.j2ee.blueprints.petstore.util.JSPUtil" %>
<%@ page import="java.util.Iterator" %>
<jsp:useBean 
  id="cart" 
  class="com.sun.j2ee.blueprints.petstore.control.web.ShoppingCartWebImpl" 
  scope="session" 
/>
<jsp:useBean 
  id="inventory" 
  class="com.sun.j2ee.blueprints.petstore.control.web.InventoryWebImpl" 
  scope="session"
/>

<table bgcolor="#336666">
  <tr background="../images/bkg-topbar.gif" border="0">
    <th><font size="3" color="white">アイテムコード</font></th>
    <th><font size="3" color="white">商品名</font></th>
    <th><font size="3" color="white">在庫</font></th>
    <th><font size="3" color="white">単価</font></th>
    <th><font size="3" color="white">数量</font></th>
    <th><font size="3" color="white">合計</font></th>
  </tr>
<%--
 % Loop through each item in the shopping cart.  The current item is
 % available to the jsp block within the loop as "item"
--%>

<%   Iterator it = cart.getItems();   while ((it != null) && it.hasNext()) {
     CartItem item = (CartItem)it.next();
 %>
    <tr bgcolor="#eeebcc">
      <td> <%=item.getItemId()%> </td>
      <td> 
	<a href="productdetails?item_id=<%=item.getItemId()%>">
	  <%=item.getAttribute()%> <%=item.getName()%>
	</a>
      </td>
      <td><%=(inventory.getInventory(item.getItemId()) >= item.getQuantity()) ? "有り" : "なし"%></td>
      <td> <%=JSPUtil.formatCurrency(item.getUnitCost(), java.util.Locale.JAPAN)%> </td>
      <td> <%=item.getQuantity()%> </td>
      <td> <%=JSPUtil.formatCurrency(item.getTotalCost(), java.util.Locale.JAPAN)%> </td>
    </tr>
<% 
  } // end for loop 
%>
  <tr background="../images/bkg-topbar.gif">
    <td><font size="3" color="white">総合計:</font></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td>
      <font size="3" color="white"> <%=JSPUtil.formatCurrency(cart.getTotalCost(), java.util.Locale.JAPAN)%> </font>
    </td>
  </tr>
</table>
