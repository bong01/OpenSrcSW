package com.company;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;


public class kuir {
    public static void main(String[] args) throws ParserConfigurationException, IOException, TransformerException, SAXException, ClassNotFoundException {
        if (args.length > 1) {
            String arg = args[0];
            switch (arg) {
                case "-c" -> {
                    String dir = args[1];
                    System.out.println(dir);
                    File path = new File(dir); // file list
                    File[] fileList = path.listFiles();
                    makeCollection mc = new makeCollection();
                    mc.mkCollection(fileList);
                }
                case "-k" -> {
                    String collection_dir = args[1];
                    File collection = new File(collection_dir);  // collection.xml
                    makeKeyword mk = new makeKeyword();
                    mk.mkKeyword(collection);
                }
                case "-i" -> {
                    String index_dir = args[1];
                    indexer indexer = new indexer();
                    indexer.makeIndex(index_dir);
                    indexer.readIndex();
                }
            }

        } else {
            System.out.println("인자를 입력해주세요");
        }

    }
}


