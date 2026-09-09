<%--
 % $Id: productcategory.jsp,v 1.15.4.1 2001/03/15 00:40:15 brydon Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
--%>

<%--
 % List all products within a given category
--%>

<%@ taglib uri="/WEB-INF/tlds/taglib.tld" prefix="j2ee" %>

<j2ee:productList numItems="4" category='<%=request.getParameter("category_id")%>'>

    <p>
      <font size="5" color="green"><%=request.getParameter("category_id")%></font>
    <p>

   <table border="0" bgcolor="#336666">
      <tr background="../images/bkg-topbar.gif">
        <th><font color="white" size="3">Product ID</font></th>
        <th><font color="white" size="3">Product Name</font></th>
      </tr>

      <j2ee:items>

      <tr bgcolor="#eeebcc">
        <td><j2ee:productAttribute attribute="id"/></td>
        <td>
          <a href="product?product_id=<j2ee:productAttribute attribute="id"/>">
            <j2ee:productAttribute attribute="name"/>
          </a>
        </td>
      </tr>

     </j2ee:items>

   <tr>
     <j2ee:prevForm action="category">
         <td colspan="2" align="left">
          <input type="hidden" name="category_id" value="<%=request.getParameter("category_id")%>">
          <input type="image" border="0" src="../images/button_prev.gif" value="Prev">

         </td>
    </j2ee:prevForm>


    <j2ee:nextForm action="category">
      <td colspan="2" align="right">
          <input type="hidden" name="category_id" value="<%=request.getParameter("category_id")%>">
          <input type="image" border="0" src="../images/button_more.gif" value="Next">
      </td>
    </j2ee:nextForm>

    </tr>

    </table>


</j2ee:productList>
