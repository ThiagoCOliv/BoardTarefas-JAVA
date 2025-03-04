package br.com.BoardTarefas;

import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static br.com.BoardTarefas.persistence.config.ConnectionConfig.getConnection;
import br.com.BoardTarefas.persistence.migration.MigrationStrategy;
import br.com.BoardTarefas.ui.MainMenu;


@SpringBootApplication
public class BoardTarefasApplication {

	public static void main(String[] args) throws SQLException
	{
		try(var connection = getConnection())
		{
			new MigrationStrategy(connection).executeMigration();
			SpringApplication.run(BoardTarefasApplication.class, args);
		}

		new MainMenu().execute();
	}

}
