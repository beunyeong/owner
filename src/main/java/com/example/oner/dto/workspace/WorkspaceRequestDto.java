package com.example.oner.dto.workspace;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WorkspaceRequestDto {

    @NotBlank(message = "제목은 필수입니다.")
    private final String name;

    @NotBlank(message = "내용은 필수입니다.")
    private final String description;
}
