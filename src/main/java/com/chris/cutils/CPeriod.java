package com.chris.cutils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Objects;

public class CPeriod implements Comparable<CPeriod> {
  
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
    return new CDuration(Duration.between(start.toLocalDateTime(), end.toLocalDateTime()));
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
    return !start.isGreater(date) && !end.isLess(date);
  }
  
  public boolean overlaps(CPeriod other) {
    return !(this.end.isLess(other.start) || this.start.isGreater(other.end));
  }
  
  public CPeriod shiftDays(int days) {
    return new CPeriod(this.start.addDay(days), this.end.addDay(days));
  }
  
  public CPeriod shiftWeeks(int weeks) {
    return new CPeriod(this.start.addWeek(weeks), this.end.addWeek(weeks));
  }
  
  public CPeriod shiftMonths(int months) {
    return new CPeriod(this.start.addMonth(months), this.end.addMonth(months));
  }
  
  public CPeriod shiftYears(int years) {
    return new CPeriod(this.start.addYear(years), this.end.addYear(years));
  }
  
  public CPeriod extend(int days) {
    return new CPeriod(this.start, this.end.addDay(days));
  }
  
  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof CPeriod that)) return false;
    
    return start.equals(that.start) && end.equals(that.end);
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
    return "CPeriod{" +
        "start=" + start +
        ", end=" + end +
        '}';
  }
  
  @Override
  public int compareTo(CPeriod o) {
    if (this.start.equals(o.start))
      return this.end.compareTo(o.end);
    return this.start.compareTo(o.start);
  }
}
