/*
 * SGBD: MySQL
 */

DROP DATABASE IF EXISTS microblog;
CREATE DATABASE microblog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE microblog;

CREATE TABLE usuario (
    id          INT NOT NULL AUTO_INCREMENT,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    email       VARCHAR(120) NOT NULL UNIQUE,
    senha_hash  VARCHAR(64)  NOT NULL,   -- SHA-256 hex
    nome        VARCHAR(100) NOT NULL,
    bio         TEXT,
    foto_perfil VARCHAR(200),            -- caminho relativo do arquivo salvo
    foto_md5    VARCHAR(32),             -- hash MD5 para evitar duplicatas no storage
    dt_criacao  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE post (
    id         INT NOT NULL AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    conteudo   VARCHAR(280) NOT NULL,
    imagem     VARCHAR(200),             -- caminho relativo 
    imagem_md5 VARCHAR(32),              -- hash MD5 da imagem
    dt_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_post_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE curtida (
    usuario_id INT NOT NULL,
    post_id    INT NOT NULL,
    dt_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (usuario_id, post_id),
    CONSTRAINT fk_curtida_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    CONSTRAINT fk_curtida_post    FOREIGN KEY (post_id)    REFERENCES post(id)
);

CREATE TABLE seguidor (
    seguidor_id INT NOT NULL,   -- quem segue
    seguido_id  INT NOT NULL,   -- quem é seguido
    dt_criacao  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (seguidor_id, seguido_id),
    CONSTRAINT fk_seguidor_usuario FOREIGN KEY (seguidor_id) REFERENCES usuario(id),
    CONSTRAINT fk_seguido_usuario  FOREIGN KEY (seguido_id)  REFERENCES usuario(id),
    CONSTRAINT ck_auto_follow CHECK (seguidor_id <> seguido_id)
);


-- Senhas: todos usam "123" (SHA-256 = a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3)
INSERT INTO usuario (id, username, email, senha_hash, nome, bio) VALUES
(1, 'roberto',  'roberto@gmail.com',  'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'Roberto Silva',   'Desenvolvedor Java.'),
(2, 'maria', 'maria@gmail.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'Maria Souza',  'Estudante de ADS.'),
(3, 'pedro', 'pedro@gmail.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'Pedro Alves',  'entusiasta de tecnologia.');

INSERT INTO post (id, usuario_id, conteudo) VALUES
(1, 1, 'Meu primeiro post no microblog.'),
(2, 1, 'Obrigada a todos pelo apoio!'),
(3, 2, 'Estudando hoje, OFF!'),
(4, 3, 'Bom dia a todos! Que dia lindo para codar.');

INSERT INTO seguidor (seguidor_id, seguido_id) VALUES
(2, 1),  -- maria segue roberto
(3, 1),  -- pedro segue roberto
(1, 2);  -- roberto segue maria

INSERT INTO curtida (usuario_id, post_id) VALUES
(2, 1),
(3, 1),
(1, 3);

