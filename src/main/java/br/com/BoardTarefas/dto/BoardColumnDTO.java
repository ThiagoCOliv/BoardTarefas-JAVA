package br.com.BoardTarefas.dto;

import br.com.BoardTarefas.persistence.entity.BoardColumnKindEnum;

public record BoardColumnDTO(Long id, String name, BoardColumnKindEnum kind, int cardsAmount) 
{
	
}