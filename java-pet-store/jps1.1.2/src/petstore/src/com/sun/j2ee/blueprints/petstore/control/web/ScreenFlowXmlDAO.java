/*
 * $Id: ScreenFlowXmlDAO.java,v 1.9.4.6 2001/03/15 00:40:05 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.web;

import org.xml.sax.InputSource;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;


// jaxp 1.0.1 imports
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.j2ee.blueprints.util.tracer.Debug;

/**
 * This class provides the data bindings for the screendefinitions.xml
 * and the requestmappings.xml file.
 * The data obtained is maintained by the ScreenFlowManager
 */

public class ScreenFlowXmlDAO {

    // constants
    public static final String URL_MAPPING = "url-mapping";
    public static final String EXCEPTION_MAPPING = "exception-mapping";
    public static final String SCREEN_DEFINITION = "screen-definition";
    public static final String URL = "url";
    public static final String LANGUAGE = "language";
    public static final String TEMPLATE = "template";
    public static final String RESULT = "result";
    public static final String NEXT_SCREEN = "screen";
    public static final String USE_REQUEST_HANDLER = "useRequestHandler";
    public static final String REQUIRES_SIGNIN = "requiresSignin";
    public static final String USE_FLOW_HANDLER = "useFlowHandler";
    public static final String FLOW_HANDLER_CLASS = "class";
    public static final String REQUEST_HANDLER_CLASS = "request-handler-class";
    public static final String HANDLER_RESULT = "handler-result";
    public static final String FLOW_HANDLER = "flow-handler";
    public static final String EXCEPTION_CLASS = "exception-class";
    public static final String DEFAULT_SCREEN = "default-screen";
    public static final String SIGNIN_SCREEN = "signin-screen";
    public static final String SIGNIN_ERROR_SCREEN = "signin-error-screen";

    public static final String KEY = "key";
    public static final String VALUE= "value";
    public static final String DIRECT="direct";
    public static final String SCREEN= "screen";
    public static final String SCREEN_NAME = "screen-name";
    public static final String PARAMETER = "parameter";

    public static Element loadDocument(String location) {
        Document doc = null;
        try {
            URL url = new URL(location);
            InputSource xmlInp = new InputSource(url.openStream());

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = docBuilderFactory.newDocumentBuilder();
            doc = parser.parse(xmlInp);
            Element root = doc.getDocumentElement();
            root.normalize();
            return root;
        } catch (SAXParseException err) {
            Debug.println ("ScreenFlowXmlDAO ** Parsing error" + ", line " +
                        err.getLineNumber () + ", uri " + err.getSystemId ());
            Debug.println("ScreenFlowXmlDAO error: " + err.getMessage ());
        } catch (SAXException e) {
            Debug.println("ScreenFlowXmlDAO error: " + e);
        } catch (java.net.MalformedURLException mfx) {
            Debug.println("ScreenFlowXmlDAO error: " + mfx);
        } catch (java.io.IOException e) {
            Debug.println("ScreenFlowXmlDAO error: " + e);
        } catch (Exception pce) {
            Debug.println("ScreenFlowXmlDAO error: " + pce);
        }
        return null;
    }

    public static ScreenFlowData loadScreenFlowData(String location) {
        Element root = loadDocument(location);
        HashMap screenDefinitionMappings = getScreenDefinitions(root);
        HashMap exceptionMappings = getExceptionMappings(root);
        String defaultScreen  = getTagValue(root, DEFAULT_SCREEN);
        String signinScreen = getTagValue(root, SIGNIN_SCREEN);
        String signinErrorScreen = getTagValue(root, SIGNIN_ERROR_SCREEN);
        return new ScreenFlowData(exceptionMappings,
                                  screenDefinitionMappings,
                                  defaultScreen,
                                  signinScreen,
                                  signinErrorScreen);
    }

    public static HashMap loadScreenDefinitions(String location) {
        Element root = loadDocument(location);
        return getScreens(root);
    }

    public static HashMap loadRequestMappings(String location) {
        Element root = loadDocument(location);
        return getRequestMappings(root);
    }
    public static HashMap loadScreenDefinitionMappings(String location) {
        Element root = loadDocument(location);
        return getScreenDefinitions(root);
    }

    public static HashMap loadExceptionMappings(String location) {
        Element root = loadDocument(location);
        return getExceptionMappings(root);
    }

    private static String getSubTagAttribute(Element root, String tagName, String subTagName, String attribute) {
        String returnString = "";
        NodeList list = root.getElementsByTagName(tagName);
        for (int loop = 0; loop < list.getLength(); loop++) {
            Node node = list.item(loop);
            if (node != null) {
                NodeList  children = node.getChildNodes();
                for (int innerLoop =0; innerLoop < children.getLength(); innerLoop++) {
                    Node  child = children.item(innerLoop);
                    if ((child != null) && (child.getNodeName() != null) && child.getNodeName().equals(subTagName) ) {
                        if (child instanceof Element) {
                            return ((Element)child).getAttribute(attribute);
                        }
                    }
                } // end inner loop
            }
        }
        return returnString;
    }

