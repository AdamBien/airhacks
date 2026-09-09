<%--
 % $Id: enteruserdata.jsp,v 1.7 2000/10/09 03:13:31 inder Exp $
 % Copyright 2000 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2000 Sun Microsystems, Inc. Tous droits r蜩erv蜩.
--%>

<%--
 % Prompt the user for personal information required before an order
 % can be placed.  The information is shown pre-filled in case the
 % user is logged into our application.

--%>
<%@ page contentType="text/html;charset=SJIS" %>

<%@ page import="com.sun.j2ee.blueprints.customer.util.CreditCard" %>
<%@ page import="com.sun.j2ee.blueprints.customer.util.ContactInformation" %>

<jsp:useBean
  id="customer"
  class="com.sun.j2ee.blueprints.petstore.control.web.CustomerWebImpl"
  scope="session"
/>

<%--
 % The following code makes the following properties available on the
 % page:
 %   isLoggedIn - Whether the user is logged in.
 %   cardNo     - The users credit card number.
 %   cardExpiryMonth  - The user's card's expiry date month.
 %   cardExpiryYear   - The user's card's expiry date year.
--%>

<%

  CreditCard card = (CreditCard)session.getAttribute("creditCard");
  ContactInformation contact = customer.getContactInformation();
  request.setAttribute("contact", contact);
  boolean isLoggedIn = customer.isLoggedIn();
  String cardNo = "9999 9999 9999 9999";
  String cardExpiryMonth = "01";
  String cardExpiryYear = "2000";

  if (customer.isLoggedIn() && card != null) {
    cardNo = "" + card.getCardNo();
    cardExpiryYear =  card.getExpiryMonthString();
    cardExpiryMonth = card.getExpiryMonthString();
  }

%>

<p>
<form action="validatebillinginformation">
  <font size="5" color="green">

<% if (isLoggedIn) { %>

    クレジットカード情報:

<% } else { %>

    クレジットカード情報を入力して下さい:

<% } %>

  </font>
  <p>
  <table border="0">
    <tr>
      <td>クレジットカード種別:</td>
      <td>
        <select name="credit_card_type">
          <option value="Visa">Visa
          <option value="Mastercard">Mastercard
          <option value="American Express">American Express
        </select>
      </td>
    </tr>
    <tr>
      <td>
        カード番号:
      </td>
      <td>
        <input type="text" size="20" name="credit_card_number"
          value="<%= cardNo %>">
      </td>
    </tr>
    <tr>
      <td>
        有効期限:
      </td>
      <td>
        月:
        <select name="expiration_month">
          <option value="01"
            <% if (cardExpiryMonth.equals("01")) { %> selected <% } %>
            >01</option>
          <option value="02"
            <% if (cardExpiryMonth.equals("02")) { %> selected <% } %>
            >02</option>
          <option value="03"
            <% if (cardExpiryMonth.equals("03")) { %> selected <% } %>
            >03</option>
          <option value="04"
            <% if (cardExpiryMonth.equals("04")) { %> selected <% } %>
            >04</option>
          <option value="05"
            <% if (cardExpiryMonth.equals("05")) { %> selected <% } %>
            >05</option>
          <option value="06"
            <% if (cardExpiryMonth.equals("06")) { %> selected <% } %>
            >06</option>
          <option value="07"
            <% if (cardExpiryMonth.equals("07")) { %> selected <% } %>
            >07</option>
          <option value="08"
            <% if (cardExpiryMonth.equals("08")) { %> selected <% } %>
            >08</option>
          <option value="09"
            <% if (cardExpiryMonth.equals("09")) { %> selected <% } %>
            >09</option>
          <option value="10"
            <% if (cardExpiryMonth.equals("10")) { %> selected <% } %>
            >10</option>
          <option value="11"
            <% if (cardExpiryMonth.equals("11")) { %> selected <% } %>
            >11</option>
          <option value="12"
            <% if (cardExpiryMonth.equals("12")) { %> selected <% } %>
            >12</option>
        </select>
        年:

        <% boolean found = false; %>
        <select name="expiration_year">
          <option value="2000"
            <% if (cardExpiryYear.equals("2000")) {
                found = true; %> selected <% } %>
            >2000</option>
          <option value="2001"
            <% if (cardExpiryYear.equals("2001")) {
                found = true; %> selected <% } %>
            >2001</option>
          <option value="2002"
            <% if (cardExpiryYear.equals("2002")) {
                found = true; %> selected <% } %>
            >2002</option>
          <option value="2003"
            <% if (cardExpiryYear.equals("2003")) {
                found = true; %> selected <% } %>
            >2003</option>
          <option value="2004"
            <% if (cardExpiryYear.equals("2004")) {
                found = true; %> selected <% } %>
            >2004</option>
          <option value="2005"
            <% if (cardExpiryYear.equals("2005")) {
                found = true; %> selected <% } %>
            >2005</option>
          <option value="2006"
            <% if (cardExpiryYear.equals("2006")) {
                found = true; %> selected <% } %>
            >2006</option>
          <% if (!found) { %>
          <option value="<%= cardExpiryYear %>" selected>
            <%= cardExpiryYear%></option>
          <% } %>
        </select>
      </td>
    </tr>
  </table>
  <p>
    <font size="5" color="green">
      請求書送付先:
    </font>

<% if (isLoggedIn) {%>
  <%--
   % User is logged in:  Display them a pre-filled form for possible
   % address changes
  --%>
  <p>
    下記の請求書送付先が正しいことをご確認いただき、
    <b>Continue</b> ボタンを押して下さい。
  <p>
    <jsp:include page="changeaddressform.jsp" flush="true" />

<% } else { %>
  <%--
   % not logged in.  Present a blank form, that they need to fill in.
  --%>

  <p>
    あなたのクレジットカードの明細書と同じ請求書送付先を入力して下さい。
  <p>
    <jsp:include page="/addressform.html" flush="true"/>

<% } // address form display %>

  <p>
    請求書送付先に商品を発送
    <input type="checkbox" name="ship_to_billing_address" checked>
  <p>
    <input type="image" src="../images/button_cont.gif" border="0">
</form>
