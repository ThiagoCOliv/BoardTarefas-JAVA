package br.com.BoardTarefas.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import br.com.BoardTarefas.persistence.dao.BoardColumnDAO;
import br.com.BoardTarefas.persistence.dao.BoardDAO;
import br.com.BoardTarefas.persistence.entity.BoardColumnEntity;
import br.com.BoardTarefas.persistence.entity.BoardEntity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardService 
{
    private final Connection connection;

    public BoardEntity create(final BoardEntity board) throws SQLException
    {
        BoardDAO boardDAO = new BoardDAO(connection);
        BoardColumnDAO boardColumnDAO = new BoardColumnDAO(connection);

        try
        {
            boardDAO.create(board);
            List<BoardColumnEntity> columns = board.getBoardColumns().stream().map(c -> { c.setBoard(board); return c; }).toList();

            for(BoardColumnEntity column : columns)
            {
                boardColumnDAO.create(column);
            }

            connection.commit();
        }
        catch(SQLException sqlex)
        {
            connection.rollback();
            throw sqlex;
        }

        return board;
    }

    public boolean delete(final Long id) throws SQLException
    {
        BoardDAO boardDAO = new BoardDAO(connection);

        try
        {
            if(!boardDAO.exists(id)){ return false; }

            boardDAO.delete(id);
            connection.commit();
            return true;
        }
        catch(SQLException sqlex)
        {
            connection.rollback();
            throw sqlex;
        }
    }
}
