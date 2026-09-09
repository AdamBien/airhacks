<%--
 % $Id: search.jsp,v 1.13.4.1 2001/03/15 00:40:15 brydon Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
--%>

<%--
 % Displays the results of a search
--%>

<%@ taglib uri="/WEB-INF/tlds/taglib.tld" prefix="j2ee" %>

<j2ee:searchList numItems="4" searchText='<%=request.getParameter("search_text")%>' emptyList="No products found">

   <table border="0" bgcolor="#336666">
      <tr background="../images/bkg-topbar.gif">
        <th><font color="white" size="3">Product ID</font></th>
        <th><font color="white" size="3">Product Name</font></th>
        <th><font color="white" size="3">Description</font></th>
      </tr>

      <j2ee:items>

      <tr bgcolor="#eeebcc">
        <td><j2ee:productAttribute attribute="id"/></td>
        <td>
          <a href="product?product_id=<j2ee:productAttribute attribute="id"/>">
            <j2ee:productAttribute attribute="name"/>
          </a>
        </td>
        <td><j2ee:productAttribute attribute="description"/></td>
      </tr>

     </j2ee:items>

   <tr>
     <j2ee:prevForm action="search">
         <td colspan="2" align="left">
          <input type="hidden" name="search_text" value="<%=request.getParameter("search_text")%>">
          <input type="image" border="0" src="../images/button_prev.gif" value="Prev">
         </td>
    </j2ee:prevForm>

    <j2ee:nextForm action="search">
      <td colspan="4" align="right">
          <input type="hidden" name="search_text" value="<%=request.getParameter("search_text")%>">
          <input type="image" border="0" src="../images/button_more.gif" value="Next">
       </td>
        </j2ee:nextForm>

    </tr>

    </table>


</j2ee:searchList>

