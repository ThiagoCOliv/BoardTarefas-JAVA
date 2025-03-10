package br.com.BoardTarefas.ui;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static br.com.BoardTarefas.persistence.config.ConnectionConfig.getConnection;
import br.com.BoardTarefas.persistence.entity.BoardColumnEntity;
import br.com.BoardTarefas.persistence.entity.BoardColumnKindEnum;
import br.com.BoardTarefas.persistence.entity.BoardEntity;
import br.com.BoardTarefas.service.BoardQueryService;
import br.com.BoardTarefas.service.BoardService;

public class MainMenu 
{
    private final Scanner scanner = new Scanner(System.in);

    public void execute() throws SQLException
    {
        System.out.println("Bem-vindo ao Board Tarefas, o que deseja fazer?");

        int option;

        do
        {
            System.out.println("1 - Criar novo board");
            System.out.println("2 - Selecionar board");
            System.out.println("3 - Excluir board");
            System.out.println("4 - Sair");

            option = scanner.nextInt();

            switch(option)
            {
                case 1 -> createBoard();
                case 2 -> selectBoard();
                case 3 -> deleteBoard();
                case 4 -> System.exit(0);
                default -> System.out.println("Opção inválida");
            }
        } while(true);
    }

    private void createBoard() throws SQLException
    {
        BoardEntity board = new BoardEntity();
        
        System.out.println("Informe o nome do board:");
        board.setName(scanner.next());

        System.out.println("Seu board terá colunas além das 3 padrões? Se sim, informe a quantidade, caso contrário, informe '0':");
        int addtionalColumns = scanner.nextInt();

        List<BoardColumnEntity> columns = new ArrayList<>();

        System.out.println("Informe o nome da coluna inicial do board:");
        String initialColumnName = scanner.next();

        BoardColumnEntity initialColumn = createColumn(initialColumnName, BoardColumnKindEnum.INITIAL, 0);
        columns.add(initialColumn);

        for(int i = 1; i <= addtionalColumns; i++)
        {
            System.out.println("Informe o nome da coluna de tarefa pendente do board:");
            String pendingColumnName = scanner.next();
            columns.add(createColumn(pendingColumnName, BoardColumnKindEnum.IN_PROGRESS, i));
        }

        System.out.println("Informe o nome da coluna final do board:");
        String finalColumnName = scanner.next();
        columns.add(createColumn(finalColumnName, BoardColumnKindEnum.FINAL, addtionalColumns + 1));

        System.out.println("Informe o nome da coluna de cancelamento do board:");
        String cancelColumnName = scanner.next();
        columns.add(createColumn(cancelColumnName, BoardColumnKindEnum.CANCEL, addtionalColumns + 2));

        board.setBoardColumns(columns);

        try(Connection connection = getConnection())
        {
            new BoardService(connection).create(board);
        }
    }

    private void selectBoard() throws SQLException
    {
        System.out.println("Informe o id do board que deseja selecionar:");
        Long id = scanner.nextLong();

        try(Connection connection = getConnection())
        {
            Optional<BoardEntity> optional = new BoardQueryService(connection).read(id);
            optional.ifPresentOrElse(board -> new BoardMenu(board).execute(), () -> System.out.printf("Board com id %s não encontrado!\n", id));
        }
    }

    private void deleteBoard() throws SQLException
    {
        System.out.println("Informa o id do board que deseja excluir:");
        Long id = scanner.nextLong();

        try(Connection connection = getConnection())
        {
            String mensagem = new BoardService(connection).delete(id) ? "O Board %s excluído com sucesso!\n" : "Board com id %s não encontrado!\n";
            System.out.printf(mensagem, id);
        }
    }

    private BoardColumnEntity createColumn(final String columnName, final BoardColumnKindEnum kind, final int order)
    {
        BoardColumnEntity column = new BoardColumnEntity();
        column.setName(columnName);
        column.setKind(kind);
        column.setOrder(order);
        return column;
    }
}
