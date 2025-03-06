package br.com.BoardTarefas.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import br.com.BoardTarefas.dto.BoardColumnInfoDTO;
import br.com.BoardTarefas.dto.CardDetailsDTO;
import br.com.BoardTarefas.exception.CardBlockedException;
import br.com.BoardTarefas.exception.CardFinishedException;
import br.com.BoardTarefas.exception.EntityNotFoundException;
import br.com.BoardTarefas.persistence.dao.BlockDAO;
import br.com.BoardTarefas.persistence.dao.CardDAO;
import static br.com.BoardTarefas.persistence.entity.BoardColumnKindEnum.CANCEL;
import static br.com.BoardTarefas.persistence.entity.BoardColumnKindEnum.FINAL;
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

    public void moveCardToNextColumn(final Long id, List<BoardColumnInfoDTO> boardColumnInfoDTOs) throws SQLException
    {
        try
        {
            CardDAO cardDAO = new CardDAO(connection);
            Optional<CardDetailsDTO> optional = cardDAO.read(id);
            CardDetailsDTO cardDetailsDTO = optional.orElseThrow(() -> new EntityNotFoundException("O card com id %s não foi encontrado.".formatted(id)));

            if(cardDetailsDTO.blocked()) throw new CardBlockedException("O card %s está bloqueado! Para move-lo primeiro o desbloqueie".formatted(id));

            BoardColumnInfoDTO currentColumnInfo = boardColumnInfoDTOs.stream().filter(bc -> bc.id().equals(cardDetailsDTO.columnId()))
                .findFirst().orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board."));
            
            if(currentColumnInfo.kind().equals(FINAL)) throw new CardFinishedException("O card já foi finalizado!");

            BoardColumnInfoDTO nextColumn = boardColumnInfoDTOs.stream().filter(bc -> bc.order() == currentColumnInfo.order() + 1)
                .findFirst().orElseThrow(() -> new IllegalStateException("O card informado está cancelado."));

            cardDAO.moveToColumn(nextColumn.id(), id);
            connection.commit();
        }
        catch(SQLException sqlex)
        {
            connection.rollback();
            throw sqlex;
        }
    }

    public void cancelCard(final Long id, final Long cancelColumnId, List<BoardColumnInfoDTO> boardColumnInfoDTOs) throws SQLException
    {
        try
        {
            CardDAO cardDAO = new CardDAO(connection);
            Optional<CardDetailsDTO> optional = cardDAO.read(id);
            CardDetailsDTO cardDetailsDTO = optional.orElseThrow(() -> new EntityNotFoundException("O card com id %s não foi encontrado.".formatted(id)));

            if(cardDetailsDTO.blocked()) throw new CardBlockedException("O card %s está bloqueado! Para move-lo primeiro o desbloqueie".formatted(id));

            BoardColumnInfoDTO currentColumnInfo = boardColumnInfoDTOs.stream().filter(bc -> bc.id().equals(cardDetailsDTO.columnId()))
                .findFirst().orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board."));
            
            if(currentColumnInfo.kind().equals(FINAL)) throw new CardFinishedException("O card já foi finalizado!");

            boardColumnInfoDTOs.stream().filter(bc -> bc.order() == currentColumnInfo.order() + 1)
                .findFirst().orElseThrow(() -> new IllegalStateException("O card informado está cancelado."));

            cardDAO.moveToColumn(cancelColumnId, id);
            connection.commit();
        }
        catch(SQLException sqlex)
        {
            connection.rollback();
            throw sqlex;
        } 
    }

    public void block(final Long cardId, final String reason, List<BoardColumnInfoDTO> boardColumnInfoDTOs) throws SQLException
    {
        try {
            CardDAO cardDAO = new CardDAO(connection);
            BlockDAO blockDAO = new BlockDAO(connection);
            Optional<CardDetailsDTO> optional = cardDAO.read(cardId);
            CardDetailsDTO cardDetailsDTO = optional.orElseThrow(() -> new EntityNotFoundException("O card com id %s não foi encontrado.".formatted(cardId)));

            if(cardDetailsDTO.blocked()) throw new CardBlockedException("O card %s já está bloqueado!".formatted(cardId));

            BoardColumnInfoDTO currentColumnInfo = boardColumnInfoDTOs.stream().filter(bc -> bc.id().equals(cardDetailsDTO.id())).findFirst().orElseThrow();

            if(currentColumnInfo.kind().equals(FINAL) || currentColumnInfo.kind().equals(CANCEL)) 
                throw new IllegalStateException("O card está em uma coluna do tipo %s e não pode ser bloqueado!".formatted(currentColumnInfo.kind()));

            blockDAO.block(cardId, reason);
            connection.commit();
        } 
        catch (SQLException sqlex) 
        {
            connection.rollback();
            throw sqlex;
        }
    }

    public void unblock(final Long cardId, final String reason) throws SQLException
    {
        try {
            CardDAO cardDAO = new CardDAO(connection);
            BlockDAO blockDAO = new BlockDAO(connection);
            Optional<CardDetailsDTO> optional = cardDAO.read(cardId);
            CardDetailsDTO cardDetailsDTO = optional.orElseThrow(() -> new EntityNotFoundException("O card com id %s não foi encontrado.".formatted(cardId)));

            if(!cardDetailsDTO.blocked()) throw new CardBlockedException("O card %s não está bloqueado!".formatted(cardId));

            blockDAO.unblock(cardId, reason);
            connection.commit();
        } 
        catch (SQLException sqlex) 
        {
            connection.rollback();
            throw sqlex;
        }
    }
}
