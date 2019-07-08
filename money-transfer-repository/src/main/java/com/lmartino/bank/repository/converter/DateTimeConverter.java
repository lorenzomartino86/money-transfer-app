package com.lmartino.bank.repository.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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
