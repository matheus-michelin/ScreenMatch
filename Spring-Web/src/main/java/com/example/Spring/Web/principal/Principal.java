package com.example.Spring.Web.principal;


import com.example.Spring.Web.model.*;
import com.example.Spring.Web.repository.SerieRepository;
import com.example.Spring.Web.service.ConsumoApi;
import com.example.Spring.Web.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private final Scanner scanner = new Scanner(System.in);
    private final ConsumoApi consumo = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    private final List<DadosSerie> dadosSerieList = new ArrayList<>();
    private List<Serie> serieList = new ArrayList<>();

    private Optional<Serie> serieBusca;


    private final SerieRepository repository;

    public Principal(SerieRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu() {

        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar série por título
                    5 - Buscar série por ator
                    6 - Top 5 Séries
                    7 - Buscar série por categoria
                    8 - Filtrar séries por temporada
                    9 - Buscar episódios por nome
                    10 - Top 5 Episódios
                    11 - Buscar episódios por data
                    
                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    bestSeriesByRating();
                    break;
                case 7:
                    findSeriesByCategoria();
                    break;
                case 8:
                    findSerieByTemps();
                    break;
                case 9:
                    findEpBySnippet(); //buscar episódio por trecho
                    break;
                case 10:
                    bestEpsByRating();
                    break;
                case 11:
                    searchEpsByDate();
                    break;
                case 12:
                    listarUnicaSerie();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    return;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }


    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //dadosSerieList.add(dados);
        repository.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = scanner.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome: ");
        var serieName = scanner.nextLine();

        Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(serieName);

        if(serie.isPresent()) {

            var serieFound = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieFound.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieFound.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);


            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieFound.setEpisodios(episodios);
            repository.save(serieFound);
        } else{
            System.out.println("Série não encontrada");
        }
    }

    private void listarSeriesBuscadas(){

        serieList = repository.findAll();

        serieList.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo(){
        System.out.println("Escolha uma série pelo nome: ");
        var serieName = scanner.nextLine();
        serieBusca = repository.findByTituloContainingIgnoreCase(serieName);

        if(serieBusca.isPresent()){
            System.out.println("** DADOS DA SERIE **\n" + serieBusca.get());
        } else {
            System.out.println("Serie não encontrada");
        }

    }

    private void buscarSeriePorAtor(){
        System.out.println("Qual o nome do ator para a busca?");
        var atoresName = scanner.nextLine();

        System.out.println("A partir de que nota?");
        var avaliacoes = scanner.nextDouble();

        List<Serie> serieEncontradas = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(atoresName, avaliacoes);


        System.out.println("Séries em que " + atoresName + " trabalhou:");
        serieEncontradas.forEach(s -> System.out.println(s.getTitulo() + "; Avaliação: " + s.getAvaliacao()));
    }

    private void bestSeriesByRating(){
        List<Serie> topSeries = repository.findTop5ByOrderByAvaliacaoDesc();

        topSeries.forEach(s -> System.out.println(s.getTitulo() + "; Avaliação: " + s.getAvaliacao()));
    }

    private void findSeriesByCategoria(){
        System.out.println("Deseja buscar séries de que gênero? ");
        var genreName = scanner.nextLine();
        Categoria categoria = Categoria.fromPortuguese(genreName);
        List<Serie> categoriaSeries = repository.findByGenero(categoria);
        System.out.println("Séries da categoria " + genreName + " encontradas:");
        categoriaSeries.forEach(System.out::println);
    }

    private void findSerieByTemps(){
        System.out.println("Qual o máximo de temporadas da série que deseja buscar?");
        var maxTempSerie = scanner.nextInt();
        System.out.println("E a partir de qual avaliação?");
        var avaliacao = scanner.nextDouble();
        List<Serie> tempSerie = repository.seriePorTemporadaEAvaliacao(maxTempSerie, avaliacao);
        tempSerie.forEach(s -> System.out.println(s.getTitulo() + "; Avaliação: " + s.getAvaliacao()));
    }

    private void findEpBySnippet(){
        System.out.println("Qual o nome do episódio para a busca?");
        var epName = scanner.nextLine();
        List<Episodio> episodeFound = repository.episodiosPorTrecho(epName);

        episodeFound.forEach(e -> System.out.printf("Série: %s, Temporada: %s - Episódio: %s - %s\n",
                e.getSerie().getTitulo(), e.getTemporada(),
                e.getNumeroEpisodio(), e.getTitulo()));
    }

    private void bestEpsByRating(){
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodio> best5Eps = repository.bestEpsByRating(serie);

            best5Eps.forEach(e -> System.out.printf("Série: %s, Temporada: %s - Episódio: %s - %s\n",
                    e.getSerie().getTitulo(), e.getTemporada(),
                    e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao()));
        }
    }

    private void searchEpsByDate(){
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()) {
            Serie serie = serieBusca.get();
            System.out.println("Digite o ano limite de lançamento: ");
            var anoLancamento = scanner.nextInt();
            scanner.nextLine();

            List<Episodio> epsAno = repository.epsBySerieAndYear(serie, anoLancamento);
            epsAno.forEach(System.out::println);
        }
    }

    private void listarUnicaSerie() {
        System.out.println("Qual série você quer conhecer?");
        var serieWanted = scanner.nextLine();
        List<Serie> serie = repository.findFirstByTituloContainingIgnoreCase(serieWanted);

        serie.stream()
                .forEach(s -> System.out.printf("Título: " + s.getTitulo() + "\n"
                        + "Total de Temporadas: " + s.getTotalTemporadas() + "\n"
                        + "Avaliação: " + s.getAvaliacao() + "\n"
                        + "Gênero: " + s.getGenero() + "\n"
                        + "Atores: " + s.getAtores() + "\n"
                        + "Pôster: " + s.getPoster() + "\n"
                        + "Sinopse: " + s.getSinopse() + "\n"));







    }
}
