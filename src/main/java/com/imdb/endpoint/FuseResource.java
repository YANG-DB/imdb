package com.imdb.endpoint;

import com.imdb.services.ImportService;
import com.imdb.services.QueryService;
import com.yangdb.fuse.model.logical.LogicalGraphModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import java.util.Set;

@RestController
@RequestMapping
public class FuseResource {
    private final static Logger logger = LoggerFactory.getLogger(FuseResource.class);

    @Inject
    private QueryService queryService;
    @Inject
    private ImportService importService;


    @PostMapping("/query")
    public LogicalGraphModel query(@RequestParam String query) {
        try {
            return queryService.postQuery(query);
        } catch (IllegalArgumentException e) {
            logger.info("Syntax Error: {}", query);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Syntax Error");
        } catch (Throwable e) {
            logger.error("Error posting query", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Error posting query");
        }
    }

    @PostMapping("/import")
    public Set<String> post(@RequestParam String folderPath) {
        try {
            return importService.importEntities(folderPath);
        } catch (Throwable e) {
            logger.error("Error uploading entities", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not upload entities");
        }
    }

}
