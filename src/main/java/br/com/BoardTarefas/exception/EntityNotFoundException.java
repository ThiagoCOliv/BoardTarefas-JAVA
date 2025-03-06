package br.com.BoardTarefas.exception;

public class EntityNotFoundException extends RuntimeException
{
    public EntityNotFoundException(final String mensagem)
    {
        super(mensagem);
    }
}
