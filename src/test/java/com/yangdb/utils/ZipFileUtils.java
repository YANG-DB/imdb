package com.yangdb.utils;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

public class ZipFileUtils {

    @Test   
    public void zipExtractTest() throws  IOException {
        GZIPInputStream gzip = new GZIPInputStream(new FileInputStream("/home/lior/projects/imdb/"));
        BufferedReader br = new BufferedReader(new InputStreamReader(gzip));
        Stream<String> lines = br.lines();
        Iterator<String> iterator = lines.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

}
