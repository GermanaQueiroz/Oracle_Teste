-- Criação do banco de dados DEVAPP
CREATE DATABASE IF NOT EXISTS DEVAPP;

-- Criação do usuário 'developer' com a senha fornecida como parâmetro
CREATE USER 'developer'@'%' IDENTIFIED BY '<PASSWORD>';

-- Conceder permissões ao usuário 'developer' no banco DEVAPP
GRANT CREATE, ALTER, DROP, INSERT, UPDATE, DELETE, SELECT, REFERENCES, RELOAD
    ON DEVAPP.* TO 'developer'@'%'
    WITH GRANT OPTION;

-- Atualizar as permissões
FLUSH PRIVILEGES;

-- Criação da tabela "departments" com dados de exemplo
USE DEVAPP;

CREATE TABLE IF NOT EXISTS departments (
    DEPT INT(4) PRIMARY KEY,
    DEPT_NAME VARCHAR(250)
);

-- Inserção de dados exemplo na tabela "departments"
INSERT INTO departments (DEPT, DEPT_NAME) VALUES
(1001, 'Sales'),
(1002, 'HR'),
(1003, 'IT'),
(1004, 'Finance');

