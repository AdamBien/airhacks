<%--
 % $Id: changepreferencesform.jsp,v 1.9.4.1 2001/03/15 00:40:12 brydon Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
--%>

<%--
 % A set of form fields, that prompt for personalization preferences.
 % This expects to be includes in the context of a FORM tag.  Right now
 % the categories for favorite cateogory are hard-coded.  The details are
 % expected to be found in an ExplicitInformation object called explicitInfo.
--%>

<table border="0" cellpadding="0" cellspacing="0">

  <% String lang = explicitInfo.getLangPref().toLowerCase(); %>

  <tr>
    <td></td>
    <td>
      I want MyPetStore to be in
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
      My favorite category is
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
    <td>Yes, I want to enable the MyList feature.  <i>MyList makes your
        favorite items and categories more prominent as you shop.</i></td>
  </tr>

  <tr>
    <td>
      &nbsp;
      <input type=checkbox name="banners_on"
        <% if (explicitInfo.getBannerOpt()) { %> checked <% } %>>
      &nbsp;
    </td>
    <td>Yes, I want to enable the pet tips banners.  <i>Java Pet Store will
        display pet tips as you shop, which are based on your favorite items
        and categories.</i>
    </td>
  </tr></td>
  </tr>
</table>

