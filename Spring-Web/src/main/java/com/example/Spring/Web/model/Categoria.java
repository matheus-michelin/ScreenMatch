package com.example.Spring.Web.model;

public enum Categoria {
    ACAO("Action", "Ação"),

    ROMANCE("Romance", "Romance"),

    COMEDIA("Comedy", "Comédia"),

    DRAMA("Drama", "Drama"),

    CRIME("Crime", "Crime");


    private final String categoriaOmdb;

    private String categoriaPortugues;

    Categoria(String categoriaOMDB, String categoriaPortugues){
        this.categoriaOmdb = categoriaOMDB;
        this.categoriaPortugues = categoriaPortugues;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

    public static Categoria fromPortuguese(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaPortugues.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}

