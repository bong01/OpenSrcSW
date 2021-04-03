package com.company;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;

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

public class makeCollection {
    public void mkCollection(File[] fileList) throws ParserConfigurationException, IOException, TransformerException {
        if (fileList.length > 0) {
            int id = 0;
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            org.w3c.dom.Document new_document = docBuilder.newDocument();    // document for collection

            Element docs = new_document.createElement("docs");
            new_document.appendChild(docs);


            for (File value : fileList) { // parse
                    File input = new File(String.valueOf(value));
                    String file = String.valueOf(input);

                    if (file.contains(".html")) {

                        Document document = Jsoup.parse(input, "UTF-8"); // parse with lib

                        Element doc = new_document.createElement("doc");     // tag
                        Element title = new_document.createElement("title");
                        Element body = new_document.createElement("body");

                        doc.setAttribute("id", String.valueOf(id));
                        title.appendChild(new_document.createTextNode(document.title()));
                        body.appendChild(new_document.createTextNode(document.body().text()));

                        doc.appendChild(title);
                        doc.appendChild(body);
                        docs.appendChild(doc);

                        id++;
                    }

                    // make collection.xml
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

                    DOMSource source = new DOMSource(new_document);
                    StreamResult result = new StreamResult(new FileOutputStream(new File("/Users/bong/SimpleIR/collection.xml")));
                    transformer.transform(source, result);
            }
        }
    }
}