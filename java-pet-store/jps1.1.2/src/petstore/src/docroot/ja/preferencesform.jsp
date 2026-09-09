<%--
 % A set of form fields, that prompt for personalization preferences.  
 % This expects to be includes in the context of a FORM tag.  Right now
 % the categories for favorite cateogory are hard-coded.
--%>

<%@ page contentType="text/html;charset=SJIS" %>


<table border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td></td>
    <td>
      わたしの PetStore は次の言語を希望します。
      <select name="language" size="1">
	<option value="English">English</option>
        <option value="Japanese" selected>Japanese</option>
      </select>
    </td>
  </tr>

  <tr>
    <td></td>
    <td>
      私の好きなカテゴリは、
      <select name="favorite_category" size="1">
        <option value="birds" selected>Birds</option>
	<option value="cats">Cats</option>
        <option value="dogs">Dogs</option>
	<option value="fish">Fish</option>
	<option value="reptiles">Reptiles</option>
      </select>
    </td>
  </tr>

  <tr>
    <td>
      &nbsp;
      <input type=checkbox name="myList_on" checked>
      &nbsp;
    </td>
    <td>MyList 機能を有効にすることを希望します。  <i>MyList は、お買い物の時に
        目に付くように、お気に入りの商品やカテゴリを表示します。</i></td>
  </tr>

  <tr>
    <td>
      &nbsp;
      <input type=checkbox name="banners_on" checked>
      &nbsp;
    </td>
    <td>ペットの情報バナー機能を有効にすることを希望します。  <i>Java ペット屋さんは、
        あなたのお気に入りの商品やカテゴリをもとに、お買い物の時に、ペットの情報を表示します。
        </i>
</td>
  </tr>
</table>

