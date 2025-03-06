package br.com.BoardTarefas.dto;

import br.com.BoardTarefas.persistence.entity.BoardColumnKindEnum;

public record BoardColumnInfoDTO(Long id, int order, BoardColumnKindEnum kind)
{
    
}
