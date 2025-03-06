package br.com.BoardTarefas.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import static br.com.BoardTarefas.persistence.converter.OffsetDateTimeConverter.toTimestamp;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BlockDAO 
{
    private final Connection connection;

    public void block(final Long cardId, final String reason) throws SQLException
    {
        String sql = "INSERT INTO blocks (blocked_at, blocked_reason, card_id) VALUES (?,?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) 
        {
            stmt.setTimestamp(1, toTimestamp(OffsetDateTime.now()));
            stmt.setString(2, reason);
            stmt.setLong(3, cardId);
            stmt.executeUpdate();
        }
    }

    public void unblock(final Long cardId, final String reason) throws SQLException
    {
        String sql = "UPDATE blocks SET unblocked_at = ?, unblocked_reason = ? WHERE card_id = ? AND unblock_reason IS NULL";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) 
        {
            stmt.setTimestamp(1, toTimestamp(OffsetDateTime.now()));
            stmt.setString(2, reason);
            stmt.setLong(3, cardId);
            stmt.executeUpdate();
        }
    }
}
