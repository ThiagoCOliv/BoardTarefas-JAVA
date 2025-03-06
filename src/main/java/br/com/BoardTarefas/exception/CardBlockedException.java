package br.com.BoardTarefas.exception;

public class CardBlockedException extends RuntimeException
{
    public CardBlockedException(final String mensagem)
    {
        super(mensagem);
    }
}
