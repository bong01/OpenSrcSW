package com.company;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class makeKeyword {
    public void mkKeyword(File collection) throws IOException, TransformerException, SAXException, ParserConfigurationException {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = docFactory.newDocumentBuilder();
            org.w3c.dom.Document document = builder.parse(collection);

            NodeList nodeList = document.getElementsByTagName("doc");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                KeywordExtractor ke = new KeywordExtractor();
                KeywordList kl = ke.extractKeyword(node.getLastChild().getTextContent(), true);

                StringBuilder sb = new StringBuilder();
                for (Keyword kw : kl) {
                    sb.append(kw.getString()).append(":").append(kw.getCnt()).append("#");
                }
                node.getLastChild().setTextContent(sb.toString());
            }

                // make index.xml
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(new FileOutputStream("/Users/bong/SimpleIR/index.xml"));
                transformer.transform(source, result);
    }
}