    public static HashMap getScreenDefinitions(Element root) {
        HashMap screensDefs = new HashMap();
        NodeList list = root.getElementsByTagName(SCREEN_DEFINITION);
        for (int loop = 0; loop < list.getLength(); loop++) {
            Node node = list.item(loop);
            if (node != null) {
                String language = null;
                String url = null;
                if (node instanceof Element) {
                    language = ((Element)node).getAttribute(LANGUAGE);
                    url = ((Element)node).getAttribute(URL);
                }
                if ((language != null) && (url != null) && !screensDefs.containsKey(language)) {
                    screensDefs.put(language, url);
                } else {
                    Debug.println("*** Non Fatal errror: ScreenDefinitions for language " + language +
                                       " defined more than once in screen definitions file");
                }
            }
        }
        return screensDefs;
    }

    public static HashMap getScreens(Element root) {
        HashMap screens = new HashMap();
        // get the template
        String templateName = getTagValue(root, TEMPLATE);
        screens.put(TEMPLATE, templateName);
        // get screens
        NodeList list = root.getElementsByTagName(SCREEN);
        for (int loop = 0; loop < list.getLength(); loop++) {
            Node node = list.item(loop);
            if (node != null) {
                String screenName = getSubTagValue(node, SCREEN_NAME);
                HashMap parameters = getParameters(node);
                Screen screen = new Screen(screenName, parameters);
                if (!screens.containsKey(screenName)) {
                    screens.put(screenName, screen);
                } else {
                    Debug.println("*** Non Fatal errror: Screen " + screenName +
                                       " defined more than once in screen definitions file");
                }
            }
        }
        return screens;
    }

    private static HashMap getParameters(Node node) {
        HashMap params = new HashMap();
        if (node != null) {
            NodeList  children = node.getChildNodes();
            for (int innerLoop =0; innerLoop < children.getLength(); innerLoop++) {
                Node  child = children.item(innerLoop);
                if ((child != null) && (child.getNodeName() != null) && child.getNodeName().equals(PARAMETER) ) {
                    if (child instanceof Element) {
                        Element childElement = ((Element)child);
                        String key = childElement.getAttribute(KEY);
                        String value = childElement.getAttribute(VALUE);
                        String directString = childElement.getAttribute(DIRECT);
                        boolean direct = false;
                        if ((directString != null) && directString.equals("true")) {
                            direct = true;
                        }
                        if (!params.containsKey(key)) {
                            params.put(key, new Parameter(key, value, direct));
                        } else {
                            Debug.println("*** Non Fatal errror: " +
                                               "Parameter " + key + " is defined more than once");

                        }
                    }
                }
            } // end inner loop
        }
        return params;
    }

    public static HashMap getExceptionMappings(Element root) {
        HashMap exceptionMappings = new HashMap();
        NodeList list = root.getElementsByTagName(EXCEPTION_MAPPING);
        for (int loop = 0; loop < list.getLength(); loop++) {
            Node node = list.item(loop);
            if (node != null) {
                String exceptionClassName = "";
                String screen = null;
                // get exception nodes
                // need to be a element to get attributes
                if (node instanceof Element) {
                    Element element = ((Element)node);
                    exceptionClassName = element.getAttribute(EXCEPTION_CLASS);
       Debug.println("ScreenFlowXmlDAO: adding " + exceptionClassName + " screen=" + screen);
                    screen = element.getAttribute(SCREEN);
                    exceptionMappings.put(exceptionClassName, screen);
                }

            }
        }
        return exceptionMappings;
    }

