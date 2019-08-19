package com.imdb.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yangdb.fuse.client.BaseFuseClient;
import com.yangdb.fuse.client.FuseClient;
import com.yangdb.fuse.model.logical.LogicalEdge;
import com.yangdb.fuse.model.logical.LogicalGraphModel;
import com.yangdb.fuse.model.logical.LogicalNode;
import com.yangdb.fuse.model.resourceInfo.CursorResourceInfo;
import com.yangdb.fuse.model.resourceInfo.FuseResourceInfo;
import com.yangdb.fuse.model.resourceInfo.PageResourceInfo;
import com.yangdb.fuse.model.resourceInfo.QueryResourceInfo;
import com.yangdb.fuse.model.results.AssignmentsQueryResult;
import com.yangdb.fuse.model.results.QueryResultBase;
import com.yangdb.fuse.model.transport.CreatePageRequest;
import com.yangdb.fuse.model.transport.cursor.LogicalGraphCursorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class FuseClientService {

    private static final Logger logger = LoggerFactory.getLogger(FuseClientService.class);
    private static final String KNOWLEDGE = "Knowledge";
    private static final int FUSE_TIMEOUT_MAX_COUNTER = 20;
    public static final int PAGE_SIZE = 1000;


    @Value("${fuse.url}")
    private String fuseUrl;

    private FuseClient fuseClient;
    private ObjectMapper objectMapper;
    private FuseResourceInfo fuseResourceInfo;


    @PostConstruct
    private void init() {
        try {
            fuseClient = new BaseFuseClient(fuseUrl);
            fuseResourceInfo = fuseClient.getFuseInfo();
            objectMapper = new ObjectMapper();
        } catch (IOException e) {
            logger.error("Could not connect to fuse", e);
        }
    }


    public LogicalGraphModel postQuery(String query) throws IOException, InterruptedException {
        // get Query URL
        QueryResourceInfo queryResourceInfo = fuseClient.postQuery(fuseResourceInfo.getQueryStoreUrl(), query, KNOWLEDGE);
        // Press on Cursor
        CursorResourceInfo cursorResourceInfo = fuseClient.postCursor(queryResourceInfo.getCursorStoreUrl(), new LogicalGraphCursorRequest(new CreatePageRequest()));

//        QueryResultBase pageData = query(fuseClient, fuseResourceInfo,100, query, KNOWLEDGE);
        TypeReference<AssignmentsQueryResult<LogicalNode, LogicalEdge>> typeReference = new TypeReference<AssignmentsQueryResult<LogicalNode, LogicalEdge>>() {
        };
        AssignmentsQueryResult<LogicalNode, LogicalEdge> pageData = (AssignmentsQueryResult<LogicalNode, LogicalEdge>) nextPage(fuseClient, cursorResourceInfo, typeReference, PAGE_SIZE);

        LogicalGraphModel model = new LogicalGraphModel() {
            @Override
            public List<LogicalNode> getNodes() {
                return pageData.getAssignments().get(0).getEntities();
            }

            @Override
            public List<LogicalEdge> getEdges() {
                return pageData.getAssignments().get(0).getRelationships();
            }
        };

        return model;
    }

    private QueryResultBase nextPage(FuseClient fuseClient, CursorResourceInfo cursorResourceInfo, TypeReference typeReference, int pageSize) throws IOException, InterruptedException {
        PageResourceInfo pageResourceInfo = getPageResourceInfo(fuseClient, cursorResourceInfo, pageSize);
        // return the relevant data
        return fuseClient.getPageData(pageResourceInfo.getDataUrl(), typeReference);
    }

    private PageResourceInfo getPageResourceInfo(FuseClient fuseClient, CursorResourceInfo cursorResourceInfo, int pageSize) throws IOException, InterruptedException {
        PageResourceInfo pageResourceInfo = fuseClient.postPage(cursorResourceInfo.getPageStoreUrl(), pageSize);
        // Waiting until it gets the response
        int i = 1;
        while (!pageResourceInfo.isAvailable()) {
            pageResourceInfo = fuseClient.getPage(pageResourceInfo.getResourceUrl());
            if (i++ == FUSE_TIMEOUT_MAX_COUNTER) {
                throw new IOException("Could not get response page");
            } else if (!pageResourceInfo.isAvailable()) {
                Thread.sleep(10);
            }
        }
        return pageResourceInfo;
    }

    public QueryResourceInfo load(LogicalGraphModel graphModel,boolean upsert) {
        Path path = null;
        try {
            path = Files.createTempFile("tmpGraphModel", ".json");
            objectMapper.writeValue(path.toFile(),graphModel);
            return load(path,upsert);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if(path!=null)//Delete file on exit
                path.toFile().deleteOnExit();

        }
    }

    private QueryResourceInfo load(Path path,boolean upsert) throws IOException {
        if(upsert)
            // load without merge
            return fuseClient.upsertData(KNOWLEDGE, path.toFile().toURL());
        //merge
        return fuseClient.loadData(KNOWLEDGE, path.toFile().toURL());
    }

    public CompletableFuture<QueryResourceInfo> asyncUpload(LogicalGraphModel model,boolean upsert) {
        return CompletableFuture.supplyAsync(() -> load(model,upsert));
    }
}
