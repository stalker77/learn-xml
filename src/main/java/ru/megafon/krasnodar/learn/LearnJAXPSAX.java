package ru.megafon.krasnodar.learn;

import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;

class SAXBreakParsingException extends Exception {
}

/**
 * Created by Stalker on 31.03.15.
 */
public class LearnJAXPSAX {
  private static final Logger LOG = LoggerFactory.getLogger(GetAppConfigSAXHandler.class.getName());

  public void readXML(String xmlData) throws Exception {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser saxParser = factory.newSAXParser();
    XMLReader xmlReader = saxParser.getXMLReader();
    xmlReader.setContentHandler(new SAXHandler());
    xmlReader.parse(new InputSource(new StringReader(xmlData)));
  }

  public String getValueFromXMLAsString(String xmlSource, String fullPathToAttrNode, String attrName) throws Exception {
    String result = "";

    fullPathToAttrNode = fullPathToAttrNode.substring(1);
    String[] nodes = fullPathToAttrNode.toUpperCase().split("/");
    if (nodes.length > 0) {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      XMLReader xmlReader = saxParser.getXMLReader();
      GetValueSAXHandler getValueSAXHandler = new GetValueSAXHandler(nodes, attrName);
      xmlReader.setContentHandler(getValueSAXHandler);
      xmlReader.setErrorHandler(new SAXErrorHandler(System.err));
      try{
       xmlReader.parse(new InputSource(new StringReader(xmlSource)));
      }
      catch (SAXException e) {
        if (e.getCause() instanceof SAXBreakParsingException) {
          //it's normal, we intentionally break the parse then got the result before the XML document end
        }
        else{
          throw e;
        }
      }

      result = getValueSAXHandler.getAttrValue();
    }

    return result;
  }

  public AppConfigData getAppConfigData(String configXMLData) throws Exception {
    //throw new UnsupportedOperationException();
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to getAppConfigData(" + configXMLData + ")");
    }

    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      XMLReader xmlReader = saxParser.getXMLReader();
      GetAppConfigSAXHandler getAppConfigSAXHandler = new GetAppConfigSAXHandler();
      xmlReader.setContentHandler(getAppConfigSAXHandler);
      xmlReader.parse(new InputSource(new StringReader(configXMLData)));

      return getAppConfigSAXHandler.getAppConfigData();
    }
    finally {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exit from getAppConfigData(" + configXMLData + ")");
      }
    }
  }
}

class GetAppConfigSAXHandler extends DefaultHandler {
  private static final Logger LOG = LoggerFactory.getLogger(GetAppConfigSAXHandler.class.getName());

  private int currentNodeLevel = -1;

  private AppConfigData appConfigData;

  public AppConfigData getAppConfigData() {
    return appConfigData;
  }

