package ru.megafon.krasnodar.learn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

/**
 * Created by Stalker on 07.04.15.
 */
public class LearnJAXPXPath {
  private static final Logger LOG = LoggerFactory.getLogger(LearnJAXPXPath.class.getName());

  public String getValueFromXMLAsString(String xmlSource, String fullPathToAttrNode, String attrName) throws Exception {
    //throw new UnsupportedOperationException();

    XPath xpath = XPathFactory.newInstance().newXPath();
    String expression = fullPathToAttrNode + "/@" + attrName;
    InputSource inputSource = new InputSource(new StringReader(xmlSource));
    NodeList nodes = (NodeList) xpath.evaluate(expression, inputSource, XPathConstants.NODESET);

    return nodes.item(0).getTextContent();
  }
}
