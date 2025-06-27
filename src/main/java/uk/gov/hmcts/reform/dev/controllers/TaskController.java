package uk.gov.hmcts.reform.dev.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import uk.gov.hmcts.reform.dev.dto.TaskCreateRequestDto;
import uk.gov.hmcts.reform.dev.dto.TaskResponseDto;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repositories.TasksRepository;


@RestController
public class TaskController {

    private TasksRepository tasksRepository;

    public TaskController(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    @PostMapping("/tasks")
    public TaskResponseDto createTask(@Valid @RequestBody TaskCreateRequestDto taskCreateRequestDto) {
        var modelMapper = new ModelMapper();
        var task = modelMapper.map(taskCreateRequestDto, Task.class);

        var savedTask = tasksRepository.save(task);

        return modelMapper.map(savedTask, TaskResponseDto.class);
    }
    
}
