package com.example.BoardTarefas.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.example.BoardTarefas.persistence.dao.BoardDAO;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardService 
{
    private final Connection connection;

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
