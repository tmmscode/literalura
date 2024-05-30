<h1>LiterAlura</h1>

##  Sobre o projeto

Projeto que interage com o usuário por meio do terminal, para pesquisa de livros, fazendo o consumo da API "[Gutendex](https://gutendex.com/)" , e realiza a persistência das informações obtidas em um banco de dados local (Utilizando Hibernate e PostgreSQL).

Este projeto foi desenvolvido durante a formação **"Oracle Next Education" (Especialização Back-End)**, em parceria com a **[Alura](https://www.alura.com.br/)**.

## Tecnologias utilizadas

- Java (JDK 21 "21.0.2")
- Spring
- Maven
- Hibernate (JPA)
- PostgreSQL

## Funções

- Pesquisar informações sobre uma grande variedade de livros pela API Gutendex e salvar em um banco de dados local;
- Consulta dos livros salvos;
- Consulta dos autores salvos;
- Consulta sobre os autores vivos em determinado ano;
- Consulta sobre os idiomas dos livros salvos e a quantidade disponível em cada idioma;
- Consulta personalizada de palavras, verificando tanto o título dos livros quanto o nome dos autores;
- Consulta dos 10 livros salvos, mais baixados de acordo com a API Gutendex.

## Configuração e utilização

É necessário que sejam criadas algumas variáveis de ambiente na máquina onde será executada a aplicação:

```
"${DB_HOST}" (Onde será informado o endereço do banco de dados)
"${DB_NAME}" (Onde será informado o nome do banco de dados)
"${DB_USERNAME}" (Onde será informado o nome de usuário que irá fazer login no banco)
"${DB_PASSWORD}" (Onde será informado a senha do usuário que fará login no banco)

Obs.: Criar as variáveis sem aspas.
```

As informações das variáveis serão utilizadas no arquivo abaixo:
```
src/main/resources/application.properties  
```
---
A execução da aplicação se inicia pelo arquivo:
``` 
src/main/java/tmmscode/literalura/LiteraluraApplication.java
```
---
## Informações adicionais e Curiosidades

Durante o desenvolvimento dessa aplicação, me deparei com alguns retornos indesejados de registros da API Gutendex, que aparentemente não bloqueia o registro de livros de mesmo título e autor, salvos no mesmo idioma, então preparei a aplicação para lidar com esse tipo de registro.

Livros retornados pela API Gutendex com autores indefinidos (null), são salvos no banco como "Autor Desconhecido", para que não haja problema em verificações futuras.


---

## Ajustes futuros

Pretendo organizar as funções de busca, verificações e persistência dos dados, em uma nova classe de serviço e fazer a separação entre a classe principal, que realiza a interação com o usuário, e classe que realiza as regras da aplicação. 

---
Criado por: [Thiago de Melo Marçal da Silva](https://github.com/tmmscode)
