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

2. Ensure you're setting the required environment variables when you're about to run the server. These can be found in `.env.template`. The
recommended approach is to make a copy of `.env.template` into `.env` and
configure your IDE to use that file.

3. The smoke tests require a full configuration to spin up a test server.
A `.env.test` file has been provided (if you have changed your settings,
this change will need changing with the values you used when you set up
your DB Docker container.

Some IDEs like VSCode may prove a bit tricky to set up with the
combination of a `.env` file and the Gradle plugin. If you are unable
to get those two configured to work together, you can run the smoke tests
with the following command in a terminal:

```bash
export $(cat .env.test | xargs) && ./gradlew smoke
```


# Endpoints

If you're using Postman, you can import the collection from the file `postman_collection.json`.
