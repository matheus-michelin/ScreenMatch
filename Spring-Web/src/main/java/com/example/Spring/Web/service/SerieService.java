package com.example.Spring.Web.service;

import com.example.Spring.Web.dto.EpisodioDTO;
import com.example.Spring.Web.dto.SerieDTO;
import com.example.Spring.Web.model.Categoria;
import com.example.Spring.Web.model.Serie;
import com.example.Spring.Web.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {



    @Autowired
    private SerieRepository repository;

    private List<SerieDTO> converteDados(List<Serie> series){
        return series.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(),
                        s.getAvaliacao(), s.getGenero(), s.getAtores(), s.getPoster(),
                        s.getSinopse()))

                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterSeries(){
        return converteDados(repository.findAll());
    }


    public List<SerieDTO> getTop5Series(){
        return converteDados(repository.findTop5ByOrderByAvaliacaoDesc());
    }


    public List<SerieDTO> getReleases(){
        return converteDados(repository.findTop5ByOrderByEpisodioListDataLancamentoDesc());
    }

    public SerieDTO getFromID(Long id){
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()){
            Serie s = serie.get();
            return new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(),
                    s.getAvaliacao(), s.getGenero(), s.getAtores(), s.getPoster(),
                    s.getSinopse());
        }
        return null;
    }


    public List<EpisodioDTO> getAllTemps(Long id) {

        Optional<Serie> serie = repository.findById(id);

        if(serie.isPresent()){
            Serie s = serie.get();
            return s.getEpisodioList().stream()
                    .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodioDTO> getEpByNumberTemp(Long id, int numero) {
        return repository.getEpsByTemp(id, numero)
                .stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> getSeriesByCategoria(String nomeCategoria){
        Categoria categoria = Categoria.fromPortuguese(nomeCategoria);
        return converteDados(repository.findByGenero(categoria));
    }
}
