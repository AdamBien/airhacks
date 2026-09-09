<%--
 % $Id: entermaildata.jsp,v 1.1.2.5 2001/04/11 23:18:21 ro89390 Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
--%>

<html>
<head>
</head>

<body>

<html>
<head>
<title>J2EE[tm] Blueprints: Blueprints Mailer > Send an E-mail</title>
</head>
<body>

<%@ include file="header.jsp" %>

<h2>Send an E-mail</h2>

<p>Please use the form below to send an e-mail.</p>

<form name="email"
action="<%=request.getContextPath()%>/control/sendMail"
method="POST">

<table border="0" bgcolor="#EEEEEE" cellpadding="5" cellspacing="0">
<tr>
<td><b>To:</b></td> <td><input type="text" size="40"
name="mail_emailaddress"></td>
</tr>
<tr>
<td><b>Subject:</b></td>
<td><input type="text" size="40" name="mail_subject"></td>
</tr>
<tr>
<td><b>Message:</b></td>
<td><textarea name="mail_message" cols="40" rows="5" wrap="soft"></textarea></td>
</tr>
<tr>
<td align="right" colspan="2">
<input type="reset" value="Clear Form">
<input type="submit" value="Send Message">
</td>
</tr>
</table>
</form>

</body>
</html>
