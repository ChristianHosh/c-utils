package com.chris.cutils;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Objects;

@SuppressWarnings("unused")
public final class CPeriod implements Comparable<CPeriod> {
  
  private final CDate start;
  private final CDate end;
  
  public CPeriod(CDate start, CDate end) {
    Objects.requireNonNull(start);
    Objects.requireNonNull(end);
    
    if (start.isGreater(end)) {
      throw new IllegalArgumentException("Start date cannot be after end date");
    }
    
    this.start = start;
    this.end = end;
  }
  
  public CPeriod(CDate start, CDuration duration) {
    Objects.requireNonNull(start);
    Objects.requireNonNull(duration);
    
    this.start = start;
    this.end = start.addMillis(duration.toMillis());
  }
  
  public CDate getStart() {
    return start;
  }
  
  public CDate getEnd() {
    return end;
  }
  
  public CDuration toDuration() {
    return CDuration.valueOf(this);
  }
  
  public long getDurationInMilliseconds() {
    return getDuration(ChronoUnit.MILLIS);
  }
  
  public long getDurationInSeconds() {
    return getDuration(ChronoUnit.SECONDS);
  }
  
  public long getDurationInMinutes() {
    return getDuration(ChronoUnit.MINUTES);
  }
  
  public long getDurationInHours() {
    return getDuration(ChronoUnit.HOURS);
  }
  
  public long getDurationInDays() {
    return getDuration(ChronoUnit.DAYS);
  }
  
  public long getDurationInWeeks() {
    return getDuration(ChronoUnit.WEEKS);
  }
  
  public long getDurationInMonths() {
    return getDuration(ChronoUnit.MONTHS);
  }
  
  public long getDurationInYears() {
    return getDuration(ChronoUnit.YEARS);
  }
  
  public long getDuration(TemporalUnit unit) {
    return this.start.toLocalDateTime().until(this.end.toLocalDateTime(), unit);
  }
  
  public boolean contains(CDate date) {
    return date.inPeriod(this);
  }
  
  public boolean overlaps(CPeriod other) {
    return !(this.end.isLess(other.start) || this.start.isGreater(other.end));
  }
  
  public CPeriod shiftSeconds(int seconds) {
    return seconds == 0 ? this : new CPeriod(this.start.addSecond(seconds), this.end.addSecond(seconds));
  }
  
  public CPeriod shiftMinutes(int minutes) {
    return minutes == 0 ? this : new CPeriod(this.start.addMinute(minutes), this.end.addMinute(minutes));
  }
  
  public CPeriod shiftHours(int hours) {
    return hours == 0 ? this : new CPeriod(this.start.addHour(hours), this.end.addHour(hours));
  }
  
  public CPeriod shiftDays(int days) {
    return days == 0 ? this : new CPeriod(this.start.addDay(days), this.end.addDay(days));
  }
  
  public CPeriod shiftWeeks(int weeks) {
    return weeks == 0 ? this : new CPeriod(this.start.addWeek(weeks), this.end.addWeek(weeks));
  }
  
  public CPeriod shiftMonths(int months) {
    return months == 0 ? this : new CPeriod(this.start.addMonth(months), this.end.addMonth(months));
  }
  
  public CPeriod shiftYears(int years) {
    return years == 0 ? this : new CPeriod(this.start.addYear(years), this.end.addYear(years));
  }
  
  public CPeriod extend(int days) {
    return new CPeriod(this.start, this.end.addDay(days));
  }
  
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CPeriod that)) return false;
    
    return this == that || (this.start.equals(that.start) && this.end.equals(that.end));
  }
  
  @Override
  public int hashCode() {
    int result = start.hashCode();
    result = 31 * result + end.hashCode();
    return result;
  }
  
  public boolean isLongerOrEqual(CPeriod other) {
    return this.getDurationInMilliseconds() >= other.getDurationInMilliseconds();
  }
  
  public boolean isLonger(CPeriod other) {
    return this.getDurationInMilliseconds() > other.getDurationInMilliseconds();
  }
  
  public boolean isShorterOrEqual(CPeriod other) {
    return this.getDurationInMilliseconds() <= other.getDurationInMilliseconds();
  }
  
  public boolean isShorter(CPeriod other) {
    return this.getDurationInMilliseconds() < other.getDurationInMilliseconds();
  }
  
  @Override
  public String toString() {
    return this.start + " - " + this.end;
  }
  
  public String toDateLabel() {
    return CDate.getDateLabel(this.start, this.end);
  }
  
  @Override
  public int compareTo(CPeriod o) {
    if (this.start.equals(o.start))
      return this.end.compareTo(o.end);
    return this.start.compareTo(o.start);
  }
}
