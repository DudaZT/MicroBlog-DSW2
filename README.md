# Microblog

Microblog é uma aplicação web no estilo Twitter/X, desenvolvida em **Java** com **JSP/Servlets**, seguindo a arquitetura em camadas **MVC (Model-View-Controller)**. O projeto foi criado como exercício acadêmico da disciplina de Desenvolvimento de Software para Web.

---

## Funcionalidades

- 🔐 Cadastro e login de usuários, com senha protegida por hash SHA-256
- 📝 Criação de posts de texto (até 280 caracteres) com imagem opcional
- ❤️ Curtir e descurtir posts
- 👥 Seguir e deixar de seguir outros usuários
- 📰 Feed com os posts de quem você segue (+ os seus próprios)
- 👤 Página de perfil com nome, bio, foto, contagem de seguidores/seguindo e lista de posts
- ✏️ Edição de perfil (nome, bio e foto)
- 📤 Upload de imagens com deduplicação via hash MD5 (a mesma imagem nunca é salva duas vezes no servidor, verificando pelo hash)

---

## Tecnologias

- **Java** (Jakarta EE / Servlets)
- **JSP** (JavaServer Pages) para as views
- **MySQL** como banco de dados
- **JDBC** para acesso ao banco 
- **Bootstrap 5** para o layout

---

## Arquitetura

O projeto segue o padrão MVC, organizado em pacotes:

```text
controllers/     → Servlets (recebem requisições HTTP e decidem o que fazer)
service/         → Regras de negócio (validações e lógica da aplicação)
persistencia/    → DAOs (acesso ao banco de dados via JDBC)
modelo/          → Entidades do sistema (Usuario, Post, Curtida)
util/            → Classes auxiliares (autenticação, hash, paginação e upload)
```

As páginas JSP ficam em `src/main/webapp/WEB-INF/pages/`, organizadas por funcionalidade (`auth`, `feed`, `perfil`).

---

## Banco de dados

O banco é MySQL e possui 4 tabelas principais:

- `usuario` — dados de cadastro e perfil
- `post` — publicações dos usuários
- `curtida` — relação N:N entre usuários e posts curtidos
- `seguidor` — relação N:N entre usuários (quem segue quem)

O script de criação e dados iniciais está em `src/main/java/resources/database.sql`.

---

## Como rodar o projeto

1. Configure um servidor MySQL e execute manualmente o `database.sql` para criar o banco e popular com dados de exemplo.
2. Configure a conexão com o banco em `DatabaseConnector.java`.
3. Implante o projeto em um servidor de aplicação compatível (ex: Tomcat 10+).
4. Acesse a aplicação pelo navegador e faça login com um dos usuários de exemplo ou cadastre um novo usuário (veja a seção abaixo).

---

## Usuários de teste

O script de banco já inclui três usuários de exemplo. A senha de todos é `123`:

| Username | E-mail            |
|----------|-------------------|
| roberto  | roberto@gmail.com |
| maria    | maria@gmail.com   |
| pedro    | pedro@gmail.com   |

---

## Autor

Projeto desenvolvido como atividade acadêmica por **Maria Eduarda Zanetti Moro**.
