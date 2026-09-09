<%--
 % $Id: template.jsp,v 1.9.4.1 2001/03/15 00:40:16 brydon Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
--%>

<%-- page errorPage="errorpage.jsp" --%>
<%@ taglib uri="/WEB-INF/tlds/taglib.tld" prefix="j2ee" %>

    <script language="JavaScript">
    if (parent.frames['annotation'])
        parent.frames['annotation'].location = '<j2ee:insert parameter="Annotation" />';
    </script>
<html>
  <head>
    <title>
      <j2ee:insert parameter="HtmlTitle" />
    </title>
  </head>

  <body bgcolor="white">
    <j2ee:insert parameter="HtmlBanner" />
    <j2ee:insert parameter="HtmlTopIndex" />
    <table height="85%" width="100%"  cellspacing="0" border="0">
     <tr>
     <td valign="top">
        <j2ee:insert parameter="HtmlBody" />
       </td>
     </tr>
     <tr>
      <td valign="bottom">
        <j2ee:insert parameter="HtmlPetFooter" />
      </td>
     </tr>
     <tr>
      <td valign="bottom">
        <j2ee:insert parameter="HtmlFooter" />
      </td>
     </tr>
    </table>
  </body>
</html>