  public void startElement(String namespaceURI,
                           String localName,
                           String qName,
                           Attributes atts) throws SAXException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to startElement(,," + qName + ",)");
    }

    currentNodeLevel++;
    if (LOG.isDebugEnabled()) {
      LOG.debug("currentNodeLevel=", currentNodeLevel);
    }

    AppConfigNodeName nodeName = AppConfigNodeName.getNodeName(qName);
    switch (nodeName) {
      case CONFIGURATION_ROOT:
        configRootProcessing(qName);
        break;
      case CONNECTION:
        connectionNodeProcessing(qName, atts);
        break;
      case MAIL:
        mailNodeProcessing(qName, atts);
        break;
      case PATHS:
        pathsNodeProcessing(qName, atts);
        break;
      case MAIN:
        mainNodeProcessing(qName, atts);
        break;
      case SERVICE:
        serviceNodeProcessing(qName, atts);
        break;
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Exit from startElement(,," + qName + ",)");
    }
  }

  private void serviceNodeProcessing(String qName, Attributes atts) throws SAXException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to serviceNodeProcessing(" + qName + ")");
    }

    if (currentNodeLevel == 0) {
      throw new SAXException("Root node invalid! Expected: " + AppConfigNodeName.CONFIGURATION_ROOT.getNodeName() + ", Actual: " + qName);
    }

    if (atts != null) {
      String administratorPhone = getAttributeValue(atts, ServiceConfigItem.ATTR_NAME_ADMINISTRATORPHONE, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.SERVICE, ServiceConfigItem.ATTR_NAME_ADMINISTRATORPHONE));

      String administratorMail = getAttributeValue(atts, ServiceConfigItem.ATTR_NAME_ADMINISTRATORMAIL, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.SERVICE, ServiceConfigItem.ATTR_NAME_ADMINISTRATORMAIL));;

      ServiceConfigItem serviceConfigItem = new ServiceConfigItem(administratorPhone, administratorMail);
      appConfigData.setServiceConfigItem(serviceConfigItem);
    }
    else {
      throw new SAXException("Attributes missing!");
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Exit from serviceNodeProcessing(" + qName + ")");
    }
  }

  private void mainNodeProcessing(String qName, Attributes atts) throws SAXException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to mainNodeProcessing(" + qName + ")");
    }

    if (currentNodeLevel == 0) {
      throw new SAXException("Root node invalid! Expected: " + AppConfigNodeName.CONFIGURATION_ROOT.getNodeName() + ", Actual: " + qName);
    }

    if (atts != null) {
      String refusalBuyerCount = getAttributeValue(atts, MainConfigItem.ATTR_NAME_REFUSALBUYERCOUNT, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIN, MainConfigItem.ATTR_NAME_REFUSALBUYERCOUNT));

      String waitingTimeConclusionAgreement = getAttributeValue(atts, MainConfigItem.ATTR_NAME_WAITINGTIMECONCLUSIONAGREEMENT, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIN, MainConfigItem.ATTR_NAME_WAITINGTIMECONCLUSIONAGREEMENT));

      String agreementLink = getAttributeValue(atts, MainConfigItem.ATTR_NAME_AGREEMENTLINK, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIN, MainConfigItem.ATTR_NAME_AGREEMENTLINK));

      String ldapUserName = getAttributeValue(atts, MainConfigItem.ATTR_NAME_LDAPUSERNAME, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIN, MainConfigItem.ATTR_NAME_LDAPUSERNAME));

      String ldapUserPwd = getAttributeValue(atts, MainConfigItem.ATTR_NAME_LDAPUSERPWD, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIN, MainConfigItem.ATTR_NAME_LDAPUSERPWD));

      MainConfigItem mainConfigItem = new MainConfigItem(refusalBuyerCount, waitingTimeConclusionAgreement, agreementLink, ldapUserName, ldapUserPwd);
      appConfigData.setMainConfigItem(mainConfigItem);
    }
    else {
      throw new SAXException("Attributes missing!");
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Exit from mainNodeProcessing(" + qName + ")");
    }
  }

  private void pathsNodeProcessing(String qName, Attributes atts) throws SAXException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to pathsNodeProcessing(" + qName + ")");
    }

    if (currentNodeLevel == 0) {
      throw new SAXException("Root node invalid! Expected: " + AppConfigNodeName.CONFIGURATION_ROOT.getNodeName() + ", Actual: " + qName);
    }

    if (atts != null) {
      String messageTemplates = getAttributeValue(atts, PathsConfigItem.ATTR_NAME_MESSAGETEMPLATES, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.PATHS, PathsConfigItem.ATTR_NAME_MESSAGETEMPLATES));

      PathsConfigItem pathsConfigItem = new PathsConfigItem(messageTemplates);
      appConfigData.setPathsConfigItem(pathsConfigItem);
    }
    else {
      throw new SAXException("Attributes missing!");
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Exit from pathsNodeProcessing(" + qName + ")");
    }
  }

  private void mailNodeProcessing(String qName, Attributes atts) throws SAXException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to mailNodeProcessing(" + qName + ")");
    }

    if (currentNodeLevel == 0) {
      throw new SAXException("Root node invalid! Expected: " + AppConfigNodeName.CONFIGURATION_ROOT.getNodeName() + ", Actual: " + qName);
    }

    if (atts != null) {
      String smtpServer = getAttributeValue(atts, MailConfigItem.ATTR_NAME_SMTPSERVER, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_SMTPSERVER));

      String systemMailFromName = getAttributeValue(atts, MailConfigItem.ATTR_NAME_SYSTEMMAILFROMNAME, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_SYSTEMMAILFROMNAME));

      String messageBodyEncoding = getAttributeValue(atts, MailConfigItem.ATTR_NAME_MESSAGEBODYENCODING, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_MESSAGEBODYENCODING));

      String systemMail = getAttributeValue(atts, MailConfigItem.ATTR_NAME_SYSTEMMAIL, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_SYSTEMMAIL));

      String systemMailUserName = getAttributeValue(atts, MailConfigItem.ATTR_NAME_SYSTEMMAILUSERNAME, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_SYSTEMMAILUSERNAME));

      String systemMailUserPassword = getAttributeValue(atts, MailConfigItem.ATTR_NAME_SYSTEMMAILUSERPASWORD, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_SYSTEMMAILUSERPASWORD));

      MailConfigItem mailConfigItem = new MailConfigItem(smtpServer, systemMailFromName, messageBodyEncoding, systemMail, systemMailUserName, systemMailUserPassword);
      appConfigData.setMailConfigItem(mailConfigItem);
    }
    else {
      throw new SAXException("Attributes missing!");
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Exit from mailNodeProcessing(" + qName + ")");
    }
  }

  private void connectionNodeProcessing(String qName, Attributes atts) throws SAXException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to connectionNodeProcessing(" + qName + ")");
    }

    if (currentNodeLevel == 0) {
      throw new SAXException("Root node invalid! Expected: " + AppConfigNodeName.CONFIGURATION_ROOT.getNodeName() + ", Actual: " + qName);
    }

    if (atts != null) {
      int attrIndex = atts.getIndex(ConnectionConfigItem.ATTR_NAME_SERVER);
      if (attrIndex < 0) {
        throw new SAXException(String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.CONNECTION, ConnectionConfigItem.ATTR_NAME_SERVER));
      }

      ConnectionConfigItem connectionConfigItem = new ConnectionConfigItem(atts.getValue(attrIndex));
      appConfigData.setConnectionConfigItem(connectionConfigItem);
    }
    else {
      throw new SAXException("Attributes missing!");
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Exit from connectionNodeProcessing(" + qName + ")");
    }
  }

  private void configRootProcessing(String qName) throws SAXException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to configRootProcessing(" + qName + ")");
    }

    if (currentNodeLevel == 0) {
      appConfigData = new AppConfigData();
    }
    else {
      throw new SAXException("Node " + qName + " invalid location!");
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Exit from configRootProcessing(" + qName + ")");
    }
  }

  private String getAttributeValue(Attributes atts, String attrName, String exceptionMsg) throws SAXException {
    int attrIndex = atts.getIndex(attrName);
    if (attrIndex < 0) {
      throw new SAXException(exceptionMsg);
    }

      return atts.getValue(attrIndex);
  }

  public void endElement(String uri, String localName, String qName) throws SAXException {
    currentNodeLevel--;
  }
}

