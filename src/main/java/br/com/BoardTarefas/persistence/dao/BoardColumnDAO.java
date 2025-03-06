package br.com.BoardTarefas.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.isNull;
import java.util.Optional;

import br.com.BoardTarefas.dto.BoardColumnDTO;
import br.com.BoardTarefas.persistence.entity.BoardColumnEntity;
import static br.com.BoardTarefas.persistence.entity.BoardColumnKindEnum.fromName;
import br.com.BoardTarefas.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardColumnDAO 
{
    private final Connection connection;

    public BoardColumnEntity create(final BoardColumnEntity boardColumn) throws SQLException 
    {
        String sql = "INSERT INTO boards_columns (name, `order`, kind, board_id) VALUES (?,?,?,?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) 
        {
            stmt.setString(1, boardColumn.getName());
            stmt.setInt(2, boardColumn.getOrder());
            stmt.setString(3, boardColumn.getKind().name());
            stmt.setLong(4, boardColumn.getBoard().getId());
            stmt.executeUpdate();

            try(ResultSet generatedKeys = stmt.getGeneratedKeys())
            {
                if(generatedKeys.next())
                {
                    boardColumn.setId(generatedKeys.getLong(1));
                }
            }
        }

        return boardColumn;
    }

    public Optional<BoardColumnEntity> read(final Long id) throws SQLException 
    {
        String sql = "SELECT id, name, `order`, kind FROM boards_columns WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) 
        {
            stmt.setLong(1, id);
            stmt.executeQuery();
            
            try (ResultSet rs = stmt.getResultSet()) 
            {
                if (rs.next()) 
                {
                    BoardColumnEntity boardColumn = new BoardColumnEntity();
                    boardColumn.setId(rs.getLong("id"));
                    boardColumn.setName(rs.getString("name"));
                    boardColumn.setOrder(rs.getInt("order"));
                    boardColumn.setKind(fromName(rs.getString("kind")));
                    return Optional.of(boardColumn);
                }

                return Optional.empty();
            }
        }
    }

    public List<BoardColumnEntity> readAll() throws SQLException 
    {
        List<BoardColumnEntity> boardsColumns = new ArrayList<>();
        String sql = "SELECT * FROM boards_columns";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) 
        {
            while (rs.next()) 
            {
                BoardColumnEntity boardColumnEntity = new BoardColumnEntity();
                boardColumnEntity.setId(rs.getLong("id"));
                boardColumnEntity.setName(rs.getString("name"));
                boardColumnEntity.setOrder(rs.getInt("order"));
                //boardColumnEntity.setKind(BoardColumnEntity.Kind.valueOf(rs.getString("kind")));
                //boardColumnEntity.setBoardId(rs.getLong("board_id"));
                boardsColumns.add(boardColumnEntity);
            }
        }
        
        return boardsColumns;
    }

    public void update(BoardColumnEntity boardColumn) throws SQLException 
    {
        String sql = "UPDATE boards_columns SET name = ?, order = ?, kind = ?, board_id = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) 
        {
            stmt.setString(1, boardColumn.getName());
            stmt.setInt(2, boardColumn.getOrder());
            stmt.setString(3, boardColumn.getKind().name());
            //stmt.setLong(4, boardColumn.getBoardId());
            stmt.setLong(5, boardColumn.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(final Long id) throws SQLException 
    {
        String sql = "DELETE FROM boards_columns WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) 
        {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public boolean exists(final Long id) throws SQLException 
    {
        String sql = "SELECT 1 FROM boards_columns WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) 
        {
            stmt.setLong(1, id);
            stmt.executeQuery();
            return stmt.getResultSet().next();
        }
    }

    public List<BoardColumnEntity> readByBoardId(Long boardId) throws SQLException
    {
        List<BoardColumnEntity> boardsColumns = new ArrayList<>();
        String sql = "SELECT id, name, `order`, kind FROM boards_columns WHERE board_id = ? ORDER BY `order`";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) 
        {
            stmt.setLong(1, boardId);
            stmt.executeQuery();
            
            try (ResultSet rs = stmt.getResultSet()) 
            {
                while (rs.next()) 
                {
                    BoardColumnEntity boardColumn = new BoardColumnEntity();
                    boardColumn.setId(rs.getLong("id"));
                    boardColumn.setName(rs.getString("name"));
                    boardColumn.setOrder(rs.getInt("order"));
                    boardColumn.setKind(fromName(rs.getString("kind")));
                    boardsColumns.add(boardColumn);
                }

                return boardsColumns;
            }
        }
    }

    public List<BoardColumnDTO> readByBoardIdWithDetails(Long boardId) throws SQLException
    {
        List<BoardColumnDTO> boardsColumnsDTOs = new ArrayList<>();
        
        String sql = """
        SELECT bc.id, bc.name, bc.kind, (SELECT COUNT(c.id) FROM cards WHERE c.board_column_id = bc.id) cards_amount 
        FROM boards_columns bc WHERE board_id = ? ORDER BY `order`
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) 
        {
            stmt.setLong(1, boardId);
            stmt.executeQuery();
            
            try (ResultSet rs = stmt.getResultSet()) 
            {
                while (rs.next()) 
                {
                    BoardColumnDTO boardColumnDTO = new BoardColumnDTO(
                        rs.getLong("bc.id"), 
                        rs.getString("bc.name"), 
                        fromName(rs.getString("bc.kind")), 
                        rs.getInt("cards_amount")
                    );
                    
                    boardsColumnsDTOs.add(boardColumnDTO);
                }

                return boardsColumnsDTOs;
            }
        }
    }

    public Optional<BoardColumnEntity> readById(Long boardId) throws SQLException
    {
        String sql = """
        SELECT bc.name, bc.kind, c.id, c.title, c.description FROM boards_columns bc 
        LEFT JOIN cards c ON c.board_column_id = bc.id WHERE bc.id = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) 
        {
            stmt.setLong(1, boardId);
            stmt.executeQuery();
            
            try (ResultSet rs = stmt.getResultSet()) 
            {
                if (rs.next()) 
                {
                    BoardColumnEntity boardColumn = new BoardColumnEntity();
                    boardColumn.setName(rs.getString("bc.name"));
                    boardColumn.setKind(fromName(rs.getString("bc.kind")));

                    do
                    {
                        if(isNull(rs.getString("c.title"))) break;

                        CardEntity card = new CardEntity();
                        card.setId(rs.getLong("c.id"));
                        card.setTitle(rs.getString("c.title"));
                        card.setDescription(rs.getString("c.description"));
                        boardColumn.getCards().add(card);
                    } while (rs.next());

                    return Optional.of(boardColumn);
                }

                return Optional.empty();
            }
        }
    }
}
