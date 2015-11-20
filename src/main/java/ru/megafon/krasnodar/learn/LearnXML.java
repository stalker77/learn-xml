package ru.megafon.krasnodar.learn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import java.io.*;

/**
 * Learn working with XML!
 *
 */
public class LearnXML {
  private static final Logger LOG = LoggerFactory.getLogger(LearnXML.class.getName());

  private static  final String TEST_TASK_XML =
          "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
          "<entries>\n" +
          "  <entry>\n" +
          "    <field>1</field>\n" +
          "  </entry>\n" +
          "  <entry>\n" +
          "    <field>2</field>\n" +
          "  </entry>\n" +
          "</entries>";

  private static final String TEST_XSL_1TO2 =
          "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
          "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" +
          "  <xsl:output method=\"xml\" indent=\"yes\"/>\n" +
          "\n" +
          "  <xsl:template match=\"entries\">\n" +
          "    <entries>\n" +
          "      <xsl:apply-templates/>\n" +
          "    </entries>\n" +
          "  </xsl:template>\n" +
          "\n" +
          "  <xsl:template match=\"entry\">\n" +
          "    <entry>\n" +
          "      <xsl:attribute name=\"field\">\n" +
          "        <xsl:value-of select=\"field\"/>\n" +
          "      </xsl:attribute>\n" +
          "    </entry>\n" +
          "  </xsl:template>\n" +
          "</xsl:stylesheet>";

  public static final String TEST_XML_DATA =
          "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                  "<Tests>" +
                  "  <Test TestId=\"10\"/>" +
                  "  <Locals>" +
                  "    <Local TestId=\"20\"/>" +
                  "    <Local TestId=\"30\"/>" +
                  "  </Locals>" +
                  "</Tests>";

  public static final String TEST_XML_DATA_ALL_CONFIG =
          "<?xml version=\"1.0\" encoding=\"utf-16\"?>" +
                  "<Configuration>" +
                  "  <Connection Server=\"KRN-SQL-DB3.megafon.ru\\ARMAGEDDON\" />" +
                  "  <Mail SMTPServer=\"VLG-UMS.megafon.ru\" SystemMailFromName=\"SERVICE_OSSRET\" MessageBodyEncoding=\"koi8-r\" SystemMail=\"KRN-OSSRet-Service@megafon.ru\" SystemMailUserName=\"KRN-OSSRet-Service\" SystemMailUserPasword=\"32dIaJMBMNLfCuVTyYeZ\" />" +
                  "  <Paths MessageTemplates=\"\" />" +
                  "  <Main RefusalBuyerCount=\"1\" WaitingTimeConclusionAgreement=\"1\" AgreementLink=\"http://megaportal/OSSDocuments/ДОГОВОР купли-продажи.docx\" LDAPUserName=\"MEGAFON\\KRN-OSSRet-Service\" LDAPUserPwd=\"32dIaJMBMNLfCuVTyYeZ\" />" +
                  "  <Service AdministratorPhone=\"1134\" AdministratorMail=\"Evgeny.Dobrokvashin@MegaFon.ru\" />" +
                  "</Configuration>";

