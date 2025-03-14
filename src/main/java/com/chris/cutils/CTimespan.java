package com.chris.cutils;

import java.time.Duration;
import java.time.Period;
import java.util.Objects;

public class CTimespan {

  public static final Duration D_ZERO = Duration.ZERO;
  public static final Period P_ZERO = Period.ZERO;
  
  public static final CTimespan ZERO = new CTimespan(P_ZERO, D_ZERO);
  
  private final Duration duration;
  private final Period period;
  
  private CTimespan(Period period, Duration duration) {
    long extraDays = duration.toDays();
    this.period = period.plusDays(extraDays);
    this.duration = duration.minusDays(extraDays);
  }

  public static CTimespan ofSeconds(int seconds) {
    return seconds == 0 ? ZERO : of(0, 0, seconds);
  }
  
  public static CTimespan ofMinutes(int minutes) {
    return minutes == 0 ? ZERO : of(0, minutes, 0);
  }
  
  public static CTimespan ofHours(int hours) {
    return hours == 0 ? ZERO : of(hours, 0, 0);
  }
  
  public static CTimespan ofDays(int days) {
    return days == 0 ? ZERO : ofPeriod(Period.ofDays(days));
  }
  
  public static CTimespan ofWeeks(int weeks) {
    return weeks == 0 ? ZERO : ofPeriod(Period.ofWeeks(weeks));
  }
  
  public static CTimespan ofMonths(int months) {
    return months == 0 ? ZERO : ofPeriod(Period.ofMonths(months));
  }
  
  public static CTimespan ofYears(int years) {
    return years == 0 ? ZERO : ofPeriod(Period.ofYears(years));
  }
  
  public static CTimespan ofPeriod(Period period) {
    return of(period, D_ZERO);
  }

  public static CTimespan ofDuration(Duration duration) {
    return of(P_ZERO, duration);    
  }

  public static CTimespan of(Period period, Duration duration) {
    return new CTimespan(period, duration);
  }

  public static CTimespan of(int hours, int minutes, int seconds) {
    return new CTimespan(P_ZERO, Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds));
  }

  public CTimespan plus(Period period, Duration duration) {
    return period.isZero() && duration.isZero() ? this : new CTimespan(this.period.plus(period), this.duration.plus(duration));
  }

  public CTimespan plus(CTimespan other) {
    return this.plus(other.period, other.duration);
  }

  public CTimespan minus(Period period, Duration duration) {
    return period.isZero() && duration.isZero() ? this : new CTimespan(this.period.minus(period), this.duration.minus(duration));
  }

  public CTimespan minus(CTimespan other) {
    return this.minus(other.period, other.duration);
  }
  
  public CPeriod toPeriodStartingOn(CDate start) {
    return new CPeriod(start, addTo(start));
  }
  
  public CPeriod toPeriodEndingOn(CDate end) {
    return new CPeriod(subtractFrom(end), end);
  }
  
  public CDate addTo(CDate dateTime) {
    return dateTime.plus(period).plus(duration);
  }

  public CDate subtractFrom(CDate dateTime) {
    return dateTime.minus(period).minus(duration);
  }

  public Period getPeriodValue() {
    return period;
  }

  public Duration getDurationValue() {
    return duration;
  }
  
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CTimespan cTimespan)) return false;
    return Objects.equals(duration, cTimespan.duration) && Objects.equals(period, cTimespan.period);
  }

  @Override
  public int hashCode() {
    return Objects.hash(duration, period);
  }
}
