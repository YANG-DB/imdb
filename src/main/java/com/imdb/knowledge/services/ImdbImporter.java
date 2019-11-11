package com.imdb.knowledge.services;

import com.imdb.model.IMDB;
import com.yangdb.fuse.model.logical.LogicalEdge;
import com.yangdb.fuse.model.logical.LogicalGraphModel;
import com.yangdb.fuse.model.logical.LogicalNode;
import javaslang.Tuple2;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

public class ImdbImporter {
    private static final SimpleDateFormat targetDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    static {
        targetDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * load title synonyms
     * @param model
     * @param line
     * @return
     */
    public static IMDB.Row<IMDB.TitleAKA> loadTitleAKA(LogicalGraphModel model, String line) {
        IMDB.Row<IMDB.TitleAKA> row = IMDB.TitleAKA.of(Arrays.asList(line.split("[\t]")));
        String id = String.format("%s.%s", row.id()._2.toString(), row.columns().get(0)._2.toString());
        //complex PK
        LogicalNode node = new LogicalNode(id, IMDB.TitleAKA.class.getSimpleName());
        //update metadata
        node.getMetadata().addProperties("creationTime", targetDateFormat.format(new Date()));
        node.getMetadata().addProperties("creationUser", "Me");

        //skip first enum column - used to calcuate id
        row.columns(p->p._1()!= IMDB.TitleAKA.ordering)
                .forEach(c -> node.withProperty(c._1.name(), c._2));
        //add node to graph
        model.getNodes().add(node);

        //add edge from current node to title node
        String edgeId = String.format("%s.%s", id, row.id()._2.toString());
        String label = String.format("has%s", IMDB.TitleAKA.class.getSimpleName());
        LogicalEdge edge = new LogicalEdge(edgeId, label,id,row.id()._2.toString(),false);
        //metadata
        edge.getMetadata().addProperties("creationTime", targetDateFormat.format(new Date()));
        edge.getMetadata().addProperties("creationUser", "Me");
        model.getEdges().add(edge);
        return row;
    }

    /**
     * load title
     * @param model
     * @param line
     * @return
     */
    public static IMDB.Row<IMDB.Title> loadTitle(LogicalGraphModel model, String line) {
        IMDB.Row<IMDB.Title> row = IMDB.Title.of(Arrays.asList(line.split("[\t]")));
        LogicalNode node = new LogicalNode(row.id()._2.toString(), IMDB.Title.class.getSimpleName());
        //update metadata
        node.getMetadata().addProperties("creationTime", targetDateFormat.format(new Date()));
        node.getMetadata().addProperties("creationUser", "Me");

        row.columns().forEach(c -> node.withProperty(c._1.name(), c._2));
        //add node to graph
        model.getNodes().add(node);
        return row;
    }

    /**
     * import relationship between a title and the director/writer
     * @param model
     * @param line
     * @return
     */
    public static IMDB.Row<IMDB.Crew> loadCrew(LogicalGraphModel model, String line) {
        IMDB.Row<IMDB.Crew> row = IMDB.Crew.of(Arrays.asList(line.split("[\t]")));
        String title = row.id()._2().toString();

        //create title->hasDirector edge
        if(!row.columns(p -> p._1() == IMDB.Crew.directors).isEmpty()) {
            Arrays.asList(row.columns(p -> p._1() == IMDB.Crew.directors).get(0)._2().toString().split("[,]"))
                    .forEach(director -> {
                        //add edge from current node to title node
                        String edgeId = String.format("%s.%s", title,director);
                        LogicalEdge edge = new LogicalEdge(edgeId, "hasDirector", title,director, true);
                        //metadata
                        edge.getMetadata().addProperties("creationTime", targetDateFormat.format(new Date()));
                        edge.getMetadata().addProperties("creationUser", "Me");
                        model.getEdges().add(edge);
                    });
        }

        if(!row.columns(p -> p._1() == IMDB.Crew.writers).isEmpty()) {
                Arrays.asList(row.columns(p -> p._1() == IMDB.Crew.writers).get(0)._2().toString().split("[,]"))
                        .forEach(writer -> {
                            //add edge from current node to title node
                            String edgeId = String.format("%s.%s", title,writer);
                            LogicalEdge edge = new LogicalEdge(edgeId, "hasWriter", title,writer, true);
                            //metadata
                            edge.getMetadata().addProperties("creationTime", targetDateFormat.format(new Date()));
                            edge.getMetadata().addProperties("creationUser", "Me");
                            model.getEdges().add(edge);
                        });
        }

        return row;
    }

    /**
     * load principal
     * @param model
     * @param line
     * @return
     */
    public static IMDB.Row<IMDB.Cast> loadCast(LogicalGraphModel model, String line) {
        IMDB.Row<IMDB.Cast> row = IMDB.Cast.of(Arrays.asList(line.split("[\t]")));
        //todo
        return row;
    }

    /**
     * load episode
     * @param model
     * @param line
     * @return
     */
    public static IMDB.Row<IMDB.Episods> loadEpisode(LogicalGraphModel model, String line) {
        IMDB.Row<IMDB.Episods> row = IMDB.Episods.of(Arrays.asList(line.split("[\t]")));
        //todo
        return row;
    }

    /**
     * load rating - will be merged into the existing title
     * @param model
     * @param line
     * @return
     */
    public static IMDB.Row<IMDB.Rating> loadRating(LogicalGraphModel model, String line) {
        IMDB.Row<IMDB.Rating> row = IMDB.Rating.of(Arrays.asList(line.split("[\t]")));
        //title id
        String id = row.id()._2.toString();

        //add value to title node
        LogicalNode node = new LogicalNode(id, IMDB.Rating.class.getSimpleName());
        row.columns()
            .forEach(c -> node.withProperty(c._1.name(), c._2));

        //add node to graph
        model.getNodes().add(node);

        return row;
    }

    /**
     * import actors,directors,writer,cast,crew
     * @param model
     * @param line
     * @return
     */
    public static IMDB.Row<IMDB.Person> loadPeople(LogicalGraphModel model, String line) {
        IMDB.Row<IMDB.Person> row = IMDB.Person.of(Arrays.asList(line.split("[\t]")));
        //person id
        String id = row.id()._2.toString();

        LogicalNode node = new LogicalNode(id, IMDB.Person.class.getSimpleName());
        //update metadata
        node.getMetadata().addProperties("creationTime", targetDateFormat.format(new Date()));
        node.getMetadata().addProperties("creationUser", "Me");

        row.columns(p->p._1()!= IMDB.Person.knownForTitles)
                .forEach(c -> node.withProperty(c._1.name(), c._2));

        //add node to graph
        model.getNodes().add(node);


        if(row.columns(p -> p._1() == IMDB.Person.knownForTitles).isEmpty()) return row;

        //fill relations to title
        Tuple2<IMDB.Person, Object> titlesRow = row.columns(p -> p._1() == IMDB.Person.knownForTitles).get(0);
        String[] titles = titlesRow._2().toString().split("[,]");
        Arrays.asList(titles).forEach(title-> {
            //add edge from current node to title node
            String edgeId = String.format("%s.%s", id, title);
            String label = String.format("has%s", IMDB.Title.class.getSimpleName());
            LogicalEdge edge = new LogicalEdge(edgeId, label, id, title, true);
            //metadata
            edge.getMetadata().addProperties("creationTime", targetDateFormat.format(new Date()));
            edge.getMetadata().addProperties("creationUser", "Me");
            model.getEdges().add(edge);
        });
        return row;
    }

}
