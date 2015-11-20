package ru.megafon.krasnodar.learn;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;

/**
 * Created by Stalker on 03.04.15.
 */
public class LearnJAXPDOM {
  private static final Logger LOG = LoggerFactory.getLogger(LearnJAXPDOM.class.getName());

  static final String outputEncoding = "UTF-8";

  private PrintWriter out;

  public LearnJAXPDOM(PrintWriter out){
    this.out = out;
  }

  public void readXML(String xmlData) throws Exception {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    OutputStreamWriter errorWriter = new OutputStreamWriter(System.err, outputEncoding);
    db.setErrorHandler(new DOMErrorHandler (new PrintWriter(errorWriter, true)));
    Document document = db.parse(new InputSource(new StringReader(xmlData)));
    printDocumentStructure(document.getDocumentElement());
  }

  public Document readXMLAsDocument(String xmlData) throws Exception {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    OutputStreamWriter errorWriter = new OutputStreamWriter(System.err, outputEncoding);
    db.setErrorHandler(new DOMErrorHandler (new PrintWriter(errorWriter, true)));
    Document document = db.parse(new InputSource(new StringReader(xmlData)));
    return document;
  }

  public String getValueFromXMLAsString(String xmlSource, String fullPathToAttrNode, String attrName) throws Exception {
    //throw new UnsupportedOperationException();
    String result = "";

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    OutputStreamWriter errorWriter = new OutputStreamWriter(System.err, outputEncoding);
    db.setErrorHandler(new DOMErrorHandler (new PrintWriter(errorWriter, true)));
    Document document = db.parse(new InputSource(new StringReader(xmlSource)));

    fullPathToAttrNode = fullPathToAttrNode.substring(1);
    String[] nodes = fullPathToAttrNode.toUpperCase().split("/");
    Node node = findSubNode(document.getDocumentElement(), nodes, -1);
    if (node != null) {
      NamedNodeMap atts = node.getAttributes();
      if (atts != null) {
        Node attr = atts.getNamedItem(attrName);
        if (attr != null) {
          result = attr.getNodeValue();
        }
        else {
          throw new DOMException(DOMException.NOT_FOUND_ERR, String.format("Attribute %1$s in requested node: %2$s - not exist!", attrName, node.getNodeName()));
        }
      }
      else {
        throw new DOMException(DOMException.NOT_FOUND_ERR, String.format("No attributes in requested node: %1$s !", node.getNodeName()));
      }
    }
    else {
      throw new DOMException(DOMException.NOT_FOUND_ERR, String.format("Node not found: %1$s !", nodes[nodes.length - 1]));
    }

    return result;
  }

  public AppConfigData getAppConfigData(String configXMLData) throws Exception {
    //throw new UnsupportedOperationException();

    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to getAppConfigData(" + configXMLData + ")");
    }

    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      OutputStreamWriter errorWriter = new OutputStreamWriter(System.err, outputEncoding);
      db.setErrorHandler(new DOMErrorHandler (new PrintWriter(errorWriter, true)));
      Document document = db.parse(new InputSource(new StringReader(configXMLData)));

