<%--
 % $Id: productdetails.jsp,v 1.8.4.1 2001/03/15 00:40:15 brydon Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
--%>

<%@ taglib uri="/WEB-INF/tlds/taglib.tld" prefix="j2ee" %>

<j2ee:productDetails>
        <table bgcolor=white width="600">
                <tr>
                        <td>
                                <font size="5" color="green">
                                        <j2ee:prodDetailsAttr attribute="ItemAttribute"/>
                                        <j2ee:prodDetailsAttr attribute="ProdName"/>
                                </font>
                        </td>
                        <td>
                                <j2ee:prodDetailsAttr attribute="Currency"/>
                        </td>
                        <td>
                                <j2ee:prodDetailsAttr attribute="Inventory"/>
                        </td>
                        <td>
                                <a href ="cart?action=purchaseItem&itemId=<j2ee:prodDetailsAttr attribute="ItemId"/>"><img src="../images/button_cart-add.gif" border="0" alt="Add Item to Your Shopping Cart"></a>
                        </td>
                </tr>
                <tr>
                        <td colspan="3">
                                <j2ee:prodDetailsAttr attribute="ProdDesc"/>
                        </td>
                </tr>
        </table>
</j2ee:productDetails>

