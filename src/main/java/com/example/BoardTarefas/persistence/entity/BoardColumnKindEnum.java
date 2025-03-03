package com.example.BoardTarefas.persistence.entity;

import java.util.stream.Stream;

public enum BoardColumnKindEnum 
{ 
    INITIAL, FINAL, CANCEL, IN_PROGRESS;

    public static BoardColumnKindEnum fromName(final String name) 
    {
        return Stream.of(BoardColumnKindEnum.values())
            .filter(columnKind -> columnKind.name().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow();
    }
}