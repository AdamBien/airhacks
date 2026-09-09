<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.HashMap" %>

<jsp:useBean
  id="populateBean"
  type="com.sun.j2ee.blueprints.tools.populate.web.PopulateBean"
  scope="request"
/>

<html>
<body bgcolor="white">
<table width="100%" height="66" cellspacing="0" border="0"  background="<%=request.getContextPath()%>/images/bkg-topbar.gif">
  <tr>
      <td colspan="3">
       <font size="5" color="white">Java Pet Store Demo Database Populate</font>
      </td>
  </tr>
  <tr>
        <td><a href="<%=request.getContextPath()%>/populate"><font color="white">Refresh</font></a></td>
        <td><a href="<%=request.getContextPath()%>/populate?command=chooseTables"><font color="white">Install tables</font></a></td>
        <td> <a href="<%=request.getContextPath()%>" ><font color="white">Return to Java Petstore Demo</font></a></td>
  </tr>
</table>

<br>The Database you are using is <%=populateBean.getDatabaseProductName()%>
<br>
<br>Select the database which you would like to install tables for.
<br>
<br><a href="<%=request.getContextPath()%>/populate?command=installTables&database_name=DBMS:cloudscape">Install Cloudscape tables </a>
<% if (populateBean.getDatabaseProductName().equals("DBMS:cloudscape")) out.println("(Recommended Based on Configuration)");%>
<br><a href="<%=request.getContextPath()%>/populate?command=installTables&database_name=Oracle">Install Oracle tables</a>
<% if (populateBean.getDatabaseProductName().equals("Oracle")) out.println("(Recommended Based on Configuration)");%>
<br><a href="<%=request.getContextPath()%>/populate?command=installTables&database_name=SQL+Server">Install Sybase tables</a>
<% if (populateBean.getDatabaseProductName().equals("SQL Server")) out.println("(Recommended Based on Configuration)");%>

