package com.imdb.model;

import javaslang.Tuple2;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.imdb.model.Model.$;
import static com.imdb.model.Type.*;

public interface IMDB {


    enum TitleAKA {
        titleId($("titleId", TitleAKA.class.getSimpleName(), FK)),
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
        tconst($("tconst", Title.class.getSimpleName(), FK)),
        directors($("directors", "stringValue", FIELD)),
        writers($("writers", "stringValue", FIELD));

        Crew(Model model) {
            this.model = model;
        }

        private Model model;

        public static Row<Crew> of(List<String> fieldValues) {
            return new Row<>(new Tuple2<>(Crew.tconst, fieldValues.get(0)),
                    fieldValues.stream().skip(1)
                            .filter(v->fieldValues.indexOf(v)<=(Crew.values().length-1))
                            .map(v -> new Tuple2<Crew, Object>(Crew.values()[fieldValues.indexOf(v)], v)).
                            collect(Collectors.toList()));
        }

    }

    enum Episods {
        tconst($("tconst", Title.class.getSimpleName(), FK)),
        parentTconst($("parentTconst", "TitleAKA", FK)),
        seasonNumber($("seasonNumber", "int", FIELD)),
        episodeNumber($("episodeNumber", "int", FIELD));

        Episods(Model model) {
            this.model = model;
        }

        private Model model;

        public static Row<Episods> of(List<String> fieldValues) {
            return new Row<>(new Tuple2<>(Episods.tconst, fieldValues.get(0)),
                    fieldValues.stream().skip(1)
                            .filter(v->fieldValues.indexOf(v)<=(Episods.values().length-1))
                            .map(v -> new Tuple2<Episods, Object>(Episods.values()[fieldValues.indexOf(v)], v)).
                            collect(Collectors.toList()));
        }

    }

    enum Cast {
        tconst($("tconst", Title.class.getSimpleName(), FK)),
        nconst($("nconst", "Person", FK)),
        ordering($("ordering", "int", FIELD)),
        category($("category", "stringValue", FIELD)),
        job($("job", "stringValue", FIELD)),
        characters($("characters", "stringValue", FIELD));

        Cast(Model model) {
            this.model = model;
        }

        private Model model;

        public static Row<Cast> of(List<String> fieldValues) {
            return new Row<>(new Tuple2<>(Cast.tconst, fieldValues.get(0)),
                    fieldValues.stream().skip(1)
                            .filter(v->fieldValues.indexOf(v)<=(Cast.values().length-1))
                            .map(v -> new Tuple2<Cast, Object>(Cast.values()[fieldValues.indexOf(v)], v)).
                            collect(Collectors.toList()));
        }

    }

    enum Rating {
        tconst($("tconst", Title.class.getSimpleName(), FK)),
        averageRating($("averageRating", "int", FIELD)),
        numVotes($("numVotes", "long", FIELD));

        Rating(Model model) {
            this.model = model;
        }

        private Model model;

        public static Row<Rating> of(List<String> fieldValues) {
            return new Row<>(new Tuple2<>(Rating.tconst, fieldValues.get(0)),
                    fieldValues.stream().skip(1)
                            .filter(v->fieldValues.indexOf(v)<=(Rating.values().length-1))
                            .map(v -> new Tuple2<Rating, Object>(Rating.values()[fieldValues.indexOf(v)], v)).
                            collect(Collectors.toList()));
        }

    }

    enum Person {
        tconst($("tconst", "techId", METADATA)),
        primaryName($("primaryName", "stringValue", FIELD)),
        birthYear($("birthYear", "date", FIELD)),
        deathYear($("deathYear", "date", FIELD)),
        primaryProfession($("primaryProfession", "stringValue", FIELD)),
        knownForTitles($("knownForTitles", Title[].class.getSimpleName(), FK));

        Person(Model model) {
            this.model = model;
        }

        private Model model;

        public static Row<Person> of(List<String> fieldValues) {
            return new Row<>(new Tuple2<>(Person.tconst, fieldValues.get(0)),
                    fieldValues.stream().skip(1)
                            .filter(v->fieldValues.indexOf(v)<=(Person.values().length-1))
                            .map(v -> new Tuple2<Person, Object>(Person.values()[fieldValues.indexOf(v)], v)).
                            collect(Collectors.toList()));
        }

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

        public List<Tuple2<T, Object>> columns(Predicate<Tuple2<T, Object>> filter) {
            return columns.stream().filter(filter).collect(Collectors.toList());
        }


    }
}
