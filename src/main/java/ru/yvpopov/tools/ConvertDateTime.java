package ru.yvpopov.tools;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ConvertDateTime {

    java.time.Instant instant;

    ZoneOffset zone;

    public ConvertDateTime plus(long amountToAdd, ChronoUnit unit) {
        if (amountToAdd == 0) {
            return this;
        }
        Calendar cl1;
        Calendar cl2;
        switch (unit) {
            case WEEKS -> {
                return this.plus(amountToAdd * 7, ChronoUnit.DAYS);
            }
            case MONTHS -> {
                cl1 = Calendar.getInstance();
                cl2 = Calendar.getInstance();
                cl2.setTimeInMillis(cl1.getTimeInMillis());
                cl2.add(Calendar.MONTH, (int) amountToAdd);
                return this.plus((cl2.getTimeInMillis() - cl1.getTimeInMillis()), ChronoUnit.MILLIS);
            }
            case YEARS -> {
                cl1 = Calendar.getInstance();
                cl2 = Calendar.getInstance();
                cl2.setTimeInMillis(cl1.getTimeInMillis());
                cl2.add(Calendar.YEAR, (int) amountToAdd);
                return this.plus((cl2.getTimeInMillis() - cl1.getTimeInMillis()), ChronoUnit.MILLIS);
            }
            case DECADES -> {
                return this.plus(amountToAdd * 10, ChronoUnit.YEARS);
            }
            case CENTURIES -> {
                return this.plus(amountToAdd * 100, ChronoUnit.YEARS);
            }
            case MILLENNIA -> {
                return this.plus(amountToAdd * 1000, ChronoUnit.YEARS);
            }
            case ERAS -> {
                return this.plus(amountToAdd, ChronoUnit.FOREVER);
            }
            case FOREVER -> {
                this.instant = (amountToAdd > 0 ? java.time.Instant.MAX : java.time.Instant.MIN);
            }
            default ->
                instant = instant.plus(amountToAdd, unit);
        }
        return this;
    }

    public ConvertDateTime minus(long amountToAdd, ChronoUnit unit) {
        return this.plus(amountToAdd * -1L, unit);
    }

    public ZoneId getZone() {
        return zone;
    }

    public ConvertDateTime setZone(ZoneOffset zone) {
        this.zone = zone;
        return this;
    }

    public ConvertDateTime() {
        this.instant = Instant.now();
        zone = ZoneId.systemDefault().getRules().getOffset(Instant.now());
    }

    public ConvertDateTime(Instant instant) {
        this();
        this.instant = instant;
    }

    public ConvertDateTime(Calendar calendar) {
        this();
        this.instant = calendar.toInstant();
    }

    public ConvertDateTime(java.util.Date date) {
        this();
        this.instant = date.toInstant();
    }

    public ConvertDateTime(java.sql.Date sqldate) {
        this();
        this.instant = sqldate.toInstant();
    }

    public ConvertDateTime(OffsetDateTime offsetDateTime) {
        this();
        this.instant = offsetDateTime.toInstant();
    }

    public ConvertDateTime(ZonedDateTime zonedDateTime) {
        this();
        this.instant = zonedDateTime.toInstant();
    }

    public ConvertDateTime(LocalDateTime localDateTime) {
        this();
        this.instant = localDateTime.toInstant(zone);
    }

    public ConvertDateTime(LocalDateTime localDateTime, ZoneOffset zone) {
        this();
        this.zone = zone;
        this.instant = localDateTime.toInstant(this.zone);
    }

    public ConvertDateTime(java.sql.Timestamp sqltimestamp) {
        this();
        this.instant = sqltimestamp.toInstant();
    }

    public ConvertDateTime(com.google.protobuf.Timestamp timestamp) {
        this();
        this.instant = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }

    public java.time.Instant toInstant() {
        return this.instant;
    }

    public java.util.Calendar toCalendar() {
        return GregorianCalendar.from(toZonedDateTime());
    }

    public java.util.Date toDate() {
        return Date.from(instant);
    }

    public java.sql.Date toSqlDate() {
        return new java.sql.Date(toDate().getTime());
    }

    public java.sql.Timestamp toSqlTimestamp() {
        return java.sql.Timestamp.from(instant);
    }

    public java.time.OffsetDateTime toOffsetDateTime() {
        return java.time.OffsetDateTime.ofInstant(instant, zone);
    }

    public java.time.ZonedDateTime toZonedDateTime() {
        return ZonedDateTime.ofInstant(instant, zone);
    }

    public java.time.LocalDateTime toLocalDateTime() {
        return java.time.LocalDateTime.ofInstant(instant, zone);
    }

    public com.google.protobuf.Timestamp toTimestamp() {
        return com.google.protobuf.Timestamp.newBuilder().setSeconds(instant.getEpochSecond()).setNanos(instant.getNano()).build();
    }
    
    public static int Compare(com.google.protobuf.Timestamp x, com.google.protobuf.Timestamp y) {
        int a = Long.compare(x.getSeconds(), y.getSeconds());
        if (a == 0) 
            a = Integer.compare(x.getNanos(),y.getNanos());
        return a;
    }

    @Override
    public String toString() {
        return instant.toString();
    }

}
