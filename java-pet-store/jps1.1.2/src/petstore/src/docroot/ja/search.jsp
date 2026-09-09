<%--
 % $Id: search.jsp,v 1.3.4.3 2001/04/03 21:39:02 gmurray Exp $
 % Copyright 2000 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2000 Sun Microsystems, Inc. Tous droits r?serv?s. 
--%>

<%--
 % Displays the results of a search 
--%>

<%@ page contentType="text/html;charset=SJIS" %>
<%@ taglib uri="/WEB-INF/tlds/taglib.tld" prefix="j2ee" %>
<%@ page import="com.sun.j2ee.blueprints.petstore.util.JSPUtil" %>

<j2ee:searchList numItems="4" searchText='<%=JSPUtil.convertJISEncoding(request.getParameter("search_text"))%>' emptyList="該当する商品は見つかりません。">

   <table border="0" bgcolor="#336666">
      <tr background="../images/bkg-topbar.gif">
	<th><font color="white" size="3">商品コード</font></th>
	<th><font color="white" size="3">商品名</font></th>
	<th><font color="white" size="3">説明</font></th>
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