class GetValueSAXHandler extends DefaultHandler {
  private String[] nodesToAttr;

  private String attrName;

  private int currentNodeLevel = -1;

  private String attrValue = "";

  private String priorNodeName = "";

  public GetValueSAXHandler(String[] nodesToAttr, String attrName) {
    this.nodesToAttr = nodesToAttr;

    this.attrName = attrName;
  }

  public String getAttrValue() {
    return attrValue;
  }

  public void startDocument() throws SAXException {
  }

  public void startElement(String namespaceURI,
                           String localName,
                           String qName,
                           Attributes atts) throws SAXException {
    String nodeName = qName.toUpperCase();
    currentNodeLevel++;
    if (currentNodeLevel <= nodesToAttr.length - 1) {
      if (nodesToAttr[currentNodeLevel].equals(nodeName)) {
        if (currentNodeLevel == nodesToAttr.length - 1 &&
            !priorNodeName.equals(nodeName)) {
          if (atts != null) {
            int attrIndex = atts.getIndex(attrName);
            if (attrIndex >= 0) {
              attrValue = atts.getValue(attrIndex);
              throw new SAXException(new SAXBreakParsingException());
            }
            else {
              throw new SAXException(String.format("Attribute %1$s in requested node: %2$s - not exist!", attrName, qName));
            }
          }
          else {
            throw new SAXException("Attributes collection is null!");
          }
        }
      }
      /*else {
        throw new SAXException("fullPathToAttrNode does not match the structure of passed XML!");
      }*/
    }

    priorNodeName = nodeName;
  }

  public void endElement(String uri, String localName, String qName) throws SAXException {
    currentNodeLevel--;
  }


  public void endDocument() throws SAXException {

  }
}

class SAXErrorHandler implements ErrorHandler {
  private PrintStream out;

  SAXErrorHandler(PrintStream out) {
    this.out = out;
  }

  private String getParseExceptionInfo(SAXParseException spe) {
    String systemId = spe.getSystemId();

    if (systemId == null) {
      systemId = "null";
    }

    String info = "URI=" + systemId + " Line="
            + spe.getLineNumber() + ": " + spe.getMessage();

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

class SAXHandler extends DefaultHandler {
  public void startDocument() throws SAXException {
    System.out.println("Start XML document:");
  }

  public void startElement(String namespaceURI,
                           String localName,
                           String qName,
                           Attributes atts) throws SAXException {
    System.out.println("Start element: " + qName);
    if (atts != null) {
      for(int i = 0; i < atts.getLength(); i++) {
        System.out.println(String.format("Attribute: %1$s = %2$s", atts.getQName(i), atts.getValue(i)));
      }
    }
  }

  public void endElement(String uri, String localName, String qName) throws SAXException {
    System.out.println("End element: " + qName);
  }


  public void endDocument() throws SAXException {
    System.out.println("End XML document.");
  }
}
