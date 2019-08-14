package com.imdb.model;

public final class Model {
    private final String name;
    private final String schemaName;
    private final Type type;

    public Model(String name, String schemaName, Type type) {
        this.name = name;
        this.schemaName = schemaName;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public Type getType() {
        return type;
    }

    public static Model $(String name, String schemaName, Type type) {
        return new Model(name,schemaName, type);
    }
}
