package uk.gov.hmcts.reform.dev.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import uk.gov.hmcts.reform.dev.dto.TaskCreateRequestDto;
import uk.gov.hmcts.reform.dev.dto.TaskUpdateRequestDto;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repositories.TasksRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.util.Arrays;


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

    @Test
    void testThatGetTaskThrowsNotFoundException() {
        when(tasksRepository.getTaskById(1)).thenReturn(null);

        var exception = assertThrows(
            ResponseStatusException.class,
            () -> sut.getTaskById(1)
        );
        assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND);
    }


    @Test
    void testThatGetAllTasksReturnsAllTasks() {
        List<Task> tasks = Stream.of(
            new Task().setId(1),
            new Task().setId(2),
            new Task().setId(3)
        ).collect(Collectors.toList());

        when(tasksRepository.findAll()).thenReturn(tasks);

        var response = sut.getAllTasks();
        for (int i = 0; i < response.size(); i++) {
            assertEquals(response.get(i).getId(), tasks.get(i).getId());
        }
    }

    @Test
    void testThatGetAllTasksReturnsEmptyList() {
        when(tasksRepository.findAll()).thenReturn(Collections.emptyList());

        var response = sut.getAllTasks();
        assertTrue(response.isEmpty());
    }

    @Test
    void testThatUpdateTaskReturnsUpdatedTask() {
        var task = new Task()
            .setId(1)
            .setTitle("Test Task")
            .setStatus("pending");
        
        when(tasksRepository.getTaskById(1)).thenReturn(task);
        when(tasksRepository.save(any(Task.class))).thenReturn(task);

        var response = sut.updateTask(
            1, new TaskUpdateRequestDto().setStatus("in_progress"));
        
        assertEquals(response.getStatus(), task.getStatus());
        assertEquals(response.getId(), task.getId());
    }

    @Test
    void testThatUpdateTaskThrowsNotFoundException() {
        when(tasksRepository.getTaskById(1)).thenReturn(null);

        var exception = assertThrows(
            ResponseStatusException.class,
            () -> sut.updateTask(1, new TaskUpdateRequestDto().setStatus("in_progress"))
        );
        assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND);
    }

}
