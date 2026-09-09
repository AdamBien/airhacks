<%--
 % $Id: mouseover.js,v 1.4.4.1 2001/03/15 00:40:14 brydon Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits réservés. 
--%>

<%--
 % Functions for changing images on the fly.
--%>

<script language="JavaScript">
  // change image to name_on 
  function img_on(name) {
    if (document.images) 
      document['img_'+name].src = eval('img_'+name+'_on.src');
  }

  // change image to name_off
  function img_off(name) {
    if (document.images)
      document['img_'+name].src = eval('img_'+name+'_off.src');
  }
</script>
