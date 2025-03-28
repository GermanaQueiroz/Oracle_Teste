Solução do Desafio

Solução de Docker para Suporte a Múltiplos Bancos de Dados
Este projeto fornece uma solução para criar um contêiner Docker que pode ser configurado para usar diferentes tipos de bancos de dados (MySQL, PostgreSQL ou Oracle XE) com base em um parâmetro de configuração.

Funcionalidade: 

Multi-Stage Build: A imagem Docker é construída com base no banco de dados escolhido, permitindo a flexibilidade de usar MySQL, PostgreSQL ou Oracle XE, tudo em um único Dockerfile.

Escolha Dinâmica do Banco de Dados: O banco de dados é selecionado com base no argumento DB_TYPE fornecido no momento da construção da imagem.

- MySQL (mysql): Baseado na imagem oficial mysql:latest.

- PostgreSQL (postgres): Baseado na imagem oficial postgres:latest.

- Oracle XE (oracle): Baseado na imagem oficial oracle/database:19.3.0-ee.

Estrutura do Dockerfile:

Multi-Stage Build: O Dockerfile usa múltiplas etapas para definir imagens separadas para MySQL, PostgreSQL e Oracle. O estágio final escolhe qual imagem será usada, dependendo do valor do argumento DB_TYPE.

Script de Inicialização: Um script SQL (create_developer.sql) é copiado para o contêiner e executado para configurar as tabelas e dados iniciais no banco de dados após a inicialização.

Exposição de Portas: As portas necessárias para cada banco de dados são expostas:

MySQL: 3306

PostgreSQL: 5432

Oracle XE: 1521

Como Construir a Imagem
Você pode construir a imagem para o banco de dados desejado, passando o argumento DB_TYPE durante a construção.

Como Usar o Contêiner
Após a construção da imagem, você pode rodar o contêiner com o banco de dados escolhido. O ponto de entrada e o comando de inicialização são configurados de acordo com o banco de dados selecionado.

Portas Expostas
MySQL: 3306

PostgreSQL: 5432

Oracle XE: 1521

Essa solução permite que você tenha um único Dockerfile para múltiplos tipos de bancos de dados, tornando a implementação mais flexível e modular.