  public static final String TEST_XML_DATA_ALL_CONFIG_XSD =
          "<?xml version=\"1.0\" encoding=\"utf-16\"?>" +
                  "<xsd:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">" +
                  "<xsd:element name=\"Configuration\" minOccurs=\"1\">" +
                  "  <xsd:complexType>" +
                  "    <xsd:sequence>" +
                  "      <xsd:element name=\"Connection\" minOccurs=\"1\">" +
                  "        <xsd:complexType>" +
                  "          <xsd:attribute name=\"Server\" type=\"xsd:string\"/>" +
                  "        </xsd:complexType>" +
                  "      </xsd:element>" +
                  "      <xsd:element name=\"Mail\" minOccurs=\"1\">" +
                  "        <xsd:complexType>" +
                  "          <xsd:attribute name=\"SMTPServer\" type=\"xsd:string\"/>" +
                  "          <xsd:attribute name=\"SystemMailFromName\" type=\"xsd:string\"/>" +
                  "          <xsd:attribute name=\"MessageBodyEncoding\" type=\"xsd:string\"/>" +
                  "          <xsd:attribute name=\"SystemMail\" type=\"xsd:string\"/>" +
                  "          <xsd:attribute name=\"SystemMailUserName\" type=\"xsd:string\"/>" +
                  "          <xsd:attribute name=\"SystemMailUserPasword\" type=\"xsd:string\"/>" +
                  "        </xsd:complexType>" +
                  "      </xsd:element>" +
                  "      <xsd:element name=\"Paths\" minOccurs=\"1\">" +
                  "        <xsd:complexType>" +
                  "          <xsd:attribute name=\"MessageTemplates\" type=\"xsd:string\"/>" +
                  "        </xsd:complexType>" +
                  "      </xsd:element>" +
                  "      <xsd:element name=\"Main\" minOccurs=\"1\">" +
                  "        <xsd:complexType>" +
                  "          <xsd:attribute name=\"RefusalBuyerCount\" type=\"xsd:string\"/>" +
                  "          <xsd:attribute name=\"WaitingTimeConclusionAgreement\" type=\"xsd:string\"/>" +
                  "          <xsd:attribute name=\"AgreementLink\" type=\"xsd:string\"/>" +
                  "          <xsd:attribute name=\"LDAPUserName\" type=\"xsd:string\"/>" +
                  "          <xsd:attribute name=\"LDAPUserPwd\" type=\"xsd:string\"/>" +
                  "        </xsd:complexType>" +
                  "      </xsd:element>" +
                  "      <xsd:element name=\"Service\" minOccurs=\"1\">" +
                  "        <xsd:complexType>" +
                  "          <xsd:attribute name=\"AdministratorPhone\" type=\"xsd:integer\"/>" +
                  "          <xsd:attribute name=\"AdministratorMail\" type=\"xsd:string\"/>" +
                  "        </xsd:complexType>" +
                  "      </xsd:element>" +
                  "    </xsd:sequence>" +
                  "  </xsd:complexType>" +
                  "</xsd:schema>";

  public static final String TEST_XSL_TRANSFORM =
          "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                  "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">" +
                  "  <xsl:output method=\"html\"/>" +
                  "  <xsl:template match=\"/\">" +
                  "    <html>" +
                  "    </html>" +
                  "  </xsl:template>" +
                  "</xsl:stylesheet>";

