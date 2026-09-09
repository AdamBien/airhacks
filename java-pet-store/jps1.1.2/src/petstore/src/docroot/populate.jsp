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
<br>

<% if (populateBean.isDatabaseInstallationValid() &&
          populateBean.isOptionalDatabaseInstallationValid()){ %>
The tables necessary to run the Java Petstore Application are
installed. You may proceed into the Java Pet Store Demo by clicking
on the &quot;Return to Java Pet Store Demo&quot; Link at the top of the Page.
<% } else if (populateBean.isDatabaseInstallationValid()){ %>
The tables necessary to run the English Java Petstore Application are
installed. You may proceed into the Java Pet Store Demo by clicking
on the &quot;Return to Java Pet Store Demo&quot; Link at the top of the Page.
<br><font color="red"> However you will not be able to browse the Japanese Store.</font>
<% } else { %>
<font color="red">
The tables necessary to run the Java Petstore Application are not
installed. Please select the &quot;Install Tables&quot; link on the banner.
</font>
<% } %>
<br>
<br>
<br> Tables Required to Run the English Java Petstore Demo
<br>
<br>
<table>
<tr bgcolor="#336666"><td><font color="white">Table Name</font></td><td><font color="white">Installed</font></td></tr>

<%
    HashMap tables = populateBean.getRequiredTables();
    Iterator it = tables.keySet().iterator();
    // loop through the list
    while ((it != null) && it.hasNext()) {
     String tableName = (String)it.next();
 %>
    <tr bgcolor="#eeebcc">
      <td><%=tableName%></td>
      <td><%=tables.get(tableName)%></td>
    </tr>
<%
  } // end loop
%>
</table>
<br>
<br>Additional Tables Required for the Japanese Pet Store
<br>
<br>
<table>
<tr bgcolor="#336666"><td><font color="white">Japanese Table Name</font></td><td><font color="white">Installed</font></td></tr>

<%
    HashMap oTables = populateBean.getOptionalTables();
    it = oTables.keySet().iterator();
    // loop through the list
    while ((it != null) && it.hasNext()) {
     String tableName = (String)it.next();
 %>
    <tr bgcolor="#eeebcc">
      <td><%=tableName%></td>
      <td><%=oTables.get(tableName)%></td>
    </tr>
<%
  } // end loop
%>
</table>


