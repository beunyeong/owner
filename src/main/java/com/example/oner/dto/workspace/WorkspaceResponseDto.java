package com.example.oner.dto.workspace;

import com.example.oner.entity.Workspace;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WorkspaceResponseDto {
    private Long id;
    private String name;
    private String description;
//    private List<BoardResponseDto> boards;

    public WorkspaceResponseDto(Workspace workspace) {
        this.id = workspace.getId();
        this.name = workspace.getName();
        this.description = workspace.getDescription();
//        this.boards = workspace.getBoards().stream()
//                .map(BoardResponseDto::new)
//                .collect(Collectors.toList());

    }
}