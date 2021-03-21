package com.company;

import java.io.File;


public class kuir {
    public static void main(String[] args) {
//        args = new String[2];
//        args[0]="-c";
//        args[1]="/Users/bong/SimpleIR";
        String arg = args[0];
        if (arg.equals("-c")) {
            if (args[1] != null) {
                String dir = args[1];
                System.out.println(dir);
                File path = new File(dir); // file list
                File[] fileList = path.listFiles();
                makeCollection mc = new makeCollection();
                mc.mkCollection(fileList);
            }
        } else if (arg.equals("-k")) {
            if (args[1] != null) {
                String collection_dir = args[1];
                File collection = new File(collection_dir);  // collection.xml
                makeKeyword mk = new makeKeyword();
                mk.mkKeyword(collection);
            }
        }
    }
}

