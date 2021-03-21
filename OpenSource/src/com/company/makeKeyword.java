package com.company;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class makeKeyword {
    public void mkKeyword(File collection) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = docFactory.newDocumentBuilder();
            org.w3c.dom.Document d = builder.parse(collection);
            NodeList nodeList = d.getElementsByTagName("doc");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node n = nodeList.item(i);

                KeywordExtractor ke = new KeywordExtractor();
                KeywordList kl = ke.extractKeyword(n.getLastChild().getTextContent(), true);

                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < kl.size(); j++) {
                    Keyword kwrd = kl.get(j);
                    sb.append(kwrd.getString() + ":" + kwrd.getCnt() + "#");
                }
                n.getLastChild().setTextContent(sb.toString());
            }
            try {  // collection to index
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

                DOMSource source = new DOMSource(d);
                StreamResult result = new StreamResult(new FileOutputStream(new File("/Users/bong/SimpleIR/index.xml")));
                transformer.transform(source, result);
            } catch (TransformerException | FileNotFoundException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
