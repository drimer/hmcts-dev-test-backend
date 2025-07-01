# HMCTS Dev Test Backend
This will be the backend for the brand new HMCTS case management system. As a potential candidate we are leaving
this in your hands. Please refer to the brief for the complete list of tasks! Complete as much as you can and be
as creative as you want.

You should be able to run `./gradlew build` to start with to ensure it builds successfully. Then from that you
can run the service in IntelliJ (or your IDE of choice) or however you normally would.

There is an example endpoint provided to retrieve an example of a case. You are free to add/remove fields as you
wish.


# Local Dev setup

1. Spin up a Postgres DB:

```bash
docker run --name hmcts-local-postgres -e POSTGRES_DB=testdb -e POSTGRES_USER=testuser -e POSTGRES_PASSWORD=secretpass -p 5432:5432 -d postgres
```

2. Ensure you're setting the following environment variables when you're about to run the server:

```bash
DB_HOST=http://localhost
DB_PORT=5432
DB_NAME=testdb
DB_OPTIONS=
DB_USER_NAME=testuser
DB_PASSWORD=secretpass
```


# Endpoints

If you're using Postman, you can import the collection from the file `postman_collection.json`.