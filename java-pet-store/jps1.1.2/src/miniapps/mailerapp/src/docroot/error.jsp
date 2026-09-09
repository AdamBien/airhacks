<!--
 % $Id: error.jsp,v 1.1.2.3 2001/04/10 01:35:52 ro89390 Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits r&eacute;serv&eacute;s.
-->

<html>
<head>
<title>J2EE[tm] Blueprints: Blueprints Mailer > Error</title>
</head>
<body>

<%@ include file="header.jsp" %>

<h2>Error</h2>

<p>We had problems processing your e-mail.</p>

<%= request.getAttribute("error_message") %>

<p><a href="<%=request.getContextPath()%>/control/index">Try Again!</a></p>

</body>
</html>
