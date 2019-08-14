package com.imdb.model;

import javaslang.Tuple2;

import java.util.List;
import java.util.stream.Collectors;

import static com.imdb.model.Model.$;
import static com.imdb.model.Type.*;

public interface IMDB {


    enum TitleAKA {
        titleId($("titleId", "TitleAKA", FK)),
        ordering($("ordering", "stringValue", FIELD)),
        title($("title", "stringValue", FIELD)),
        region($("region", "stringValue", FIELD)),
        types($("types", "stringValue", FIELD)),
        attributes($("types", "stringValue", FIELD)),
        isOriginalTitle($("isOriginalTitle", "boolean", FIELD));

        private Model model;

        TitleAKA(Model model) {
            this.model = model;
        }

        public static Row<TitleAKA> of(List<String> fieldValues) {
            return new Row<>(new Tuple2<>(TitleAKA.titleId, fieldValues.get(0)),
                    fieldValues.stream().skip(1)
                            .filter(v->fieldValues.indexOf(v)<=(TitleAKA.values().length-1))
                            .map(v -> new Tuple2<TitleAKA, Object>(TitleAKA.values()[fieldValues.indexOf(v)], v)).
                            collect(Collectors.toList()));
        }
    }

    enum Title {
        tconst($("tconst", "techId", METADATA)),
        titleType($("titleType", "stringValue", FIELD)),
        primaryTitle($("primaryTitle", "stringValue", FIELD)),
        originalTitle($("originalTitle", "stringValue", FIELD)),
        isAdult($("isAdult", "boolean", FIELD)),
        //YYYY format
        startYear($("startYear", "date", FIELD)),
        //YYYY format
        endYear($("endYear", "date", FIELD)),
        runtimeMinutes($("runtimeMinutes", "int", FIELD)),
        genres($("genres", "stringValue", FIELD));

        Title(Model model) {
            this.model = model;
        }

        private Model model;

        public static Row<Title> of(List<String> fieldValues) {
            return new Row<>(new Tuple2<>(Title.tconst, fieldValues.get(0)),
                    fieldValues.stream().skip(1)
                            .filter(v->fieldValues.indexOf(v)<=(Title.values().length-1))
                            .map(v -> new Tuple2<Title, Object>(Title.values()[fieldValues.indexOf(v)], v)).
                            collect(Collectors.toList()));
        }

    }

    enum Crew {
        tconst($("tconst", "TitleAKA", FK)),
        directors($("directors", "stringValue", FIELD)),
        writers($("writers", "stringValue", FIELD));

        Crew(Model model) {
            this.model = model;
        }

        private Model model;

    }

    enum Episods {
        tconst($("tconst", "TitleAKA", FK)),
        parentTconst($("parentTconst", "TitleAKA", FK)),
        seasonNumber($("seasonNumber", "int", FIELD)),
        episodeNumber($("episodeNumber", "int", FIELD));

        Episods(Model model) {
            this.model = model;
        }

        private Model model;

    }

    enum Cast {
        tconst($("tconst", "TitleAKA", FK)),
        nconst($("nconst", "Person", FK)),
        ordering($("ordering", "int", FIELD)),
        category($("category", "stringValue", FIELD)),
        job($("job", "stringValue", FIELD)),
        characters($("characters", "stringValue", FIELD));

        Cast(Model model) {
            this.model = model;
        }

        private Model model;

    }

    enum Rating {
        tconst($("tconst", "TitleAKA", FK)),
        averageRating($("averageRating", "int", FIELD)),
        numVotes($("numVotes", "long", FIELD));

        Rating(Model model) {
            this.model = model;
        }

        private Model model;

    }

    enum Person {
        tconst($("tconst", "Person", FK)),
        primaryName($("primaryName", "stringValue", FIELD)),
        birthYear($("birthYear", "date", FIELD)),
        deathYear($("deathYear", "date", FIELD)),
        primaryProfession($("primaryProfession", "stringValue", FIELD)),
        knownForTitles($("knownForTitles", "stringValue", FIELD));

        Person(Model model) {
            this.model = model;
        }

        private Model model;

    }


    class Row<T> {
        Tuple2<T, Object> id;
        List<Tuple2<T, Object>> columns;


        public Row(Tuple2<T, Object> id, List<Tuple2<T, Object>> columns) {
            this.id = id;
            this.columns = columns;
        }

        public Tuple2<T, Object> id() {
            return id;
        }

        public List<Tuple2<T, Object>> columns() {
            return columns;
        }
    }
}
