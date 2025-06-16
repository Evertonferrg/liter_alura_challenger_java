📚 LiterAlura
Visão Geral do Projeto
O LiterAlura é uma aplicação em Java desenvolvida com Spring Boot que interage com a API Gutendex para buscar informações sobre livros e autores. Ele permite que os usuários pesquisem livros por título, listem livros e autores registrados no banco de dados, consultem autores vivos em um determinado ano, filtrem livros por idioma e visualizem estatísticas de download e os top 10 livros mais baixados. Os dados são persistidos em um banco de dados PostgreSQL.
Este projeto demonstra a integração com uma API externa, persistência de dados utilizando Spring Data JPA e Hibernate, e oferece uma interface de linha de comando (CLI) para interação com o usuário.
Funcionalidades
O aplicativo LiterAlura oferece as seguintes opções através de seu menu interativo:

![Menu Principal da Aplicação](https://github.com/Evertonferrg/liter_alura_challenger_java/blob/main/literAlura/image/menu.png)
1.	Buscar livro por título: Pesquisa um livro na API Gutendex pelo título e o registra no banco de dados, se ainda não estiver presente.
   
    ![Menu 1 Pequisa um livro](https://github.com/Evertonferrg/liter_alura_challenger_java/blob/main/literAlura/image/opcao1.png)
 
	==================================================================
2.	Listar livros registrados: Exibe todos os livros que foram salvos no banco de dados.
	
    ![Menu 2 Exibe livro salvos](https://github.com/Evertonferrg/liter_alura_challenger_java/blob/main/literAlura/image/opcao2.png)
  
    ==================================================================
3.	Listar autores registrados: Mostra todos os autores que foram persistidos.
	  ![Menu3 exibe autores registrados](https://github.com/Evertonferrg/liter_alura_challenger_java/blob/main/literAlura/image/opcao3.png)

  	 ==================================================================
4.	Listar autores vivos em um determinado ano: Filtra autores que estavam vivos no ano especificado pelo usuário.
	  ![Menu 4 autores vivos em uma determinada data](https://github.com/Evertonferrg/liter_alura_challenger_java/blob/main/literAlura/image/opcao4.png)

    ==================================================================
5.	Listar livros por idioma: Exibe livros registrados em um idioma específico (ex: pt, en, es).
	  ![Menu 5 exibe livro por edioma escolhido](https://github.com/Evertonferrg/liter_alura_challenger_java/blob/main/literAlura/image/opcao5.png)
   
	==================================================================
6.	Gerar estatísticas de downloads: Apresenta dados como total, média, máximo e mínimo de downloads para os livros registrados.
    ![Menu 6 Exibe estatistica de downloads](https://github.com/Evertonferrg/liter_alura_challenger_java/blob/main/literAlura/image/opcao6.png)

    ==================================================================
   
7.	Top 10 livros mais baixados: Lista os dez livros com o maior número de downloads.
  	![Menu 7 Exibe top 10 mais baixados ](https://github.com/Evertonferrg/liter_alura_challenger_java/blob/main/literAlura/image/opcao7.png)

  	===================================================================
8.	Buscar autor por nome: Permite buscar autores registrados no banco de dados pelo nome e visualizar os livros associados.
	![Menu 8 Busca autor por nome](https://github.com/Evertonferrg/liter_alura_challenger_java/blob/main/literAlura/image/opcao%208.png)

    ==================================================================
Tecnologias Utilizadas
•	Java 17+: Linguagem de programação principal.
•	Spring Boot: Framework para simplificar o desenvolvimento de aplicações Java, incluindo configuração e execução.
•	Spring Data JPA: Abstração para facilitar a interação com bancos de dados relacionais usando JPA (Java Persistence API).
•	Hibernate: Implementação da especificação JPA utilizada para o ORM (Object-Relational Mapping).
•	PostgreSQL: Sistema de gerenciamento de banco de dados relacional.
•	Jackson (fasterxml): Biblioteca para serialização e desserialização de JSON, utilizada para mapear as respostas da API Gutendex para objetos Java.
•	Gutendex API: API pública utilizada para obter dados de livros (https://gutendex.com/).
•	Java 11+ HTTP Client: Cliente HTTP moderno para realizar requisições à API Gutendex.
Estrutura do Projeto
O projeto segue uma estrutura típica de aplicações Spring Boot:
•	br.com.alura.literAlura.model: Contém as classes de modelo (Autor, Livro) mapeadas para o banco de dados via JPA, e os records (DadosAutor, DadosLivro, RespostaApi) para mapeamento de dados da API.
•	br.com.alura.literAlura.reposirory (Note o pequeno erro de digitação no nome do pacote, deveria ser repository): Contém as interfaces de repositório (AutorRepository, LivroRepository) que estendem JpaRepository para operações CRUD e consultas personalizadas.
•	br.com.alura.literAlura.service: Abriga as classes de serviço que contêm a lógica de negócio, comunicação com a API externa e conversão de dados (ConsumoApi, ConverteDados, ConversorDeDados, GutenService, IConverteDados).
•	br.com.alura.literAlura.principal: Contém a classe Principal, que atua como o ponto de entrada da aplicação CLI.
Configuração do Ambiente
Para configurar e executar o projeto localmente, siga os passos abaixo:
Pré-requisitos
•	Java Development Kit (JDK) 17 ou superior
•	Maven (para gerenciamento de dependências e construção do projeto)
•	PostgreSQL: Instância de banco de dados rodando.
•	Git (opcional, para clonar o repositório)
Configuração do Banco de Dados
1.	Crie um banco de dados PostgreSQL com o nome literAlura_db.
2.	CREATE DATABASE literAlura_db;

3.	Configure as credenciais do banco de dados no arquivo src/main/resources/application.properties. Substitua ${DB_HOST}, ${DB_USER} e ${DB_PASSWORD} pelos valores corretos do seu ambiente.
4.	spring.datasource.url=jdbc:postgresql://${DB_HOST}/literAlura_db
5.	spring.datasource.username=${DB_USER}
6.	spring.datasource.password=${DB_PASSWORD}
7.	spring.datasource.driver-class-name=org.postgresql.Driver
8.	
9.	spring.jpa.hibernate.ddl-auto=update
10.	spring.jpa.show-sql=true
11.	spring.jpa.format-sql=true

o	spring.jpa.hibernate.ddl-auto=update: Esta configuração fará com que o Hibernate atualize o esquema do banco de dados automaticamente com base nas entidades JPA. Em ambientes de produção, considere usar validate ou migrações de banco de dados (Flyway/Liquibase).
o	hibernate.dialect=org.hibernate.dialect.HSQLDialect: Há um dialeto HSQLDB especificado aqui, mas o driver é PostgreSQL. É importante corrigir esta linha para org.hibernate.dialect.PostgreSQLDialect para garantir o comportamento correto com PostgreSQL.
Executando a Aplicação
1.	Clone o repositório (se ainda não o fez):
2.	git clone <URL_DO_SEU_REPOSITORIO>
3.	cd literAlura

4.	Compile o projeto usando Maven:
5.	./mvnw clean install

6.	Execute a aplicação Spring Boot:
7.	./mvnw spring-boot:run

Ou, se preferir executar o JAR gerado:
java -jar target/literAlura-0.0.1-SNAPSHOT.jar # O nome do JAR pode variar

Após a execução, o menu da aplicação será exibido no console, e você poderá interagir com ele.
Contribuição
Contribuições são bem-vindas! Sinta-se à vontade para abrir issues e pull requests.

