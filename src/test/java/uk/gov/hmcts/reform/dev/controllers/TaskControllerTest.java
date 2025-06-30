package uk.gov.hmcts.reform.dev.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.hmcts.reform.dev.dto.TaskCreateRequestDto;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repositories.TasksRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private TasksRepository tasksRepository;

    @InjectMocks
    private TaskController sut;

    @Test
    void testThatItCreatesTaskWithValidPayload() {
        var newTask = new Task()
            .setId(1)
            .setTitle("Test Task")
            .setDescription("This is a test task description")
            .setStatus("OPEN");

        when(tasksRepository.save(any(Task.class))).thenReturn(newTask);

        var response = sut.createTask(new TaskCreateRequestDto()
                .setTitle("Test Task")
                .setDescription("This is a test task description")
                .setStatus("OPEN"));

        assertEquals(response.getTitle(), newTask.getTitle());
        assertEquals(response.getDescription(), newTask.getDescription());
        assertEquals(response.getStatus(), newTask.getStatus());
        assertTrue(response.getId() > 0);
    }

    @Test
    void testThatGetTaskByIdReturnsTask() {
        var task = new Task()
            .setId(1)
            .setTitle("Test Task");
        when(tasksRepository.getTaskById(1)).thenReturn(task);

        var response = sut.getTaskById(1);

        assertEquals(response.getTitle(), task.getTitle());
        assertEquals(response.getId(), task.getId());
    }

}
