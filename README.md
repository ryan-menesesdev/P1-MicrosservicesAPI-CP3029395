# Sistema de E-Commerce - Arquitetura de Microsserviços

Este projeto consiste em um sistema simplificado de e-commerce baseado na arquitetura de microsserviços. O domínio foi decomposto em serviços independentes, cada um com sua própria persistência de dados isolada (H2 Database), comunicando-se exclusivamente via APIs REST utilizando o Spring Cloud OpenFeign.

## Pré-requisitos para execução

Antes de iniciar, certifique-se de ter as seguintes ferramentas instaladas em sua máquina:

* **Java Development Kit (JDK) 17**: Necessário para compilar e rodar a aplicação.
* **Maven**: Gerenciador de dependências e build.
* **Git**: Para clonar o repositório.
* **Postman ou Insomnia**: Para realizar as chamadas às APIs e simular os cenários de uso.
* **IDE Recomendada**: IntelliJ IDEA, Eclipse ou VS Code.

## Descrição e Portas Utilizadas

O sistema é composto por cinco microsserviços obrigatórios. Cada serviço roda em uma porta específica para evitar conflitos locais.

| Microsserviço | Porta  | Descrição Resumida |
| :--- |:-------| :--- |
| **User Service** (Usuários) | `8080` | Responsável pelo gerenciamento dos clientes do sistema, armazenando dados pessoais. |
| **Inventory Service** (Estoque) | `8081` | Controla a quantidade disponível de produtos. Permite verificar a disponibilidade e deduzir itens do estoque após confirmações de pedidos. |
| **Payment Service** (Pagamento)| `8082` | Simula o processamento de pagamentos, avaliando se o valor pago pelo usuário é suficiente para cobrir o valor total do pedido, retornando status de APROVADO ou RECUSADO. |
| **Order Service** (Pedidos) | `8083` | **Orquestrador principal**. Responsável por criar e gerenciar o ciclo de vida dos pedidos. Ele consulta o Catálogo, verifica/deduz o Estoque e aciona o Pagamento. |
| **Catalog Service** (Catálogo)| `8084` | Gerencia os produtos disponíveis no sistema. Fornece dados como nome e valor para os outros serviços. |

## Instruções para inicializar cada serviço

Como os serviços operam de forma independente, você precisará inicializar cada um deles separadamente.

### Passo a passo via Terminal / Linha de Comando:

1. Clone o repositório para a sua máquina local:
   ```bash
   git clone https://github.com/ryan-menesesdev/P1-MicrosservicesAPI-CP3029395.git
   
   ou
   
   git clone git@github.com:ryan-menesesdev/P1-MicrosservicesAPI-CP3029395.git
   ```
   
   ```bash
   cd <pasta-do-repositorio>
   ```

2. Abra terminais separados para cada microsserviço.


3. Em cada terminal, navegue até a pasta do serviço correspondente e execute o comando do Maven para iniciar a aplicação Spring Boot:


   **Terminal 1 (Catalog):**
   ```bash
   cd catalog
   mvn spring-boot:run
   ```

   **Terminal 2 (User):**
   ```bash
   cd users
   mvn spring-boot:run
   ```

   **Terminal 3 (Inventory):**
   ```bash
   cd inventory
   mvn spring-boot:run
   ```

   **Terminal 4 (Payment):**
   ```bash
   cd payments
   mvn spring-boot:run
   ```

   **Terminal 5 (Order):**
   ```bash
   cd orders
   mvn spring-boot:run
   ```

### Via IDE:
Se preferir, abra o diretório raiz na sua IDE e execute a classe principal anotada com `@SpringBootApplication` de cada serviço (ex: `CatalogApplication.java`, `UsersApplication.java`, etc.).

## Acessando o Banco de Dados (H2 Console)

Como o projeto utiliza o banco de dados em memória H2, o banco será recriado a cada inicialização. Você pode acessar o console do banco de dados de qualquer serviço através do navegador para realizar consultas SQL manuais.

* **URL de acesso genérica:** `http://localhost:<porta-do-servico>/h2-console`
* **Exemplo para acessar o banco do Catálogo:** `http://localhost:8084/h2-console`

**Credenciais Padrão:**
* **Driver Class:** `org.h2.Driver`
* **JDBC URL:** `jdbc:h2:mem:<nome-do-db>` *(Ex: `jdbc:h2:mem:catalogdb`, `jdbc:h2:mem:orderdb`)*
* **User Name:** `sa`
* **Password:** *(deixar em branco)*

## URL's para requisições REST

### 👥 USERS (Porta 8080)
* **GET /users/1**: Receber USUÁRIO por ID.
* **POST /users**: Cadastrar USUÁRIO (Envia `nome`, `senha`, `email`, `cpf`, `idade`, `dataNascimento`).
* **GET /users**: Receber USUÁRIOS.

### 🏢 INVENTORY (Porta 8081)
* **GET /inventory/3**: Receber PRODUTO no ESTOQUE.
* **POST /inventory/2**: Adicionar QUANTIDADE de PRODUTO no ESTOQUE (Envia `"quantidade": 10`).
* **GET /inventory/1/check?quantity=10**: Verificar se PRODUTO existe na QUANTIDADE digitada no ESTOQUE.
* **PUT /inventory/3**: Alterar/Retirar QUANTIDADE de PRODUTO no ESTOQUE (Envia `"quantidade": 1`).

### 💳 PAYMENTS (Porta 8082)
* **POST /payments**: Processar PAGAMENTO (Envia `pedidoId`, `valorPedido` e `valorPago`).
* **GET /payments**: Receber PAGAMENTOS.
* **GET /payments/1**: Receber PAGAMENTO por ID.

### 📦 ORDERS (Porta 8083)
* **GET /orders/{id}**: Ver PEDIDO.
* **GET /orders/user/1**: Ver PEDIDO por ID de USUÁRIO.
* **GET /orders**: Ver PEDIDOS.
* **POST /orders/2/confirm-payment**: Confirmar PEDIDO (Envia o JSON com `"valorPago": 10.0` com `id do pedido como Path Variable`).
* **POST /orders**: Criar PEDIDO com status CRIADO (Envia o `usuarioId` e a lista de itens com `produtoId` e `quantidade`).

### 🏷️ CATALOG (Porta 8084)
* **POST /products**: Criar PRODUTO (Envia `nome`, `observacao` e `valorIndividual`).
* **GET /products/2**: Receber PRODUTO por ID.
* **GET /products**: Receber PRODUTOS.



