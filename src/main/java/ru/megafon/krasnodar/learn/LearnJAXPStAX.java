/*
* This class developed in training purposes only to understood JAXP StAX technology
* in action
*
* Created by Evgeny Dobrokvashin (aka Stalker) on 10.04.15.
*
* Copyright (c) 2015 MegaFon, All Rights Reserved.
*
* */
package ru.megafon.krasnodar.learn;

import com.sun.xml.internal.stream.events.XMLEventAllocatorImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.XMLEventAllocator;
import java.io.StringReader;
import java.util.Calendar;

/**
 * LearnJAXPStAX class provided access to learn Java XML technology (JAXP StAX) methods
 * developed in training purposes
 *
 * @version 1.0 Apr 2015
 * @author Evgeny Dobrokvashin
 *
 * Created by Stalker on 10.04.15.
 */
public class LearnJAXPStAX implements StreamFilter {
  private static final Logger LOG = LoggerFactory.getLogger(LearnJAXPStAX.class.getName());

  private XMLEventAllocator allocator = null;

  XMLEventFactory xmlEventFactory = XMLEventFactory.newInstance();

  public void readXMLAsCursor(String xmlData) throws Exception {
    XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
    long startTime = System.currentTimeMillis() ;

    try {
      XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new StringReader(xmlData));
      int eventType = xmlStreamReader.getEventType();
      printEventType(eventType);
      printStartDocument(xmlStreamReader);

      while(xmlStreamReader.hasNext()) {
        eventType = xmlStreamReader.next();
        printEventType(eventType);

        printStartElement(xmlStreamReader);
        printEndElement(xmlStreamReader);
        printText(xmlStreamReader);
        printPIData(xmlStreamReader);
        printComment(xmlStreamReader);
      }
    }
    catch (XMLStreamException e) {
      System.out.println(e.getMessage());
      if (e.getNestedException() != null) {
        e.getNestedException().printStackTrace();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    long endTime = System.currentTimeMillis();
    System.out.println("\nParsing Time = " + (endTime - startTime) );
  }

  private void printEventType(int eventType) {
    System.out.print("Event type(" + eventType + "): " + getEventType(eventType) + " ");
  }

  private void printStartDocument(XMLStreamReader xmlStreamReader) {
    if (xmlStreamReader.getEventType() == XMLEvent.START_DOCUMENT) {
      System.out.println("<?xml version=\"" + xmlStreamReader.getVersion() + "\"" + " encoding=\"" + xmlStreamReader.getCharacterEncodingScheme() + "\"" + "?>");
    }
  }

  private void printStartElement(XMLStreamReader xmlStreamReader) {
    if (xmlStreamReader.isStartElement()) {
      System.out.print("<" + xmlStreamReader.getName().toString());
      printAttributes(xmlStreamReader);
      System.out.println(">");
    }
  }

  private void printAttributes(XMLStreamReader xmlStreamReader) {
    int count = xmlStreamReader.getAttributeCount() ;
    if (count > 0){
      for(int i = 0 ; i < count ; i++) {
        System.out.print(" ");
        System.out.print(xmlStreamReader.getAttributeName(i).toString());
        System.out.print("=");
        System.out.print("\"");
        System.out.print(xmlStreamReader.getAttributeValue(i));
        System.out.print("\"");
      }
    }

    count = xmlStreamReader.getNamespaceCount();
    if(count > 0){
      for(int i = 0 ; i < count ; i++) {
        System.out.print(" ");
        System.out.print("xmlns");
        if(xmlStreamReader.getNamespacePrefix(i) != null ){
          System.out.print(":" + xmlStreamReader.getNamespacePrefix(i));
        }
        System.out.print("=");
        System.out.print("\"");
        System.out.print(xmlStreamReader.getNamespaceURI(i));
        System.out.print("\"");
      }
    }
  }

  private static void printComment(XMLStreamReader xmlStreamReader){
    if(xmlStreamReader.getEventType() == XMLEvent.COMMENT){
      System.out.print("<!--" + xmlStreamReader.getText() + "-->");
    }
  }

  private static void printPIData(XMLStreamReader xmlStreamReader){
    if (xmlStreamReader.getEventType() == XMLEvent.PROCESSING_INSTRUCTION){
      System.out.print("<?" + xmlStreamReader.getPITarget() + " " + xmlStreamReader.getPIData() + "?>") ;
    }
  }

  private void printEndElement(XMLStreamReader xmlStreamReader) {
    if (xmlStreamReader.isEndElement()) {
      System.out.println("</" + xmlStreamReader.getName().toString() + ">");
    }
  }

  private void printText(XMLStreamReader xmlStreamReader) {
    if (xmlStreamReader.hasText()) {
      System.out.println(xmlStreamReader.getText());
    }
  }

  private String getEventType(int eventType) {
    switch (eventType) {
      case XMLEvent.START_ELEMENT:
        return "START_ELEMENT";

      case XMLEvent.END_ELEMENT:
        return "END_ELEMENT";

      case XMLEvent.PROCESSING_INSTRUCTION:
        return "PROCESSING_INSTRUCTION";

      case XMLEvent.CHARACTERS:
        return "CHARACTERS";

      case XMLEvent.COMMENT:
        return "COMMENT";

      case XMLEvent.START_DOCUMENT:
        return "START_DOCUMENT";

      case XMLEvent.END_DOCUMENT:
        return "END_DOCUMENT";

      case XMLEvent.ENTITY_REFERENCE:
        return "ENTITY_REFERENCE";

      case XMLEvent.ATTRIBUTE:
        return "ATTRIBUTE";

      case XMLEvent.DTD:
        return "DTD";

      case XMLEvent.CDATA:
        return "CDATA";

      case XMLEvent.SPACE:
        return "SPACE";
    }

    return "UNKNOWN_EVENT_TYPE , " + eventType;
  }

  public void readXMLAsCursor2Event(String xmlData) throws Exception {
    try {
      XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
      System.out.println("FACTORY: " + xmlInputFactory);

      xmlInputFactory.setEventAllocator(new XMLEventAllocatorImpl());
      allocator = xmlInputFactory.getEventAllocator();

      XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new StringReader(xmlData));

      int eventType = xmlStreamReader.getEventType();
      while(xmlStreamReader.hasNext()) {
        eventType = xmlStreamReader.next();

        if (eventType == XMLStreamConstants.START_ELEMENT &&
            xmlStreamReader.getLocalName().equals("Test")) {
          StartElement event = getXMLEvent(xmlStreamReader).asStartElement();
          System.out.println("EVENT: " + event.toString());
        }
      }
    }
    catch (XMLStreamException e) {
      e.printStackTrace();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private XMLEvent getXMLEvent(XMLStreamReader reader) throws XMLStreamException {
    return allocator.allocate(reader);
  }

  public void readXMLAsEvent(String xmlData) throws Exception {
    XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
    System.out.println("FACTORY: " + xmlInputFactory);

    long startTime = System.currentTimeMillis() ;
    XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new StringReader(xmlData));

    while(xmlEventReader.hasNext()) {
      XMLEvent event = xmlEventReader.nextEvent();
      System.out.println(event.toString());
    }

    long endTime = System.currentTimeMillis();
    System.out.println("\nParsing Time = " + (endTime - startTime) );
  }

  public void readXMLAsFilteredCursor(String xmlData) throws Exception {
    XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

    System.out.println("FACTORY: " + xmlInputFactory);

    try {
      XMLStreamReader xmlStreamReader = xmlInputFactory.createFilteredReader(xmlInputFactory.createXMLStreamReader(new StringReader(xmlData)), this);

      int eventType = xmlStreamReader.getEventType();
      printEventType(eventType);
      while(xmlStreamReader.hasNext()) {
        eventType = xmlStreamReader.next();
        printEventType(eventType);
        printName(xmlStreamReader, eventType);
        printText1(xmlStreamReader);
        if (xmlStreamReader.isStartElement()) {
          printAttributes1(xmlStreamReader);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void printName(XMLStreamReader xmlr,int eventType) {
    if(xmlr.hasName()){
      System.out.println("HAS NAME: " + xmlr.getLocalName());
    } else {
      System.out.println("HAS NO NAME");
    }
  }

  private static void printText1(XMLStreamReader xmlr) {
    if(xmlr.hasText()){
      System.out.println("HAS TEXT: " + xmlr.getText());
    } else {
      System.out.println("HAS NO TEXT");
    }
  }

  private static void printAttributes1(XMLStreamReader xmlr) {
    if(xmlr.getAttributeCount() > 0){
      System.out.println("\nHAS ATTRIBUTES: ");
      int count = xmlr.getAttributeCount() ;
      for(int i = 0 ; i < count ; i++) {

        QName name = xmlr.getAttributeName(i) ;
        String namespace = xmlr.getAttributeNamespace(i) ;
        String  type = xmlr.getAttributeType(i) ;
        String prefix = xmlr.getAttributePrefix(i) ;
        String value = xmlr.getAttributeValue(i) ;

        System.out.println("ATTRIBUTE-PREFIX: " + prefix );
        System.out.println("ATTRIBUTE-NAMESP: " + namespace );
        System.out.println("ATTRIBUTE-NAME:   " + name.toString() );
        System.out.println("ATTRIBUTE-VALUE:  " + value );
        System.out.println("ATTRIBUTE-TYPE:  " + type );
        System.out.println();

      }

    } else {
      System.out.println("HAS NO ATTRIBUTES");
    }
  }

  public boolean accept(XMLStreamReader reader) {
    if (!reader.isStartElement() &&
        !reader.isEndElement()) {
      return false;
    }
    else {
      return true;
    }
  }

  public void readwriteXMLAsEvent(String xmlData) throws Exception {
    try {
      XMLEventReader xmlEventReader = XMLInputFactory.newInstance().createXMLEventReader(new StringReader(xmlData));
      XMLEventWriter xmlEventWriter = XMLOutputFactory.newInstance().createXMLEventWriter(System.out);

      while (xmlEventReader.hasNext()) {
        XMLEvent event = (XMLEvent) xmlEventReader.next();
        if (event.getEventType() == XMLEvent.CHARACTERS) {
          xmlEventWriter.add(getNewCharactersEvent(event.asCharacters()));
        }
        else {
          xmlEventWriter.add(event);
        }
      }

      xmlEventWriter.flush();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  Characters getNewCharactersEvent(Characters event) {
    if (event.getData().equalsIgnoreCase("    ")) {
      return xmlEventFactory.createCharacters(Calendar.getInstance().getTime().toString());
    }
    else {
      return event;
    }
  }

  public void writeXMLAsStream() throws Exception {
    XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();

    XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(System.out, "UTF-8");

    xmlStreamWriter.writeComment("This is comment!");
    xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
    xmlStreamWriter.writeStartElement("Tests");

    xmlStreamWriter.writeStartElement("Test");
    xmlStreamWriter.writeAttribute("TestId", "10");
    xmlStreamWriter.writeCharacters("Absurd");
    xmlStreamWriter.writeEndElement();

    xmlStreamWriter.writeEndElement();
    xmlStreamWriter.writeEndDocument();

    xmlStreamWriter.flush();
    xmlStreamWriter.close();
  }

  public AppConfigData getAppConfigDataCursor(String configXMLData) throws Exception {
    //throw new UnsupportedOperationException();

    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to getAppConfigDataCursor(" + configXMLData + ")");
    }

    try {
      XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
      XMLStreamReader xmlReader = xmlInputFactory.createXMLStreamReader(new StringReader(configXMLData));
      AppConfigXMLCursorProcessing appConfigXMLCursorProcessing = new AppConfigXMLCursorProcessing();

      while (xmlReader.hasNext()) {
        xmlReader.next();
        appConfigXMLCursorProcessing.process(xmlReader);
      }

      return appConfigXMLCursorProcessing.getAppConfigData();
    }
    finally {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exit from getAppConfigDataCursor(" + configXMLData + ")");
      }
    }
  }

  public AppConfigData getAppConfigDataEvent(String configXMLData) throws Exception {
    //throw new UnsupportedOperationException();

    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to getAppConfigDataEvent(" + configXMLData + ")");
    }

    try {
      XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
      XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new StringReader(configXMLData));

      XMLEvent xmlEvent = null;
      AppConfigXMLEventProcessing appConfigXMLEventProcessing = new AppConfigXMLEventProcessing();
      while (xmlEventReader.hasNext()) {
        xmlEvent = xmlEventReader.nextEvent();
        appConfigXMLEventProcessing.process(xmlEvent);
      }

      return appConfigXMLEventProcessing.getAppConfigData();
    } finally {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exit from getAppConfigDataEvent(" + configXMLData + ")");
      }
    }
  }
}

class AppConfigXMLCursorProcessing {
  private static final Logger LOG = LoggerFactory.getLogger(AppConfigXMLCursorProcessing.class.getName());

  private int currentNodeLevel = -1;

  private AppConfigData appConfigData = null;

  private XMLStreamReader xmlReader = null;

  public void process(XMLStreamReader xmlReader) throws XMLStreamException {
    if (null == xmlReader) {
      throw new NullPointerException("xmlReader can't be null!");
    }

    this.xmlReader = xmlReader;

    if (xmlReader.isStartElement()) {
      processStartElement();
    }
    else if (xmlReader.isEndElement()) {
      currentNodeLevel--;
    }
  }

  public AppConfigData getAppConfigData() {
    return appConfigData;
  }

  private void processStartElement() throws XMLStreamException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to processStartElement()");
    }

    currentNodeLevel++;

    AppConfigNodeName nodeName = AppConfigNodeName.getNodeName(xmlReader.getName().toString());
    switch (nodeName) {
      case CONFIGURATION_ROOT:
        configRootProcessing();
        break;
      case CONNECTION:
        connectionNodeProcessing();
        break;
      case MAIL:
        mailNodeProcessing();
        break;
      case PATHS:
        pathsNodeProcessing();
        break;
      case MAIN:
        mainNodeProcessing();
        break;
      case SERVICE:
        serviceNodeProcessing();
        break;
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Exit from processStartElement()");
    }
  }

