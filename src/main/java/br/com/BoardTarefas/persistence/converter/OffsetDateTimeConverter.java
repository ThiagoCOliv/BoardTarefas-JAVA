package br.com.BoardTarefas.persistence.converter;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import static java.time.ZoneOffset.UTC;
import static java.util.Objects.nonNull;

import static lombok.AccessLevel.PRIVATE;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class OffsetDateTimeConverter
{
    public static OffsetDateTime toOffsetDateTime(final Timestamp timestamp)
    {
        return nonNull(timestamp) ? OffsetDateTime.ofInstant(timestamp.toInstant(), UTC) : null;
    }

    public static Timestamp toTimestamp(final OffsetDateTime offsetDateTime)
    {
        return nonNull(offsetDateTime) ? Timestamp.valueOf(offsetDateTime.atZoneSameInstant(UTC).toLocalDateTime()) : null;
    }
}