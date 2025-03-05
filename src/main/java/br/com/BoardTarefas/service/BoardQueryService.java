package br.com.BoardTarefas.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import br.com.BoardTarefas.dto.BoardColumnDTO;
import br.com.BoardTarefas.dto.BoardDetailsDTO;
import br.com.BoardTarefas.persistence.dao.BoardColumnDAO;
import br.com.BoardTarefas.persistence.dao.BoardDAO;
import br.com.BoardTarefas.persistence.entity.BoardEntity;

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

    public Optional<BoardDetailsDTO> showBoardDetails(final Long id) throws SQLException
    {
        BoardDAO boardDAO = new BoardDAO(connection);
        BoardColumnDAO boardColumnDAO = new BoardColumnDAO(connection);
        Optional<BoardEntity> optional = boardDAO.read(id);

        if(optional.isPresent())
        {
            BoardEntity board = optional.get();
            List<BoardColumnDTO> columns = boardColumnDAO.readByBoardIdWithDetails(board.getId());
            var boardDetailsDTO = new BoardDetailsDTO(board.getId(), board.getName(), columns);
            return Optional.of(boardDetailsDTO);
        }

        return Optional.empty();
    }
}
