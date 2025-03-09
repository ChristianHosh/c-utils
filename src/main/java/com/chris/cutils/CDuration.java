package com.chris.cutils;

import java.time.Duration;
import java.time.temporal.*;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class CDuration implements Comparable<CDuration>, TemporalAmount {
  
  public static final CDuration ZERO = new CDuration(Duration.ZERO);
  public static final String DAY_PART = "d";  public static final CDuration ONE_SECOND = ofSeconds(1);
  public static final String HOUR_PART = "h";  public static final CDuration ONE_MINUTE = ofMinutes(1);
  public static final String MINUTE_PART = "m";  public static final CDuration ONE_HOUR = ofHours(1);
  public static final String SECOND_PART = "s";  public static final CDuration ONE_DAY = ofDays(1);
  public static final String FULL_DURATION_FORMAT = DAY_PART + HOUR_PART + MINUTE_PART + SECOND_PART;
  
  static final int SECONDS_IN_DAY = 86400;
  static final int SECONDS_IN_HOUR = 3600;
  static final int SECONDS_IN_MINUTE = 60;
  static final int MINUTES_IN_DAY = 1440;
  static final int HOURS_IN_DAY = 24;
  private static final List<TemporalUnit> UNITS = List.of(ChronoUnit.NANOS, ChronoUnit.SECONDS, ChronoUnit.MINUTES, ChronoUnit.HOURS, ChronoUnit.DAYS);
  
  private final Duration value;
  
  private CDuration(Duration value) {
    this.value = value;
  }
  
  public static CDuration ofSeconds(long seconds) {
    if (seconds == 0) return ZERO;
    if (seconds == 1) return ONE_SECOND;
    if (seconds == SECONDS_IN_MINUTE) return ONE_MINUTE;
    if (seconds == SECONDS_IN_HOUR) return ONE_HOUR;
    if (seconds == SECONDS_IN_DAY) return ONE_DAY;
    return new CDuration(Duration.ofSeconds(seconds));
  }
  
  public static CDuration ofMinutes(long minutes) {
    if (minutes == 0) return ZERO;
    if (minutes == 1) return ONE_MINUTE;
    if (minutes == SECONDS_IN_MINUTE) return ONE_HOUR;
    if (minutes == MINUTES_IN_DAY) return ONE_DAY;
    return new CDuration(Duration.ofMinutes(minutes));
  }
  
  public static CDuration ofHours(long hours) {
    if (hours == 0) return ZERO;
    if (hours == 1) return ONE_HOUR;
    if (hours == HOURS_IN_DAY) return ONE_DAY;
    return new CDuration(Duration.ofHours(hours));
  }
  
  public static CDuration ofDays(long days) {
    if (days == 0) return ZERO;
    return days == 1 ? ONE_DAY : new CDuration(Duration.ofDays(days));
  }
  
  public static CDuration valueOf(Duration duration) {
    return duration == null || duration.isZero() ? ZERO : new CDuration(duration);
  }
  
  public static CDuration valueOf(CPeriod period) {
    return valueOf(Duration.between(period.getStart().toLocalDateTime(), period.getEnd().toLocalDateTime()));
  }
  
  public static Builder builder() {
    return new Builder();
  }
  
  public long toSeconds() {
    return value.getSeconds();
  }
  
  public long toMinutes() {
    return value.toMinutes();
  }
  
  public long toHours() {
    return value.toHours();
  }
  
  public long toDays() {
    return value.toDays();
  }
  
  public long toMillis() {
    return value.toMillis();
  }
  
  public Duration getValue() {
    return value;
  }
  
  public CDuration add(CDuration other) {
    return other == null ? this : new CDuration(value.plus(other.value));
  }
  
  public CDuration minus(CDuration other) {
    return other == null ? this : new CDuration(value.minus(other.value));
  }
  
  public CDuration multiply(long factor) {
    return new CDuration(value.multipliedBy(factor));
  }
  
  public CDuration abs() {
    return this.isNegative() ? new CDuration(this.value.negated()) : this;
  }
  
  public boolean isNegative() {
    return this.value.isNegative();
  }
  
  public boolean isZero() {
    return this.value.isZero();
  }
  
  public boolean isPositive() {
    return !this.value.isNegative();
  }
  
  public boolean isLongerOrEqual(CDuration other) {
    return this.compareTo(other) >= 0;
  }
  
  public boolean isLonger(CDuration other) {
    return this.compareTo(other) > 0;
  }
  
  public boolean isShorterOrEqual(CDuration other) {
    return this.compareTo(other) <= 0;
  }
  
  public boolean isShorter(CDuration other) {
    return this.compareTo(other) < 0;
  }
  
  public CPeriod toPeriod(CDate start) {
    return new CPeriod(start, this);
  }
  
  @Override
  public String toString() {
    return toString(FULL_DURATION_FORMAT);
  }
  
  public String toString(String format) {
    long totalSeconds = value.getSeconds();
    
    long days = totalSeconds / 86400;
    long hours = (totalSeconds % 86400) / 3600;
    long minutes = (totalSeconds % 3600) / 60;
    long seconds = totalSeconds % 60;
    
    StringBuilder sb = new StringBuilder();
    
    if (format.contains(DAY_PART) && days > 0) sb.append(days).append("d ");
    if (format.contains(HOUR_PART) && hours > 0) sb.append(hours).append("h ");
    if (format.contains(MINUTE_PART) && minutes > 0) sb.append(minutes).append("m ");
    if (format.contains(SECOND_PART) && seconds > 0) sb.append(seconds).append("s ");
    
    // If no components were added but the format expects something, add the smallest unit in the format
    if (sb.isEmpty()) {
      if (format.contains(DAY_PART)) sb.append(days).append("d ");
      else if (format.contains(HOUR_PART)) sb.append(hours).append("h ");
      else if (format.contains(MINUTE_PART)) sb.append(minutes).append("m ");
      else if (format.contains(SECOND_PART)) sb.append(seconds).append("s ");
    }
    
    return sb.toString().trim();
  }
  
  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof CDuration that)) return false;
    
    return this == that || this.value.equals(that.value);
  }
  
  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
  
  @Override
  public int compareTo(CDuration o) {
    return this.value.compareTo(o.value);
  }
  
  @Override
  public long get(TemporalUnit unit) {
    if (unit == ChronoUnit.SECONDS || unit == ChronoUnit.NANOS)
      return this.value.get(unit);
    else if (unit == ChronoUnit.MINUTES)
      return this.value.getSeconds() / SECONDS_IN_MINUTE;
    else if (unit == ChronoUnit.HOURS)
      return this.value.getSeconds() / SECONDS_IN_HOUR;
    else if (unit == ChronoUnit.DAYS)
      return this.value.getSeconds() / SECONDS_IN_DAY;
    else
      throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit);
  }
  
  @Override
  public List<TemporalUnit> getUnits() {
    return UNITS;
  }
  
  @Override
  public Temporal addTo(Temporal temporal) {
    return this.value.addTo(temporal);
  }
  
  @Override
  public Temporal subtractFrom(Temporal temporal) {
    return this.value.subtractFrom(temporal);
  }
  
  public static final class Builder {
    private int years;
    private int months;
    private int days;
    private int hours;
    private int minutes;
    private int seconds;
    private int milliseconds;
    
    public Builder() {
    }
    
    public Builder years(int years) {
      this.years = years;
      return this;
    }
    
    public Builder months(int months) {
      this.months = months;
      return this;
    }
    
    public Builder days(int days) {
      this.days = days;
      return this;
    }
    
    public Builder hours(int hours) {
      this.hours = hours;
      return this;
    }
    
    public Builder minutes(int minutes) {
      this.minutes = minutes;
      return this;
    }
    
    public Builder seconds(int seconds) {
      this.seconds = seconds;
      return this;
    }
    
    public Builder milliseconds(int milliseconds) {
      this.milliseconds = milliseconds;
      return this;
    }
    
    public CDuration build() {
      CDate start = CDate.EPOCH_ZERO;
      CDate end = start.addYear(years)
          .addMonth(months)
          .addDay(days)
          .addHour(hours)
          .addMinute(minutes)
          .addSecond(seconds)
          .addMillis(milliseconds);
      return valueOf(new CPeriod(start, end));
    }
  }
}
