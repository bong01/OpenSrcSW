import org.jsoup.Jsoup;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.jsoup.nodes.Document;

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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class kuir {
    public static void main(String[] args) {

        String arg = args[0];
        if (arg.equals("-c")) {
            String dir = args[1];
            File path = new File(dir); // file list
            File[] fileList = path.listFiles();
            makeCollection mc = new makeCollection();
            mc.mkCollection(fileList);
        } else if (arg.equals("-k")) {
            String collection_dir = args[1];
            File collection = new File(collection_dir);  // collection.xml
            makeKeyword mk = new makeKeyword();
            mk.mkKeyword(collection);
        }
    }


}

class makeCollection {
    public void mkCollection(File[] fileList) {
        if (fileList.length > 0) {
            int id = 0;
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = null;
            try {
                docBuilder = docFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            org.w3c.dom.Document new_document = docBuilder.newDocument();    // new document (collection)

            Element docs = new_document.createElement("docs");

            new_document.appendChild(docs);


            for (int i = 0; i < fileList.length; i++) { // parse
                try {
                    File input = new File(String.valueOf(fileList[i]));
                    String file = String.valueOf(input);

                    if (file.contains(".html")) {

                        Document document = Jsoup.parse(input, "UTF-8"); // parse html files

                        Element doc = new_document.createElement("doc");
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


                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {  // html to xml
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

                    DOMSource source = new DOMSource(new_document);
                    StreamResult result = new StreamResult(new FileOutputStream(new File("/Users/bong/SimpleIR/collection.xml")));
                    transformer.transform(source, result);
                } catch (TransformerException | FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}

class makeKeyword {
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
