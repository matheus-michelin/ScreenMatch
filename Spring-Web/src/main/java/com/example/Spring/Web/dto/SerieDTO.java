package com.example.Spring.Web.dto;

import com.example.Spring.Web.model.Categoria;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record SerieDTO(Long id,

                       String titulo,

                       Integer totalTemporadas,

                       Double avaliacao,

                       Categoria genero,

                       String atores,

                       String poster,

                       String sinopse) {
}
