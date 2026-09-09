<%--
 % $Id: help.jsp,v 1.3.4.1 2001/03/15 00:40:13 brydon Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
--%>

<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
   <title>Java(TM) Pet Store Demo Help</title>
</head>
<body text="#000000" bgcolor="#FFFFFF" link="#0000FF" vlink="#FF0000" alink="#FF0000">

<center>
<h1>
Java(TM) Pet Store Demo Help</h1></center>
The Java(TM) Pet Store Demo is an online pet store. Like most e-stores,
you can browse and search the product catalog, choose items to add to a
shopping cart, amend the shopping cart, and order the items in the shopping
cart. You can perform many of these actions without registering with or
logging into the application. However, before you can order items you must
log in (sign in) to the application. In order to sign in, you must have
an account with the application, which is created when you register (sign
up) with the application.
<p><a href="#SigningUp">Signing Up</a>
<br><a href="#SigningIn">Signing In</a>
<br><a href="#Catalog">Working with the Product Catalog</a>
<br>&nbsp;&nbsp;&nbsp; <a href="#CatalogBrowsing">Browsing the Catalog</a>
<br>&nbsp;&nbsp;&nbsp; <a href="#CatalogSearching">Searching the Catalog</a>
<br><a href="#ShoppingCart">Working with the Shopping Cart</a>
<br>&nbsp;&nbsp;&nbsp; <a href="#ShoppingCartAdd">Adding and Removing Items</a>
<br>&nbsp;&nbsp;&nbsp; <a href="#ShoppingCartUpdate">Updating the Quantity
of an Item</a>
<br>&nbsp;&nbsp;&nbsp; <a href="#Ordering">Ordering Items</a>
<br><a href="#OrderReview">Reviewing an Order</a>
<br><a href="#Issues">Known Issues</a>
<h2>
<a NAME="SigningUp"></a>Signing Up</h2>
To sign up, click the Sign-in link at the right end of the banner. Next,
click the New User link in the resulting page. Among other information,
the signup page requires you to provide a user identifier and password.
This information is used to identify your account and must be provided
when signing in.
<h2>
<a NAME="SigningIn"></a>Signing In</h2>
You sign in to the application by clicking the Sign-in link at the right
end of the banner, filling in the user identifier and password, and clicking
the Submit button.
<p>You will also be redirected to the signin page when you try to place
an order and you have not signed in. Once you have signed in, you can return
to your shopping session by clicking the shopping cart icon at the right
end of the banner.
<br>&nbsp;
<h2>
<a NAME="Catalog"></a>Working with the Product Catalog</h2>
This section describes how to browse and search the product catalog.
<h4>
<a NAME="CatalogBrowsing"></a>Browsing the Catalog</h4>
The pet store catalog is organized hierarchically as follows: categories,
products, items.
<p>You list the pets in a category by clicking on the category name in
the left column of the main page, or by clicking on the picture representing
the category.
<p>Once you select a category, the pet store will display a list of products
within a category. Selecting a product displays a list of items and their
prices. Selecting a product item displays a text and visual description
of the item and the number of that item in stock.
<h4>
<a NAME="CatalogSearching"></a>Searching the Catalog</h4>
You search for products by typing the product name in search field in the
middle of the banner.
<h2>
<a NAME="ShoppingCart"></a>Working with the Shopping Cart</h2>

<h4>
<a NAME="ShoppingCartAdd"></a>Adding and Removing Items</h4>
You add an item to your shopping cart by clicking the Add to Cart button
to the right of an item. This action also displays your shopping cart.
<p>You can remove the item by clicking the Remove button to the left of
the item.
<p>To continue shopping, you select a product category from the list under
the banner.
<h4>
<a NAME="ShoppingCartUpdate"></a>Updating the Quantity of&nbsp; an Item</h4>
You adjust the quantity of an item by typing the quantity in the item's
Quantity field in the shopping cart and clicking the Update button.
<p>If the quantity of items requested is greater than that in stock, the
In Stock field in the shopping cart will show that the item is backordered.
<h4>
<a NAME="Ordering"></a>Ordering Items</h4>
You order the items in the shopping cart by selecting the Proceed to Checkout
button. The pet store will display a read-only list of the shopping cart
contents. To proceed with the checkout, click the Continue button.
<p>If you have not signed in, the application will display the signin page,
where you will need to provide your account name and password. Otherwise,
the application will display a page requesting payment and shipping information.
When you have filled in the required information, you click the Submit
button, and the application will display a read-only page containing your
billing and shipping address.&nbsp; If you need to change any information,
click your browser's Back button and enter the correct information. To
complete the order, you click the Continue button.
<h2>
<a NAME="OrderReview"></a>Reviewing An Order</h2>
The final screen contains your order information.
<p>The application can be set up to send email confirmation of
orders.&nbsp; This option can only be set when the application is deployed.
See the installation instructions for further information.
<h2>
<a NAME="Issues"></a>Known Issues</h2>
Using the browser back button can lead to problems in certain situations.
For example, if you complete an order and then use the back button and
try to place the order again, you get an error.
</body>
</html>
