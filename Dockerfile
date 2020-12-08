FROM postgres:12.5-alpine
COPY create_fuzzy.sh /docker-entrypoint-initdb.d/
