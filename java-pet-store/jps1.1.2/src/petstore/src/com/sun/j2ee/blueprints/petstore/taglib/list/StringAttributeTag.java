package com.sun.j2ee.blueprints.petstore.taglib.list;

/*
 * StringAttributeTag
 * ------------------
 * Extension of ItemAttributeTag that handles a string object.  It just
 * prints out the string, with "text" as the default attribute.  Conceivably,
 * the string length or something could be printed instead.
 */
public class StringAttributeTag extends ItemAttributeTag {

  protected String createText() {
    if ((attribute == null) ||
        (attribute.equalsIgnoreCase("text"))) return ((String) item);
    return(null);
  }
}
