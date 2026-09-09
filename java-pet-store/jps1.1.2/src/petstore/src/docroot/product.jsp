<%--
 % $Id: product.jsp,v 1.15.4.2 2001/04/13 22:50:27 gmurray Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits r?erv?.
--%>

<%--
 % Lists all items in the inventory for a particular product type.
--%>
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
  Product product = catalog.getProduct(request.getParameter("product_id"), Locale.US);
%>


<j2ee:productItemList numItems="3" productId='<%=request.getParameter("product_id")%>' emptyList="Unable to find requested Product" >
    <font size="5" color="green"><%= product.getName() %></font>
    <p>
    <table border="0" bgcolor="#336666">
    <tr background="../images/bkg-topbar.gif">
        <th><font color="white" size="3">Item ID</font></th>
        <th><font color="white" size="3">Item Name</font></th>
        <th><font color="white" size="3">Item Price</font></th>
    </tr>
    <j2ee:items>
      <tr bgcolor="#eeebcc">
        <td><j2ee:productItemAttribute attribute="id"/></td>
        <td>
            <a href="productdetails?item_id=<j2ee:productItemAttribute attribute="id"/>">
             <j2ee:productItemAttribute attribute="productAttribute"/> <j2ee:productItemAttribute attribute="name"/></a>
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

    <tr>
        <j2ee:prevForm action="product">
          <td align="left">
           <input type="image" border="0" src="../images/button_prev.gif" value="Prev">
           <input type="hidden" name="product_id" value="<%=request.getParameter("product_id")%>">
          </td>
        </j2ee:prevForm>
        <j2ee:nextForm action="product">
          <td align="right">
           <input type="hidden" name="product_id" value="<%=request.getParameter("product_id")%>">
           <input type="image" border="0" src="../images/button_more.gif" value="Next">

          </td>
        </j2ee:nextForm>
    </tr>
  </table>
</j2ee:productItemList>

