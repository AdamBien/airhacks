<%--
 % $Id: checkout.jsp,v 1.4.4.4 2001/03/15 00:40:12 brydon Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits r?serv?s.
--%>

<%--
 % Displays the contents of the current cart for final approval of
 % the user, before initiating the order processing.
--%>

<%@ taglib uri="/WEB-INF/tlds/taglib.tld" prefix="j2ee" %>

<jsp:useBean
  id="cart"
  class="com.sun.j2ee.blueprints.petstore.control.web.ShoppingCartWebImpl"
  scope="session"
/>

   <%-- Shopping cart Starts--%>
   <j2ee:cart numItems="4" cartEmptyMessage="Your Shopping Cart is Empty">
    <font size="5" color="black">Shopping Cart:</font>
     <p></p>
     <table bgcolor="white">
      <tr>
       <td>
        <table bgcolor="#336666">
         <tr background="../images/bkg-topbar.gif" border="0">
          <th><font size="3" color="white">Item ID</font></th>
          <th><font size="3" color="white">Product Name</font></th>
          <th><font size="3" color="white">In Stock</font></th>
          <th><font size="3" color="white">Unit Price</font></th>
          <th><font size="3" color="white">Quantity</font></th>
          <th><font size="3" color="white">Total Cost</font></th>
         </tr>
         <j2ee:items>
          <tr bgcolor="#eeebcc">
           <td> <j2ee:cartAttribute attribute="itemid"/> </td>
           <td>
            <a href="productdetails?item_id=<j2ee:cartAttribute attribute="itemid"/>"> <j2ee:cartAttribute attribute="name"/></a>
           </td>
           <td><j2ee:cartAttribute attribute="instock" true="yes" false="no"/></td>
           <td><j2ee:cartAttribute attribute="unitcost"/></td>
           <td>
               <j2ee:cartAttribute attribute="quantity"/>
           </td>
           <td><j2ee:cartAttribute attribute="itemTotal"/></td>
          </tr>
         </j2ee:items>
         <tr background="../images/bkg-topbar.gif">
           <td><font size="3" color="white">Total:</font></td>
           <td></td>
           <td></td>
           <td></td>
           <td></td>
           <td><font size="3" color="white"><jsp:getProperty name="cart" property="cartTotal"/></font></td>
         </tr>
        </table>
       </td>
      </form>
      </tr>
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
    <p></p>
    <a href="placeorder"><img src="../images/button_cont.gif" alt="Continue" border="0"></a>
   </j2ee:cart>
