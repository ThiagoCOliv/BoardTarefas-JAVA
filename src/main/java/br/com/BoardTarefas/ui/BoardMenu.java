package br.com.BoardTarefas.ui;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import br.com.BoardTarefas.dto.BoardColumnInfoDTO;
import br.com.BoardTarefas.dto.BoardDetailsDTO;
import static br.com.BoardTarefas.persistence.config.ConnectionConfig.getConnection;
import br.com.BoardTarefas.persistence.entity.BoardColumnEntity;
import br.com.BoardTarefas.persistence.entity.BoardEntity;
import br.com.BoardTarefas.persistence.entity.CardEntity;
import br.com.BoardTarefas.service.BoardColumnQueryService;
import br.com.BoardTarefas.service.BoardQueryService;
import br.com.BoardTarefas.service.CardQueryService;
import br.com.BoardTarefas.service.CardService;
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
            System.out.printf("Bem vindo ao board %s, selecione a operação deseada:\n", board.getId());

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

    private void createCard() throws SQLException
    {
        CardEntity card = new CardEntity();
        System.out.println("Informe o título do card");
        card.setTitle(scanner.next());
        System.out.println("Informe a descrição do card");
        card.setDescription(scanner.next());
        card.setBoardColumn(board.getInitialColumn());

        try(Connection connection = getConnection())
        {
            new CardService(connection).create(card);
        }
    }

    private void moveCardToNextColumn() throws SQLException
    {
        System.err.println("Informe o id do card que deseja mover para a próxima coluna:");
        Long cardId = scanner.nextLong();
        List<BoardColumnInfoDTO> boardColumnInfoDTOs = board.getBoardColumns().stream().map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind())).toList();

        try(Connection connection = getConnection())
        {
            new CardService(connection).moveCardToNextColumn(cardId, boardColumnInfoDTOs);
        }
        catch(RuntimeException rtex)
        {
            System.err.println(rtex.getMessage());
        }
    }

    private void blockCard() 
    {
        // Implementation for blocking a card
    }

    private void unblockCard() 
    {
        // Implementation for unblocking a card
    }

    private void cancelCard() throws SQLException
    {
        System.err.println("Informe o id do card que deseja cancelar:");
        Long cardId = scanner.nextLong();
        BoardColumnEntity cancelColumn = board.getCancelColumn();
        List<BoardColumnInfoDTO> boardColumnInfoDTOs = board.getBoardColumns().stream().map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind())).toList();

        try(Connection connection = getConnection())
        {
            new CardService(connection).cancelCard(cardId, cancelColumn.getId(), boardColumnInfoDTOs);
        }
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

    private void showColumn() throws SQLException
    {
        List<Long> columnsIds = board.getBoardColumns().stream().map(BoardColumnEntity::getId).toList();
        Long selectedColumn = -1L;

        while(!columnsIds.contains(selectedColumn))
        {
            System.out.printf("Escolha uma coluna do board %s\n", board.getName());
            board.getBoardColumns().forEach(c -> System.out.printf("%s - %s [%s]\n", c.getId(), c.getName(), c.getKind()));
            selectedColumn = scanner.nextLong();
        }

        try(Connection connection = getConnection())
        {
            Optional<BoardColumnEntity> optional = new BoardColumnQueryService(connection).read(selectedColumn);

            optional.ifPresent(co -> {
                System.out.printf("Coluna: [%s] tipo: [%s]\n", co.getName(), co.getKind());
                co.getCards().forEach(ca -> System.out.printf("Card %s - %s\nDescrição: %s", ca.getId(), ca.getTitle(), ca.getDescription()));
            });
        }
    }

    private void showCard() throws SQLException
    {
        System.out.println("Informe o id do card que deseja visualizar");
        Long cardId = scanner.nextLong();

        try(Connection connection = getConnection())
        {
            new CardQueryService(connection).read(cardId).ifPresentOrElse(c -> {
                System.out.printf("Card %s - %s\nDescrição: %s\n", c.id(), c.title(), c.description());
                System.out.println(c.blocked() ? "Card está bloqueado. Motivo: " + c.blockReason() : "Card está desbloqueado");
                System.out.printf("Card já foi bloqueado %s vezes\nEstá na coluna %s - %s\n", c.blocksAmount(), c.columnId(), c.columnName());
            }, 
            () -> System.out.println("Card não encontrado"));
        }
    }
}
