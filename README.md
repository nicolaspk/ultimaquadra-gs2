# ÚltimaQuadra — API Java

*Acessibilidade começa antes da porta.*

## Integrantes

| Integrante | RM | Turma |
|---|---|---|
| Maria Eduarda Escandor | 568216 | 1TDSPO |
| Erick Menezes | 570325 | 1TDSPF |
| Maria Eduarda Lopes de Lima | 572425 | 1TDSPO |
| Matheus Yudi | 571245 | 1TDSPF |
| Nicolas Sousa | 574141 | 1TDSPH |

## Como executar

**Pré-requisitos:** Java 21 e acesso ao Oracle FIAP. Se o esquema ainda estiver vazio, execute uma vez o script `sql/ultimaquadra_oracle.sql` no SQL Developer.

Na raiz do projeto, abra o PowerShell e execute:

```powershell
java -version
.\mvnw.cmd clean package

$env:DB_URL='SUA_URL_JDBC'
$env:DB_USER='SEU_USUARIO_ORACLE'
$env:DB_PASSWORD='SUA_SENHA_ORACLE'

.\mvnw.cmd spring-boot:run
```

A API local estará disponível em `http://localhost:8080/api/unidades`.

Use as credenciais somente no ambiente de execução, nunca nos arquivos ou no GitHub.

## Links

- **API no Render:** https://ultimaquadra-gs2.onrender.com/api/unidades
- **Vídeo de demonstração (YouTube, não listado):** https://youtu.be/zag-vELMXRs
