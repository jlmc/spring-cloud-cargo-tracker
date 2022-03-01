#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE bookingms_db;
    GRANT ALL PRIVILEGES ON DATABASE bookingms_db TO postgres;
EOSQL


psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE routingms_db;
    GRANT ALL PRIVILEGES ON DATABASE routingms_db TO postgres;
EOSQL
