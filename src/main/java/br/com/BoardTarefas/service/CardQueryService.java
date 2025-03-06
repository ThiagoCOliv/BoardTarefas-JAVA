package br.com.BoardTarefas.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import br.com.BoardTarefas.dto.CardDetailsDTO;
import br.com.BoardTarefas.persistence.dao.CardDAO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CardQueryService 
{
    private final Connection connection;

    public Optional<CardDetailsDTO> read(final Long id) throws SQLException 
    {
        CardDAO cardDAO = new CardDAO(connection);

        return cardDAO.read(id);
    }
}
