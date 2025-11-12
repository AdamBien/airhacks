#!/bin/zsh
docker rm -f ebank-postgres
docker run --rm --name ebank-postgres -e POSTGRES_USER=ebank -e POSTGRES_DB=ebankdb -e POSTGRES_PASSWORD=ebanksecret -p 5432:5432 -d postgres