package br.com.BoardTarefas.exception;

public class CardFinishedException extends RuntimeException
{
    public CardFinishedException(final String mensagem)
    {
        super(mensagem);
    }
}