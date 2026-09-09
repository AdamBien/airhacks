/*
 * $Id: PendingOrders.java,v 1.1.4.6 2001/04/10 18:42:16 ro89390 Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

// Generic imports

import java.util.*;
import java.io.*;
import java.net.*;

// Imports reqd for XML parsing

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;

// Imports reqd for staroffice stuff

import com.sun.star.uno.XInterface;
import com.sun.star.comp.servicemanager.ServiceManager;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.connection.XConnector;
import com.sun.star.connection.XConnection;
import com.sun.star.uno.IBridge;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XInterface;
import com.sun.star.uno.XNamingService;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.sheet.*;
import com.sun.star.container.*;
import com.sun.star.table.*;
import com.sun.star.beans.*;
import com.sun.star.style.*;
import com.sun.star.lang.*;
import com.sun.star.text.*;
import com.sun.star.chart.*;
import com.sun.star.document.*;
import com.sun.star.awt.Rectangle;

public class PendingOrders  {

    String orderId, userId, itemId, itemQty, orderDate, orderAmount;

    static String neededServices[] = new String[] {
        "com.sun.star.comp.servicemanager.ServiceManager",
        "com.sun.star.comp.loader.JavaLoader",
        "com.sun.star.comp.connections.Connector",
        "com.sun.star.comp.connections.Acceptor"
    };

    private String getXml(String jpsHost, String jpsPort) {
        StringBuffer inpXml = new StringBuffer("");
        try {
            URL myUrl = new
                URL("http://" + jpsHost + ":" + jpsPort +
                                            "/admin/pendingorders.jsp");
            URLConnection myCon = myUrl.openConnection();
            myCon.connect();

            DataInputStream inp = new DataInputStream(myCon.getInputStream());
            String res;
            while(true) {
                res = inp.readLine();
                if(res == null)
                    break;
                if(res.length() == 0)
                    continue;
                inpXml.append(res+"\n");
            }
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL : " + e);
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("IOException : " + e);
            System.exit(-1);
        }
        return(inpXml.toString());
    }

    private void getItemIdAndQty(Element orderRoot) {
        Element elem;
        Node    fchild;

        NodeList itemList = orderRoot.getElementsByTagName("Item");
        for(int i=0; i<itemList.getLength(); i++) {

// Get item id

               elem = (Element) itemList.item(i);
               if(elem == null) {
                   System.out.println("Unable to determine the item");
                   return;
               }
               itemId = elem.getAttribute("Id");

// Get user id

               fchild = elem.getElementsByTagName("Quantity").item(0);
            fchild = fchild.getFirstChild();
               itemQty = fchild.getNodeValue();
        }
    }

    private void getOrderDate(Element orderRoot) {
        Element elem;
        Node    fchild;
        int     i;

        NodeList dateList = orderRoot.getElementsByTagName("Date");
        for(i=0; i<dateList.getLength(); i++) {

// Get Date's attribute and ensure we have the order date node

               elem = (Element) dateList.item(i);
               if(elem == null) {
                   System.out.println("Unable to determine the date");
                   return;
               }
               String dateAttr = elem.getAttribute("Id");
            if(dateAttr.equals("OrderDate"))
                break;
        }
        if(i >= dateList.getLength()) {
            System.out.println("Did not find the Order Date node at all !!!");
            return;
        }

// Get the date

           elem = (Element) dateList.item(i);
        fchild = elem.getElementsByTagName("Month").item(0);
        fchild = fchild.getFirstChild();
        orderDate = fchild.getNodeValue();
        fchild = elem.getElementsByTagName("Day").item(0);
        fchild = fchild.getFirstChild();
        orderDate += "_" + fchild.getNodeValue();
        fchild = elem.getElementsByTagName("Year").item(0);
        fchild = fchild.getFirstChild();
        orderDate += "_" + fchild.getNodeValue();
    }

    private void getAndPrintDetails(String inpXml, XSpreadsheet spSheet)
                                                throws IOException {
        Document doc;
        Element  root;
        int      loop=0;
        Element  elem;
        Node     node, fchild;
        NodeList list, clist;

// Convert input XML in the form of string stream into Input Source

        ByteArrayInputStream xmlBa = new
                            ByteArrayInputStream(inpXml.getBytes());
        InputSource xmlInp = new InputSource(xmlBa);

/* These two lines will be used if the input XML is a file */

        //FileReader xmlFr = new FileReader(inpXml);
        //InputSource xmlInp = new InputSource(xmlFr);

        try {

// DOM init stuff

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.
                                                newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(xmlInp);
            root = doc.getDocumentElement();
            root.normalize();

               System.out.println("Filling in the details");
            list = root.getElementsByTagName("Order");
            for(int i=0; i<list.getLength(); i++) {

// Get order id

                elem = (Element) list.item(i);
                if(elem == null) {
                    System.out.println("Unable to determine the Order");
                    return;
                }
                orderId = elem.getAttribute("Id");

// Get user id

                clist = elem.getElementsByTagName("UserId");
                node = clist.item(0);
                if(node == null) {
                    System.out.println("Unable to determine the UserId");
                    return;
                }
                fchild = node.getFirstChild();
                if(fchild == null) {
                    System.out.println("Unable to determine the UserId");
                    return;
                }
                userId = fchild.getNodeValue();

// Get Item Id And Qty

                getItemIdAndQty(elem);

// Get Order Date

                getOrderDate(elem);

// Get Order Amount

                clist = elem.getElementsByTagName("TotalPrice");
                node = clist.item(0);
                if(node == null) {
                    System.out.println("Unable to determine the Total Price");
                    return;
                }
                fchild = node.getFirstChild();
                if(fchild == null) {
                    System.out.println("Unable to determine the Total Price");
                    return;
                }
                orderAmount = fchild.getNodeValue();

                insertIntoCell(0,i+1,orderId,spSheet);
                insertIntoCell(1,i+1,userId,spSheet);
                insertIntoCell(2,i+1,itemId,spSheet);
                insertIntoCell(3,i+1,itemQty,spSheet);
                insertIntoCell(4,i+1,orderDate,spSheet);
                insertIntoCell(5,i+1,orderAmount,spSheet);
                   System.out.println("Date is " + orderDate);
            }

// All those exception stuff

        } catch (SAXParseException err) {
            System.out.println ("** Parsing error" + ", line " +
                        err.getLineNumber () + ", uri " + err.getSystemId ());
            System.out.println("   " + err.getMessage ());
        } catch (SAXException e) {
            Exception    x = e.getException ();
            ((x == null) ? e : x).printStackTrace ();
        } catch (Throwable t) {
            t.printStackTrace ();
        }
        return;
    }

    public static XMultiServiceFactory connect(String connectStr)
                    throws com.sun.star.uno.Exception,
                        com.sun.star.uno.RuntimeException, Exception {
        XMultiServiceFactory servFactory = null;

// initial serviceManager

        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addFactories(neededServices);

// create a connector, so that it can contact the office

        Object  connector =
            serviceManager.createInstance("com.sun.star.connection.Connector");
        XConnector connector_xConnector = (XConnector)
                UnoRuntime.queryInterface(XConnector.class, connector);

// connect to the office

        XConnection xConnection = connector_xConnector.connect(connectStr);

        String rootOid = "classic_uno";
        IBridge iBridge = UnoRuntime.getBridgeByName( "java", null, "remote",
                        null, new Object[]{"iiop", xConnection, null});
        Object initObj = iBridge.mapInterfaceFrom(rootOid, XInterface.class);
        XNamingService name = (XNamingService)
                    UnoRuntime.queryInterface(XNamingService.class, initObj);
        if( name != null ) {
            System.err.println("got the remote naming service !");
            Object servMgr =
                    name.getRegisteredObject("StarOffice.ServiceManager" );
            servFactory = (XMultiServiceFactory)
                UnoRuntime.queryInterface(XMultiServiceFactory.class, servMgr);
        }
        return(servFactory);
    }

    public static XSpreadsheetDocument openCalc(XMultiServiceFactory servFact) {
        XInterface iFace;
        XDesktop desktop;
        XComponentLoader clLoader;
        XSpreadsheetDocument spSheet = null;
        XComponent docComp = null;

        try {
            iFace = (XInterface)
                    servFact.createInstance("com.sun.star.frame.Desktop");
            desktop = (XDesktop)
                    UnoRuntime.queryInterface(XDesktop.class, iFace);
            clLoader = (XComponentLoader)
                UnoRuntime.queryInterface(XComponentLoader.class, desktop);
            PropertyValue [] szEmptyArgs = new PropertyValue [0];
            String doc = "StarOffice.factory:scalc";
            docComp = clLoader.loadComponentFromURL(doc,"_blank",0,szEmptyArgs);
            spSheet = (XSpreadsheetDocument)
                UnoRuntime.queryInterface(XSpreadsheetDocument.class, docComp);
        } catch(Exception e){
            System.out.println(" Exception " + e);
        }
        return spSheet;
    }

    public static void insertIntoCell(int cellX, int cellY,
                String theValue, XSpreadsheet TT1) {
        XCell cellRef = TT1.getCellByPosition(cellX, cellY);
        cellRef.setFormula(theValue);
    }

    public XSpreadsheetDocument initSpreadSheet() {

// connect to the office an get the MultiServiceFactory
// this is necessary to create instances of Services

        XMultiServiceFactory servFactory = null;
        String connectStr="";
        XSpreadsheetDocument myDoc = null;

// create connection(s) and get multiservicefactory
//the connection string to connet the office

        connectStr = "socket, host=localhost,port=8100";

// create connection(s) and get multiservicefactory

        System.out.println("getting MultiServiceFactory");
        try {
            servFactory = connect(connectStr);
        } catch(Exception Ex) {
            System.out.println("Couldn't get MSF" + Ex);
        }

// open an empty document. In this case it's a calc document.
// For this purpose an instance of com.sun.star.frame.Desktop
// is created. It's interface XDesktop provides the XComponentLoader,
// which is used to open the document via loadComponentFromURL

        System.out.println("Opening an empty Calc document");
        myDoc = openCalc(servFactory);

// create cell styles.
// For this purpose get the StyleFamiliesSupplier and the the familiy
// CellStyle. Create an instance of com.sun.star.style.CellStyle and
// add it to the family. Now change some properties

        try {
            XStyleFamiliesSupplier xSFS = (XStyleFamiliesSupplier)
                UnoRuntime.queryInterface(XStyleFamiliesSupplier.class, myDoc);
            XNameAccess xSF = (XNameAccess) xSFS.getStyleFamilies();
            XNameAccess xCS = (XNameAccess) xSF.getByName("CellStyles");
            XMultiServiceFactory msfDoc = (XMultiServiceFactory)
                UnoRuntime.queryInterface( XMultiServiceFactory.class, myDoc );
            XNameContainer famNameCont = (XNameContainer)
                UnoRuntime.queryInterface(XNameContainer.class, xCS);
            XInterface interface1 = (XInterface)
                msfDoc.createInstance("com.sun.star.style.CellStyle");
            famNameCont.insertByName("My Style", interface1);
            XPropertySet propSet = (XPropertySet)
                UnoRuntime.queryInterface( XPropertySet.class, interface1 );
            propSet.setPropertyValue("IsCellBackgroundTransparent",
                                                    new Boolean(false));
            propSet.setPropertyValue("CellBackColor",new Integer(6710932));
            propSet.setPropertyValue("CharColor",new Integer(16777215));
            XInterface interface2 = (XInterface)
                    msfDoc.createInstance("com.sun.star.style.CellStyle");
            famNameCont.insertByName("My Style2", interface2);
            XPropertySet propSet2 = (XPropertySet)
                    UnoRuntime.queryInterface(XPropertySet.class, interface2);
            propSet2.setPropertyValue("IsCellBackgroundTransparent",
                    new Boolean(false));
            propSet2.setPropertyValue("CellBackColor",new Integer(13421823));
        } catch (Exception e) {
            System.out.println("Exception while creating cell styles : " + e);
            System.exit(-1);
        }
        return(myDoc);
    }

    public void displayPendingOrderDetails(XSpreadsheetDocument mySheet,
                                           String orderXml) {

// Get the sheets from the document and then the first from this container.
// Now some data can be inserted. For this purpose get a Cell via
// getCellByPosition and insert into this cell via setValue() (for floats)
// or setFormula() for formulas and Strings

        XSpreadsheet spSheet=null;
        try {
            System.out.println("Getting spreadsheet") ;
            XSpreadsheets multipleSheets = mySheet.getSheets() ;
            XIndexAccess indexSheet = (XIndexAccess)
                 UnoRuntime.queryInterface(XIndexAccess.class, multipleSheets);
            spSheet = (XSpreadsheet) indexSheet.getByIndex(0);
        } catch (Exception e) {
            System.out.println("Couldn't get Sheet " +e);
        }

        System.out.println("Creating the Header") ;
        insertIntoCell(0,0,"ORDER ID",spSheet);
        insertIntoCell(1,0,"USER ID",spSheet);
        insertIntoCell(2,0,"ITEM ID",spSheet);
        insertIntoCell(3,0,"QUANTITY",spSheet);
        insertIntoCell(4,0,"DATE",spSheet);
        insertIntoCell(5,0,"AMOUNT",spSheet);

        try {
            getAndPrintDetails(orderXml, spSheet);
        } catch(IOException e) {
            System.out.println("IOEx while getAndPrintDetails : " + e);
            System.exit(-1);
        }
    }

    public static void main(String args[]) {

// Set default host and port

        String host, port;

        if(args.length == 1)
            host = args[0];
        else
            host = "localhost";
        if(args.length == 2)
            port = args[1];
        else
            port = "8000";

// Get pending orders in xml format from Java Pet Store

        PendingOrders inst = new PendingOrders();
        String pendOrderXml = inst.getXml(host, port);

// Do the init required for creating the spreadsheet

        XSpreadsheetDocument mySheet = inst.initSpreadSheet();

// Display the pending order details

        inst.displayPendingOrderDetails(mySheet, pendOrderXml);

        System.exit(0);
    }
}
