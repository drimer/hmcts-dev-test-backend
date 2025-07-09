package uk.gov.hmcts.reform.dev.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.StreamSupport;

import jakarta.validation.Valid;
import uk.gov.hmcts.reform.dev.dto.TaskCreateRequestDto;
import uk.gov.hmcts.reform.dev.dto.TaskResponseDto;
import uk.gov.hmcts.reform.dev.dto.TaskUpdateRequestDto;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repositories.TasksRepository;

@RestController
public class TaskController {

    private TasksRepository tasksRepository;

    public TaskController(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDto createTask(@Valid @RequestBody TaskCreateRequestDto taskCreateRequestDto) {
        var modelMapper = new ModelMapper();
        var task = modelMapper.map(taskCreateRequestDto, Task.class);

        var savedTask = tasksRepository.save(task);

        return modelMapper.map(savedTask, TaskResponseDto.class);
    }

    @GetMapping("/tasks/{id}")
    public TaskResponseDto getTaskById(@PathVariable("id") int id) {
        var task = tasksRepository.getTaskById(id);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        var modelMapper = new ModelMapper();
        return modelMapper.map(task, TaskResponseDto.class);
    }

    @GetMapping("/tasks")
    public List<TaskResponseDto> getAllTasks() {
        var tasks = tasksRepository.findAll();

        var modelMapper = new ModelMapper();
        return StreamSupport.stream(tasks.spliterator(), false)
                .map(task -> modelMapper.map(task, TaskResponseDto.class)).toList();
    }

    @PatchMapping("/tasks/{id}")
    public TaskResponseDto updateTask(@PathVariable("id") int id,
            @Valid @RequestBody TaskUpdateRequestDto taskUpdateRequestDto) {
        var task = tasksRepository.getTaskById(id);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        var modelMapper = new ModelMapper();
        modelMapper.map(taskUpdateRequestDto, task);
        tasksRepository.save(task);

        return modelMapper.map(task, TaskResponseDto.class);
    }

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable("id") int id) {
        var task = tasksRepository.getTaskById(id);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        tasksRepository.delete(task);
    }

}
