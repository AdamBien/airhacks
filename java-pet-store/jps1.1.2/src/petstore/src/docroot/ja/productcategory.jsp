<%--
 % $Id: productcategory.jsp,v 1.10 2000/09/21 07:11:35 gmurray Exp $
 % Copyright 2000 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2000 Sun Microsystems, Inc. Tous droits r?serv?s. 
--%>

<%--
 % List all products within a given category
--%>
<%@ page contentType="text/html;charset=SJIS" %>
<%@ taglib uri="/WEB-INF/tlds/taglib.tld" prefix="j2ee" %>

<j2ee:productList numItems="4" category='<%=request.getParameter("category_id")%>'>

    <p>
      <font size="5" color="green"><%=request.getParameter("category_id")%></font>
    <p>
      
    <table border="0" bgcolor="#336666">
      <tr background="../images/bkg-topbar.gif">
	<th><font color="white" size="3">商品コード</font></th>
	<th><font color="white" size="3">商品名</font></th>
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

   <tr background="../images/bkg-topbar.gif">
      <j2ee:prevForm action="category">
         <td align="left" colspan="2">
          <input type="hidden" name="category_id" value="<%=request.getParameter("category_id")%>">
	  <input type="submit" name="submit" value="Prev">
         </td>
        </j2ee:prevForm>
	<j2ee:nextForm action="category">
         <td align="right" colspan="2">
          <input type="hidden" name="category_id" value="<%=request.getParameter("category_id")%>">
	  <input type="submit" name="submit" value="More">
        </j2ee:nextForm>
      </td>
    </tr>
   </table>
 

</j2ee:productList>
