package br.com.BoardTarefas.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.util.Objects.nonNull;
import java.util.Optional;

import br.com.BoardTarefas.dto.CardDetailsDTO;
import static br.com.BoardTarefas.persistence.converter.OffsetDateTimeConverter.toOffsetDateTime;
import br.com.BoardTarefas.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CardDAO
{
    private final Connection connection;

    public void create(final CardEntity card) throws SQLException
    {
        String sql = "INSERT INTO cards (title, description, board_column_id) VALUES (?, ?, ?)";
        
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, card.getTitle());
            stmt.setString(2, card.getDescription());
            stmt.setLong(3, card.getBoardColumn().getId());
            stmt.executeUpdate();

            try(ResultSet generatedKeys = stmt.getGeneratedKeys())
            {
                if(generatedKeys.next())
                {
                    card.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public Optional<CardDetailsDTO> read(final Long id) throws SQLException
    {
        String sql = """
                SELECT c.id, c.title, c.description, b.blocked_at, b.blocked_reason, c.board_column_id, bc.name, 
                (SELECT COUNT(sub_b.id) FROM blocks sub_b WHERE sub_b.card_id = c.id) blocks_amount
                FROM cards c LEFT JOIN blocks b ON c.id = b.card_id AND b.unblocked_at IS NOT NULL INNER JOIN boards_columns bc ON bc.id = c.board_column_id WHERE c.id = ?
                """;
        
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setLong(1, id);
            stmt.executeQuery();

            try(var rs = stmt.getResultSet())
            {
                if(rs.next())
                {
                    return Optional.of(new CardDetailsDTO(
                        rs.getLong("c.id"),
                        rs.getString("c.title"),
                        rs.getString("c.description"),
                        nonNull(rs.getString("b.blocked_reason")),
                        toOffsetDateTime(rs.getTimestamp("b.blocked_at")),
                        rs.getString("b.blocked_reason"),
                        rs.getInt("blocks_amount"),
                        rs.getLong("c.board_column_id"),
                        rs.getString("bc.name")
                    ));
                }
            }
        }

        return null;
    }
}