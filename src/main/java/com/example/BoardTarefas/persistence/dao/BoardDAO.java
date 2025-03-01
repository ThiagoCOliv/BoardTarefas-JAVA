package com.example.BoardTarefas.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.BoardTarefas.persistence.entity.BoardEntity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardDAO 
{
    private final Connection connection;

    public BoardEntity create(final BoardEntity board) throws SQLException 
    {
        String sql = "INSERT INTO boards (name) VALUES (?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) 
        {
            stmt.setString(1, board.getName());
            stmt.executeUpdate();

            try(ResultSet generatedKeys = stmt.getGeneratedKeys())
            {
                if(generatedKeys.next())
                {
                    board.setId(generatedKeys.getLong(1));
                }
            }
        }

        return board;
    }

    public Optional<BoardEntity> read(final Long id) throws SQLException 
    {
        String sql = "SELECT * FROM boards WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) 
        {
            stmt.setLong(1, id);
            stmt.executeQuery();
            
            try (ResultSet rs = stmt.getResultSet()) 
            {
                if (rs.next()) 
                {
                    BoardEntity board = new BoardEntity();
                    board.setId(rs.getLong("id"));
                    board.setName(rs.getString("name"));
                    return Optional.of(board);
                }

                return Optional.empty();
            }
        }
    }

    public List<BoardEntity> readAll() throws SQLException 
    {
        List<BoardEntity> boards = new ArrayList<>();
        String sql = "SELECT * FROM boards";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) 
        {
            while (rs.next()) 
            {
                BoardEntity boardEntity = new BoardEntity();
                boardEntity.setId(rs.getLong("id"));
                boardEntity.setName(rs.getString("name"));
                boards.add(boardEntity);
            }
        }
        
        return boards;
    }

    public void update(BoardEntity board) throws SQLException 
    {
        String sql = "UPDATE boards SET name = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) 
        {
            stmt.setString(1, board.getName());
            stmt.setLong(2, board.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(final Long id) throws SQLException 
    {
        String sql = "DELETE FROM boards WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) 
        {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public boolean exists(final Long id) throws SQLException 
    {
        String sql = "SELECT 1 FROM boards WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) 
        {
            stmt.setLong(1, id);
            stmt.executeQuery();
            return stmt.getResultSet().next();
        }
    }
}