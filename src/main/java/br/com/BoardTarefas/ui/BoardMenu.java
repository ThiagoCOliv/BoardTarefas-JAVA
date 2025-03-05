package br.com.BoardTarefas.ui;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

import br.com.BoardTarefas.dto.BoardDetailsDTO;
import static br.com.BoardTarefas.persistence.config.ConnectionConfig.getConnection;
import br.com.BoardTarefas.persistence.entity.BoardEntity;
import br.com.BoardTarefas.service.BoardQueryService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardMenu 
{
    private final BoardEntity board;
    private final Scanner scanner = new Scanner(System.in);

    public void execute()
    {
        try
        {
            System.out.printf("Bem vindo ao board %s, selecione a operação deseada:", board.getId());

            var option = -1;

            while(option != 9)
            {
                System.out.println("1 - Criar novo card");
                System.out.println("2 - Mover card");
                System.out.println("3 - Bloquear card");
                System.out.println("4 - Desbloquear card");
                System.out.println("5 - Cancelar card");
                System.out.println("6 - Visualizar board");
                System.out.println("7 - Visualizar coluna com cards");
                System.out.println("8 - Visualizar card");
                System.out.println("9 - Voltar ao menu anterior um card");
                System.out.println("10 - Sair");

                option = scanner.nextInt();

                switch(option)
                {
                    case 1 -> createCard();
                    case 2 -> moveCardToNextColumn();
                    case 3 -> blockCard();
                    case 4 -> unblockCard();
                    case 5 -> cancelCard();
                    case 6 -> showBoard();
                    case 7 -> showColumn();
                    case 8 -> showCard();
                    case 9 -> System.out.println("Voltando ao menu anterior");
                    case 10 -> System.exit(0);
                    default -> System.out.println("Opção inválida");
                }
            }   
        }
        catch(SQLException sqlex)
        {
            sqlex.printStackTrace();
            System.exit(0);
        }
    }

    private void createCard() 
    {
        // Implementation for creating a card
    }

    private void moveCardToNextColumn() 
    {
        // Implementation for moving a card to the next column
    }

    private void blockCard() 
    {
        // Implementation for blocking a card
    }

    private void unblockCard() 
    {
        // Implementation for unblocking a card
    }

    private void cancelCard() 
    {
        // Implementation for canceling a card
    }

    private void showBoard() throws SQLException
    {
        try(Connection connection = getConnection())
        {
            Optional<BoardDetailsDTO> optional = new BoardQueryService(connection).showBoardDetails(board.getId());

            optional.ifPresent(b -> {
                System.out.printf("Board: [%s, %s]\n", b.id(), b.name());
                b.columns().forEach(c -> System.out.printf("Coluna: [%s] tipo: [%s] tem %s cards\n", c.name(), c.kind(), c.cardsAmount()));
            });
        }
    }

    private void showColumn() 
    {
        // Implementation for showing a column with cards
    }

    private void showCard() 
    {
        // Implementation for showing a card
    }
}
