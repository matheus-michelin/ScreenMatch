package com.example.Spring.Web.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("totalSeasons") Integer totalTemporadas,
                         @JsonAlias("imdbRating") String avaliacao,
                         @JsonAlias("Genre") String genero,
                         @JsonAlias("Actors") String atores,
                         @JsonAlias("Poster") String poster,
                         @JsonAlias("Plot") String sinopse) {

    @Override
    public String toString() {
        return "Título: " + titulo +
                ", Total de Temporadas: " + totalTemporadas +
                ", Avaliação: " + avaliacao +
                ", Gênero:  " + genero +
                ", Atores: " + atores +
                ", Poster: " + poster +
                ", Sinopse: " + sinopse;
    }
}
