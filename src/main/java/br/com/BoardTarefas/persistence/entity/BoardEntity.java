package br.com.BoardTarefas.persistence.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static br.com.BoardTarefas.persistence.entity.BoardColumnKindEnum.CANCEL;
import static br.com.BoardTarefas.persistence.entity.BoardColumnKindEnum.INITIAL;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
public class BoardEntity {
    private Long id;
    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BoardColumnEntity> boardColumns = new ArrayList<>();

    public BoardColumnEntity getInitialColumn()
    {
        return getFilteredColumn(bc -> bc.getKind().equals(INITIAL));
    }

    public BoardColumnEntity getCancelColumn()
    {
        return getFilteredColumn(bc -> bc.getKind().equals(CANCEL));
    }

    private BoardColumnEntity getFilteredColumn(Predicate<BoardColumnEntity> filter)
    {
        return boardColumns.stream().filter(filter).findFirst().orElseThrow();
    } 
}
