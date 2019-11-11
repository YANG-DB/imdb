package com.imdb.knowledge.services;

import com.yangdb.fuse.model.logical.LogicalGraphModel;

import java.io.IOException;

public interface QueryService {
    LogicalGraphModel postQuery(String query) throws IOException, InterruptedException;
}
