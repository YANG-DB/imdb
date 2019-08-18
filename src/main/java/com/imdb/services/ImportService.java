package com.imdb.services;

import com.yangdb.fuse.model.resourceInfo.QueryResourceInfo;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public interface ImportService {

    Collection<QueryResourceInfo> loadTitleAKA(File jsonFile) throws IOException;
    Collection<QueryResourceInfo> loadTitle(File jsonFile) throws IOException;
    Collection<QueryResourceInfo> loadCrew(File jsonFile) throws IOException;
    Collection<QueryResourceInfo> loadCast(File jsonFile) throws IOException;
    Collection<QueryResourceInfo> loadPeople(File jsonFile) throws IOException;
    Collection<QueryResourceInfo> loadRating(File jsonFile) throws IOException;
    Collection<QueryResourceInfo> loadEpisode(File jsonFile) throws IOException;

}
