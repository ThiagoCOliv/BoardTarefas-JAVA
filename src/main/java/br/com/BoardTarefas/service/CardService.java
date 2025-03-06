package br.com.BoardTarefas.service;

import java.sql.Connection;
import java.sql.SQLException;

import br.com.BoardTarefas.persistence.dao.CardDAO;
import br.com.BoardTarefas.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CardService 
{
    private final Connection connection;
    
    public CardEntity create(final CardEntity card) throws SQLException
    {
        try
        {
            new CardDAO(connection).create(card);
            connection.commit();
            return card;
        }
        catch(SQLException sqlex)
        {
            connection.rollback();
            throw sqlex;
        }
    }
}