  public static void main( String[] args ) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("JRE Version: " + System.getProperty("java.version"));
      LOG.debug("Enter to main()");
    }

    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
      //SAXPart(br);
      //DOMPart(br);
      //XPathPart(br);
      TransformPart(br);
      //StAXPart(br);
      //DOM4JPart(br);
    }
    catch (Exception e){
      System.out.println("Ошибка: " + e);
    }
    finally {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exit from main()");
      }
    }
  }

  public static void SAXPart(BufferedReader br) throws Exception {
    System.out.println("JAXP SAX:");
    LearnJAXPSAX learnJAXPSAX = new LearnJAXPSAX();
    //learnJAXPSax.readXML(TEST_XML_DATA);
    //System.out.println(String.format("TestId = %1$s from node: %2$s", learnJAXPSax.getValueFromXMLAsString(TEST_XML_DATA, "/Tests/Locals/Local", "TestId"), "/Tests/Locals/Local"));

    AppConfigData appConfigData = learnJAXPSAX.getAppConfigData(TEST_XML_DATA_ALL_CONFIG);
    if (appConfigData != null) {
      System.out.println(String.format("Server is [%1$s]", appConfigData.getConnectionConfigItem().getServer()));
    }

    System.out.println("For close app press Enter key...");
    br.readLine();
  }

  public static void DOMPart(BufferedReader br) throws Exception {
    System.out.println("JAXP DOM:");
    LearnJAXPDOM learnJAXPDOM = new LearnJAXPDOM(new PrintWriter(System.out, true));
   // learnJAXPDOM.readXML(TEST_XML_DATA);
   // System.out.println(String.format("TestId = %1$s from node: %2$s", learnJAXPDOM.getValueFromXMLAsString(TEST_XML_DATA, "/Tests/Locals/Local", "TestId"), "/Tests/Locals/Local"));

    AppConfigData appConfigData = learnJAXPDOM.getAppConfigData(TEST_XML_DATA_ALL_CONFIG);
    if (appConfigData != null) {
      System.out.println(String.format("Server is [%1$s]", appConfigData.getConnectionConfigItem().getServer()));
    }

    System.out.println("For close app press Enter key...");
    br.readLine();
  }

  public static void XPathPart(BufferedReader br) throws Exception {
    System.out.println("JAXP XPath:");
    LearnJAXPXPath learnJAXPXPath = new LearnJAXPXPath();
    System.out.println(String.format("TestId = %1$s from node: %2$s", learnJAXPXPath.getValueFromXMLAsString(TEST_XML_DATA, "/Tests/Locals/Local", "TestId"), "/Tests/Locals/Local"));

    System.out.println("For close app press Enter key...");
    br.readLine();
  }

  public static void TransformPart(BufferedReader br) throws Exception {
    System.out.println("JAXP Transform:");
    LearnJAXPTransform learnJAXPTransform = new LearnJAXPTransform();

    /*String resultXmlAsString ="";
    LearnJAXPDOM learnJAXPDOM = new LearnJAXPDOM(null);
    Document inputXML = learnJAXPDOM.readXMLAsDocument(TEST_XML_DATA);
    inputXML.setXmlStandalone(true);
    resultXmlAsString = learnJAXPTransform.saveXMLAsString(inputXML);
    System.out.println(TEST_XML_DATA);
    System.out.println(resultXmlAsString);*/

    LearnJAXPDOM learnJAXPDOM = new LearnJAXPDOM(null);
    Document inputXML = learnJAXPDOM.readXMLAsDocument(TEST_TASK_XML);
    inputXML.setXmlStandalone(true);
    String result = learnJAXPTransform.getXMLDocumentXSLTransformAsString(inputXML, TEST_XSL_1TO2);
    System.out.println(result);

    System.out.println("For close app press Enter key...");
    br.readLine();
  }

  public static void StAXPart(BufferedReader br) throws Exception {
    System.out.println("JAXP StAX:");
    LearnJAXPStAX learnJAXPStAX = new LearnJAXPStAX();
    //learnJAXPStAX.readXMLAsCursor(TEST_XML_DATA);
    //learnJAXPStAX.readXMLAsCursor2Event(TEST_XML_DATA);
    //learnJAXPStAX.readXMLAsEvent(TEST_XML_DATA);
    //learnJAXPStAX.readXMLAsFilteredCursor(TEST_XML_DATA);
    //learnJAXPStAX.readwriteXMLAsEvent(TEST_XML_DATA);
    //learnJAXPStAX.writeXMLAsStream();
    //AppConfigData appConfigData = learnJAXPStAX.getAppConfigDataCursor(TEST_XML_DATA_ALL_CONFIG);
    AppConfigData appConfigData = learnJAXPStAX.getAppConfigDataEvent(TEST_XML_DATA_ALL_CONFIG);
    if (appConfigData != null) {
      System.out.println(String.format("Server is [%1$s]", appConfigData.getConnectionConfigItem().getServer()));
    }

    System.out.println("For close app press Enter key...");
    br.readLine();
  }

  public static void DOM4JPart(BufferedReader br) throws Exception {
    System.out.println("DOM4J:");
    LearnDOM4J learnDOM4J = new LearnDOM4J();
    AppConfigData appConfigData = learnDOM4J.getAppConfigDataDOM4J(TEST_XML_DATA_ALL_CONFIG);
    if (appConfigData != null) {
      System.out.println(String.format("Server is [%1$s]", appConfigData.getConnectionConfigItem().getServer()));
    }

    System.out.println("For close app press Enter key...");
    br.readLine();
  }
}
