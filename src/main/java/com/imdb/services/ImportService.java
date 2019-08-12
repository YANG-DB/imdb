package com.imdb.services;

import java.io.IOException;
import java.util.Set;

public interface ImportService {
    Set<String> importEntities(String fuseDataLocation) throws IOException;
}
