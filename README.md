# Automação de Testes de API – Desafio Sicredi

## Visão Geral do Projeto

Este projeto tem como objetivo automatizar testes da **FakeRestAPI**, uma API pública utilizada para fins de estudo e validação de serviços REST.

A automação foi desenvolvida como parte de um **desafio técnico**, com foco na validação funcional dos endpoints, contratos (schemas), códigos de status HTTP e identificação de comportamentos relevantes da API.

---

## API Testada

### Base URL
```
https://fakerestapi.azurewebsites.net/api/v1
```

Essa configuração é definida na classe `TestBaseBook`, garantindo que todos os testes utilizem a mesma base de execução.

### Collection que foi Testada
```
/Books
```

### Endpoints Utilizados

| Método HTTP | Endpoint | Descrição                      |
|-----------|---------|--------------------------------|
| POST | /Books | Cadastro de um novo livro      |
| GET | /Books | Listagem de todos os livros    |
| GET | /Books/{id} | Consulta de livro por ID       |
| PUT | /Books/{id} | Atualização de um livro por ID |
| DELETE | /Books/{id} | Exclusão de um livro  por ID   |

---

## Tecnologias Utilizadas

- Java 21  
- JUnit 5  
- RestAssured 5.4.0  
- Hamcrest  
- JSON Schema Validator  
- Maven  
- Maven Surefire Plugin
- Allure Report
- Allure JUnit 5 Integration
- Jackson

---

## Estrutura do Projeto

```
src
 ├── test
 │   ├── java
 │   │   └── br.com.api.tests
 │   │       ├── base        -> Configurações base
 │   │       ├── clients     -> Camada de chamadas HTTP
 │   │       ├── models      -> Modelos de dados
 │   │       ├── tests       -> Cenários de teste
 │   │       └── utils       -> Factory de dados de teste
 │   └── resources
 │       └── schemas         -> Schemas JSON de contrato
```

---

## Como Executar os Testes

### Pré-requisitos instalados
- Java 21
- Maven
- IDE (IntelliJ IDEA recomendado)
- Allure

#### OBS: Tanto Java quanto Maven devem ser adicionados ao PATH via variáveis de ambiente

### Gerenciador de dependências
Após instalar o Maven e configurar via PATH, o arquivo pom.xml está configurado para baixar todas as dependências necessárias do projeto automaticamente.

### Passo-a-passo de comandos via terminal/prompt

1. Clone o repositório:
```
git clone <url-do-repositorio>
```

2. Acesse a pasta do projeto:
```
cd desafioApiSicredi
```

3. Execute os testes (Maven - Surefire):
```
mvn test
```

Durante essa execução:
- O **Maven Surefire Plugin** é responsável por localizar e executar os testes JUnit.
- Os resultados de execução dos testes são gerados automaticamente.

---

## Relatórios de Testes (Allure)

O projeto utiliza o **Allure Report** para visualização gráfica via browser dos resultados dos testes.

### Geração do relatório visual

Após a execução dos testes, cole o comando abaixo no terminal:

```
mvn allure:serve
```

Esse comando:
- Processa os resultados gerados durante a execução dos testes
- Abre automaticamente o relatório em um servidor local no navegador

### O que o relatório Allure apresenta

- Quantidade de testes executados
- Testes aprovados, falhados e ignorados
- Tempo de execução
- Detalhes de falhas
- Histórico de execuções

#### OBS: Para abrir automaticamente o relatório visual no Allure após a conclusão da execução dos testes, cole os dois comandos no terminal:

```
mvn test
mvn allure:serve
```


---

## Estratégia de Testes

Os casos de testes estão centralizados na classe `TestsBook`, organizados por blocos de responsabilidade conforme os métodos HTTP.

### Tipos de Testes Implementados

- Testes positivos
- Testes negativos
- Validação de códigos de status HTTP
- Validação de contrato (JSON Schema)
- Testes de segurança (tokens)

### Independência dos Testes

Os testes **não dependem de IDs fixos** para cenários positivos.  
Cada teste cria seu próprio recurso, captura o ID retornado e o utiliza nas operações posteriores, removendo a dependência de ordem na execução.


---

## Decisões Técnicas

### 1 - Centralização dos testes em uma única classe

Os testes foram mantidos em uma única classe (`TestsBook`) para facilitar leitura, execução, avaliação e manutenção no sentido de escalabilidade, como boas práticas de código limpo e arquitetura estrutural.

---

### 2 - Eliminação de IDs fixos

IDs fixos foram removidos dos cenários positivos para evitar dependência de dados pré-existentes e garantir que cada teste seja independente e determinístico.

Entretanto, os IDs fixos foram mantidos apenas em cenários negativos, onde o objetivo é validar comportamentos de erro.

---

### 3 - Adequação dos testes ao comportamento real da API

Durante os testes e codificação, alguns comportamentos "estranhos" da API foram identificados e documentados, e os testes foram ajustados para refletir esses comportamentos:

- **DELETE idempotente**: exclusão de recurso inexistente retorna sucesso
- **PUT com comportamento de upsert**: atualização cria recurso se não existir
- **Ausência de validações obrigatórias**: campo `title` no response body não é validado pelo backend
- **Consistência eventual**: GET imediato após POST pode retornar 404 em alguns cenários

Esses comportamentos foram tratados como informações relevantes e oportunidades de melhoria, não como falhas da automação.

---

## Relatório de Bugs e Oportunidades de Melhoria

Durante a automação, foram identificados comportamentos "estranhos" da API:

### 1. DELETE idempotente
- DELETE em recurso inexistente retorna `200` ou `204`
- Não há retorno `404` mesmo de objetos com ID inexistente

### 2. Comportamento de UPSERT no PUT
- PUT com ID inexistente retorna `200`
- O recurso é criado ou sobrescrito

### 3. Ausência de validações obrigatórias
- É possível criar livros sem campos como `title`
- A API retorna sucesso mesmo com dados incompletos

### 4. Consistência eventual
- Em alguns casos, um GET imediato após POST pode retornar `404`

---

## Considerações Finais

Os testes foram projetados para refletir o **comportamento real da API**, documentando inclusive falhas e oportunidades de melhoria.

Por fim, os objetivos principais da automação de testes foram:
- Cobertura funcional
- Validação de contratos
- Identificação de riscos
- Clareza e organização para avaliação técnica
