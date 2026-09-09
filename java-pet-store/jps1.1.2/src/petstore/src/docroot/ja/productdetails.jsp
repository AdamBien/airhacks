<%--
 % $Id: productdetails.jsp,v 1.8 2000/09/23 00:51:00 vijaysr Exp $
 % Copyright 2000 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2000 Sun Microsystems, Inc. Tous droits r蜩erv蜩.
--%>
<%@ page contentType="text/html;charset=SJIS" %>
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
                                <a href ="cart?action=purchaseItem&itemId=<j2ee:prodDetailsAttr attribute="ItemId"/>"><img src="../images/button_cart-add.gif" border="0" alt="ショピングカートに商品を追加"></a>
                        </td>
                </tr>
                <tr>
                        <td colspan="3">
                                <j2ee:prodDetailsAttr attribute="ProdDesc"/>
                        </td>
                </tr>
        </table>
</j2ee:productDetails>

