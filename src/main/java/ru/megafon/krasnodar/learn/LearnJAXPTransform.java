package ru.megafon.krasnodar.learn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Stalker on 08.04.15.
 */
public class LearnJAXPTransform {
  private static final Logger LOG = LoggerFactory.getLogger(LearnJAXPTransform.class.getName());

  public String saveXMLAsString(Document xmlDocument) throws Exception {
    //throw new UnsupportedOperationException();

    TransformerFactory tFactory = TransformerFactory.newInstance();
    Transformer transformer = tFactory.newTransformer();

    DOMSource source = new DOMSource(xmlDocument);
    StringWriter sw = new StringWriter();
    StreamResult result = new StreamResult(sw);
    /*if (xmlDocument.getDoctype() != null) {
      String systemValue = (new File (xmlDocument.getDoctype().getSystemId())).getName();
      transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, systemValue);
    }*/
    transformer.transform(source, result);
    return sw.toString();
  }

  public String getXMLDocumentXSLTransformAsString(Document xmlSourceDocument, String xslTransformData) throws Exception {
    //throw new UnsupportedOperationException();

    TransformerFactory tFactory = TransformerFactory.newInstance();
    Transformer transformer = tFactory.newTransformer(new StreamSource(new StringReader(xslTransformData)));

    DOMSource source = new DOMSource(xmlSourceDocument);
    StringWriter sw = new StringWriter();
    StreamResult result = new StreamResult(sw);
    transformer.transform(source, result);
    return sw.toString();
  }

  public String getXMLDocumentXSLTransformWParamsAsString(Document xmlSourceDocument, String xslTransformData, HashMap<String, Object> xslParams) throws Exception {
    //throw new UnsupportedOperationException();

    TransformerFactory tFactory = TransformerFactory.newInstance();
    Transformer transformer = tFactory.newTransformer(new StreamSource(new StringReader(xslTransformData)));

    if (null != xslParams) {
      for(Map.Entry<String, Object> xslParam : xslParams.entrySet()) {
        transformer.setParameter(xslParam.getKey(), xslParam.getValue());
      }
    }

    DOMSource source = new DOMSource(xmlSourceDocument);
    StringWriter sw = new StringWriter();
    StreamResult result = new StreamResult(sw);
    transformer.transform(source, result);
    return sw.toString();
  }
}