  private void serviceNodeProcessing() throws XMLStreamException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to serviceNodeProcessing()");
    }

    if (currentNodeLevel == 0) {
      throw new XMLStreamException("Root node invalid! Expected: " + AppConfigNodeName.CONFIGURATION_ROOT.getNodeName() + ", Actual: " + AppConfigNodeName.SERVICE.getNodeName());
    }

    if (xmlReader.getAttributeCount() > 0) {
      String administratorPhone = getAttributeValue(ServiceConfigItem.ATTR_NAME_ADMINISTRATORPHONE, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.SERVICE, ServiceConfigItem.ATTR_NAME_ADMINISTRATORPHONE));

      String administratorMail = getAttributeValue(ServiceConfigItem.ATTR_NAME_ADMINISTRATORMAIL, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.SERVICE, ServiceConfigItem.ATTR_NAME_ADMINISTRATORMAIL));;

      ServiceConfigItem serviceConfigItem = new ServiceConfigItem(administratorPhone, administratorMail);
      appConfigData.setServiceConfigItem(serviceConfigItem);
    }
    else {
      throw new XMLStreamException("Attributes missing!");
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Exit from serviceNodeProcessing()");
    }
  }

  private void mainNodeProcessing() throws XMLStreamException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to mainNodeProcessing()");
    }

    if (currentNodeLevel == 0) {
      throw new XMLStreamException("Root node invalid! Expected: " + AppConfigNodeName.CONFIGURATION_ROOT.getNodeName() + ", Actual: " + AppConfigNodeName.MAIN.getNodeName());
    }

    if (xmlReader.getAttributeCount() > 0) {
      String refusalBuyerCount = getAttributeValue(MainConfigItem.ATTR_NAME_REFUSALBUYERCOUNT, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIN, MainConfigItem.ATTR_NAME_REFUSALBUYERCOUNT));

      String waitingTimeConclusionAgreement = getAttributeValue(MainConfigItem.ATTR_NAME_WAITINGTIMECONCLUSIONAGREEMENT, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIN, MainConfigItem.ATTR_NAME_WAITINGTIMECONCLUSIONAGREEMENT));

      String agreementLink = getAttributeValue(MainConfigItem.ATTR_NAME_AGREEMENTLINK, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIN, MainConfigItem.ATTR_NAME_AGREEMENTLINK));

      String ldapUserName = getAttributeValue(MainConfigItem.ATTR_NAME_LDAPUSERNAME, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIN, MainConfigItem.ATTR_NAME_LDAPUSERNAME));

      String ldapUserPwd = getAttributeValue(MainConfigItem.ATTR_NAME_LDAPUSERPWD, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIN, MainConfigItem.ATTR_NAME_LDAPUSERPWD));

      MainConfigItem mainConfigItem = new MainConfigItem(refusalBuyerCount, waitingTimeConclusionAgreement, agreementLink, ldapUserName, ldapUserPwd);
      appConfigData.setMainConfigItem(mainConfigItem);
    }
    else {
      throw new XMLStreamException("Attributes missing!");
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Exit from mainNodeProcessing()");
    }
  }

  private void pathsNodeProcessing() throws XMLStreamException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to pathsNodeProcessing()");
    }

    if (currentNodeLevel == 0) {
      throw new XMLStreamException("Root node invalid! Expected: " + AppConfigNodeName.CONFIGURATION_ROOT.getNodeName() + ", Actual: " + AppConfigNodeName.PATHS.getNodeName());
    }

    if (xmlReader.getAttributeCount() > 0) {
      String messageTemplates = getAttributeValue(PathsConfigItem.ATTR_NAME_MESSAGETEMPLATES, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.PATHS, PathsConfigItem.ATTR_NAME_MESSAGETEMPLATES));

      PathsConfigItem pathsConfigItem = new PathsConfigItem(messageTemplates);
      appConfigData.setPathsConfigItem(pathsConfigItem);
    }
    else {
      throw new XMLStreamException("Attributes missing!");
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Exit from pathsNodeProcessing()");
    }
  }

  private void mailNodeProcessing() throws XMLStreamException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to mailNodeProcessing()");
    }

    if (currentNodeLevel == 0) {
      throw new XMLStreamException("Root node invalid! Expected: " + AppConfigNodeName.CONFIGURATION_ROOT.getNodeName() + ", Actual: " + AppConfigNodeName.MAIL.getNodeName());
    }

    if (xmlReader.getAttributeCount() > 0) {
      String smtpServer = getAttributeValue(MailConfigItem.ATTR_NAME_SMTPSERVER, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_SMTPSERVER));

      String systemMailFromName = getAttributeValue(MailConfigItem.ATTR_NAME_SYSTEMMAILFROMNAME, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_SYSTEMMAILFROMNAME));

      String messageBodyEncoding = getAttributeValue(MailConfigItem.ATTR_NAME_MESSAGEBODYENCODING, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_MESSAGEBODYENCODING));

      String systemMail = getAttributeValue(MailConfigItem.ATTR_NAME_SYSTEMMAIL, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_SYSTEMMAIL));

      String systemMailUserName = getAttributeValue(MailConfigItem.ATTR_NAME_SYSTEMMAILUSERNAME, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_SYSTEMMAILUSERNAME));

      String systemMailUserPassword = getAttributeValue(MailConfigItem.ATTR_NAME_SYSTEMMAILUSERPASWORD, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_SYSTEMMAILUSERPASWORD));

      MailConfigItem mailConfigItem = new MailConfigItem(smtpServer, systemMailFromName, messageBodyEncoding, systemMail, systemMailUserName, systemMailUserPassword);
      appConfigData.setMailConfigItem(mailConfigItem);
    }
    else {
      throw new XMLStreamException("Attributes missing!");
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Exit from mailNodeProcessing()");
    }
  }

  private void connectionNodeProcessing() throws XMLStreamException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to connectionNodeProcessing()");
    }

    try {
      if (currentNodeLevel == 0) {
        throw new XMLStreamException("Root node invalid! Expected: " + AppConfigNodeName.CONFIGURATION_ROOT.getNodeName() + ", Actual: " + AppConfigNodeName.CONNECTION.getNodeName());
      }

      if (xmlReader.getAttributeCount() > 0) {
        String attrValue = xmlReader.getAttributeValue(null, ConnectionConfigItem.ATTR_NAME_SERVER);
        if (null == attrValue) {
          throw new XMLStreamException(String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.CONNECTION, ConnectionConfigItem.ATTR_NAME_SERVER));
        }

        ConnectionConfigItem connectionConfigItem = new ConnectionConfigItem(attrValue);
        appConfigData.setConnectionConfigItem(connectionConfigItem);
      }
      else {
        throw new XMLStreamException("Attributes missing!");
      }
    }
    finally {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exit from connectionNodeProcessing()");
      }
    }
  }

  private void configRootProcessing() throws XMLStreamException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to configRootProcessing()");
    }

    try {
      if (currentNodeLevel == 0) {
        appConfigData = new AppConfigData();
      }
      else {
        throw new XMLStreamException("Node " + AppConfigNodeName.CONFIGURATION_ROOT.getNodeName() + " invalid location!");
      }
    }
    finally {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exit from configRootProcessing()");
      }
    }
  }

  private String getAttributeValue(String attrName, String exceptionMsg) throws XMLStreamException {
    if (null == attrName) {
      throw new IllegalArgumentException("attrName can't be null!");
    }

    if (null == exceptionMsg) {
      throw new IllegalArgumentException("exceptionMsg can't be null!");
    }

    String attrValue = xmlReader.getAttributeValue(null, attrName);
    if (null == attrValue) {
      throw new XMLStreamException(exceptionMsg);
    }

    return attrValue;
  }
}

