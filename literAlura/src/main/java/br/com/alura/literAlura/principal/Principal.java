package br.com.alura.literAlura.principal;

import br.com.alura.literAlura.model.DadosAutor;
import br.com.alura.literAlura.model.DadosLivro;
import br.com.alura.literAlura.model.RespostaApi;
import br.com.alura.literAlura.service.ConsumoApi;
import br.com.alura.literAlura.service.ConverteDados;

import java.util.List;
import java.util.Scanner;

public class Principal {
    Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://gutendex.com/books/?search=";


    public void exibeMenu(){
        System.out.println("Digite o nome do livro para buscar");
        var nomeLivro = leitura.nextLine();

        var json = consumo.obterDados(ENDERECO + nomeLivro.replace(" ", "%20"));
        System.out.println("\nJSON bruto recebido: \n" + json);
        RespostaApi respostaApi = conversor.obterDados(json, RespostaApi.class);

        if (respostaApi != null && respostaApi.resultados() != null && !respostaApi.resultados().isEmpty()) {
            List<DadosLivro> livrosEncotrados = respostaApi.resultados();
            System.out.println("\n -- Livros encontrados (processando para objetos java): --");
            for (DadosLivro livro : livrosEncotrados) {
                System.out.println(livro);
            }
        } else {
            System.out.println("\nNenhum livro encontrado para a busca; '" + nomeLivro + "' ou erro no processamento.");
        }
        DadosLivro dados;
        dados = conversor.obterDados(json, DadosLivro.class);
        System.out.println(dados);






    }
}
