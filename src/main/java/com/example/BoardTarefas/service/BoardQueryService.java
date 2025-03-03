package com.example.BoardTarefas.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.example.BoardTarefas.persistence.dao.BoardColumnDAO;
import com.example.BoardTarefas.persistence.dao.BoardDAO;
import com.example.BoardTarefas.persistence.entity.BoardEntity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardQueryService 
{
    private final Connection connection;

    public Optional<BoardEntity> read(final Long id) throws SQLException 
    {
        BoardDAO boardDAO = new BoardDAO(connection);
        BoardColumnDAO boardColumnDAO = new BoardColumnDAO(connection);
        Optional<BoardEntity> optional = boardDAO.read(id);

        if(optional.isPresent())
        {
            BoardEntity board = optional.get();
            board.setBoardColumns(boardColumnDAO.readByBoardId((board.getId())));
            return Optional.of(board);
        }

        return Optional.empty();
    }
}