      return getDataFromDOM(document.getDocumentElement());
    }
    finally {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exit from getAppConfigData(" + configXMLData + ")");
      }
    }
  }

  private AppConfigData getDataFromDOM(Node rootNode) {
    AppConfigData result = null;

    if (AppConfigNodeName.getNodeName(rootNode.getNodeName()) == AppConfigNodeName.CONFIGURATION_ROOT) {
      result = new AppConfigData();

      NodeList childNodes = rootNode.getChildNodes();

      for (int iNodeCounter = 0; iNodeCounter < childNodes.getLength(); iNodeCounter++) {
        Node currentNode = childNodes.item(iNodeCounter);

        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
          switch (AppConfigNodeName.getNodeName(currentNode.getNodeName())) {
            case CONNECTION:
              result.setConnectionConfigItem(connectionNodeProcessing(currentNode));
              break;
            case MAIL:
              result.setMailConfigItem(mailNodeProcessing(currentNode));
              break;
            case PATHS:
              result.setPathsConfigItem(pathsNodeProcessing(currentNode));
              break;
            case MAIN:
              result.setMainConfigItem(mainNodeProcessing(currentNode));
              break;
            case SERVICE:
              result.setServiceConfigItem(serviceNodeProcessing(currentNode));
              break;
          }
        }
      }

      return result;
    }
    else {
      throw new DOMException(DOMException.NOT_FOUND_ERR, "Root node invalid name!");
    }
  }

  private ConnectionConfigItem connectionNodeProcessing(Node node) throws DOMException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to connectionNodeProcessing(" + node.getNodeName() + ")");
    }

    try {
      NamedNodeMap atts = node.getAttributes();
      if (atts != null) {
        return new ConnectionConfigItem(getAttributeValue(atts, ConnectionConfigItem.ATTR_NAME_SERVER, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.CONNECTION, ConnectionConfigItem.ATTR_NAME_SERVER)));
      }
      else {
        throw new DOMException(DOMException.NOT_FOUND_ERR, "Attributes missing!");
      }
    }
    finally {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exit from connectionNodeProcessing(" + node.getNodeName() + ")");
      }
    }
  }

  private MailConfigItem mailNodeProcessing(Node node) throws DOMException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to connectionNodeProcessing(" + node.getNodeName() + ")");
    }

    try {
      NamedNodeMap atts = node.getAttributes();
      if (atts != null) {
        String smtpServer = getAttributeValue(atts, MailConfigItem.ATTR_NAME_SMTPSERVER, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_SMTPSERVER));

        String systemMailFromName = getAttributeValue(atts, MailConfigItem.ATTR_NAME_SYSTEMMAILFROMNAME, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_SYSTEMMAILFROMNAME));

        String messageBodyEncoding = getAttributeValue(atts, MailConfigItem.ATTR_NAME_MESSAGEBODYENCODING, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_MESSAGEBODYENCODING));

        String systemMail = getAttributeValue(atts, MailConfigItem.ATTR_NAME_SYSTEMMAIL, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_SYSTEMMAIL));

        String systemMailUserName = getAttributeValue(atts, MailConfigItem.ATTR_NAME_SYSTEMMAILUSERNAME, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_SYSTEMMAILUSERNAME));

        String systemMailUserPassword = getAttributeValue(atts, MailConfigItem.ATTR_NAME_SYSTEMMAILUSERPASWORD, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_SYSTEMMAILUSERPASWORD));

        return new MailConfigItem(smtpServer, systemMailFromName, messageBodyEncoding, systemMail, systemMailUserName, systemMailUserPassword);
      }
      else {
        throw new DOMException(DOMException.NOT_FOUND_ERR, "Attributes missing!");
      }
    }
    finally {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exit from connectionNodeProcessing(" + node.getNodeName() + ")");
      }
    }
  }

  private PathsConfigItem pathsNodeProcessing(Node node) throws DOMException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to connectionNodeProcessing(" + node.getNodeName() + ")");
    }

    try {
      NamedNodeMap atts = node.getAttributes();
      if (atts != null) {
        return new PathsConfigItem(getAttributeValue(atts, PathsConfigItem.ATTR_NAME_MESSAGETEMPLATES, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.PATHS, PathsConfigItem.ATTR_NAME_MESSAGETEMPLATES)));
      }
      else {
        throw new DOMException(DOMException.NOT_FOUND_ERR, "Attributes missing!");
      }
    }
    finally {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exit from connectionNodeProcessing(" + node.getNodeName() + ")");
      }
    }
  }

  private MainConfigItem mainNodeProcessing(Node node) throws DOMException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to connectionNodeProcessing(" + node.getNodeName() + ")");
    }

    try {
      NamedNodeMap atts = node.getAttributes();
      if (atts != null) {
        String refusalBuyerCount = getAttributeValue(atts, MainConfigItem.ATTR_NAME_REFUSALBUYERCOUNT, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIN, MainConfigItem.ATTR_NAME_REFUSALBUYERCOUNT));

        String waitingTimeConclusionAgreement = getAttributeValue(atts, MainConfigItem.ATTR_NAME_WAITINGTIMECONCLUSIONAGREEMENT, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIN, MainConfigItem.ATTR_NAME_WAITINGTIMECONCLUSIONAGREEMENT));

        String agreementLink = getAttributeValue(atts, MainConfigItem.ATTR_NAME_AGREEMENTLINK, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIN, MainConfigItem.ATTR_NAME_AGREEMENTLINK));

        String ldapUserName = getAttributeValue(atts, MainConfigItem.ATTR_NAME_LDAPUSERNAME, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIN, MainConfigItem.ATTR_NAME_LDAPUSERNAME));

        String ldapUserPwd = getAttributeValue(atts, MainConfigItem.ATTR_NAME_LDAPUSERPWD, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIN, MainConfigItem.ATTR_NAME_LDAPUSERPWD));

        return new MainConfigItem(refusalBuyerCount, waitingTimeConclusionAgreement, agreementLink, ldapUserName, ldapUserPwd);
      }
      else {
        throw new DOMException(DOMException.NOT_FOUND_ERR, "Attributes missing!");
      }
    }
    finally {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exit from connectionNodeProcessing(" + node.getNodeName() + ")");
      }
    }
  }

  private ServiceConfigItem serviceNodeProcessing(Node node) throws DOMException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to connectionNodeProcessing(" + node.getNodeName() + ")");
    }

    try {
      NamedNodeMap atts = node.getAttributes();
      if (atts != null) {
        String administratorPhone = getAttributeValue(atts, ServiceConfigItem.ATTR_NAME_ADMINISTRATORPHONE, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.SERVICE, ServiceConfigItem.ATTR_NAME_ADMINISTRATORPHONE));

        String administratorMail = getAttributeValue(atts, ServiceConfigItem.ATTR_NAME_ADMINISTRATORMAIL, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.SERVICE, ServiceConfigItem.ATTR_NAME_ADMINISTRATORMAIL));;

        return new ServiceConfigItem(administratorPhone, administratorMail);
      }
      else {
        throw new DOMException(DOMException.NOT_FOUND_ERR, "Attributes missing!");
      }
    }
    finally {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exit from connectionNodeProcessing(" + node.getNodeName() + ")");
      }
    }
  }

  private String getAttributeValue(NamedNodeMap atts, String attrName, String exceptionMsg) throws DOMException {
    Node attr = atts.getNamedItem(attrName);
    if (attr == null) {
      throw new DOMException(DOMException.NOT_FOUND_ERR, exceptionMsg);
    }

    return attr.getNodeValue();
  }

  private Node findSubNode(Node node, String[] nodesToAttr, int currentNodeLevel) {
    currentNodeLevel++;

    if (node.getNodeType() == Node.ELEMENT_NODE) {
      if (node.getNodeName().toUpperCase().equals(nodesToAttr[currentNodeLevel])) {
        if (currentNodeLevel == nodesToAttr.length - 1) {
          return node;
        }
        else {
          for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
            Node foundnode = findSubNode(child, nodesToAttr, currentNodeLevel);
            if (foundnode != null) {
              return foundnode;
            }
          }
        }
      }
    }

    return null;
  }

  private void printlnCommon(Node node) {
    out.print(" nodeName=\"" + node.getNodeName() + "\"");

    String val = node.getNodeValue();
    if (val != null) {
      out.print(" nodeValue=");
      if (val.trim().equals("")) {
        // Whitespace
        out.print("[WS]");
      }
      else {
        out.print("\"" + node.getNodeValue() + "\"");
      }
    }

    out.println();
  }

  private void printDocumentStructure(Node node) {
    int type = node.getNodeType();

    switch (type) {
      case Node.ELEMENT_NODE:
        out.print("ELEM:");
        printlnCommon(node);

        NamedNodeMap atts = node.getAttributes();
        for (int i = 0; i < atts.getLength(); i++) {
          Node att = atts.item(i);
          printDocumentStructure(att);
        }
        break;
      case Node.TEXT_NODE:
        out.print("TEXT:");
        printlnCommon(node);
        break;
      case Node.ATTRIBUTE_NODE:
        out.print("ATTR:");
        printlnCommon(node);
        break;
    }

    for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
      printDocumentStructure(child);
    }
  }
}

class DOMErrorHandler implements ErrorHandler {
  private PrintWriter out;

  DOMErrorHandler(PrintWriter out) {
    this.out = out;
  }

  private String getParseExceptionInfo(SAXParseException spe) {
    String systemId = spe.getSystemId();
    if (systemId == null) {
      systemId = "null";
    }

    String info = "URI=" + systemId + " Line=" + spe.getLineNumber() +
            ": " + spe.getMessage();
    return info;
  }

  public void warning(SAXParseException spe) throws SAXException {
    out.println("Warning: " + getParseExceptionInfo(spe));
  }

  public void error(SAXParseException spe) throws SAXException {
    String message = "Error: " + getParseExceptionInfo(spe);
    throw new SAXException(message);
  }

  public void fatalError(SAXParseException spe) throws SAXException {
    String message = "Fatal Error: " + getParseExceptionInfo(spe);
    throw new SAXException(message);
  }
}
