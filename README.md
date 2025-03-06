# BoardTarefas

BoardTarefas é uma aplicação Java que utiliza Spring Boot para gerenciar tarefas em um quadro Kanban. Ele permite criar, mover, bloquear, desbloquear, cancelar e visualizar cards (tarefas) em diferentes colunas de um board (quadro). A aplicação interage com um banco de dados MySQL para armazenar informações sobre boards, colunas e cards.

## Estrutura do Projeto

- **Entidades**: Representam os objetos principais do sistema, como [`BoardEntity`](src/main/java/br/com/BoardTarefas/persistence/entity/BoardEntity.java), [`BoardColumnEntity`](src/main/java/br/com/BoardTarefas/persistence/entity/BoardColumnEntity.java), [`CardEntity`](src/main/java/br/com/BoardTarefas/persistence/entity/CardEntity.java), etc.
- **DAOs**: Classes de acesso a dados, como [`BoardDAO`](src/main/java/br/com/BoardTarefas/persistence/dao/BoardDAO.java), [`BoardColumnDAO`](src/main/java/br/com/BoardTarefas/persistence/dao/BoardColumnDAO.java), [`CardDAO`](src/main/java/br/com/BoardTarefas/persistence/dao/CardDAO.java), que executam operações SQL para manipular os dados no banco.
- **DTOs**: Objetos de transferência de dados, como [`BoardDetailsDTO`](src/main/java/br/com/BoardTarefas/dto/BoardDetailsDTO.java), [`CardDetailsDTO`](src/main/java/br/com/BoardTarefas/dto/CardDetailsDTO.java), usados para transferir dados entre camadas.
- **Serviços**: Classes de serviço, como [`BoardService`](src/main/java/br/com/BoardTarefas/service/BoardService.java), [`CardService`](src/main/java/br/com/BoardTarefas/service/CardService.java), que contêm a lógica de negócios.
- **UI**: Classes de interface do usuário, como [`MainMenu`](src/main/java/br/com/BoardTarefas/ui/MainMenu.java), [`BoardMenu`](src/main/java/br/com/BoardTarefas/ui/BoardMenu.java), que gerenciam a interação com o usuário via console.
- **Exceções**: Classes de exceção personalizadas, como [`CardFinishedException`](src/main/java/br/com/BoardTarefas/exception/CardFinishedException.java), [`CardBlockedException`](src/main/java/br/com/BoardTarefas/exception/CardBlockedException.java), para tratar erros específicos.

## Funcionalidades Principais

- **Criação de Boards e Colunas**: Permite criar novos boards e adicionar colunas com diferentes tipos (INICIAL, FINAL, CANCEL, IN_PROGRESS).
- **Gerenciamento de Cards**: Permite criar, mover, bloquear, desbloquear e cancelar cards dentro das colunas de um board.
- **Visualização**: Permite visualizar detalhes de boards, colunas e cards.
- **Persistência**: Utiliza DAOs para realizar operações de CRUD no banco de dados MySQL.
- **Migração de Banco de Dados**: Utiliza Liquibase para gerenciar mudanças no esquema do banco de dados.

## Configuração

- **Banco de Dados**: Configurado para conectar a um banco MySQL.
- **Dependências**: Utiliza Spring Boot, Lombok, Liquibase, e MySQL Connector.

## Execução

- **Main Class**: [`BoardTarefasApplication`](src/main/java/br/com/BoardTarefas/BoardTarefasApplication.java) é a classe principal que inicia a aplicação e executa migrações de banco de dados antes de iniciar o menu principal.

Para mais detalhes, você pode explorar os arquivos e classes mencionados na estrutura do projeto.