package ru.megafon.krasnodar.learn;

import org.junit.*;
import org.w3c.dom.Document;

import java.util.HashMap;

/**
 * Unit test for simple LearnXML.
 */
public class LearnXMLTest {
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

  public static final String TEST_XSL_TRANSFORM =
          "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
          "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">" +
          "  <xsl:output method=\"html\"/>" +
          "  <xsl:template match=\"/\">" +
          "    <html>" +
          "    </html>" +
          "  </xsl:template>" +
          "</xsl:stylesheet>";

  public static final String TEST_XSL_TRANSFORM_WPARAM =
          "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                  "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">" +
                  "  <xsl:param name=\"lotId\"/>" +
                  "  <xsl:output method=\"html\"/>" +
                  "  <xsl:template match=\"/\">" +
                  "    <html>" +
                  "    lotId = <xsl:value-of select=\"$lotId\"/>;" +
                  "    </html>" +
                  "  </xsl:template>" +
                  "</xsl:stylesheet>";

  /**
   *
   */
  @Test
  //UnitOfWork_StateUnderTest_ExpectedBehavior - test name best practice ()
  public void GetValueFromXMLSAXAsString_ValidParams_ValueReturned() throws Exception {
    //setup
    LearnJAXPSAX learnJAXPSAX = new LearnJAXPSAX();

    //act
    String result = learnJAXPSAX.getValueFromXMLAsString(TEST_XML_DATA, "/Tests/Locals/Local", "TestId");

    //assert
    Assert.assertEquals("Expected value 20 was not returned!", result, "20");
  }

  @Test
  public void GetAppConfigDataSAX_ValidConfigParam_AppConfigObjectFilled() throws Exception {
    //setup
    LearnJAXPSAX learnJAXPSAX = new LearnJAXPSAX();

    //act
    AppConfigData appConfigData = learnJAXPSAX.getAppConfigData(TEST_XML_DATA_ALL_CONFIG);

    //assert
    Assert.assertNotNull("Config object is null!", appConfigData);
    Assert.assertNotNull("Connection object is null!", appConfigData.getConnectionConfigItem());
    Assert.assertNotNull("Mail object is null!", appConfigData.getMailConfigItem());
    Assert.assertNotNull("Paths object is null!", appConfigData.getPathsConfigItem());
    Assert.assertNotNull("Main object is null!", appConfigData.getMainConfigItem());
    Assert.assertNotNull("Service object is null!", appConfigData.getServiceConfigItem());
    Assert.assertEquals("ConnectionConfigItem.Server not read!", appConfigData.getConnectionConfigItem().getServer(), "KRN-SQL-DB3.megafon.ru\\ARMAGEDDON");
    Assert.assertEquals("MailConfigItem.SystemMailUserPassword not read!", appConfigData.getMailConfigItem().getSystemMailUserPassword(), "32dIaJMBMNLfCuVTyYeZ");
    Assert.assertEquals("PathsConfigItem.MessageTemplates not read!", appConfigData.getPathsConfigItem().getMessageTemplates(), "");
    Assert.assertEquals("MainConfigItem.LdapUserPwd not read!", appConfigData.getMainConfigItem().getLdapUserPwd(), "32dIaJMBMNLfCuVTyYeZ");
    Assert.assertEquals("ServiceConfigItem.AdministratorMail not read!", appConfigData.getServiceConfigItem().getAdministratorMail(), "Evgeny.Dobrokvashin@MegaFon.ru");
  }

  @Test
  public void GetValueFromXMLDOMAsString_ValidParams_ValueReturned() throws Exception {
    //setup
    LearnJAXPDOM learnJAXPDOM = new LearnJAXPDOM(null);

    //act
    String result = learnJAXPDOM.getValueFromXMLAsString(TEST_XML_DATA, "/Tests/Locals/Local", "TestId");

    //assert
    Assert.assertEquals("Expected value 20 was not returned!", result, "20");
  }

  @Test
  public void GetAppConfigDataDOM_ValidConfigParam_AppConfigObjectFilled() throws Exception {
    //setup
    LearnJAXPDOM learnJAXPDOM = new LearnJAXPDOM(null);

    //act
    AppConfigData appConfigData = learnJAXPDOM.getAppConfigData(TEST_XML_DATA_ALL_CONFIG);

    //assert
    Assert.assertNotNull("Config object is null!", appConfigData);
    Assert.assertNotNull("Connection object is null!", appConfigData.getConnectionConfigItem());
    Assert.assertNotNull("Mail object is null!", appConfigData.getMailConfigItem());
    Assert.assertNotNull("Paths object is null!", appConfigData.getPathsConfigItem());
    Assert.assertNotNull("Main object is null!", appConfigData.getMainConfigItem());
    Assert.assertNotNull("Service object is null!", appConfigData.getServiceConfigItem());
    Assert.assertEquals("ConnectionConfigItem.Server not read!", appConfigData.getConnectionConfigItem().getServer(), "KRN-SQL-DB3.megafon.ru\\ARMAGEDDON");
    Assert.assertEquals("MailConfigItem.SystemMailUserPassword not read!", appConfigData.getMailConfigItem().getSystemMailUserPassword(), "32dIaJMBMNLfCuVTyYeZ");
    Assert.assertEquals("PathsConfigItem.MessageTemplates not read!", appConfigData.getPathsConfigItem().getMessageTemplates(), "");
    Assert.assertEquals("MainConfigItem.LdapUserPwd not read!", appConfigData.getMainConfigItem().getLdapUserPwd(), "32dIaJMBMNLfCuVTyYeZ");
    Assert.assertEquals("ServiceConfigItem.AdministratorMail not read!", appConfigData.getServiceConfigItem().getAdministratorMail(), "Evgeny.Dobrokvashin@MegaFon.ru");
  }

