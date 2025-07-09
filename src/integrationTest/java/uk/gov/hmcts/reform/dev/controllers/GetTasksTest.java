package uk.gov.hmcts.reform.dev.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repositories.TasksRepository;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc
class GetTasksTest {

    @Autowired
    private transient MockMvc mockMvc;

    @Autowired
    private transient ObjectMapper objectMapper;

    @MockitoBean
    private transient TasksRepository tasksRepository;

    @DisplayName("Should return empty list of tasks with 200 response code")
    @Test
    void getTasksEndpoint() throws Exception {
        when(tasksRepository.findAll()).thenReturn(Collections.emptyList());

        MvcResult response = mockMvc.perform(get("/tasks")).andExpect(status().isOk()).andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(200);
        assertThat(response.getResponse().getContentAsString()).isEqualTo("[]");
    }

    @DisplayName("Should return list with tasks with 200 response code")
    @Test
    void getTasksEndpointWithTasks() throws Exception {
        List<Task> tasks = List.of(
                new Task()
                        .setId(1)
                        .setTitle("Test Task")
                        .setDescription("description test")
                        .setStatus("OPEN"));
        when(tasksRepository.findAll()).thenReturn(tasks);

        MvcResult response = mockMvc.perform(get("/tasks")).andExpect(status().isOk()).andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(200);
        assertThat(response.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(tasks));
    }

    @DisplayName("Should return individual task when found by ID")
    @Test
    void getTaskByIdEndpoint() throws Exception {
        Task task = new Task()
                .setId(1)
                .setTitle("Test Task")
                .setDescription("description test")
                .setStatus("OPEN");
        when(tasksRepository.getTaskById(1)).thenReturn(task);

        MvcResult response = mockMvc.perform(get("/tasks/1")).andExpect(status().isOk()).andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(200);
        assertThat(response.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(task));
    }

    @DisplayName("Should return 404 when task not found by ID")
    @Test
    void getTaskByIdEndpointNotFound() throws Exception {
        when(tasksRepository.getTaskById(1)).thenReturn(null);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Should create a new task and return 201 response code")
    @Test
    void createTaskEndpointShouldCreateNewTask() throws Exception {
        Map<String, String> taskToCreate = Map.of(
                "title", "New Task",
                "description", "New description",
                "status", "OPEN");

        Task savedTask = new Task()
                .setId(2)
                .setTitle("New Task")
                .setDescription("New description")
                .setStatus("OPEN");

        when(tasksRepository.save(any(Task.class))).thenReturn(savedTask);

        MvcResult response = mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskToCreate)))
                .andExpect(status().isCreated())
                .andReturn();

        assertThat(response.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(savedTask));
    }

    @DisplayName("Should update task status and return 200 response code")
    @Test
    void updateTaskEdnpointShouldUpdateTaskStatus() throws Exception {
        Task existingTask = new Task()
                .setId(1)
                .setTitle("Test Task")
                .setDescription("description test")
                .setStatus("OPEN");

        Task updatedTask = new Task()
                .setId(1)
                .setTitle("Test Task")
                .setDescription("description test")
                .setStatus("IN_PROGRESS");

        when(tasksRepository.getTaskById(1)).thenReturn(existingTask);
        when(tasksRepository.save(any(Task.class))).thenReturn(updatedTask);

        Map<String, String> statusUpdate = Map.of("status", "IN_PROGRESS");

        MvcResult response = mockMvc.perform(patch("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(response.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(updatedTask));
    }

    @DisplayName("Should return 404 when updating status for a task that does not exist")
    @Test
    void updateTaskEndpointShouldReturn404WhenUpdatingNonExistentTask() throws Exception {
        when(tasksRepository.getTaskById(99)).thenReturn(null);

        Map<String, String> statusUpdate = Map.of("status", "IN_PROGRESS");

        mockMvc.perform(patch("/tasks/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Should delete a task and return 204 response code")
    @Test
    void deleteTaskEndpointShouldDeleteTaskWhenFound() throws Exception {
        Task existingTask = new Task()
                .setId(1)
                .setTitle("Test Task")
                .setDescription("description test")
                .setStatus("OPEN");

        when(tasksRepository.getTaskById(1)).thenReturn(existingTask);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());

        verify(tasksRepository).delete(existingTask);
    }

    @DisplayName("Should return 404 when deleting a task that does not exist")
    @Test
    void deleteTaskEndpointShouldReturn404WhenDeletingNonExistentTask() throws Exception {
        when(tasksRepository.getTaskById(99)).thenReturn(null);

        mockMvc.perform(delete("/tasks/99"))
                .andExpect(status().isNotFound());
    }
}
