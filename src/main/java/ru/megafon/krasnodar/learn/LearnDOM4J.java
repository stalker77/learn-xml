/*
* This class developed in training purposes only to understood DOM4J XML library in action
*
* Created by Evgeny Dobrokvashin (aka Stalker) on 15.04.15 
*
* Copyright (c) 2015 MegaFon, All Rights Reserved.
*/

package ru.megafon.krasnodar.learn;

import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.List;

/**
 * @author Evgeny Dobrokvashin
 * @version 1.0 апр 2015
 *
 * Created by Stalker on 15.04.15.
 */
public class LearnDOM4J {
  private static final Logger LOG = LoggerFactory.getLogger(LearnDOM4J.class.getName());

  public AppConfigData getAppConfigDataDOM4J(String configXMLData) throws Exception {
    //throw new UnsupportedOperationException();

    if (LOG.isDebugEnabled()) {
      LOG.debug("Enter to getAppConfigDataDOM4J(" + configXMLData + ")");
    }

    try {
      SAXReader saxReader = new SAXReader();
      Document document = saxReader.read(new StringReader(configXMLData));//or DocumentHelper.parseText(configXMLData);

      AppConfigData appConfigData = new AppConfigData();
      processConnectionConfigItem(document, appConfigData);
      processMailConfigItem(document, appConfigData);
      processPathsConfigItem(document, appConfigData);
      processMainConfigItem(document, appConfigData);
      processServiceConfigItem(document, appConfigData);

      return appConfigData;
    } finally {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exit from getAppConfigDataDOM4J(" + configXMLData + ")");
      }
    }
  }

  private void processServiceConfigItem(Document document, AppConfigData appConfigData) throws DocumentException {
    ServiceConfigItem serviceConfigItem = new ServiceConfigItem(
            getAttributeValue(document, AppConfigNodeName.SERVICE.getNodeName(),
                    ServiceConfigItem.ATTR_NAME_ADMINISTRATORPHONE),
            getAttributeValue(document, AppConfigNodeName.SERVICE.getNodeName(),
                    ServiceConfigItem.ATTR_NAME_ADMINISTRATORMAIL)
    );
    appConfigData.setServiceConfigItem(serviceConfigItem);
  }

  private void processMainConfigItem(Document document, AppConfigData appConfigData) throws DocumentException {
    MainConfigItem mainConfigItem = new MainConfigItem(
            getAttributeValue(document, AppConfigNodeName.MAIN.getNodeName(),
                    MainConfigItem.ATTR_NAME_REFUSALBUYERCOUNT),
            getAttributeValue(document, AppConfigNodeName.MAIN.getNodeName(),
                    MainConfigItem.ATTR_NAME_WAITINGTIMECONCLUSIONAGREEMENT),
            getAttributeValue(document, AppConfigNodeName.MAIN.getNodeName(),
                    MainConfigItem.ATTR_NAME_AGREEMENTLINK),
            getAttributeValue(document, AppConfigNodeName.MAIN.getNodeName(), MainConfigItem.ATTR_NAME_LDAPUSERNAME),
            getAttributeValue(document, AppConfigNodeName.MAIN.getNodeName(),
                    MainConfigItem.ATTR_NAME_LDAPUSERPWD)
    );
    appConfigData.setMainConfigItem(mainConfigItem);
  }

  private void processPathsConfigItem(Document document, AppConfigData appConfigData) throws DocumentException {
    PathsConfigItem pathsConfigItem = new PathsConfigItem(getAttributeValue(document,
            AppConfigNodeName.PATHS.getNodeName(), PathsConfigItem.ATTR_NAME_MESSAGETEMPLATES));
    appConfigData.setPathsConfigItem(pathsConfigItem);
  }

  private void processMailConfigItem(Document document, AppConfigData appConfigData) throws DocumentException {
    MailConfigItem mailConfigItem = new MailConfigItem(
            getAttributeValue(document, AppConfigNodeName.MAIL.getNodeName(), MailConfigItem.ATTR_NAME_SMTPSERVER),
            getAttributeValue(document, AppConfigNodeName.MAIL.getNodeName(),
                    MailConfigItem.ATTR_NAME_SYSTEMMAILFROMNAME),
            getAttributeValue(document, AppConfigNodeName.MAIL.getNodeName(),
                    MailConfigItem.ATTR_NAME_MESSAGEBODYENCODING),
            getAttributeValue(document, AppConfigNodeName.MAIL.getNodeName(), MailConfigItem.ATTR_NAME_SYSTEMMAIL),
            getAttributeValue(document, AppConfigNodeName.MAIL.getNodeName(),
                    MailConfigItem.ATTR_NAME_SYSTEMMAILUSERNAME),
            getAttributeValue(document, AppConfigNodeName.MAIL.getNodeName(),
                    MailConfigItem.ATTR_NAME_SYSTEMMAILUSERPASWORD)
    );
    appConfigData.setMailConfigItem(mailConfigItem);
  }

  private void processConnectionConfigItem(Document document, AppConfigData appConfigData) throws DocumentException {
    ConnectionConfigItem connectionConfigItem = new ConnectionConfigItem(getAttributeValue(document,
            AppConfigNodeName.CONNECTION.getNodeName(), ConnectionConfigItem.ATTR_NAME_SERVER));
    appConfigData.setConnectionConfigItem(connectionConfigItem);
  }

  private String getAttributeValue(Document document, String nodeName, String attrName) throws DocumentException {
    String result = null;

    Attribute attr = (Attribute)document.selectSingleNode("//" + AppConfigNodeName.CONFIGURATION_ROOT.getNodeName() + "/" + nodeName + "/@" + attrName);
    if (null == attr) {
      throw new DocumentException(String.format("Attribute '%2$s' in '%1$s' node missing!", nodeName, attrName));
    } else {
      result = attr.getValue();
    }

    return result;
  }
}