package br.com.alura.literAlura.principal;

import br.com.alura.literAlura.model.Autor;
import br.com.alura.literAlura.model.Livro;

import br.com.alura.literAlura.reposirory.AutorRepository;
import br.com.alura.literAlura.reposirory.LivroRepository; // Lembre-se do typo 'reposirory'

import br.com.alura.literAlura.service.GutenService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.DoubleSummaryStatistics;
import java.util.InputMismatchException; // Pode ser útil se você usar nextInt() diretamente
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal implements CommandLineRunner {
    private final Scanner scanner = new Scanner(System.in);
    private final GutenService gutenService;

    private final AutorRepository autorRepository;

    public Principal(GutenService gutenService,  AutorRepository autorRepository) {
        this.gutenService = gutenService;
        this.autorRepository = autorRepository;

    }


    @Override
    public void run(String... args) {
        int opcao = -1;
        while (opcao != 0) {
            exibirMenu();
            // AQUI: Adiciona o try-catch para a entrada da opção do menu
            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1 -> buscarLivroPorTitulo();
                    case 2 -> listarLivros();
                    case 3 -> listarAutores();
                    case 4 -> listarAutoresVivosPorAno();
                    case 5 -> listarLivrosPorIdioma();
                    case 6 -> gerarEstatisticasDeDownloads();
                    case 7 -> exibirTop10LivrosMaisBaixados();
                    case 8 -> buscarAutorPorNome();
                    case 0 -> System.out.println("Encerrando o programa.");
                    default -> System.out.println("Opção inválida. Por favor, digite um número de 0 a 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Por favor, digite um número para escolher a opção do menu.");
                // Opcional: Você pode querer limpar o buffer do scanner se usar nextInt()
                // scanner.nextLine(); // Se fosse nextInt(), aqui consumiria o restante da linha
            }
        }
    }




    public void exibirMenu() {
        System.out.println("\n========== LiterAlura ==========");
        System.out.println("1 - Buscar livro por título");
        System.out.println("2 - Listar livros registrados");
        System.out.println("3 - Listar autores registrados");
        System.out.println("4 - Listar autores vivos em um determinado ano");
        System.out.println("5 - Listar livros por idioma");
        System.out.println("6 - Gerar Estatísticas de downloads");
        System.out.println("7 - Top 10 livros mais baixados");
        System.out.println("8 - Buscar Autor por nome");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private void buscarLivroPorTitulo() {
        System.out.print("Digite o título do livro: ");
        String titulo = scanner.nextLine();
        // A lógica de printar o livro já está no GutenService ou pode ser feita aqui
        var livroOptional = gutenService.buscarLivroPorTitulo(titulo);
        livroOptional.ifPresentOrElse(
                System.out::println, // Imprime o livro se presente (usa o toString() do Livro)
                () -> System.out.println("Livro não encontrado na API ou falha no processamento.") // Mensagem se vazio
        );
    }

    private void listarLivros() {
        System.out.println("--- Livros Registrados ---");
        // Chame o novo método transacional do serviço
        List<Livro> livros = gutenService.listarTodosLivros();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado ainda.");
        } else {
            livros.forEach(System.out::println);
        }
    }

    private void listarAutores() {
        var autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado.");
        } else {
            System.out.println("\n--- Autores Registrados ---");
            autores.forEach(System.out::println);
            System.out.println("---------------------------");
        }
    }

    private void listarAutoresVivosPorAno() {
        System.out.print("Digite o ano desejado: ");
        // AQUI: Adiciona o try-catch para a entrada do ano
        try {
            int ano = Integer.parseInt(scanner.nextLine());
            var autores = autorRepository.findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(ano, ano);
            if (autores.isEmpty()) {
                System.out.println("Nenhum autor encontrado vivo no ano " + ano);
            } else {
                System.out.println("\n--- Autores Vivos em " + ano + " ---");
                autores.forEach(System.out::println);
                System.out.println("---------------------------------");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida! Por favor, digite um ano válido (número inteiro).");
        }
    }

    private void listarLivrosPorIdioma() {
        System.out.print("Digite o idioma (ex: 'pt', 'en', 'es', 'fr'): ");
        String idioma = scanner.nextLine().toLowerCase();
        List<Livro> livros = gutenService.listarLivrosPorIdioma(idioma);

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado no idioma '" + idioma + "'");
        } else {
            System.out.println("\n--- Livros no Idioma '" + idioma.toUpperCase() + "' ---");
            livros.forEach(System.out::println);
            System.out.println("----------------------------------------");
        }
    }

    private void gerarEstatisticasDeDownloads() {

        gutenService.gerarEstatisticasDeDownloads();


    }

    private void exibirTop10LivrosMaisBaixados() {
       List<Livro> top10 = gutenService.encontrarTop10LivrosMaisBaixados();
        System.out.println("\n--- Top 10 Livros mais baixados ---");
        if (top10.isEmpty()) {
            System.out.println("Nenhum livro registrado.");
        } else {
            for (int i = 0; i < top10.size(); i++) {
                Livro livro = top10.get(i);
                System.out.println((i + 1) + ". " + livro.getTitulo() + " (Downloads: " + livro.getNumeroDownloads() + ")");
            }
        }
        System.out.println(" ------------------------------------------------\n");
    }
    private void buscarAutorPorNome() {
        System.out.print("Digite o nome ou parte do nome do autor: ");
        String nomeAutor = scanner.nextLine();
        List<Autor> autoresEncontrados = gutenService.buscarAutorPorNomeNoDB(nomeAutor);

        System.out.println("\n--- Autores Encontrados ---");
        if (autoresEncontrados.isEmpty()) {
            System.out.println("Nenhum autor encontrado com o nome '" + nomeAutor + "'.");
        } else {
            autoresEncontrados.forEach(autor -> {
                System.out.println("Autor: " + autor.getName() +
                        ", Nascimento: " + autor.getAnoNascimento() +
                        ", Falecimento: " + autor.getAnoFalecimento());
                // Opcional: Imprimir os livros deste autor
                if (autor.getLivros() != null && !autor.getLivros().isEmpty()) {
                    System.out.println("  Livros:");
                    autor.getLivros().forEach(livro -> System.out.println("    - " + livro.getTitulo()));
                } else {
                    System.out.println("  Nenhum livro associado encontrado no DB.");
                }
                System.out.println("---------------------------");
            });
        }
        System.out.println("---------------------------\n");
    }
}