    public static HashMap getRequestMappings(Element root) {
        HashMap urlMappings = new HashMap();
        NodeList list = root.getElementsByTagName(URL_MAPPING);
        for (int loop = 0; loop < list.getLength(); loop++) {
            Node node = list.item(loop);
            if (node != null) {
                String url = "";
                String screen = null;
                String useRequestHandlerString = null;
                String useFlowHandlerString = null;
                String requiresSigninString = null;
                String flowHandler = null;
                String requestHandler =null;
                HashMap resultMappings = null;
                boolean useFlowHandler = false;
                boolean useRequestHandler = false;
                boolean requiresSignin = false;
                // get url mapping attributes
                // need to be a element to get attributes
                if (node instanceof Element) {
                    Element element = ((Element)node);
                    url = element.getAttribute(URL);
                    screen = element.getAttribute(NEXT_SCREEN);
                    useRequestHandlerString = element.getAttribute(USE_REQUEST_HANDLER);
                    useFlowHandlerString = element.getAttribute(USE_FLOW_HANDLER);
                    requiresSigninString = element.getAttribute(REQUIRES_SIGNIN);
                }
                if ((useRequestHandlerString != null) && useRequestHandlerString.equals("true")) useRequestHandler = true;
                if (useRequestHandler) {
                    requestHandler = getSubTagValue(node, REQUEST_HANDLER_CLASS);
                }
                if ((requiresSigninString != null) && requiresSigninString.equals("true")) requiresSignin = true;
                // get request handler
                if ((useFlowHandlerString != null) && useFlowHandlerString.equals("true")) useFlowHandler = true;
                // get flow handler
                if ((useFlowHandlerString != null) && useFlowHandlerString.equals("true")) useFlowHandler = true;
                if (useFlowHandler) {
                    // need to be a element to find sub nodes by name
                    if (node instanceof Element) {
                        Element element = (Element)node;
                        NodeList children = element.getElementsByTagName(FLOW_HANDLER);
                        Node flowHandlerNode = null;
                        if (children.getLength() >= 1) {
                            flowHandlerNode = children.item(0);
                        }
                        if (children.getLength() > 1) {
                                 Debug.println("Non fatal error: There can be only one <" + FLOW_HANDLER +
                                               "> definition in a <" + URL_MAPPING + ">");
                        }
                        // get the flow handler details
                        if (flowHandlerNode != null) {
                            if (flowHandlerNode instanceof Element) {
                                Element flowElement = (Element)flowHandlerNode;
                                flowHandler = flowElement.getAttribute(FLOW_HANDLER_CLASS);
                                NodeList results = flowElement.getElementsByTagName(HANDLER_RESULT);
                                if (results.getLength() > 0){
                                    resultMappings = new HashMap();
                                }
                                for (int resultLoop=0; resultLoop < results.getLength(); resultLoop++) {
                                    Node resultNode = results.item(resultLoop);
                                    if (resultNode instanceof Element) {
                                        Element resultElement = (Element)resultNode;
                                        String key = resultElement.getAttribute(RESULT);
                                        String value = resultElement.getAttribute(NEXT_SCREEN);
                                        if (!resultMappings.containsKey(key)) {
                                            resultMappings.put(key,value);
                                        } else {
                                            Debug.println("*** Non Fatal errror: Screen " + url + " <" + FLOW_HANDLER +
                                                           "> key " + "\"" + key + "\" defined more than one time");
                                        }
                                    }
                                } // end for
                            }
                        } // end if (flowHandler != null)
                    }
                } // end if (useFlowHandler)
                URLMapping mapping = new URLMapping(url, screen,
                                             useRequestHandler,
                                             useFlowHandler,
                                             requestHandler,
                                             flowHandler,
                                             resultMappings,
                                             requiresSignin);

                if (!urlMappings.containsKey(url)) {
                    urlMappings.put(url, mapping);
                } else {
                    Debug.println("*** Non Fatal errror: Screen " + url +
                                       " defined more than once in screen definitions file");
                }
            }
        }
        return urlMappings;
    }

    public static String getSubTagValue(Node node, String subTagName) {
        String returnString = "";
        if (node != null) {
            NodeList  children = node.getChildNodes();
            for (int innerLoop =0; innerLoop < children.getLength(); innerLoop++) {
                Node  child = children.item(innerLoop);
                if ((child != null) && (child.getNodeName() != null) && child.getNodeName().equals(subTagName) ) {
                    Node grandChild = child.getFirstChild();
                    if (grandChild.getNodeValue() != null) return grandChild.getNodeValue();
                }
            } // end inner loop
        }
        return returnString;
    }

    public static String getSubTagValue(Element root, String tagName, String subTagName) {
        String returnString = "";
        NodeList list = root.getElementsByTagName(tagName);
        for (int loop = 0; loop < list.getLength(); loop++) {
            Node node = list.item(loop);
            if (node != null) {
                NodeList  children = node.getChildNodes();
                for (int innerLoop =0; innerLoop < children.getLength(); innerLoop++) {
                    Node  child = children.item(innerLoop);
                    if ((child != null) && (child.getNodeName() != null) && child.getNodeName().equals(subTagName) ) {
                        Node grandChild = child.getFirstChild();
                        if (grandChild.getNodeValue() != null) return grandChild.getNodeValue();
                    }
                } // end inner loop
            }
        }
        return returnString;
    }

    public static String getTagValue(Element root, String tagName) {
        String returnString = "";
        NodeList list = root.getElementsByTagName(tagName);
        for (int loop = 0; loop < list.getLength(); loop++) {
            Node node = list.item(loop);
            if (node != null) {
                Node child = node.getFirstChild();
                if ((child != null) && child.getNodeValue() != null) return child.getNodeValue();
            }
        }
        return returnString;
    }
}

