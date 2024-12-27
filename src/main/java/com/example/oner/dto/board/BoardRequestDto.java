package com.example.oner.dto.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class BoardRequestDto {

    @NotNull(message = "워크스페이스 ID는 필수 입니다.")
    private Long workspaceId;

    @NotBlank(message = "보드 제목은 필수 입니다.")
    private String title;

    private String backgroundColor;

    private MultipartFile backgroundImage;


    public BoardRequestDto(Long workspaceId, String title,
                           String backgroundColor, MultipartFile backgroundImage) {
        this.workspaceId = workspaceId;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.backgroundImage = backgroundImage;
    }
}

