# Eclipse Hotel

**Eclipse Hotel** é uma aplicação de gerenciamento de hotéis que permite o cadastro de clientes, quartos e reservas, além de consultas e operações básicas. O projeto foi desenvolvido em **Java** utilizando o framework **Spring Boot** com suporte a **PostgreSQL**.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.3.4**
  - Spring Data JPA
  - Spring Validation
  - Spring Web
  - SpringDoc OpenAPI (para documentação da API)
- **PostgreSQL** como banco de dados relacional
- **Docker Compose** para gerenciar o ambiente de banco de dados
- **Lombok** para reduzir boilerplate no código
- **MapStruct** para o mapeamento entre DTOs e entidades

## Funcionalidades

- **Gerenciamento de Clientes**: Adição, consulta, atualização e exclusão de clientes.
- **Gerenciamento de Quartos**: Criação, consulta, atualização, remoção de quartos e consulta de quartos ocupados do hotel.
- **Gerenciamento de Reservas**: Registro de reservas para clientes, fechamento de reservas e consulta por intervalo de datas.

## Arquitetura do Projeto

A estrutura do projeto segue a divisão por camadas:

- **Controller**: Responsável pelo gerenciamento de requisições HTTP e a comunicação com os serviços.
  - `CustomerController`: Manipula operações relacionadas a clientes.
  - `RoomController`: Manipula operações relacionadas a quartos.
  - `ReservationController`: Manipula operações relacionadas a reservas.
  
- **Service**: Contém a lógica de negócios para cada uma das entidades principais.
  - `CustomerService`: Lida com a lógica de criação, atualização e exclusão de clientes.
  - `RoomService`: Lida com a criação e atualização de quartos e busca de quartos reservados.
  - `ReservationService`: Lida com a criação de reservas e consultas por intervalo de datas.

- **DTOs** (Data Transfer Objects): Define objetos para a comunicação entre as camadas.
  - Exemplo: `CustomerCreateRequestDTO`, `RoomResponseDTO`, `ReservationRequestDTO`.

- **Entidades**: Representações do modelo de dados.
  - `Customer`, `Room`, `Reservation`: Definem os modelos para clientes, quartos e reservas, respectivamente.

- **Exceções**: Contém classes para tratar os diferentes tipos de exceções que podem ocorrer na aplicação.
  - O projeto conta com um `GlobalExceptionHandler`, que intercepta exceções globais e retorna respostas padronizadas, facilitando o tratamento centralizado de erros na aplicação.
  - Também possui Exceções customizadas como `CustomerAlreadyExistsException`, `RoomNotAvailableException`, e `ReservationStateException`, que são usadas para fornecer respostas claras e específicas.
 

- **Configurações**: A camada de configuração contém classes que definem configurações globais e comportamentos específicos da aplicação.
  - `SwaggerConfig`: Configura o SpringDoc para gerar a documentação da API automaticamente.
  - `AsyncConfig`: Configura um **Executor** personalizado, garantindo o suporte para operações assíncronas e otimizando o desempenho de requisições que envolvem consultas ao banco de dados e processamento de dados.


## Configuração e Execução

### Pré-requisitos

- **JDK 17**
- **Maven**
- **Docker** e **Docker Compose** para o banco de dados PostgreSQL.

### Rodando Localmente

1. Clone o repositório:
   ```bash
   git clone https://github.com/erisdll/eclipse-hotel.git
   cd eclipse-hotel

2. Compile o projeto e execute-o via Maven:
   ```bash
   ./mvnw spring-boot:run

3. Acesse a documentação da API gerada automaticamente:
  
    URL: http://localhost:8080/swagger-ui.html

### Variáveis de Configuração

O arquivo `application.properties` contém as configurações principais para o banco de dados e o log:

```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/eclipse-hotel
spring.datasource.username=erika
spring.datasource.password=secret
```

Outras propriedades importantes incluem:
```bash
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
logging.level.org.springframework.web=INFO
logging.level.com.erika.eclipse_hotel=INFO
logging.file.name=logs/eclipse-hotel.log
logging.file.max-size=10MB
```

### Contribuição

Contribuições são bem-vindas! Para contribuir, siga os seguintes passos:
1. Faça um fork do projeto
2. Crie uma branch para a sua feature (git checkout -b feature/nova-feature)
3. Commit suas alterações (git commit -m 'Adiciona nova feature')
4. Push para a branch (git push origin feature/nova-feature)
5. Abra um Pull Request
