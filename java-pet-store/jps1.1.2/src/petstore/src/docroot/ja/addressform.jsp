<%--
 % $Id: addressform.jsp,v 1.1.2.3 2001/04/03 21:39:01 gmurray Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits rÈservÈs. 
--%>

<%--
 % A set of form fields, that prompt for an address.  This expects to
 % be includes in the context of a FORM tag.
--%>
<%@ page contentType="text/html;charset=SJIS" %>

<table>
  <tr>
    <td align="right">ñºéö:</td>
    <td align="left" colspan="2">
      <input type="text" name="family_name" size="30" maxlength="30">
    </td>
  </tr>
  <tr>
    <td align="right">ñºëO:</td>
    <td align="left" colspan="2">
      <input type="text" name="given_name" size="30" maxlength="30">
    </td>
  </tr>
  <tr>
    <td align="right">í¨ñºî‘ín:</td>
    <td align="left" colspan="2">
      <input type="text" name="address_1" size="55" maxlength="70">
    </td>
  </tr>
  <tr>
    <td></td>
    <td align="left" colspan="2">
      <input type="text" name="address_2" size="55" maxlength="70">
    </td>
  </tr>
  <tr>
    <td align="right">ésí¨ë∫ñº:</td>
    <td align="left" colspan="2">
      <input type="text" name="city" size="55" maxlength="70">
    </td>
  </tr>
  <tr>
    <td>èBñº/ìsìπï{åßñº:</td>
    <td align="left">
      <select size="1" name="state_or_province">
        <option value="ìåãûìs">ìåãûìs</option>
        <option value="ëÂç„ï{">ëÂç„ï{</option>
        <option value="í∑ñÏåß">í∑ñÏåß</option>
      </select>
    </td>
    <td>óXï÷î‘çÜ: 
      <input type="text" name="postal_code" size="12" maxlength="12">
    </td>
  </tr>
  <tr>
    <td>çëñº:</td>
    <td align="left" colspan="2">
      <select size="1" name="country">
        <option value="USA">USA</option>
        <option value="Canada">Canada</option>
        <option value="Japan" selected>Japan</option>
      </select>
    </td>
  </tr>
  <tr>
    <td>ìdòbî‘çÜ:</td>
    <td align="left" colspan="2">
      <input type="text" name="telephone_number" size="12" maxlength="70">
    </td>
  </tr>
</table>