  @Test
  public void GetValueFromXMLXPathAsString_ValidParams_ValueReturned() throws Exception {
    //setup
    LearnJAXPXPath learnJAXPXPath = new LearnJAXPXPath();

    //act
    String result = learnJAXPXPath.getValueFromXMLAsString(TEST_XML_DATA, "/Tests/Locals/Local", "TestId");

    //assert
    Assert.assertEquals("Expected value 20 was not returned!", result, "20");
  }

  @Test
  public void SaveXMLAsString_ValidParams_DataSaved() throws Exception {
    //setup
    LearnJAXPTransform learnJAXPTransform = new LearnJAXPTransform();
    String resultXmlAsString ="";
    LearnJAXPDOM learnJAXPDOM = new LearnJAXPDOM(null);
    Document inputXML = learnJAXPDOM.readXMLAsDocument(TEST_XML_DATA);
    inputXML.setXmlStandalone(true);

    //act
    resultXmlAsString = learnJAXPTransform.saveXMLAsString(inputXML);

    //assert
    LearnJAXPXPath learnJAXPXPath = new LearnJAXPXPath();
    String result = learnJAXPXPath.getValueFromXMLAsString(resultXmlAsString, "/Tests/Locals/Local", "TestId");
    Assert.assertEquals("Expected value 20 was not returned!", result, "20");
  }

  @Test
  public void getXMLDocumentXSLTransformAsString_ValidParams_TransformPerformed() throws Exception {
    //setup
    LearnJAXPTransform learnJAXPTransform = new LearnJAXPTransform();
    LearnJAXPDOM learnJAXPDOM = new LearnJAXPDOM(null);
    Document inputXML = learnJAXPDOM.readXMLAsDocument(TEST_XML_DATA);
    inputXML.setXmlStandalone(true);


    //act
    String result = learnJAXPTransform.getXMLDocumentXSLTransformAsString(inputXML, TEST_XSL_TRANSFORM);

    //assume
    Assert.assertTrue("XSL Transform fail! <html> not found in result!", result.contains("<html>"));
  }

  @Test
  public void getXMLDocumentXSLTransformWParamsAsString_ValidParams_TransformPerformed() throws Exception {
    //setup
    LearnJAXPTransform learnJAXPTransform = new LearnJAXPTransform();
    LearnJAXPDOM learnJAXPDOM = new LearnJAXPDOM(null);
    Document inputXML = learnJAXPDOM.readXMLAsDocument(TEST_XML_DATA);
    inputXML.setXmlStandalone(true);


    //act
    HashMap<String, Object> xslParams = new HashMap<>();
    xslParams.put("lotId", 1);
    String result = learnJAXPTransform.getXMLDocumentXSLTransformWParamsAsString(inputXML, TEST_XSL_TRANSFORM_WPARAM, xslParams);

    //assume
    Assert.assertTrue("XSL Transform fail! <html> not found in result!", result.contains("lotId = 1;"));
  }

  @Test
  public void getXMLDocumentXSLTransformWParamsAsString_NullxslParams_TransformPerformed() throws Exception {
    //setup
    LearnJAXPTransform learnJAXPTransform = new LearnJAXPTransform();
    LearnJAXPDOM learnJAXPDOM = new LearnJAXPDOM(null);
    Document inputXML = learnJAXPDOM.readXMLAsDocument(TEST_XML_DATA);
    inputXML.setXmlStandalone(true);


    //act
    String result = learnJAXPTransform.getXMLDocumentXSLTransformWParamsAsString(inputXML, TEST_XSL_TRANSFORM_WPARAM, null);

    //assume
    Assert.assertTrue("XSL Transform fail!", result.contains("lotId = ;"));
  }

