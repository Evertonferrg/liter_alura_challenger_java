package br.com.alura.literAlura.principal;


import br.com.alura.literAlura.model.Livro;

import br.com.alura.literAlura.reposirory.AutorRepository;
import br.com.alura.literAlura.reposirory.LivroRepository;

import br.com.alura.literAlura.service.GutenService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class Principal implements CommandLineRunner {
    private final Scanner scanner = new Scanner(System.in);
    private final GutenService gutenService;
    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;

    public Principal(GutenService gutenService, LivroRepository livroRepository, AutorRepository autorRepository) {
        this.gutenService = gutenService;
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    @Override
    public void run(String... args) {
        int opcao = -1;
        while (opcao != 0) {
            exibirMenu();
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> buscarLivroPorTitulo();
                case 2 -> listarLivros();
                case 3 -> listarAutores();
                case 4 -> listarAutoresVivosPorAno();
                case 5 -> listarLivrosPorIdioma();
                case 0 -> System.out.println("Encerrando o programa.");
                default -> System.out.println("Opção inválida.");
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
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private void buscarLivroPorTitulo() {
        System.out.print("Digite o título do livro: ");
        String titulo = scanner.nextLine();
        var livroOptional = gutenService.buscarLivroPorTitulo(titulo);
        livroOptional.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Livro não encontrado.")
        );
    }

    private void listarLivros() {
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado.");
        } else {
            livros.forEach(System.out::println);
        }
    }

    private void listarAutores() {
        var autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado.");
        } else {
            autores.forEach(System.out::println);
        }
    }

    private void listarAutoresVivosPorAno() {
        System.out.print("Digite o ano desejado: ");
        int ano = Integer.parseInt(scanner.nextLine());
        var autores = autorRepository.findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(ano, ano);
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor encontrado vivo no ano " + ano);
        } else {
            autores.forEach(System.out::println);
        }
    }

    private void listarLivrosPorIdioma() {
        System.out.print("Digite o idioma (ex: 'pt', 'en', 'es', 'fr'): ");
        String idioma = scanner.nextLine().toLowerCase();
        var livros = livroRepository.findByIdiomasContains(idioma);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado no idioma '" + idioma + "'");
        } else {
            livros.forEach(System.out::println);
        }
    }

}


