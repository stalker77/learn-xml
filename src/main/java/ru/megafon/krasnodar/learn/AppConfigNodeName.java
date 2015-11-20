package ru.megafon.krasnodar.learn;

public enum AppConfigNodeName {
  CONFIGURATION_ROOT("Configuration"),

  CONNECTION("Connection"),

  MAIL("Mail"),

  PATHS("Paths"),

  MAIN("Main"),

  SERVICE("Service");

  private String nodeName;

  private AppConfigNodeName(String nodeName) {
    this.nodeName = nodeName;
  }

  static public AppConfigNodeName getNodeName(String nodeName) {
    for (AppConfigNodeName type: AppConfigNodeName.values()) {
      if (type.getNodeName().equals(nodeName)) {
        return type;
      }
    }

    throw new RuntimeException("Unknown type");
  }

  public String getNodeName() {
    return nodeName;
  }
}
