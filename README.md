# Agregador de Investimentos

API REST de estudo feita com Spring Boot para gerenciar usuários, contas e ativos financeiros, com consulta de cotações em tempo real via [Brapi](https://brapi.dev).

![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.4-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)

## Sobre

Projeto desenvolvido para praticar construção de APIs REST com Spring Boot, persistência com JPA/Hibernate e integração com serviços externos.

## O que aprendi

### API REST
- Criação de endpoints com `@RestController` e mapeamentos HTTP
- Uso correto de `GET`, `POST`, `PUT` e `DELETE`
- Respostas tipadas com `ResponseEntity`
- Separação de responsabilidades: controller → service → repository
- Uso de DTOs para isolar a camada de apresentação das entidades

### JPA e banco de dados
- Mapeamento de entidades com `@Entity` e `@Table`
- Relacionamentos `@OneToMany`, `@ManyToOne` e `@OneToOne`
- Chave composta com `@EmbeddedId` e `@Embeddable`
- Repositories com `JpaRepository`
- Integração com MySQL via Docker

### Integração externa
- Consumo de API externa com Spring Cloud OpenFeign
- Configuração de client com `@FeignClient`
- Uso de variável de ambiente para não expor o token no código
- Cálculo do valor total da posição: `quantidade × preço atual`

### Testes
- Testes unitários com JUnit 5
- Isolamento de dependências com Mockito
- Captura de argumentos com `ArgumentCaptor`
- Verificação de interações com `verify`
- Cobertura dos principais fluxos do `UserService`


A API permite:

- Cadastrar e gerenciar usuários
- Criar contas de investimento vinculadas a um usuário
- Cadastrar ativos financeiros como `PETR4`, `VALE3` e `ITUB4`
- Associar ativos a uma conta com quantidade definida
- Consultar cotações em tempo real via Brapi
- Calcular o valor total investido por ativo: `quantidade × preço atual`

## Stack

| Tecnologia | Uso |
|------------|-----|
| Java 21 | Linguagem principal |
| Spring Boot 3.4.5 | Framework base |
| Spring Web | Criação dos endpoints REST |
| Spring Data JPA | Persistência com Hibernate |
| Spring Cloud OpenFeign | Client HTTP para a Brapi |
| MySQL 8.4 | Banco de dados relacional |
| Docker Compose | Ambiente do banco em container |
| JUnit 5 + Mockito | Testes unitários |

## Endpoints

### Usuários

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/v1/users` | Cadastrar usuário |
| `GET` | `/v1/users` | Listar todos os usuários |
| `GET` | `/v1/users/{userId}` | Buscar usuário por ID |
| `PUT` | `/v1/users/{userId}` | Atualizar usuário |
| `DELETE` | `/v1/users/{userId}` | Remover usuário |

### Contas

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/v1/users/{userId}/accounts` | Criar conta de investimento |
| `GET` | `/v1/users/{userId}/accounts` | Listar contas do usuário |

### Ativos

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/v1/stocks` | Cadastrar ativo |
| `POST` | `/v1/accounts/{accountId}/stocks` | Associar ativo a uma conta |
| `GET` | `/v1/accounts/{accountId}/stocks` | Listar ativos da conta com cotação |

### Exemplo de resposta — `GET /v1/accounts/{accountId}/stocks`

```json
[
  {
    "stockId": "PETR4",
    "description": "Petrobras PN",
    "quantity": 10,
    "currentPrice": 38.52,
    "totalValue": 385.20
  },
  {
    "stockId": "VALE3",
    "description": "Vale ON",
    "quantity": 5,
    "currentPrice": 61.10,
    "totalValue": 305.50
  }
]
```

Cobertura: camada de serviço com JUnit 5 e Mockito, incluindo `ArgumentCaptor` e verificação de chamadas com `verify`.

## Estrutura do projeto

```
src/
└── main/
    └── java/com/example/agregador/
        ├── controller/     # Endpoints da API
        ├── service/        # Regras de negócio
        ├── repository/     # Acesso ao banco via JPA
        ├── entity/         # Mapeamento das tabelas
        ├── client/         # Integração com a Brapi (OpenFeign)
        └── dto/            # Objetos de entrada e resposta
```

## Próximos passos

- [ ] Adicionar validações com Bean Validation (`@Valid`, `@NotBlank`)
- [ ] Melhorar tratamento de erros da Brapi com fallback
- [ ] Criar DTOs de resposta para não expor entidades diretamente
- [ ] Criptografar senhas com BCrypt
- [ ] Adicionar testes de controller com `MockMvc`
- [ ] Buscar múltiplos tickers em uma única chamada à Brapi
