# ÚltimaQuadra — API Java

**Acessibilidade começa antes da porta.**

API REST desenvolvida com Java e Spring Boot para o ÚltimaQuadra, uma plataforma colaborativa que permite consultar Unidades Básicas de Saúde (UBS), identificar trechos de acesso e registrar ocorrências relacionadas às condições de acessibilidade.

O backend utiliza Oracle para persistência dos dados e disponibiliza endpoints REST para integração com o Front-End React.

## Integrantes

| Integrante | RM | Turma |
|---|---|---|
| Maria Eduarda Escandor | 568216 | 1TDSPO |
| Erick Menezes | 570325 | 1TDSPF |
| Maria Eduarda Lopes de Lima | 572425 | 1TDSPO |
| Matheus Yudi | 571245 | 1TDSPF |
| Nicolas Sousa | 574141 | 1TDSPH |

## Links do projeto

- [API Java publicada no Render](https://ultimaquadra-gs2.onrender.com/api/unidades)
- [Front-End React](https://github.com/nicolaspk/ultimaquadra-front-gs2-2026)
- Vídeo de demonstração no YouTube: **a inserir após a gravação**

## Tecnologias

- Java 21
- Spring Boot 4.1.2
- Maven
- Oracle Database
- JDBC, DataSource e PreparedStatement
- ModelMapper
- Bean Validation
- Docker

## Estrutura do projeto

O projeto utiliza o pacote-base `br.com.fiap`, organizado da seguinte forma:

```text
src/main/
├── java/br/com/fiap/
│   ├── bo/
│   ├── config/
│   ├── controller/
│   ├── dao/
│   ├── dto/
│   ├── exception/
│   ├── handler/
│   ├── model/
│   └── UltimaQuadraApplication.java
└── resources/
    └── application.properties
```

Os pacotes separam as entidades, as regras de negócio, o acesso ao banco, os endpoints, as validações e o tratamento de exceções.

## Banco de dados

A aplicação utiliza Oracle Database com persistência JDBC.

O domínio possui três entidades relacionadas:

- **UnidadeSaude:** informações das Unidades Básicas de Saúde.
- **TrechoAcesso:** áreas de acesso associadas a uma UBS.
- **Ocorrencia:** registros de condições de acessibilidade associados a um trecho.

Os relacionamentos seguem a estrutura:

```text
UnidadeSaude (1)
       |
       | 1:N
       v
TrechoAcesso (N)
       |
       | 1:N
       v
Ocorrencia (N)
```

O script `sql/ultimaquadra_oracle.sql` contém a criação das tabelas, chaves estrangeiras, sequences e dados iniciais.

A base inicial possui sete UBS, sete áreas provisórias de acesso e nenhuma ocorrência cadastrada.

## Configuração e execução

### Pré-requisitos

- JDK 21
- Acesso a um banco Oracle
- Maven Wrapper incluído no projeto

### Compilação

No terminal PowerShell, na raiz do projeto:

```powershell
java -version
.\mvnw.cmd clean package
```

O Maven deverá gerar o arquivo `target/ultimaquadra-api-1.0.0.jar`.

### Configuração do Oracle

A aplicação utiliza três variáveis de ambiente:

| Variável | Descrição |
|---|---|
| `DB_URL` | URL de conexão JDBC do Oracle |
| `DB_USER` | Usuário Oracle |
| `DB_PASSWORD` | Senha Oracle |

Configure as variáveis no PowerShell:

```powershell
$env:DB_URL='SUA_URL_JDBC'
$env:DB_USER='SEU_USUARIO'
$env:DB_PASSWORD='SUA_SENHA'
```

Substitua os valores pelas informações da sua conexão Oracle. Não adicione credenciais reais aos arquivos do projeto ou ao repositório.

### Executar a aplicação

No mesmo terminal:

```powershell
.\mvnw.cmd spring-boot:run
```

A API estará disponível em:

```text
http://localhost:8080
```

Para encerrar a aplicação, pressione `Ctrl + C`.

## Endpoints REST

### Unidades de Saúde

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/unidades` | Listar unidades |
| GET | `/api/unidades/{id}` | Buscar unidade por ID |
| POST | `/api/unidades` | Cadastrar unidade |
| PUT | `/api/unidades/{id}` | Atualizar unidade |
| DELETE | `/api/unidades/{id}` | Excluir unidade |

### Trechos de Acesso

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/trechos` | Listar trechos |
| GET | `/api/trechos/{id}` | Buscar trecho por ID |
| POST | `/api/trechos` | Cadastrar trecho |
| PUT | `/api/trechos/{id}` | Atualizar trecho |
| DELETE | `/api/trechos/{id}` | Excluir trecho |

### Ocorrências

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/ocorrencias` | Listar ocorrências |
| GET | `/api/ocorrencias/{id}` | Buscar ocorrência por ID |
| POST | `/api/ocorrencias` | Registrar ocorrência |
| PUT | `/api/ocorrencias/{id}` | Atualizar ocorrência |
| DELETE | `/api/ocorrencias/{id}` | Excluir ocorrência |

### Rotas aninhadas

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/unidades/{id}/trechos` | Listar trechos de uma UBS |
| POST | `/api/unidades/{id}/trechos` | Cadastrar trecho em uma UBS |
| GET | `/api/trechos/{id}/ocorrencias` | Listar ocorrências de um trecho |
| POST | `/api/trechos/{id}/ocorrencias` | Registrar ocorrência em um trecho |

## Exemplo de requisição

Cadastro de ocorrência:

**POST** `/api/ocorrencias`

```json
{
  "unidadeId": 15,
  "trechoId": 16,
  "categoria": "Outros",
  "descricao": "Registro temporario para testar o cadastro de ocorrencias na API Java.",
  "referenciaLocal": "Trecho temporario utilizado nos testes",
  "passagem": "Não sei informar"
}
```

Os identificadores são ilustrativos e devem ser substituídos pelos IDs existentes no banco.

No cadastro, a API gera automaticamente o identificador, a data do relato e a situação inicial `Relatada`.

## Validações e regras de negócio

A aplicação utiliza Bean Validation e regras implementadas na camada BO.

Entre as principais regras estão:

- Os campos obrigatórios devem ser preenchidos corretamente.
- A descrição da ocorrência deve possuir entre 15 e 1.500 caracteres.
- O trecho informado deve pertencer à UBS selecionada.
- Relatos associados a áreas provisórias devem conter uma referência local suficientemente descritiva.
- Não é permitido excluir uma UBS que possua trechos vinculados.
- Não é permitido excluir um trecho que possua ocorrências vinculadas.

As respostas HTTP incluem:

| Código | Significado |
|---|---|
| 200 | Operação realizada com sucesso |
| 201 | Recurso criado |
| 204 | Recurso excluído |
| 400 | Dados inválidos |
| 404 | Recurso não encontrado |
| 405 | Método HTTP não permitido |
| 409 | Conflito com uma regra de negócio |
| 500 | Erro interno |

Os erros são tratados globalmente e retornados em formato JSON.

## Docker e Render

O projeto possui um `Dockerfile` multi-stage, com um estágio Maven para compilação e outro com Java 21 para execução.

A aplicação está publicada no Render a partir do repositório GitHub.

No serviço Render, selecione o ambiente Docker e configure as variáveis:

```text
DB_URL
DB_USER
DB_PASSWORD
```

As credenciais devem ser cadastradas diretamente nas variáveis de ambiente do serviço.

A API publicada está disponível em `https://ultimaquadra-gs2.onrender.com`, com os endpoints sob `/api`.

## Integração com o Front-End

A API está integrada ao Front-End React do ÚltimaQuadra.

A interface consulta UBS e trechos, lista ocorrências e envia novos relatos pelos endpoints REST.

Os identificadores `unidadeId` e `trechoId` são utilizados na comunicação entre a interface e a API, mantendo os relacionamentos definidos no banco Oracle.

---

**ÚltimaQuadra — Acessibilidade começa antes da porta.**
