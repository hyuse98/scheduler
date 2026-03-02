# Scheduler

Um aplicativo robusto orientado a eventos que demonstra a utilização do padrão **Event Outbox** com **Spring Modulith**. Este projeto ilustra como construir sistemas modulares e desacoplados no Spring Boot, garantindo a entrega confiável de mensagens para message brokers externos.

## Arquitetura

Esta aplicação foi desenvolvida com **Spring Modulith** para aplicar limites lógicos e manter a integridade estrutural entre os domínios. O principal padrão implementado é o **Event Outbox**, que assegura que as transações do banco de dados e a publicação de eventos de domínio ocorram de forma atômica.

Os eventos são persistidos em uma tabela local (Outbox) na mesma transação que a lógica de negócios e, em seguida, publicados de forma assíncrona no RabbitMQ. Esse padrão oferece garantias de entrega do tipo *at-least-once* (pelo menos uma vez) e evita inconsistências de dados entre o banco de dados e a mensageria.

## Stack Tecnológico

- **Linguagem:** Java 25
- **Framework:** Spring Boot 4.0.5
- **Modularidade:** Spring Modulith 2.0.5
- **Banco de Dados:** PostgreSQL (gerenciado pelo Flyway)
- **Mensageria:** RabbitMQ
- **Segurança:** Spring Security & JWT
- **Resiliência:** Spring Cloud Circuit Breaker (Resilience4j)
- **Mapeamento:** MapStruct
- **Testes:** JUnit, Testcontainers (PostgreSQL, RabbitMQ), Jacoco

## Pré-requisitos

- [Java Development Kit (JDK) 25](https://jdk.java.net/25/)
- [Docker](https://docs.docker.com/get-docker/) e Docker Compose (para os serviços de infraestrutura)
- Gradle (fornecido via wrapper no repositório)

## Primeiros Passos

### 1. Configuração da Infraestrutura

O projeto depende do PostgreSQL e RabbitMQ. Você pode iniciar essas dependências usando o arquivo Docker Compose fornecido:

```bash
docker compose up -d
```

Isso iniciará os seguintes serviços:
- PostgreSQL na porta `5432`
- RabbitMQ na porta `5672` (Interface de gerenciamento disponível na porta `15672`)

### 2. Compilação do Projeto

Compile o projeto e execute os testes para garantir o funcionamento correto. O Testcontainers está configurado para subir instâncias temporárias de banco de dados e broker automaticamente durante os testes de integração.

Linux/macOS:
```bash
./gradlew build
```

Windows:
```cmd
gradlew.bat build
```

### 3. Execução da Aplicação

Para iniciar a aplicação localmente, utilize o plugin do Spring Boot para o Gradle:

Linux/macOS:
```bash
./gradlew bootRun
```

Windows:
```cmd
gradlew.bat bootRun
```

A aplicação será iniciada e aplicará automaticamente quaisquer migrações pendentes no banco de dados via Flyway. A integração do Spring Boot com o Docker Compose está habilitada em ambiente de desenvolvimento, o que gerencia os contêineres definidos no `compose.yaml` automaticamente, caso não estejam em execução.

## Testes e Cobertura de Código

O projeto utiliza o Testcontainers para testes de integração confiáveis em instâncias reais do PostgreSQL e RabbitMQ.

O Jacoco está configurado para análise de cobertura de código. Para executar os testes e gerar o relatório:

Linux/macOS:
```bash
./gradlew test jacocoTestReport
```

Windows:
```cmd
gradlew.bat test jacocoTestReport
```

O relatório gerado estará disponível no diretório `build/customJacocoReportDir`. A verificação de cobertura exige um limite mínimo de 50%.

## Documentação da API

A documentação da API em formato OpenAPI/Swagger está integrada através da biblioteca Springdoc. Com a aplicação em execução, a interface do Swagger UI pode ser acessada para explorar e testar os endpoints REST da aplicação.

Geralmente disponível na raiz ou caminho configurado da aplicação, como por exemplo: `http://localhost:8080/swagger-ui.html`
