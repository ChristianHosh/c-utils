package com.chris.cutils;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

public class CDate implements Comparable<CDate> {
  
  public static final long DAY_IN_MS = 86400000L;
  public static final int SECOND = 1000;
  public static final int MINUTE = SECOND * 60;
  public static final int HOUR = MINUTE * 60;
  
  private static final ZoneOffset UTC = ZoneOffset.UTC;
  private static final LocalTime ZERO_TIME = LocalTime.MIN;
  private static final LocalDate ZERO_DATE = LocalDate.EPOCH;
  
  public static final CDate EPOCH_ZERO = new CDate(ZERO_DATE, ZERO_TIME);
  
  static {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }
  
  private LocalDateTime dateTime;
  private long time;
  
  public CDate(long time) {
    this.setTime(time);
  }
  
  public CDate(LocalDateTime dateTime) {
    this.dateTime = dateTime;
    this.time = this.toInstantMilli();
  }
  
  public CDate(CDate date) {
    this.setTime(date.getTime());
  }
  
  public CDate(int day, int month, int year) {
    this(LocalDate.of(year, month, day));
  }
  
  public CDate(LocalDate date) {
    this(date, ZERO_TIME);
  }
  
  public CDate(LocalTime time) {
    this(ZERO_DATE, time);
  }
  
  public CDate(LocalDate date, LocalTime time) {
    this.dateTime = LocalDateTime.of(date, time);
    this.time = this.toInstantMilli();
  }
  
  private static long truncate(long time) {
    return time - time % DAY_IN_MS;
  }
  
  public static CDate currentServerDate() {
    return currentDate(UTC);
  }
  
  public static CDate currentDate(ZoneId zoneId)  {
    return new CDate(LocalDateTime.now(zoneId));
  }
  
  public static String format(CDate date, String format) {
    return date.toLocalDateTime().format(DateTimeFormatter.ofPattern(format));
  }
  
  public static CDate parse(String date, String pattern) {
    return new CDate(LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern)));
  }
  
  public String format(String pattern) {
    return format(this, pattern);
  }
  
  public long getTime() {
    return this.time;
  }
  
  public void setTime(long time) {
    this.time = time;
    this.dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), UTC);
    checkTime();
  }
  
  private void checkTime() {
    if (this.time != toInstantMilli())
      throw new IllegalArgumentException();
  }
  
  public long toInstantMilli() {
    return this.toInstant().toEpochMilli();
  }
  
  public Instant toInstant() {
    return this.dateTime.atZone(UTC).toInstant();
  }
  
  public LocalDateTime toLocalDateTime() {
    return this.dateTime;
  }
  
  public LocalDate toLocalDate() {
    return this.dateTime.toLocalDate();
  }
  
  public LocalTime toLocalTime() {
    return this.dateTime.toLocalTime();
  }
  
  @Override
  public String toString() {
    return toDateTimeString();
  }
  
  public String toDateString() {
    return format(this, "dd/MM/yyyy");
  }
  
  public String toTimeString() {
    return format(this, "HH:mm:ss");
  }
  
  public String toDateTimeString() {
    return format(this, "dd/MM/yyyy HH:mm:ss");
  }
  
  public CDate addMillis(long millis) {
    return new CDate(this.dateTime.plus(millis, ChronoUnit.MILLIS));
  }
  
  public CDate addSecond(long seconds) {
    return new CDate(this.dateTime.plusSeconds(seconds));
  }
  
  public CDate addMinute(long minutes) {
    return new CDate(this.dateTime.plusMinutes(minutes));
  }
  
  public CDate addHour(long hours) {
    return new CDate(this.dateTime.plusHours(hours));
  }
  
  public CDate addDay(long days) {
    return new CDate(this.dateTime.plusDays(days));
  }
  
  public CDate addWeek(long weeks) {
    return new CDate(this.dateTime.plusWeeks(weeks));
  }
  
  public CDate addMonth(long months) {
    return new CDate(this.dateTime.plusMonths(months));
  }
  
  public CDate addYear(long years) {
    return new CDate(this.dateTime.plusYears(years));
  }
  
  public int getYear() {
    return this.dateTime.getYear();
  }
  
  public int getMonth() {
    return this.dateTime.getMonthValue();
  }
  
  public int getDay() {
    return this.dateTime.getDayOfMonth();
  }
  
  public int getHour() {
    return this.dateTime.getHour();
  }
  
  public int getMinute() {
    return this.dateTime.getMinute();
  }
  
  public int getSecond() {
    return this.dateTime.getSecond();
  }
  
  public CDate zeroTime() {
    return new CDate(truncate(this.getTime()));
  }
  
  public boolean isGreater(CDate dd) {
    return this.compareTo(dd) > 0;
  }
  
  public boolean isGreaterOrEqual(CDate dd) {
    return this.compareTo(dd) >= 0;
  }
  
  public boolean isLess(CDate dd) {
    return this.compareTo(dd) < 0;
  }
  
  public boolean isLessOrEqual(CDate dd) {
    return this.compareTo(dd) <= 0;
  }
  
  public DayOfWeek getDayOfWeek() {
    return this.dateTime.getDayOfWeek();
  }
  
  public CPeriod toPeriod(CDate other) {
    return this.isLessOrEqual(other) ? new CPeriod(this, other) : new CPeriod(other, this);
  }
  
  public boolean isInPeriod(CPeriod period) {
    return period.contains(this);
  }
  
  public int hashCode() {
    return 111 + Long.hashCode(this.getTime());
  }
  
  public boolean equals(Object obj) {
    if (!(obj instanceof CDate date)) {
      return false;
    } else {
      return this.compareTo(date) == 0;
    }
  }
  
  @Override
  public int compareTo(CDate that) {
    if (that == null) {
      return -1;
    } else {
      long thisTime = this.getTime();
      long thatTime = that.getTime();
      if (thisTime == thatTime) {
        return 0;
      } else {
        return thisTime > thatTime ? 1 : -1;
      }
    }
  }
  
}
