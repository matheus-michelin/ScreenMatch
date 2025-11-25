package com.example.Spring.Web.controller;

import com.example.Spring.Web.dto.SerieDTO;
import com.example.Spring.Web.dto.EpisodioDTO;
import com.example.Spring.Web.model.Categoria;
import com.example.Spring.Web.model.Serie;
import com.example.Spring.Web.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService service;

    @GetMapping
    public List<SerieDTO> obterSeries(){
        return service.obterSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> getTop5Series(){
        return service.getTop5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO>obterLancamentos() {
        return service.getReleases();
    }

    @GetMapping("/{id}")
    public SerieDTO getFromID(@PathVariable Long id){
        return service.getFromID(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasTemporadas(@PathVariable Long id){
        return service.getAllTemps(id);
    }


    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDTO> getTempByNumber(@PathVariable Long id, @PathVariable int numero){
        return service.getEpByNumberTemp(id, numero);
    }

    @GetMapping("/categoria/{nomeCategoria}")
    public List<SerieDTO> getSerieByCategoria(@PathVariable String nomeCategoria){

        return service.getSeriesByCategoria(nomeCategoria);
    }
}