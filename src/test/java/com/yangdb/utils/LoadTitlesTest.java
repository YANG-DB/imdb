package com.yangdb.utils;

import com.imdb.Application;
import com.imdb.services.ImportService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class LoadTitlesTest {

    @Autowired
    ImportService importService;

    @Test
    public void importTitleAKA() throws  IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("data/title.akas.tsv.gz");
        importService.loadTitleAKA(new File(resource.getFile()));

    }

    @Test
    public void importTitle() throws  IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("data/title.basics.tsv.gz");
        importService.loadTitle(new File(resource.getFile()));

    }

}
