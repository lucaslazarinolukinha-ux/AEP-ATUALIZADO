# EcoConnect

Plataforma que integra cidadãos, recicladores e pontos de coleta de materiais recicláveis.
Java 17 + Spring Boot 3.3.5 + Spring Data JPA + MySQL.

## Como rodar
1. Instale Java 17+, Maven e MySQL.
2. Edite usuário/senha do MySQL em `src/main/resources/application.properties`.
3. Execute: `mvn spring-boot:run`
   - O banco `ecoconnect` e as tabelas são criados automaticamente.
   - Os materiais iniciais (RN06) são carregados do `data.sql`.
   - Admin padrão: `admin@ecoconnect.com` / `admin123`.
4. API em `http://localhost:8080`.

## Autenticação (simplificada)
Faça login em `POST /api/login` e envie o `id` retornado no header `X-Usuario-Id` nas rotas protegidas.

## Endpoints
| Requisito | Método | Rota | Acesso |
|---|---|---|---|
| RF01 | POST | `/api/usuarios` | público |
| RF01 | POST | `/api/login` | público |
| RF02 | GET | `/api/pontos` | público |
| RF03 | GET | `/api/pontos/pesquisa?cidade=&bairro=&material=` | público |
| RF05 | GET | `/api/pontos/{id}` | público |
| RN06 | GET | `/api/materiais` | público |
| RN08 | POST/PUT/DELETE | `/api/pontos` | reciclador/admin |
| RF04 | GET | `/api/recicladores` | autenticado |
| RF04 | POST/PUT/DELETE | `/api/recicladores` | reciclador/admin |
| RN11 | GET/DELETE | `/api/usuarios` | admin |

## Exemplos
```json
POST /api/usuarios
{ "nome": "Maria", "email": "maria@email.com", "senha": "123456", "tipo": "RECICLADOR" }

POST /api/pontos   (header X-Usuario-Id: 2)
{
  "nome": "Ecoponto Centro",
  "descricao": "Ponto de coleta municipal",
  "horarioFuncionamento": "Seg a Sex, 8h às 17h",
  "endereco": { "logradouro": "Av. Brasil", "numero": "100", "bairro": "Centro",
                "cidade": "Maringá", "estado": "PR", "cep": "87013-000" },
  "materiaisIds": [1, 2, 3]
}
```

## Regras de negócio aplicadas
RN01/RN08: header + checagem de perfil · RN02/RN03: nome, e-mail, senha, e-mail único ·
RN04/RN05/RN06: ponto com endereço, horário e vários materiais pré-cadastrados ·
RN07: contato de recicladores só para autenticados · RN09: bloqueio de duplicados ·
RN11: admin gerencia usuários · RN12: senha com BCrypt, nunca devolvida na API.
