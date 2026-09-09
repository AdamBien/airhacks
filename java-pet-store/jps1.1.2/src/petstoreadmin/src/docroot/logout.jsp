<!--
 % $Id: logout.jsp,v 1.1.4.1 2001/03/14 23:56:43 brydon Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits r&eacute;serv&eacute;s.
-->

<%
  request.getSession().invalidate();
%>

<html>
  <head>
    <title>Estore Admin Page</title>
  </head>
  <body bgcolor="white">
    <center>
      <h1>Estore Admin Page</h1>
    </center>
    <center><hr width="100%"></center>
    <p>
    <p>
    <p>
    <a href="/admin/AdminRequestProcessor"> Click here to enter Admin</a>
  </body>
</html>
