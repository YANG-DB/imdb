package com.imdb.services;

import com.yangdb.fuse.model.logical.LogicalGraphModel;
import com.yangdb.fuse.model.resourceInfo.QueryResourceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

@Component
@ComponentScan({"com.imdb.*"})
public class BaseImportService implements ImportService {
    private static final Logger logger = LoggerFactory.getLogger(BaseImportService.class);

    @Inject
    private FuseClientService fuseClientService;

    @Value("${import.bulk.size}")
    private int bulkSize;

    @Value("${import.timeout}")
    private int timeout;

    private ConcurrentLinkedQueue<QueryResourceInfo> responses = new ConcurrentLinkedQueue<>();

    /**
     * load imdb titles nodes from zip file
     * @param jsonFile
     * @throws IOException
     * @return
     */
    public ConcurrentLinkedQueue<QueryResourceInfo> loadTitleAKA(File jsonFile) throws IOException {
        return load(jsonFile, ImdbImporter::loadTitleAKA);
    }

    public ConcurrentLinkedQueue<QueryResourceInfo> loadTitle(File jsonFile) throws IOException {
        return load(jsonFile, ImdbImporter::loadTitle);
    }

    @Override
    public Collection<QueryResourceInfo> loadCrew(File jsonFile) throws IOException {
        return load(jsonFile, ImdbImporter::loadCrew);
    }

    @Override
    public Collection<QueryResourceInfo> loadPeople(File jsonFile) throws IOException {
        return load(jsonFile, ImdbImporter::loadPeople);
    }

    @Override
    public Collection<QueryResourceInfo> loadRating(File jsonFile) throws IOException {
        return load(jsonFile, ImdbImporter::loadCast);
    }

    @Override
    public Collection<QueryResourceInfo> loadEpisode(File jsonFile) throws IOException {
        return load(jsonFile, ImdbImporter::loadEpisode);
    }

    @Override
    public Collection<QueryResourceInfo> loadCast(File jsonFile) throws IOException {
        return load(jsonFile, ImdbImporter::loadCast);
    }

    private <T> ConcurrentLinkedQueue<QueryResourceInfo> load(File jsonFile,LoadRow<T> loader) throws IOException {
        responses.clear();

        GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(jsonFile.getPath()));
        BufferedReader br = new BufferedReader(new InputStreamReader(gzip));
        //go over all the lines and translate to graph elements
        Stream<String> lines = br.lines();
        Iterator<String> iterator = lines.iterator();

        //print file
        System.out.println(jsonFile.getName());
        System.out.println(iterator.next());

        //create initial graph model
        LogicalGraphModel graphModel = new LogicalGraphModel();
        int counter = 0;
        //work with fuse uploader with predefined bulk size - graphModel is the bulk unit
        while (iterator.hasNext()) {
            if(counter>=bulkSize) {
/*
                //upload async
                fuseClientService.asyncUpload(graphModel)
                        .thenAccept(this::notify);
*/
                fuseClientService.upload(graphModel);
                //restart counter and create a new graphModel
                counter=0;
                graphModel = new LogicalGraphModel();
            }
            //populate title node in the graph graphModel
            loader.loadRow(graphModel,iterator.next());
            counter++;

        }
        //wait for all threads to be idle
        ForkJoinPool.commonPool().awaitQuiescence(timeout, TimeUnit.MINUTES);
        return responses;
    }


    private void notify(QueryResourceInfo queryResourceInfo) {
        responses.add(queryResourceInfo);
    }


    interface LoadRow<T> {
        T loadRow(LogicalGraphModel model,String line);
    }

}
