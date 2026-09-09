<%--
 % $Id: changepreferencesform.jsp,v 1.9 2000/10/04 02:23:59 lblair Exp $
 % Copyright 2000 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2000 Sun Microsystems, Inc. Tous droits r蜩erv蜩.
--%>

<%@ page import="com.sun.j2ee.blueprints.personalization.profilemgr.model.ExplicitInformation" %>
<%@ page contentType="text/html;charset=SJIS" %>

<%--
 % A set of form fields, that prompt for personalization preferences.
 % This expects to be includes in the context of a FORM tag.  Right now
 % the categories for favorite cateogory are hard-coded.  The details are
 % expected to be found in an ExplicitInformation object called explicitInfo.
--%>


<table border="0" cellpadding="0" cellspacing="0">

  <%
      ExplicitInformation explicitInfo = (ExplicitInformation)request.getAttribute("explicitInfo");
      String lang = explicitInfo.getLangPref().toLowerCase();
   %>

  <tr>
    <td></td>
    <td>
      わたしの PetStore は次の言語を希望します。
      <select name="language" size="1">
        <option
          value="English"
          <% if (lang.equals("english")) { %> selected <% } %>
          >English</option>
        <option
          value="Japanese"
          <% if (lang.equals("japanese")) { %> selected <% } %>
          >Japanese</option>
      </select>
    </td>
  </tr>

  <% String favCat = explicitInfo.getFavCategory().toLowerCase(); %>

  <tr>
    <td></td>
    <td>
      私の好きなカテゴリは、
      <select name="favorite_category" size="1">
        <option
          value="Birds"
          <% if (favCat.equals("birds")) { %> selected <% } %>
          >Birds</option>
        <option
          value="Cats"
          <% if (favCat.equals("cats")) { %> selected <% } %>
          >Cats</option>
        <option
          value="Dogs"
          <% if (favCat.equals("dogs")) { %> selected <% } %>
          >Dogs</option>
        <option
          value="Fish"
          <% if (favCat.equals("fish")) { %> selected <% } %>
          >Fish</option>
        <option
          value="Reptiles"
          <% if (favCat.equals("reptiles")) { %> selected <% } %>
          >Reptiles</option>
      </select>
    </td>
  </tr>

  <tr>
    <td>
      &nbsp;
      <input type=checkbox name="myList_on"
        <% if (explicitInfo.getMyListOpt()) { %> checked <% } %>>
      &nbsp;
    </td>
    <td>MyList 機能を有効にすることを希望します。  <i>MyList は、お買い物の時に
        目に付くように、お気に入りの商品やカテゴリを表示します。</i></td>
  </tr>

  <tr>
    <td>
      &nbsp;
      <input type=checkbox name="banners_on"
        <% if (explicitInfo.getBannerOpt()) { %> checked <% } %>>
      &nbsp;
    </td>
    <td>ペットの情報バナー機能を有効にすることを希望します。  <i>Java ペット屋さんは、
        あなたのお気に入りの商品やカテゴリをもとに、お買い物の時に、ペットの情報を表示します。
        </i>
    </td>
  </tr></td>
  </tr>
</table>

