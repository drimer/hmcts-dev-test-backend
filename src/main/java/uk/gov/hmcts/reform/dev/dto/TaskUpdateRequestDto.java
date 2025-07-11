package uk.gov.hmcts.reform.dev.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public class TaskUpdateRequestDto {
    
    @NotBlank
    String status;

}
