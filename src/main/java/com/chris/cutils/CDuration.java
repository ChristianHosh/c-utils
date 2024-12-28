package com.chris.cutils;

import java.time.Duration;
import java.time.ZoneId;
import java.util.Objects;

public class CDuration implements Comparable<CDuration> {
  
  public static final CDuration ZERO = new CDuration(Duration.ZERO);
  public static final CDuration ONE_SECOND = ofSeconds(1);
  public static final CDuration ONE_MINUTE = ofMinutes(1);
  public static final CDuration ONE_HOUR = ofHours(1);
  public static final CDuration ONE_DAY = ofDays(1);
  
  private final Duration value;
  
  public CDuration(Duration value) {
    this.value = value;
  }
  
  public CDuration(CPeriod period) {
    this.value = Duration.between(period.getStart().toLocalDateTime(), period.getEnd().toLocalDateTime());
  }
  
  public static CDuration ofSeconds(long seconds) {
    return new CDuration(Duration.ofSeconds(seconds));
  }
  
  public static CDuration ofMinutes(long minutes) {
    return new CDuration(Duration.ofMinutes(minutes));
  }
  
  public static CDuration ofHours(long hours) {
    return new CDuration(Duration.ofHours(hours));
  }
  
  public static CDuration ofDays(long days) {
    return new CDuration(Duration.ofDays(days));
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
    return value.toString();
  }
  
  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof CDuration that)) return false;
    
    return Objects.equals(value, that.value);
  }
  
  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
  
  @Override
  public int compareTo(CDuration o) {
    return this.value.compareTo(o.value);
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
    
    public CPeriod build() {
      return build(ZoneId.systemDefault());
    }
    
    public CPeriod build(ZoneId zoneId) {
      CDate start = CDate.currentDate(zoneId);
      CDate end = start.addYear(years)
          .addMonth(months)
          .addDay(days)
          .addHour(hours)
          .addMinute(minutes)
          .addSecond(seconds)
          .addMillis(milliseconds);
      return new CPeriod(start, end);
    }
  }
}
