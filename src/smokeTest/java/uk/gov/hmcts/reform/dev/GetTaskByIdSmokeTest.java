package uk.gov.hmcts.reform.dev;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repositories.TasksRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GetTaskByIdSmokeTest {

    protected static final String CONTENT_TYPE_VALUE = "application/json";

    @LocalServerPort
    int port;

    @Autowired
    private TasksRepository tasksRepository;

    private Task task;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.useRelaxedHTTPSValidation();

        task = tasksRepository.save(new Task()
                .setTitle("Test Task")
                .setDescription("This is a test task description")
                .setStatus("OPEN"));
    }

    @Test
    void smokeTest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/tasks/" + task.getId())
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(response.asString().startsWith("{"));
    }

}
