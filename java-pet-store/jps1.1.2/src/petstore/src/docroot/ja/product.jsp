<%--
 % $Id: product.jsp,v 1.13 2000/10/05 01:51:33 gmurray Exp $
 % Copyright 2000 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2000 Sun Microsystems, Inc. Tous droits r?serv?s. 
--%>

<%--
 % Lists all items in the inventory for a particular product type.
--%>
<%@ page contentType="text/html;charset=SJIS" %>
<%@ taglib uri="/WEB-INF/tlds/taglib.tld" prefix="j2ee" %>
<%@ page import="com.sun.j2ee.blueprints.shoppingcart.catalog.model.Product" %>
<%@ page import="java.util.Locale" %>

<jsp:useBean 
  id="catalog" 
  class="com.sun.j2ee.blueprints.petstore.control.web.CatalogWebImpl" 
  scope="application" 
/>

<p>

<%
  Product product = catalog.getProduct(request.getParameter("product_id"), Locale.JAPAN);
%>

 
<j2ee:productItemList numItems="3" productId='<%=request.getParameter("product_id")%>' emptyList="Unable to find requested Product" >
    <font size="5" color="green"><%= product.getName() %></font>
    <p>
    <table border="0" bgcolor="#336666">
    <tr background="../images/bkg-topbar.gif">
        <th><font color="white" size="3">アイテムコード</font></th>
        <th><font color="white" size="3">アイテム名</font></th>
        <th><font color="white" size="3">単価</font></th>
    </tr>
    <j2ee:items>
      <tr bgcolor="#eeebcc">
        <td><j2ee:productItemAttribute attribute="id"/></td>
        <td>
            <a href="productdetails?item_id=<j2ee:productItemAttribute attribute="id"/>">
            <j2ee:productItemAttribute attribute="name"/> <j2ee:productItemAttribute attribute="productAttribute"/></a>
        </td>
        <td><j2ee:productItemAttribute attribute="listcost"/></td> 
        <td>
            <a href="cart?action=purchaseItem&itemId=<j2ee:productItemAttribute attribute="id"/>">
            <img src="../images/button_cart-add.gif"  border="0" alt="Add Item to Your Shopping Cart"></a>
        </td>
      </tr>
	  </a>
	  </p>
        </j2ee:items>
      </td>
    </tr>

    <tr background="../images/bkg-topbar.gif">
	<j2ee:prevForm action="product">
          <td align="left" colspan="3">
	   <input type="submit" name="submit" value="Prev">
           <input type="hidden" name="product_id" value="<%=request.getParameter("product_id")%>">
          </td>
        </j2ee:prevForm>
	<j2ee:nextForm action="product">
          <td align="right" colspan="3">
           <input type="hidden" name="product_id" value="<%=request.getParameter("product_id")%>">
           <input type="submit" name="submit" value="Next">
          </td>
        </j2ee:nextForm>
    </tr>
  </table>
</j2ee:productItemList>

