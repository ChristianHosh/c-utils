package com.chris.cutils;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Objects;
import java.util.TimeZone;

@SuppressWarnings("unused")
public final class CDate implements Comparable<CDate> {
  
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
  
  private final LocalDateTime dateTime;
  
  public CDate(LocalDateTime dateTime) {
    this.dateTime = dateTime;
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
  
  public static String getDateLabel(CDate date1, CDate date2) {
    date1 = Objects.requireNonNull(date1).zeroTime();
    date2 = Objects.requireNonNull(date2).zeroTime();
    if (date1.isLess(date2))
      throw new IllegalArgumentException("date1 should be greater or equal to date2");
    
    if (date1.equals(date2)) {
      return date1.toDateString();
    } else if (date1.getMonth() == date2.getMonth() && date1.isFirstDayOfMonth() && date2.isLastDayOfMonth()) {
      return CString.padLeft(String.valueOf(date1.getMonth()), 2, '0') + "/" + date1.getYear();
    } else if (date1.getYear() == date2.getYear() && date1.isFirstDayOfYear() && date2.isLastDayOfYear()) {
      return String.valueOf(date1.getYear());
    } else if (date1.addMonth(3).equals(date2) && date1.isFirstDayOfYear() && date2.isLastDayOfMonth()) {
      return "Q" + ((date1.getMonth() / 3) + 1) + "/" + date1.getYear();
    }
    
    return date1.toDateString() + " - " + date2.toDateString();
  }
  
  public String format(String pattern) {
    return format(this, pattern);
  }
  
  public long getTime() {
    return this.toInstantMilli();
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
    return ZERO_TIME.equals(this.toLocalTime()) ? this : new CDate(toLocalDate(), ZERO_TIME);
  }
  
  public CDate zeroDate() {
    return ZERO_DATE.equals(this.toLocalDate()) ? this : new CDate(ZERO_DATE, toLocalTime());
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
  
  public boolean inPeriod(CPeriod period) {
    return inPeriod(period.getStart(), period.getEnd());
  }
  
  public boolean inPeriod(CDate start, CDate end) {
    return this.isGreaterOrEqual(start) && this.isLess(end);
  }
  
  public boolean inOpenPeriod(CDate start, CDate end) {
    if (start != null && end != null)
      return inPeriod(start, end);
    else if (start != null) {
      return this.isGreaterOrEqual(start);
    } else if (end != null) {
      return this.isLess(end);
    }
    
    return true;
  }
  
  public int hashCode() {
    return 111 + Objects.hash(this.dateTime);
  }
  
  public boolean equals(Object obj) {
    if (!(obj instanceof CDate date)) {
      return false;
    } else {
      return this.compareTo(date) == 0;
    }
  }
  
  public boolean equalsDate(CDate date) {
    if (date == null) return false;
    return this.toLocalDate().equals(date.toLocalDate());
  }
  
  public boolean equalsTime(CDate date) {
    if (date == null) return false;
    return this.toLocalTime().equals(date.toLocalTime());
  }
  
  @Override
  public int compareTo(CDate that) {
    return this.dateTime.compareTo(that.dateTime);
  }
  
  public boolean isLastDayOfMonth() {
    LocalDate date = this.toLocalDate();
    return date.getDayOfMonth() == date.lengthOfMonth();
  }
  
  public boolean isFirstDayOfMonth() {
    return this.toLocalDate().getDayOfMonth() == 1;
  }
  
  public CDate toFirstDayOfMonth() {
    return this.isFirstDayOfMonth() ? this : new CDate(this.dateTime.withDayOfMonth(1));
  }
  
  public CDate toLastDayOfMonth() {
    return this.isLastDayOfMonth() ? this : new CDate(this.dateTime.withDayOfMonth(this.toLocalDate().lengthOfMonth()));
  }
  
  public boolean isLastDayOfYear() {
    LocalDate date = this.toLocalDate();
    return date.getDayOfYear() == date.lengthOfYear();
  }
  
  public boolean isFirstDayOfYear() {
    return this.toLocalDate().getDayOfYear() == 1;
  }
  
  public CDate toFirstDayOfYear() {
    return this.isFirstDayOfYear() ? this : new CDate(this.dateTime.withDayOfYear(1));
  }
  
  public CDate toLastDayOfYear() {
    return this.isLastDayOfYear() ? this : new CDate(this.dateTime.withDayOfYear(this.toLocalDate().lengthOfYear()));
  }
  
  public Month getMonthEnum() {
    return this.dateTime.getMonth();
  }

  public CDate plus(TemporalAmount amount) {
    if (amount instanceof Period period && period.isZero()) return this;
    if (amount instanceof Duration duration && duration.isZero()) return this;
    return new CDate(toLocalDateTime().plus(amount));
  }


  public CDate minus(TemporalAmount amount) {
    if (amount instanceof Period period && period.isZero()) return this;
    if (amount instanceof Duration duration && duration.isZero()) return this;
    return new CDate(toLocalDateTime().minus(amount));
  }
}
