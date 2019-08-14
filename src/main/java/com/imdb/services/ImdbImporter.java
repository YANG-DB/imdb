package com.imdb.services;

import com.imdb.model.IMDB;
import com.yangdb.fuse.model.logical.LogicalEdge;
import com.yangdb.fuse.model.logical.LogicalGraphModel;
import com.yangdb.fuse.model.logical.LogicalNode;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

public class ImdbImporter {
    private static final SimpleDateFormat targetDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    static {
        targetDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static IMDB.Row<IMDB.TitleAKA> loadTitleAKA(LogicalGraphModel model, String line) {
        IMDB.Row<IMDB.TitleAKA> row = IMDB.TitleAKA.of(Arrays.asList(line.split("[\t]")));
        String id = String.format("%s.%s", row.id()._2.toString(), row.columns().get(0)._2.toString());
        LogicalNode node = new LogicalNode(id, IMDB.TitleAKA.class.getSimpleName());
        //update metadata
        node.getMetadata().addProperties("creationTime", targetDateFormat.format(new Date()));
        node.getMetadata().addProperties("creationUser", "Me");
//        node.getMetadata().addProperties("lastUpdateTime", targetDateFormat.format(new Date()));
//        node.getMetadata().addProperties("lastUpdateUser", updateUserID);

        row.columns().stream().skip(1).forEach(c -> node.withProperty(c._1.name(), c._2));
        //add node to graph
        model.getNodes().add(node);

        //add edge from current node to title node
        String edgeId = String.format("%s.%s", id, row.id()._2.toString());
        String label = String.format("has%s", IMDB.TitleAKA.class.getSimpleName());
        LogicalEdge edge = new LogicalEdge(edgeId, label,id,row.id()._2.toString(),false);
        model.getEdges().add(edge);
        return row;
    }

    public static IMDB.Row<IMDB.Title> loadTitle(LogicalGraphModel model, String line) {
        IMDB.Row<IMDB.Title> row = IMDB.Title.of(Arrays.asList(line.split("[\t]")));
        LogicalNode node = new LogicalNode(row.id()._2.toString(), IMDB.Title.class.getSimpleName());
        //update metadata
        node.getMetadata().addProperties("creationTime", targetDateFormat.format(new Date()));
        node.getMetadata().addProperties("creationUser", "Me");
//        node.getMetadata().addProperties("lastUpdateTime", targetDateFormat.format(new Date()));
//        node.getMetadata().addProperties("lastUpdateUser", updateUserID);

        row.columns().forEach(c -> node.withProperty(c._1.name(), c._2));
        //add node to graph
        model.getNodes().add(node);
        return row;
    }
}
