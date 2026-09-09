<%--
 % Display the shopping cart with editable values of quantities and
 % the like.  Displays the myList of the user.
--%>
<%--
 % Generate a tablular representation of the contents of the shopping
 % cart.  Each of the items in the cart is shown with an editable
 % quantity field.  It assumes that it is included in the context of a
 % FORM element that will handle the changes to the item quantities.
--%>

<%--
 % Loop through each item in the shopping cart.  The current item is
 % available to the jsp block within the loop as "item"
--%>
<%@ page contentType="text/html;charset=SJIS" %>
<%@ taglib uri="/WEB-INF/tlds/taglib.tld" prefix="j2ee" %>


<jsp:useBean 
  id="cart" 
  class="com.sun.j2ee.blueprints.petstore.control.web.ShoppingCartWebImpl" 
  scope="session"
/>

<p>
<table>
 <tr>
  <td align="left">
   <j2ee:cart numItems="4" cartEmptyMessage="ショッピングカートには何も入ってません。">
   <font size="5" color="black">ショッピングカート:</font>
   <p>
   <form action="cart">
   <input type="hidden" name="action" value="updateCart">
   <table bgcolor="white">
   <tr>
    <td> 
     <table bgcolor="#336666">
      <tr background="../images/bkg-topbar.gif" border="0">
       <th><!-- for the remove column --></th>
       <th><font size="3" color="white">アイテムコード</font></th>
       <th><font size="3" color="white">商品名</font></th>
       <th><font size="3" color="white">在庫</font></th>
       <th><font size="3" color="white">単価</font></th>
       <th><font size="3" color="white">数量</font></th>
       <th><font size="3" color="white">合計</font></th>
      </tr>
      <j2ee:items>
       <tr bgcolor="#eeebcc">
        <td>
         <a href="cart?action=removeItem&itemId=<j2ee:cartAttribute attribute="itemid"/>"><img src="../images/button_remove.gif" border="0" alt="Remove Item From Shopping Cart"></a>
        </td>
        <td> <j2ee:cartAttribute attribute="itemid"/> </td>
        <td> 
         <a href="productdetails?item_id=<j2ee:cartAttribute attribute="itemid"/>"> <j2ee:cartAttribute attribute="name"/></a>
        </td>
        <td><j2ee:cartAttribute attribute="instock" true="有り" false="なし"/>
        </td>
        <td><j2ee:cartAttribute attribute="unitcost"/></td>
        <td>
         <input name="itemQuantity_<j2ee:cartAttribute attribute="itemid"/>" 
                type="text" 
                size="4" 
                value="<j2ee:cartAttribute attribute="quantity"/>">
        </td>
        <td><j2ee:cartAttribute attribute="itemTotal"/></td>
       </tr>
      </j2ee:items>
      <tr background="../images/bkg-topbar.gif">
       <td></td>
       <td><font size="3" color="white">総合計:</font></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td><font size="3" color="white"><jsp:getProperty name="cart" property="cartTotal"/></font></td>
      </tr>
     </td>
    </table>
      <td>
        <input type="image" border="0" src="../images/cart-update.gif" name="update">
      </td>
     </tr>
    </form>
      <tr>
       <j2ee:prevForm action="cart">
        <td align="left" colspan="1">
         <input type="image" border="0" src="../images/button_prev.gif" value="Prev">
        </td>
       </j2ee:prevForm>
       <j2ee:nextForm action="cart">
        <td align="right" colspan="1">
         <input type="image" border="0" src="../images/button_more.gif" value="Next">
        </td>
       </j2ee:nextForm>
      </tr>
    </table>
    <p>
    <a href="checkout"><img src="<%= request.getContextPath() %>/images/button_checkout.gif" alt="Proceed To Checkout" border="0"></a> 
   </j2ee:cart>
  </td>
  <td align="right">
        <jsp:include page="/ja/mylist.jsp" flush="true" />
  </td>
</tr>
</table>

