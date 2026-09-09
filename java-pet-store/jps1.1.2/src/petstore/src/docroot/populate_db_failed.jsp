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
<br>
 <font size="+5" color="red">Unable to Connect to Database</font>
 <br>Database Connection is closed: Confirm that you have started your database
 </body>
