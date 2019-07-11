package com.lmartino.bank.repository.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateTimeConverter {

    public static LocalDateTime toLocalDateTime(final Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return java.sql.Timestamp.valueOf(localDateTime);
    }
}
