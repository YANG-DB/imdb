package com.imdb.services;

import com.yangdb.fuse.model.logical.LogicalGraphModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;

@Component
@ComponentScan({"com.imdb.*"})
public class BaseQueryService implements QueryService {
    private static final Logger logger = LoggerFactory.getLogger(BaseQueryService.class);

    @Inject
    private FuseClientService service;


    @Override
    public LogicalGraphModel postQuery(String query) throws IOException, InterruptedException {
        return service.postQuery(query);
    }
}
