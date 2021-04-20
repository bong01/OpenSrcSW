package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class genSnippet {

    public static void main(String[] args) throws IOException, ClassNotFoundException, ParserConfigurationException {
        String dir = "";
        String query = "";
        if (args.length > 2) {
            if (args[0].equals("-f")) {
                dir = args[1];
                query = args[2];
            }
            FileInputStream fileInputStream = new FileInputStream(dir);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();


            System.out.println(object);
        }
    }
}