  @Test
  public void getAppConfigDataStAXCursor_ValidConfigParam_AppConfigObjectFilled() throws Exception {
    //setup
    LearnJAXPStAX learnJAXPStAX = new LearnJAXPStAX();

    //act
    AppConfigData appConfigData = learnJAXPStAX.getAppConfigDataCursor(TEST_XML_DATA_ALL_CONFIG);

    //assert
    Assert.assertNotNull("Config object is null!", appConfigData);
    Assert.assertNotNull("Connection object is null!", appConfigData.getConnectionConfigItem());
    Assert.assertNotNull("Mail object is null!", appConfigData.getMailConfigItem());
    Assert.assertNotNull("Paths object is null!", appConfigData.getPathsConfigItem());
    Assert.assertNotNull("Main object is null!", appConfigData.getMainConfigItem());
    Assert.assertNotNull("Service object is null!", appConfigData.getServiceConfigItem());
    Assert.assertEquals("ConnectionConfigItem.Server not read!", appConfigData.getConnectionConfigItem().getServer(), "KRN-SQL-DB3.megafon.ru\\ARMAGEDDON");
    Assert.assertEquals("MailConfigItem.SystemMailUserPassword not read!", appConfigData.getMailConfigItem().getSystemMailUserPassword(), "32dIaJMBMNLfCuVTyYeZ");
    Assert.assertEquals("PathsConfigItem.MessageTemplates not read!", appConfigData.getPathsConfigItem().getMessageTemplates(), "");
    Assert.assertEquals("MainConfigItem.LdapUserPwd not read!", appConfigData.getMainConfigItem().getLdapUserPwd(), "32dIaJMBMNLfCuVTyYeZ");
    Assert.assertEquals("ServiceConfigItem.AdministratorMail not read!", appConfigData.getServiceConfigItem().getAdministratorMail(), "Evgeny.Dobrokvashin@MegaFon.ru");
  }

  @Test
  public void getAppConfigDataStAXEvent_ValidConfigParam_AppConfigObjectFilled() throws Exception {
    //setup
    LearnJAXPStAX learnJAXPStAX = new LearnJAXPStAX();

    //act
    AppConfigData appConfigData = learnJAXPStAX.getAppConfigDataEvent(TEST_XML_DATA_ALL_CONFIG);

    //assert
    Assert.assertNotNull("Config object is null!", appConfigData);
    Assert.assertNotNull("Connection object is null!", appConfigData.getConnectionConfigItem());
    Assert.assertNotNull("Mail object is null!", appConfigData.getMailConfigItem());
    Assert.assertNotNull("Paths object is null!", appConfigData.getPathsConfigItem());
    Assert.assertNotNull("Main object is null!", appConfigData.getMainConfigItem());
    Assert.assertNotNull("Service object is null!", appConfigData.getServiceConfigItem());
    Assert.assertEquals("ConnectionConfigItem.Server not read!", appConfigData.getConnectionConfigItem().getServer(), "KRN-SQL-DB3.megafon.ru\\ARMAGEDDON");
    Assert.assertEquals("MailConfigItem.SystemMailUserPassword not read!", appConfigData.getMailConfigItem().getSystemMailUserPassword(), "32dIaJMBMNLfCuVTyYeZ");
    Assert.assertEquals("PathsConfigItem.MessageTemplates not read!", appConfigData.getPathsConfigItem().getMessageTemplates(), "");
    Assert.assertEquals("MainConfigItem.LdapUserPwd not read!", appConfigData.getMainConfigItem().getLdapUserPwd(), "32dIaJMBMNLfCuVTyYeZ");
    Assert.assertEquals("ServiceConfigItem.AdministratorMail not read!", appConfigData.getServiceConfigItem().getAdministratorMail(), "Evgeny.Dobrokvashin@MegaFon.ru");
  }

  @Test
  public void getAppConfigDataDOM4J_ValidConfigParam_AppConfigObjectFilled() throws Exception {
    //setup
    LearnDOM4J learnDOM4J = new LearnDOM4J();

    //act
    AppConfigData appConfigData = learnDOM4J.getAppConfigDataDOM4J(TEST_XML_DATA_ALL_CONFIG);

    //assert
    Assert.assertNotNull("Config object is null!", appConfigData);
    Assert.assertNotNull("Connection object is null!", appConfigData.getConnectionConfigItem());
    Assert.assertNotNull("Mail object is null!", appConfigData.getMailConfigItem());
    Assert.assertNotNull("Paths object is null!", appConfigData.getPathsConfigItem());
    Assert.assertNotNull("Main object is null!", appConfigData.getMainConfigItem());
    Assert.assertNotNull("Service object is null!", appConfigData.getServiceConfigItem());
    Assert.assertEquals("ConnectionConfigItem.Server not read!", appConfigData.getConnectionConfigItem().getServer(), "KRN-SQL-DB3.megafon.ru\\ARMAGEDDON");
    Assert.assertEquals("MailConfigItem.SystemMailUserPassword not read!", appConfigData.getMailConfigItem().getSystemMailUserPassword(), "32dIaJMBMNLfCuVTyYeZ");
    Assert.assertEquals("PathsConfigItem.MessageTemplates not read!", appConfigData.getPathsConfigItem().getMessageTemplates(), "");
    Assert.assertEquals("MainConfigItem.LdapUserPwd not read!", appConfigData.getMainConfigItem().getLdapUserPwd(), "32dIaJMBMNLfCuVTyYeZ");
    Assert.assertEquals("ServiceConfigItem.AdministratorMail not read!", appConfigData.getServiceConfigItem().getAdministratorMail(), "Evgeny.Dobrokvashin@MegaFon.ru");
  }
}
