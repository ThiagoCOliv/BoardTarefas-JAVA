package br.com.BoardTarefas.persistence.entity;

import java.util.ArrayList;
import java.util.List;

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
        return boardColumns.stream().filter(bc -> bc.getKind().equals(INITIAL)).findFirst().orElseThrow();
    }
}
