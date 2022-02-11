/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.yvpopov.tools;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/*import java.util.Date;
import java.util.Calendar;

 */

/**
 *
 * @author yvpop
 */
public class ConvertDateTime {

    java.time.Instant instant;
    
    ZoneOffset zone;

    public ZoneId getZone() {
        return zone;
    }

    public ConvertDateTime setZone(ZoneOffset zone) {
        this.zone = zone;
        return this;
    }
    
    private ConvertDateTime(){        
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

}
