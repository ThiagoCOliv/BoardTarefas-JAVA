package br.com.BoardTarefas.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import br.com.BoardTarefas.persistence.dao.BoardColumnDAO;
import br.com.BoardTarefas.persistence.entity.BoardColumnEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardColumnQueryService 
{
    private final Connection connection;

    public Optional<BoardColumnEntity> read(final Long id) throws SQLException 
    {
        BoardColumnDAO boardColumnDAO = new BoardColumnDAO(connection);

        return boardColumnDAO.read(id);
    }
}
