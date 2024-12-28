package com.chris.cutils;

import java.security.SecureRandom;

public class CString {
  
  /**
   * Reverses the given string.
   *
   * @param input the string to be reversed. If null, the method returns null.
   * @return the reversed string, or null if the input is null.
   */
  public static String reverse(String input) {
    return input == null ? null : new StringBuilder(input).reverse().toString();
  }
  
  public static String trimToEmpty(String input) {
    return input == null ? "" : input.trim();
  }
  
  public static boolean isNullOrEmpty(String input) {
    return input == null || input.isEmpty();
  }
  
  public static boolean isNullOrBlank(String input) {
    return input == null || input.trim().isEmpty();
  }
  
  /**
   * Capitalizes the first character of the given string and converts the rest to lowercase.
   *
   * @param str the string to be capitalized. If null or empty, the method returns the input as is.
   * @return the capitalized string, or the original string if it is null or empty.
   */
  public static String capitalize(String str) {
    if (str == null || str.isEmpty()) return str;
    return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
  }
  
  /**
   * Uncapitalizes the first character of the given string.
   * This method converts the first character of the input string to lowercase,
   * leaving the rest of the string unchanged.
   *
   * @param str the string to be uncapitalized. If null or empty, the method returns the input as is.
   * @return the uncapitalized string, or the original string if it is null or empty.
   */
  public static String uncapitalize(String str) {
    if (str == null || str.isEmpty()) return str;
    return str.substring(0, 1).toLowerCase() + str.substring(1);
  }
  
  public static boolean isNumeric(String str) {
    return str != null && str.matches("\\d+");
  }
  
  /**
   * Pads the left side of a string with a specified character to reach a desired length.
   * If the input string is null, it is treated as an empty string.
   * If the input string's length is already equal to or greater than the specified length,
   * the original string is returned without modification.
   *
   * @param str     the string to be padded. If null, it is treated as an empty string.
   * @param length  the desired length of the resulting string.
   * @param padChar the character to use for padding.
   * @return the padded string, or the original string if its length is already
   * equal to or greater than the specified length.
   */
  public static String padLeft(String str, int length, char padChar) {
    if (str == null) str = "";
    if (str.length() >= length) return str;
    return String.valueOf(padChar).repeat(length - str.length()) + str;
  }
  
  public static String padRight(String str, int length, char padChar) {
    if (str == null) str = "";
    if (str.length() >= length) return str;
    return str + String.valueOf(padChar).repeat(length - str.length());
  }
  
  public static String randomString(int length) {
    final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    SecureRandom random = new SecureRandom();
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      sb.append(chars.charAt(random.nextInt(chars.length())));
    }
    return sb.toString();
  }
}
