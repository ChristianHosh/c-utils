package com.chris.cutils;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.util.Objects;
import java.util.TimeZone;

@SuppressWarnings("unused")
public final class CDate implements Comparable<CDate>, Temporal, TemporalAdjuster {
  
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
  
  private final LocalDateTime value;
  
  public CDate(LocalDateTime value) {
    this.value = value;
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
    this.value = LocalDateTime.of(date, time);
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
    return this.value.atZone(UTC).toInstant();
  }
  
  public LocalDateTime toLocalDateTime() {
    return this.value;
  }
  
  public LocalDate toLocalDate() {
    return this.value.toLocalDate();
  }
  
  public LocalTime toLocalTime() {
    return this.value.toLocalTime();
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
    return new CDate(this.value.plus(millis, ChronoUnit.MILLIS));
  }
  
  public CDate addSecond(long seconds) {
    return new CDate(this.value.plusSeconds(seconds));
  }
  
  public CDate addMinute(long minutes) {
    return new CDate(this.value.plusMinutes(minutes));
  }
  
  public CDate addHour(long hours) {
    return new CDate(this.value.plusHours(hours));
  }
  
  public CDate addDay(long days) {
    return new CDate(this.value.plusDays(days));
  }
  
  public CDate addWeek(long weeks) {
    return new CDate(this.value.plusWeeks(weeks));
  }
  
  public CDate addMonth(long months) {
    return new CDate(this.value.plusMonths(months));
  }
  
  public CDate addYear(long years) {
    return new CDate(this.value.plusYears(years));
  }
  
  public int getYear() {
    return this.value.getYear();
  }
  
  public int getMonth() {
    return this.value.getMonthValue();
  }
  
  public int getDay() {
    return this.value.getDayOfMonth();
  }
  
  public int getHour() {
    return this.value.getHour();
  }
  
  public int getMinute() {
    return this.value.getMinute();
  }
  
  public int getSecond() {
    return this.value.getSecond();
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
    return this.value.getDayOfWeek();
  }
  
  public CPeriod toPeriod(CDate other) {
    return this.isLessOrEqual(other) ? new CPeriod(this, other) : new CPeriod(other, this);
  }
  
  public boolean inPeriod(CPeriod period) {
    return inPeriod(period.getStart(), period.getEnd());
  }
  
  public boolean inPeriod(CDate start, CDate end) {
    return this.isGreaterOrEqual(start) && this.isLessOrEqual(end);
  }
  
  public boolean inOpenPeriod(CDate start, CDate end) {
    if (start != null && end != null)
      return inPeriod(start, end);
    else if (start != null) {
      return this.isGreaterOrEqual(start);
    } else if (end != null) {
      return this.isLessOrEqual(end);
    }
    
    return true;
  }
  
  public int hashCode() {
    return 111 + Objects.hash(this.value);
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
    return this.value.compareTo(that.value);
  }
  
  public boolean isLastDayOfMonth() {
    LocalDate date = this.toLocalDate();
    return date.getDayOfMonth() == date.lengthOfMonth();
  }
  
  public boolean isFirstDayOfMonth() {
    return this.toLocalDate().getDayOfMonth() == 1;
  }
  
  public CDate toFirstDayOfMonth() {
    return this.isFirstDayOfMonth() ? this : new CDate(this.value.withDayOfMonth(1));
  }
  
  public CDate toLastDayOfMonth() {
    return this.isLastDayOfMonth() ? this : new CDate(this.value.withDayOfMonth(this.toLocalDate().lengthOfMonth()));
  }
  
  public boolean isLastDayOfYear() {
    LocalDate date = this.toLocalDate();
    return date.getDayOfYear() == date.lengthOfYear();
  }
  
  public boolean isFirstDayOfYear() {
    return this.toLocalDate().getDayOfYear() == 1;
  }
  
  public CDate toFirstDayOfYear() {
    return this.isFirstDayOfYear() ? this : new CDate(this.value.withDayOfYear(1));
  }
  
  public CDate toLastDayOfYear() {
    return this.isLastDayOfYear() ? this : new CDate(this.value.withDayOfYear(this.toLocalDate().lengthOfYear()));
  }
  
  public Month getMonthEnum() {
    return this.value.getMonth();
  }

  @Override
  public boolean isSupported(TemporalUnit unit) {
    return value.isSupported(unit);
  }

  @Override
  public CDate with(TemporalField field, long newValue) {
    return get(field) == newValue ? this : new CDate(this.value.with(field, newValue));
  }

  @Override
  public int get(TemporalField field) {
    return this.value.get(field);
  }

  @Override
  public CDate plus(long amountToAdd, TemporalUnit unit) {
    return amountToAdd == 0 ? this : new CDate(this.value.plus(amountToAdd, unit));
  }

  @Override
  public long until(Temporal endExclusive, TemporalUnit unit) {
    return this.value.until(endExclusive, unit);
  }

  @Override
  public boolean isSupported(TemporalField field) {
    return this.value.isSupported(field);
  }

  @Override
  public long getLong(TemporalField field) {
    return this.value.getLong(field);
  }

  @Override
  public Temporal adjustInto(Temporal temporal) {
    return this.value.adjustInto(temporal);
  }
}
