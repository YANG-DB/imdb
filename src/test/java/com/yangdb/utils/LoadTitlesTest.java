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

    @Test
    public void importCrew() throws  IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("data/title.crew.tsv.gz");
        importService.loadCrew(new File(resource.getFile()));

    }

    @Test
    public void importPeople() throws  IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("data/name.basics.tsv.gz");
        importService.loadPeople(new File(resource.getFile()));

    }

    @Test
    public void importRating() throws  IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("data/title.ratings.tsv.gz");
        importService.loadRating(new File(resource.getFile()));
    }

    @Test
    public void importEpisods() throws  IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("data/title.episode.tsv.gz");
        importService.loadEpisode(new File(resource.getFile()));
    }

    @Test
    public void importCast() throws  IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("data/title.principals.tsv.gz");
        importService.loadCast(new File(resource.getFile()));

    }

}
