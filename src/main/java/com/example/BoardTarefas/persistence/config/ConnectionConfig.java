package com.example.BoardTarefas.persistence.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static lombok.AccessLevel.PRIVATE;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ConnectionConfig 
{
    public static Connection getConnection() throws SQLException
    {
        var connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/board", "root", "Minhasenh_1SQL");
        connection.setAutoCommit(false);
        return connection; 
    }    
}
