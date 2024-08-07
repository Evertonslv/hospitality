# Sistema de Reservas de Hotel
![Imagem do Sistema](https://github.com/Evertonslv/hospitality/blob/main/system-image.png)

## Descrição

Este sistema de reservas de hotel foi desenvolvido utilizando Java 17 e Spring Boot 3.3.2 para o backend, com PostgreSQL 15 como banco de dados. No frontend, utilizamos Angular 18 e Jasmine para testes unitários. Todo o ambiente de desenvolvimento e execução está configurado para rodar em containers Docker.

## Tecnologias Utilizadas

- **Backend:** Java 17, Spring Boot 3.3.2
- **Banco de Dados:** PostgreSQL 15
- **Frontend:** Angular 18
- **Testes:** JUnit (Backend), Jasmine (Frontend)
- **Containers:** Docker

## Como Executar o Sistema com Docker Compose

1. **Certifique-se de ter o Docker e Docker Compose instalados.** Caso não tenha, siga as instruções de instalação no [site oficial do Docker](https://docs.docker.com/get-docker/).

2. **Clone o repositório do sistema:**

    ```bash
    git clone https://github.com/Evertonslv/hospitality.git
    cd hospitality
    ```

3. **Execute o comando para iniciar os containers:**

    ```bash
    docker-compose up
    ```

   Esse comando irá construir as imagens necessárias e iniciar os containers para o backend, frontend e banco de dados.

4. **Acesse o sistema via navegador na URL:**

    - Frontend: [http://localhost:4200](http://localhost:4200)
    - Backend: [http://localhost:8080](http://localhost:8080)

## Rodando os Testes

### Backend

1. **Para rodar os testes de unidade no backend, utilize o seguinte comando:**

    ```bash
    cd hospitality
    docker build -t api-test -f Dockerfile.test ./api
    docker run --rm api-test
    ```

2. **Os resultados dos testes serão exibidos no terminal.**


### Frontend

1. **Para rodar os testes de unidade no frontend, utilize o seguinte comando dentro do diretório do frontend:**

    ```bash
    cd hospitality/client
    npm run test
    ```

2. **Para visualizar os resultados dos testes acesse via navegador a URL:**
    - Jasmine: [http://localhost:9876](http://localhost:9876)

## Arquitetura e Organização das Pastas

### Backend

- **Raiz do Sistema:**
  - `Domain/`: Contém a lógica de negócios e regras do sistema.
    - `Entities/`: Entidades do sistema.
    - `Exceptions/`: Exceções personalizadas.
    - `UseCase/`: Casos de uso e lógica de aplicação.
  - `Infrastructure/`: Implementações técnicas e de infraestrutura.
    - `Controller/`: Controladores REST.
    - `Persistence/`: Persistência de dados e mapeamento do JPA.
    - `Repository/`: Implementação das Interfaces de repositório.
    - `Response/`: Modelos de resposta.
  - `main/`: Configurações do Spring Boot.
    - `Beans/`: Configurações de beans do Spring.
    - `Cors/`: Configurações de CORS.

### Frontend

- **Raiz do Sistema:**
  - `Domain/`: Contém a lógica de negócios e regras do sistema.
    - `Models/`: Modelos de dados.
    - `Repositories/`: Repositórios e interfaces de dados.
    - `Usecase/`: Casos de uso e lógica de aplicação.
  - `Infrastructure/`: Implementações técnicas e de infraestrutura.
    - `Http/`: Configurações e serviços HTTP.
    - `Interceptor/`: Interceptadores HTTP.
  - `Presentation/`: Componentes e páginas de apresentação.
    - `Page/`: Páginas e layouts.
    - `Components/`: Componentes reutilizáveis.

## Banco de Dados

- **Tabelas Criadas:**
  - `reservations`: Armazena informações sobre reservas.
  - `guests`: Armazena informações sobre hóspedes.

## Referências e Links Úteis

- [Documentação do Docker](https://docs.docker.com/get-docker/)
- [Documentação do Docker Compose](https://docs.docker.com/compose/)

---

Para mais informações, consulte a documentação adicional no repositório ou entre em contato com a equipe de desenvolvimento.

