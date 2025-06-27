package uk.gov.hmcts.reform.dev.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TaskResponseDto{
    private int id;
    private String title;
    private String description;
    private String status;
    private String createdDate;
}
