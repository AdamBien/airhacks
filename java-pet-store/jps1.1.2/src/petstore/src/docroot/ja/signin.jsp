<%--
 % $Id: signin.jsp,v 1.1.2.4 2001/04/06 01:13:59 gmurray Exp $
 % Copyright 2000 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2000 Sun Microsystems, Inc. Tous droits r蜩erv蜩.
--%>
<%@ page contentType="text/html;charset=SJIS" %>


<%@ page errorPage="errorpage.jsp" %>

  <body bgcolor="white">
    <h2><center>Java ペット屋さん デモ にログインして下さい。</center></h2>
    <br><br><br><br>
    <center>
      <form action="verifysignin">
      <input type="hidden" name="target_screen" value='<%=request.getParameter("target_screen")%>'> 
      <table>
      <tr>
       <td align="center" >
       <table border="0">
       <tr>
        <td><b>ユーザー ID:</b></td>
        <td>
          <input type="text" size="15" name="j_username" value="j2ee-日本">
        </td>
       </tr>
       <tr>
        <td><b>パスワード:</b></td>
         <td>
          <input type="password" size="15" name="j_password" value="j2ee-日本">
        </td>
       </tr>
       <tr>
        <td></td>
        <td align="right">
         <input type="image" border="0" src="<%=request.getContextPath()%>/images/button_submit.gif" name="submit">
        </td>
       </tr>
       <tr>
       <td><br></td>
       </tr>
      <tr>
       <td colspan="3" align="center">
         <a href="createnewaccount">
          新規ユーザー登録
        </a>
       </td>
       </tr>
     </table>
      </td>
        </tr>
      </table>
    </form>
    </center>
  </body>
