import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class Test {
    public static void main(String[] args) {
        File path = new File("/Users/bong/SimpleIR"); // dir
        File[] fileList = path.listFiles();

        if (fileList.length > 0) {   // empty?
            int id =0;
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = null;
            try {
                docBuilder = docFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            org.w3c.dom.Document doc_2 = docBuilder.newDocument();    // new doc

            Element e_docs = doc_2.createElement("docs");

            doc_2.appendChild(e_docs);

            for (int i = 0; i < fileList.length; i++) { // parse
                try {
                    File input = new File(String.valueOf(fileList[i]));
                    String name = String.valueOf(input);

                    if (name.contains(".html")) {

                        Document doc = Jsoup.parse(input, "UTF-8"); // library

                        Element e_doc = doc_2.createElement("doc");
                        Element e_title = doc_2.createElement("title");
                        Element e_body = doc_2.createElement("body");

                        e_doc.setAttribute("id", String.valueOf(id));
                        e_title.appendChild(doc_2.createTextNode(doc.title()));
                        e_body.appendChild(doc_2.createTextNode(doc.text()));

                        e_doc.appendChild(e_title);
                        e_doc.appendChild(e_body);
                        e_docs.appendChild(e_doc);

                        id++;
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            try {  // xml
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

                DOMSource source = new DOMSource(doc_2);
                StreamResult result = new StreamResult(new FileOutputStream(new File("/Users/bong/SimpleIR/collection.xml")));
                transformer.transform(source, result);
            } catch (TransformerException | FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}