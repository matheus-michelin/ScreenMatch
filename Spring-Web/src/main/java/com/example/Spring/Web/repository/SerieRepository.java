package com.example.Spring.Web.repository;


import com.example.Spring.Web.model.Categoria;
import com.example.Spring.Web.model.Episodio;
import com.example.Spring.Web.model.Serie;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String serieName);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String atoresName, double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);

    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer maxTempSerie, double avaliacao);

    @Query("select s from Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
    List<Serie> seriePorTemporadaEAvaliacao(int totalTemporadas, double avaliacao);

    @Query("SELECT e FROM Serie s JOIN s.episodioList e WHERE e.titulo ILIKE %:epName%")
    List<Episodio> episodiosPorTrecho(String epName);

    @Query("SELECT e FROM Serie s JOIN s.episodioList e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> bestEpsByRating(Serie serie);

    @Query("SELECT e FROM Serie s JOIN s.episodioList e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> epsBySerieAndYear(Serie serie, int anoLancamento);

    List<Serie> findFirstByTituloContainingIgnoreCase(String serieWanted);

    List<Serie> findTop5ByOrderByEpisodioListDataLancamentoDesc();

    @Query("SELECT e FROM Serie s JOIN s.episodioList e WHERE s.id = :id AND e.temporada = :numero")
    List<Episodio> getEpsByTemp(Long id, Integer numero);
}
