<%--
 % $Id: missingformdata.jsp,v 1.5 2000/09/28 05:59:35 brydon Exp $
 % Copyright 2000 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2000 Sun Microsystems, Inc. Tous droits r蜩erv蜩.
--%>

<%--
 % Report the missing fields to the user after a form validation
 % fails.
--%>
<%@ page contentType="text/html;charset=SJIS" %>

<%@ page import="com.sun.j2ee.blueprints.petstore.control.web.MissingFormDataException" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<p>

<%
  MissingFormDataException error =
    (MissingFormDataException)request.getAttribute("missingFormData");
  Collection missingFields = null;
  if (error != null)
    missingFields = error.getMissingFields();
%>

<font size="5" color="red"><%= error.getMessage() %></font>
<p>
  ブラウザの"戻る"ボタンで前のページに戻って、以下の項目が正しく
  入力されていることを確認して、再度、そのフォームを送信して
  下さい。
<ul>

<%
  if (missingFields != null) {
    Iterator it = missingFields.iterator();
    while (it.hasNext()) {
      String item = (String)it.next();
%>

    <li><%= item %></li>

<%   } %>
<% } %>

</ul>
