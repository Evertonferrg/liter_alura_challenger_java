package br.com.alura.literAlura;

import br.com.alura.literAlura.model.DadosLivro;
import br.com.alura.literAlura.principal.Principal;
import br.com.alura.literAlura.service.ConsumoApi;
import br.com.alura.literAlura.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LiterAluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibeMenu();


//

	}
}