class AppConfigXMLEventProcessing {
  private static final Logger LOG = LoggerFactory.getLogger(AppConfigXMLEventProcessing.class.getName());

  private int currentNodeLevel = -1;

  private AppConfigData appConfigData = null;

  private XMLEvent xmlEvent = null;

  private StartElement startElement = null;

  public void process(XMLEvent xmlEvent) throws XMLStreamException {
    if (null == xmlEvent) {
      throw new NullPointerException("xmlEvent can't be null!");
    }

    this.xmlEvent = xmlEvent;

    if (xmlEvent.isStartElement()) {
      processStartElement();
    }
    else if (xmlEvent.isEndElement()) {
      currentNodeLevel--;
    }
  }

  public AppConfigData getAppConfigData() {
    return appConfigData;
  }

  private void processStartElement() throws XMLStreamException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to processStartElement()");
    }

    currentNodeLevel++;

    startElement = xmlEvent.asStartElement();
    AppConfigNodeName nodeName = AppConfigNodeName.getNodeName(startElement.getName().toString());
    switch (nodeName) {
      case CONFIGURATION_ROOT:
        configRootProcessing();
        break;
      case CONNECTION:
        connectionNodeProcessing();
        break;
      case MAIL:
        mailNodeProcessing();
        break;
      case PATHS:
        pathsNodeProcessing();
        break;
      case MAIN:
        mainNodeProcessing();
        break;
      case SERVICE:
        serviceNodeProcessing();
        break;
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Exit from processStartElement()");
    }
  }

  private void serviceNodeProcessing() throws XMLStreamException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to serviceNodeProcessing()");
    }

    if (currentNodeLevel == 0) {
      throw new XMLStreamException("Root node invalid! Expected: " + AppConfigNodeName.CONFIGURATION_ROOT.getNodeName() + ", Actual: " + AppConfigNodeName.SERVICE.getNodeName());
    }

    if (startElement.getAttributes().hasNext()) {
      String administratorPhone = getAttributeValue(ServiceConfigItem.ATTR_NAME_ADMINISTRATORPHONE, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.SERVICE, ServiceConfigItem.ATTR_NAME_ADMINISTRATORPHONE));

      String administratorMail = getAttributeValue(ServiceConfigItem.ATTR_NAME_ADMINISTRATORMAIL, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.SERVICE, ServiceConfigItem.ATTR_NAME_ADMINISTRATORMAIL));;

      ServiceConfigItem serviceConfigItem = new ServiceConfigItem(administratorPhone, administratorMail);
      appConfigData.setServiceConfigItem(serviceConfigItem);
    }
    else {
      throw new XMLStreamException("Attributes missing!");
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Exit from serviceNodeProcessing()");
    }
  }

  private void mainNodeProcessing() throws XMLStreamException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to mainNodeProcessing()");
    }

    if (currentNodeLevel == 0) {
      throw new XMLStreamException("Root node invalid! Expected: " + AppConfigNodeName.CONFIGURATION_ROOT.getNodeName() + ", Actual: " + AppConfigNodeName.MAIN.getNodeName());
    }

    if (startElement.getAttributes().hasNext()) {
      String refusalBuyerCount = getAttributeValue(MainConfigItem.ATTR_NAME_REFUSALBUYERCOUNT, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIN, MainConfigItem.ATTR_NAME_REFUSALBUYERCOUNT));

      String waitingTimeConclusionAgreement = getAttributeValue(MainConfigItem.ATTR_NAME_WAITINGTIMECONCLUSIONAGREEMENT, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIN, MainConfigItem.ATTR_NAME_WAITINGTIMECONCLUSIONAGREEMENT));

      String agreementLink = getAttributeValue(MainConfigItem.ATTR_NAME_AGREEMENTLINK, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIN, MainConfigItem.ATTR_NAME_AGREEMENTLINK));

      String ldapUserName = getAttributeValue(MainConfigItem.ATTR_NAME_LDAPUSERNAME, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIN, MainConfigItem.ATTR_NAME_LDAPUSERNAME));

      String ldapUserPwd = getAttributeValue(MainConfigItem.ATTR_NAME_LDAPUSERPWD, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIN, MainConfigItem.ATTR_NAME_LDAPUSERPWD));

      MainConfigItem mainConfigItem = new MainConfigItem(refusalBuyerCount, waitingTimeConclusionAgreement, agreementLink, ldapUserName, ldapUserPwd);
      appConfigData.setMainConfigItem(mainConfigItem);
    }
    else {
      throw new XMLStreamException("Attributes missing!");
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Exit from mainNodeProcessing()");
    }
  }

  private void pathsNodeProcessing() throws XMLStreamException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to pathsNodeProcessing()");
    }

    if (currentNodeLevel == 0) {
      throw new XMLStreamException("Root node invalid! Expected: " + AppConfigNodeName.CONFIGURATION_ROOT.getNodeName() + ", Actual: " + AppConfigNodeName.PATHS.getNodeName());
    }

    if (startElement.getAttributes().hasNext()) {
      String messageTemplates = getAttributeValue(PathsConfigItem.ATTR_NAME_MESSAGETEMPLATES, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.PATHS, PathsConfigItem.ATTR_NAME_MESSAGETEMPLATES));

      PathsConfigItem pathsConfigItem = new PathsConfigItem(messageTemplates);
      appConfigData.setPathsConfigItem(pathsConfigItem);
    }
    else {
      throw new XMLStreamException("Attributes missing!");
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Exit from pathsNodeProcessing()");
    }
  }

  private void mailNodeProcessing() throws XMLStreamException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to mailNodeProcessing()");
    }

    if (currentNodeLevel == 0) {
      throw new XMLStreamException("Root node invalid! Expected: " + AppConfigNodeName.CONFIGURATION_ROOT.getNodeName() + ", Actual: " + AppConfigNodeName.MAIL.getNodeName());
    }

    if (startElement.getAttributes().hasNext()) {
      String smtpServer = getAttributeValue(MailConfigItem.ATTR_NAME_SMTPSERVER, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_SMTPSERVER));

      String systemMailFromName = getAttributeValue(MailConfigItem.ATTR_NAME_SYSTEMMAILFROMNAME, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_SYSTEMMAILFROMNAME));

      String messageBodyEncoding = getAttributeValue(MailConfigItem.ATTR_NAME_MESSAGEBODYENCODING, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_MESSAGEBODYENCODING));

      String systemMail = getAttributeValue(MailConfigItem.ATTR_NAME_SYSTEMMAIL, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_SYSTEMMAIL));

      String systemMailUserName = getAttributeValue(MailConfigItem.ATTR_NAME_SYSTEMMAILUSERNAME, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_SYSTEMMAILUSERNAME));

      String systemMailUserPassword = getAttributeValue(MailConfigItem.ATTR_NAME_SYSTEMMAILUSERPASWORD, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.MAIL, MailConfigItem.ATTR_NAME_SYSTEMMAILUSERPASWORD));

      MailConfigItem mailConfigItem = new MailConfigItem(smtpServer, systemMailFromName, messageBodyEncoding, systemMail, systemMailUserName, systemMailUserPassword);
      appConfigData.setMailConfigItem(mailConfigItem);
    }
    else {
      throw new XMLStreamException("Attributes missing!");
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Exit from mailNodeProcessing()");
    }
  }

  private void connectionNodeProcessing() throws XMLStreamException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to connectionNodeProcessing()");
    }

    try {
      if (currentNodeLevel == 0) {
        throw new XMLStreamException("Root node invalid! Expected: " + AppConfigNodeName.CONFIGURATION_ROOT.getNodeName() + ", Actual: " + AppConfigNodeName.CONNECTION.getNodeName());
      }

      if (startElement.getAttributes().hasNext()) {
        String attrValue = getAttributeValue(ConnectionConfigItem.ATTR_NAME_SERVER, String.format("Attribute '%2$s' in '%1$s' node missing!", AppConfigNodeName.CONNECTION, ConnectionConfigItem.ATTR_NAME_SERVER));

        ConnectionConfigItem connectionConfigItem = new ConnectionConfigItem(attrValue);
        appConfigData.setConnectionConfigItem(connectionConfigItem);
      }
    }
    finally {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exit from connectionNodeProcessing()");
      }
    }
  }

  private void configRootProcessing() throws XMLStreamException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to configRootProcessing()");
    }

    try {
      if (currentNodeLevel == 0) {
        appConfigData = new AppConfigData();
      }
      else {
        throw new XMLStreamException("Node " + AppConfigNodeName.CONFIGURATION_ROOT.getNodeName() + " invalid location!");
      }
    }
    finally {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exit from configRootProcessing()");
      }
    }
  }

  private String getAttributeValue(String attrName, String exceptionMsg) throws XMLStreamException {
    if (null == attrName) {
      throw new IllegalArgumentException("attrName can't be null!");
    }

    if (null == exceptionMsg) {
      throw new IllegalArgumentException("exceptionMsg can't be null!");
    }

    Attribute attr = startElement.getAttributeByName(new QName(null, attrName));
    if (null == attr) {
      throw new XMLStreamException(exceptionMsg);
    }

    return attr.getValue();
  }
}