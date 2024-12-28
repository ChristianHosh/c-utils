package com.chris.cutils;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CDecimal extends Number implements Comparable<CDecimal>, Serializable {
  
  @Serial
  private static final long serialVersionUID = 998866456123654789L;
  
  public static final BigDecimal BD_ZERO = BigDecimal.ZERO;
  public static final CDecimal ZERO = valueOf(0);
  public static final CDecimal ONE = valueOf(1);
  public static final CDecimal TWO = valueOf(2);
  public static final CDecimal THREE = valueOf(3);
  public static final CDecimal FOUR = valueOf(4);
  public static final CDecimal FIVE = valueOf(5);
  public static final CDecimal SIX = valueOf(6);
  public static final CDecimal TEN = valueOf(10);
  public static final CDecimal TWELVE = valueOf(12);
  public static final CDecimal HUNDRED = valueOf(100);
  
  private final BigDecimal value;
  
  public CDecimal(BigDecimal value) {
    this.value = value == null ? BD_ZERO : value;
  }
  
  public static CDecimal valueOf(int value) {
    return new CDecimal(BigDecimal.valueOf(value));
  }
  
  public static CDecimal valueOf(double value) {
    return new CDecimal(BigDecimal.valueOf(value));
  }
  
  public static CDecimal valueOf(String value) {
    return new CDecimal(new BigDecimal(value));
  }
  
  public static CDecimal fromString(String value) {
    try {
      return valueOf(value);
    } catch (NumberFormatException e) {
      return ZERO;
    }
  }
  
  public BigDecimal getValue() {
    return value;
  }
  
  @Override
  public int intValue() {
    return this.value.intValue();
  }
  
  @Override
  public long longValue() {
    return this.value.longValue();
  }
  
  @Override
  public float floatValue() {
    return this.value.floatValue();
  }
  
  @Override
  public double doubleValue() {
    return this.value.doubleValue();
  }
  
  public CDecimal add(CDecimal value) {
    return value == null ? this : new CDecimal(this.value.add(value.value));
  }
  
  public CDecimal subtract(CDecimal value) {
    return value == null ? this : new CDecimal(this.value.subtract(value.value));
  }
  
  public CDecimal multiply(CDecimal value) {
    return value == null ? ZERO : new CDecimal(this.value.multiply(value.value));
  }
  
  public CDecimal divide(CDecimal value) {
    return this.divide(value, RoundingMode.HALF_EVEN);
  }
  
  public CDecimal divide(CDecimal value, RoundingMode mode) {
    return value == null ? ZERO : new CDecimal(this.value.divide(value.value, mode));
  }
  
  public CDecimal abs() {
    return isNegative() ? new CDecimal(this.value.abs()) : this;
  }
  
  public CDecimal negate() {
    return new CDecimal(this.value.negate());
  }
  
  public CDecimal setScale(int scale) {
    return setScale(scale, RoundingMode.HALF_EVEN);
  }
  
  public CDecimal setScale(int scale, RoundingMode mode) {
    return new CDecimal(this.value.setScale(scale, mode));
  }
  
  public int scale() {
    return this.value.scale();
  }
  
  public boolean isZero() {
    return this.compareTo(ZERO) == 0;
  }
  
  public boolean isPositive() {
    return this.compareTo(ZERO) > 0;
  }
  
  public boolean isNegative() {
    return this.compareTo(ZERO) < 0;
  }
  
  public boolean isGreaterThan(CDecimal other) {
    return compareTo(other) > 0;
  }
  
  public boolean isLessThan(CDecimal other) {
    return compareTo(other) < 0;
  }
  
  public boolean isGreaterEqualTo(CDecimal other) {
    return compareTo(other) >= 0;
  }
  
  public boolean isLessEqualTo(CDecimal other) {
    return compareTo(other) <= 0;
  }
  
  public CDecimal max(CDecimal other) {
    return this.isGreaterEqualTo(other) ? this : other;
  }
  
  public CDecimal min(CDecimal other) {
    return this.isLessThan(other)? this : other;
  }
  
  public CDecimal pow(int exponent) {
    return exponent == 1 ? this : new CDecimal(this.value.pow(exponent));
  }
  
  public CDecimal percentOf(CDecimal base) {
    return base == null ? ZERO : this.multiply(base).divide(HUNDRED);
  }
  
  @Override
  public int hashCode() {
    return this.value.hashCode();
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof CDecimal other)) return false;
    return this.value.compareTo(other.value) == 0;
  }
  
  @Override
  public String toString() {
    return value.toString();
  }
  
  public String format(String pattern) {
    DecimalFormat df = new DecimalFormat(pattern);
    return df.format(this.value);
  }
  
  @Override
  public int compareTo(CDecimal o) {
    return this.value.compareTo(o.value);
  }
}
