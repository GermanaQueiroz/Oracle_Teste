# Escolha do banco de dados baseado em uma variável de ambiente
ARG DB_TYPE=mysql

# Imagem base para MySQL
FROM mysql:latest AS mysql

# Imagem base para PostgreSQL
#FROM postgres:latest AS postgres

# Imagem base para Oracle XE
#FROM oracle/database:19.3.0-ee AS oracle

# Definir variáveis para scripts de inicialização
COPY include/create_developer.sql /docker-entrypoint-initdb.d/

# Expõe as portas para cada banco de dados
EXPOSE 3306 33060 5432 1521

# Condicional para qual banco usar baseado no argumento DB_TYPE
# Para MySQL
FROM mysql AS final
ARG DB_TYPE
RUN [ "$DB_TYPE" = "mysql" ] || exit 0

# Para PostgreSQL
# FROM postgres AS final
# ARG DB_TYPE
# RUN [ "$DB_TYPE" = "postgres" ] || exit 0

# # Para Oracle XE
# FROM oracle AS final
# ARG DB_TYPE
# RUN [ "$DB_TYPE" = "oracle" ] || exit 0

# Ponto de entrada e comando de inicialização dependendo do banco escolhido
ENTRYPOINT ["docker-entrypoint.sh"]
CMD ["mysqld"]
