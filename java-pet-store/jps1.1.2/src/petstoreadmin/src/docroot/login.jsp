<%--
 % $Id: login.jsp,v 1.2.4.2 2001/03/14 23:56:43 brydon Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
--%>

<%--
 % The login form, as required by the form based login mechanism.
 % Note that this form does NOT go through the templating mechanism.
--%>

  <body bgcolor="white">
    <h2><center>Please sign into Java Pet Store Admin Module</center></h2>
    <br><br><br><br>
    <center>
      <form action="j_security_check" method=post>
      <table>
      <tr>
       <td align="center" >
       <table border="0">
       <tr>
        <td><b>User ID:</b></td>
        <td>
          <input type="text" size="15" name="j_username" value="jps_admin">
        </td>
       </tr>
       <tr>
        <td><b>Password:</b></td>
         <td>
          <input type="password" size="15" name="j_password" value="admin">
        </td>
       </tr>
       <tr>
        <td></td>
        <td align="right">
         <input type="image" border="0" src="images/button_submit.gif" name="submit">
        </td>
       </tr>
       <tr>
        <td><br></td>
       </tr>
       </table>
       </td>
      </tr>
      </table>
    </form>
    </center>
  </body>
