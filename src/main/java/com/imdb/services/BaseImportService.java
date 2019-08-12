package com.imdb.services;

import com.yangdb.fuse.client.FuseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
@ComponentScan({"com.imdb.*"})
public class BaseImportService implements ImportService {
    private static final Logger logger = LoggerFactory.getLogger(BaseImportService.class);

    @Inject
    private FuseClientService fuseClientService;

    @Override
    public Set<String> importEntities(String dir) throws IOException {
        File folder = new File(dir);
        if (!folder.exists() || !folder.isDirectory() || folder.listFiles() == null || folder.listFiles().length == 0) {
            //logger.error("{} in not a valid directory", dir);
            return Collections.emptySet();
        }

        Set<String> results = new HashSet<>();
        for (File jsonFile : Objects.requireNonNull(folder.listFiles())) {
            if (jsonFile.isFile() && jsonFile.getName().endsWith("json")) {
                results.addAll(load(jsonFile));
            }
        }

        return results;
    }

    private Set<String> load(File jsonFile) {
/*
        try (Stream<String> stream = Files.lines(Paths.get(jsonFile.getPath()))) {
            LogicalGraphModel graphModel = transformer.transform(stream.map(wrapIgnorErr(JSONObject::new))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
            return fuseClientService.upload(graphModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

        return Collections.emptySet();
    }


}
