# Agregador de Investimentos

API REST de estudo feita com Spring Boot para gerenciar usuários, contas e ativos financeiros, com consulta de cotações em tempo real via [Brapi](https://brapi.dev).

![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.4-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)

## Sobre

Projeto desenvolvido para praticar construção de APIs REST com Spring Boot, persistência com JPA/Hibernate e integração com serviços externos. Ao longo do desenvolvimento, o projeto passou por um ciclo completo de melhorias de qualidade, aplicando boas práticas modernas do ecossistema Spring.

## O que aprendi

### API REST
- Criação de endpoints com `@RestController` e mapeamentos HTTP
- Uso correto de `GET`, `POST`, `PUT` e `DELETE`
- Respostas tipadas com `ResponseEntity`
- Separação de responsabilidades: controller → service → repository
- Uso de DTOs para isolar a camada de apresentação das entidades
- Paginação com `Pageable` e `Page` do Spring Data

### JPA e banco de dados
- Mapeamento de entidades com `@Entity` e `@Table`
- Relacionamentos `@OneToMany`, `@ManyToOne` e `@OneToOne`
- Chave composta com `@EmbeddedId` e `@Embeddable`
- Implementação correta de `equals()` e `hashCode()` em entidades e chaves compostas
- `@Column` com constraints de `nullable`, `unique` e `length`
- `@Transactional` em operações de escrita para garantir consistência
- Repositories com `JpaRepository`
- Integração com MySQL via Docker

### Validação e tratamento de erros
- Bean Validation com `@Valid`, `@NotBlank`, `@Email`, `@Size`, `@Min`
- `@PathVariable UUID` para validação automática de formato pelo Spring
- `@RestControllerAdvice` com `GlobalExceptionHandler` para centralizar erros
- `ProblemDetail` (RFC 9457) como formato padrão de resposta de erro
- Exceções customizadas (`ResourceNotFoundException`, `ResourceAlreadyExistsException`)

### Integração externa
- Consumo de API externa com Spring Cloud OpenFeign
- Configuração de client com `@FeignClient`
- Uso de variável de ambiente para não expor o token no código
- Fallback resiliente para falhas na API externa
- Cálculo do valor total da posição: `quantidade × preço atual`

### Testes
- Testes unitários com JUnit 5
- Isolamento de dependências com Mockito
- Captura de argumentos com `ArgumentCaptor`
- Verificação de interações com `verify`
- Cobertura dos principais fluxos do `UserService`

## Funcionalidades

A API permite:

- Cadastrar e gerenciar usuários com validação de entrada
- Criar contas de investimento vinculadas a um usuário
- Cadastrar ativos financeiros como `PETR4`, `VALE3` e `ITUB4`
- Associar ativos a uma conta com quantidade definida
- Consultar cotações em tempo real via Brapi
- Calcular o valor total investido por ativo: `quantidade × preço atual`
- Listar usuários com paginação e ordenação

## Stack

| Tecnologia | Uso |
|------------|-----|
| Java 21 | Linguagem principal |
| Spring Boot 3.4.5 | Framework base |
| Spring Web | Criação dos endpoints REST |
| Spring Data JPA | Persistência com Hibernate |
| Spring Validation | Bean Validation nos DTOs |
| Spring Cloud OpenFeign | Client HTTP para a Brapi |
| MySQL 8.4 | Banco de dados relacional |
| Docker Compose | Ambiente do banco em container |
| JUnit 5 + Mockito | Testes unitários |

## Endpoints

### Usuários

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/v1/users` | Cadastrar usuário |
| `GET` | `/v1/users` | Listar usuários (paginado) |
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

### Exemplos de resposta

**`GET /v1/users?page=0&size=10&sort=username`**
```json
{
  "content": [
    {
      "userId": "ea7f2793-effd-49bc-88aa-130091dfdfb9",
      "username": "Gustavo",
      "email": "gustavo@email.com",
      "createdAt": "2026-06-09T00:19:18.897460Z"
    }
  ],
  "totalElements": 4,
  "totalPages": 1,
  "size": 10,
  "number": 0
}
```

**`GET /v1/accounts/{accountId}/stocks`**
```json
[
  {
    "stockId": "PETR4",
    "quantity": 10,
    "total": 388.50
  },
  {
    "stockId": "MGLU3",
    "quantity": 50,
    "total": 225.00
  }
]
```

**Erros — formato RFC 9457**
```json
{
  "type": "about:blank",
  "title": "Recurso não encontrado",
  "status": 404,
  "detail": "Usuário não encontrado: 00000000-0000-0000-0000-000000000000",
  "instance": "/v1/users/00000000-0000-0000-0000-000000000000"
}
```

## Estrutura do projeto

```
src/
└── main/
    └── java/com/gustavo/AgregadorDeInvestimentos/
        ├── controller/     # Endpoints da API e DTOs
        ├── service/        # Regras de negócio
        ├── repository/     # Acesso ao banco via JPA
        ├── entity/         # Mapeamento das tabelas
        ├── client/         # Integração com a Brapi (OpenFeign)
        └── exception/      # Exceções customizadas e handler global
```

## Histórico de melhorias aplicadas

Após a versão inicial funcional, o projeto passou por um ciclo de refatoração com foco em qualidade e boas práticas:

**Limpeza de código**
- Typos corrigidos: `BillingAdress` → `BillingAddress`, `crateUser` → `createUser`
- `@Repository` removido de interfaces que já estendem `JpaRepository`
- Imports não utilizados limpos em controllers e repositories

**Entidades JPA**
- `equals()` e `hashCode()` implementados em todas as entidades e na chave composta `AccountStockId`
- `@Column` com `nullable = false`, `unique = true` e `length` nos campos obrigatórios

**Validação de entrada**
- `spring-boot-starter-validation` adicionado ao projeto
- DTOs anotados com `@NotBlank`, `@Email`, `@Size`, `@Min` e mensagens em português
- `@Valid` ativado nos controllers
- `@PathVariable UUID` — o Spring converte e valida o formato automaticamente, retornando 400 para UUIDs inválidos

**Tratamento de erros**
- `GlobalExceptionHandler` com `@RestControllerAdvice` centralizando todos os erros
- Handlers para `MethodArgumentNotValidException`, `ResourceNotFoundException`, `MethodArgumentTypeMismatchException` e `Exception` genérica
- Respostas seguem o padrão `ProblemDetail` (RFC 9457), nativo do Spring 6+
- `else {}` vazio e `deleteById` silencioso corrigidos para lançar 404
- Fallback no `fetchPrices()` protegendo contra falhas da Brapi

**Arquitetura**
- `UserResponseDto` criado — sem expor `password`, sem loop de serialização JSON
- `spring.jpa.open-in-view=false` configurado no `application.properties`
- Paginação com `Pageable` e `@PageableDefault` no `listUsers()`

**Exceções customizadas**
- `ResourceNotFoundException` e `ResourceAlreadyExistsException` substituindo `ResponseStatusException` espalhado nos services
- Mensagens descritivas identificando qual recurso não foi encontrado

**Transações**
- `@Transactional` nos métodos de escrita de `UserService`, `AccountService` e `StockService`

## Próximos passos

- [x] Adicionar validações com Bean Validation (`@Valid`, `@NotBlank`)
- [x] Melhorar tratamento de erros com `@RestControllerAdvice` e `ProblemDetail`
- [x] Criar DTOs de resposta para não expor entidades diretamente
- [x] Corrigir loop de serialização JSON (`UserResponseDto`)
- [x] Implementar paginação no `listUsers()`
- [x] Exceções customizadas com mensagens descritivas
- [x] `@Transactional` nos métodos de escrita
- [x] Fallback resiliente para falhas na Brapi
- [ ] Criptografar senhas com BCrypt
- [ ] Spring Security com autenticação JWT
- [ ] Testes de controller com `MockMvc`
- [ ] Documentação da API com SpringDoc/OpenAPI (Swagger UI)
- [ ] Profiles de configuração (`dev` / `prod`)
