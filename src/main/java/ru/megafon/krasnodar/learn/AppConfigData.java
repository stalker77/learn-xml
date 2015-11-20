package ru.megafon.krasnodar.learn;

/**
 * Created by Stalker on 02.04.15.
 */
public class AppConfigData {
  private ConnectionConfigItem connectionConfigItem;

  private MailConfigItem mailConfigItem;

  private PathsConfigItem pathsConfigItem;

  private MainConfigItem mainConfigItem;

  private ServiceConfigItem serviceConfigItem;

  public ConnectionConfigItem getConnectionConfigItem() {
    return connectionConfigItem;
  }

  public MailConfigItem getMailConfigItem() {
    return mailConfigItem;
  }

  public PathsConfigItem getPathsConfigItem() {
    return pathsConfigItem;
  }

  public MainConfigItem getMainConfigItem() {
    return mainConfigItem;
  }

  public ServiceConfigItem getServiceConfigItem() {
    return serviceConfigItem;
  }

  public void setConnectionConfigItem(ConnectionConfigItem value) {
    connectionConfigItem = value;
  }

  public void setMailConfigItem(MailConfigItem value) {
    mailConfigItem = value;
  }

  public void setPathsConfigItem(PathsConfigItem value) {
    pathsConfigItem = value;
  }

  public void setMainConfigItem(MainConfigItem value) {
    mainConfigItem = value;
  }

  public void setServiceConfigItem(ServiceConfigItem value) {
    serviceConfigItem = value;
  }
}

class ConnectionConfigItem {
  private String server;

  public static final String ATTR_NAME_SERVER = "Server";

  ConnectionConfigItem(String server) {
    this.server = server;
  }

  public String getServer() {
    return server;
  }
}

class MailConfigItem {
  private String smtpServer;

  private String systemMailFromName;

  private String messageBodyEncoding;

  private String systemMail;

  private String systemMailUserName;

  private String systemMailUserPassword;

  public static final String ATTR_NAME_SMTPSERVER = "SMTPServer";

  public static final String ATTR_NAME_SYSTEMMAILFROMNAME = "SystemMailFromName";

  public static final String ATTR_NAME_MESSAGEBODYENCODING = "MessageBodyEncoding";

  public static final String ATTR_NAME_SYSTEMMAIL = "SystemMail";

  public static final String ATTR_NAME_SYSTEMMAILUSERNAME = "SystemMailUserName";

  public static final String ATTR_NAME_SYSTEMMAILUSERPASWORD = "SystemMailUserPasword";

  MailConfigItem(String smtpServer, String systemMailFromName, String messageBodyEncoding, String systemMail, String systemMailUserName, String systemMailUserPasword) {
    this.smtpServer = smtpServer;
    this.systemMailFromName = systemMailFromName;
    this.messageBodyEncoding = messageBodyEncoding;
    this.systemMail = systemMail;
    this.systemMailUserName = systemMailUserName;
    this.systemMailUserPassword = systemMailUserPasword;
  }

  public String getSmtpServer() {
    return smtpServer;
  }

  public String getSystemMailFromName() {
    return systemMailFromName;
  }

  public String getMessageBodyEncoding() {
    return messageBodyEncoding;
  }

  public String getSystemMail() {
    return systemMail;
  }

  public String getSystemMailUserName() {
    return systemMailUserName;
  }

  public String getSystemMailUserPassword() {
    return systemMailUserPassword;
  }
}

class PathsConfigItem {
  private String messageTemplates;

  public static final String ATTR_NAME_MESSAGETEMPLATES = "MessageTemplates";

  PathsConfigItem(String messageTemplates) {
    this.messageTemplates = messageTemplates;
  }

  public String getMessageTemplates() {
    return messageTemplates;
  }
}

class MainConfigItem {
  private String refusalBuyerCount;

  private String waitingTimeConclusionAgreement;

  private String agreementLink;

  private String ldapUserName;

  private String ldapUserPwd;

  public static final String ATTR_NAME_REFUSALBUYERCOUNT = "RefusalBuyerCount";

  public static final String ATTR_NAME_WAITINGTIMECONCLUSIONAGREEMENT = "WaitingTimeConclusionAgreement";

  public static final String ATTR_NAME_AGREEMENTLINK = "AgreementLink";

  public static final String ATTR_NAME_LDAPUSERNAME = "LDAPUserName";

  public static final String ATTR_NAME_LDAPUSERPWD = "LDAPUserPwd";

  MainConfigItem(String refusalBuyerCount, String waitingTimeConclusionAgreement, String agreementLink, String ldapUserName, String ldapUserPwd) {
    this.refusalBuyerCount = refusalBuyerCount;
    this.waitingTimeConclusionAgreement = waitingTimeConclusionAgreement;
    this.agreementLink = agreementLink;
    this.ldapUserName = ldapUserName;
    this.ldapUserPwd = ldapUserPwd;
  }

  public String getRefusalBuyerCount() {
    return refusalBuyerCount;
  }

  public String getWaitingTimeConclusionAgreement() {
    return waitingTimeConclusionAgreement;
  }

  public String getAgreementLink() {
    return agreementLink;
  }

  public String getLdapUserName() {
    return ldapUserName;
  }

  public String getLdapUserPwd() {
    return ldapUserPwd;
  }
}

class ServiceConfigItem {
  private String administratorPhone;

  private String administratorMail;

  public static final String ATTR_NAME_ADMINISTRATORPHONE = "AdministratorPhone";

  public static final String ATTR_NAME_ADMINISTRATORMAIL = "AdministratorMail";

  ServiceConfigItem(String administratorPhone, String administratorMail) {
    this.administratorPhone = administratorPhone;
    this.administratorMail = administratorMail;
  }

  public String getAdministratorPhone() {
    return administratorPhone;
  }

  public String getAdministratorMail() {
    return administratorMail;
  }
}