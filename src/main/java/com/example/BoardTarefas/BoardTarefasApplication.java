package com.example.BoardTarefas;

import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.example.BoardTarefas.persistence.config.ConnectionConfig.getConnection;
import com.example.BoardTarefas.persistence.migration.MigrationStrategy;


@SpringBootApplication
public class BoardTarefasApplication {

	public static void main(String[] args) throws SQLException
	{
		try(var connection = getConnection()){
			new MigrationStrategy(connection).executeMigration();
			SpringApplication.run(BoardTarefasApplication.class, args);
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
	}

}
