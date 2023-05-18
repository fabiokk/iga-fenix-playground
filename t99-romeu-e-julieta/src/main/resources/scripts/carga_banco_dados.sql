-- Incluir aqui o script de carga DML e DDL do seu modelo de banco de dados.
CREATE SCHEMA IF NOT EXISTS exemplo AUTHORIZATION sa;

CREATE TABLE IF NOT EXISTS exemplo.USUARIOS (
   id BIGINT NOT NULL,
   nome VARCHAR(100) NOT NULL,
   nascimento TIMESTAMP,
   PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS exemplo.ENDERECO (
   id INT NOT NULL,
   endereco VARCHAR(100) NOT NULL,
   cidade VARCHAR(50) NOT NULL,
   estado VARCHAR(2) NOT NULL,
   usuario_id INT NOT NULL,
   FOREIGN KEY(usuario_id) REFERENCES USUARIOS(id),
   PRIMARY KEY (id)
);

DELETE exemplo.USUARIOS;

INSERT INTO exemplo.USUARIOS (ID, NOME, NASCIMENTO) VALUES (1, 'Huginho' , PARSEDATETIME('2008-04-09 08:58:45', 'yyyy-MM-dd HH:mm:ss') );
INSERT INTO exemplo.USUARIOS (ID, NOME, NASCIMENTO) VALUES (2, 'Zezinho' , PARSEDATETIME('2008-04-09 08:58:45', 'yyyy-MM-dd HH:mm:ss'));
INSERT INTO exemplo.USUARIOS (ID, NOME, NASCIMENTO) VALUES (3, 'Luizinho', PARSEDATETIME('2008-04-09 08:58:45', 'yyyy-MM-dd HH:mm:ss'));
INSERT INTO exemplo.USUARIOS (ID, NOME, NASCIMENTO) VALUES (4, 'Pato Donald', PARSEDATETIME('1980-04-09 08:58:45', 'yyyy-MM-dd HH:mm:ss'));
INSERT INTO exemplo.USUARIOS (ID, NOME, NASCIMENTO) VALUES (5, 'Tio Patinhas', PARSEDATETIME('1950-04-09 08:58:45', 'yyyy-MM-dd HH:mm:ss'));
INSERT INTO exemplo.USUARIOS (ID, NOME, NASCIMENTO) VALUES (6, 'Margarida', PARSEDATETIME('1982-04-09 08:58:45', 'yyyy-MM-dd HH:mm:ss'));

DELETE exemplo.ENDERECO;

INSERT INTO exemplo.ENDERECO (ID, ENDERECO, CIDADE, ESTADO, USUARIO_ID) VALUES (1, 'Rua da Alegria numero 123', 'Brasilia', 'DF', 1);
INSERT INTO exemplo.ENDERECO (ID, ENDERECO, CIDADE, ESTADO, USUARIO_ID) VALUES (2, 'Rua da Alegria numero 123', 'Brasilia', 'DF', 2);
INSERT INTO exemplo.ENDERECO (ID, ENDERECO, CIDADE, ESTADO, USUARIO_ID) VALUES (3, 'Rua da Alegria numero 123', 'Brasilia', 'DF', 3);
INSERT INTO exemplo.ENDERECO (ID, ENDERECO, CIDADE, ESTADO, USUARIO_ID) VALUES (4, 'Rua Madeira numero 123', 'Brasilia', 'DF', 4);
INSERT INTO exemplo.ENDERECO (ID, ENDERECO, CIDADE, ESTADO, USUARIO_ID) VALUES (5, 'Rua Madeira numero 123', 'Brasilia', 'DF', 5);
INSERT INTO exemplo.ENDERECO (ID, ENDERECO, CIDADE, ESTADO, USUARIO_ID) VALUES (6, 'Rua dos Milionarios numero 321', 'São Paulo', 'SP', 6);

