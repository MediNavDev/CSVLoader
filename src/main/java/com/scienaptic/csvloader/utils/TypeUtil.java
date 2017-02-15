package com.scienaptic.csvloader.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class TypeUtil {
  private TypeUtil() { }

  public static boolean isNullOrEmpty(String s) {
    return s == null || s.length() == 0 || s.trim().length() == 0;
  }

  public static boolean isNotEmpty(String s) {
    return s != null && s.length() != 0 && s.trim().length() != 0;
  }

  public static final ImmutableList<String> MISSING_INDICATORS = ImmutableList.of("NaN", "*", "NA", "null");

  public static final ImmutableList<String> TRUE_STRINGS_FOR_DETECTION =
      ImmutableList.of("T", "t", "Y", "y", "TRUE", "true", "True", "YES", "Yes", "yes");

  public static final ImmutableList<String> FALSE_STRINGS_FOR_DETECTION =
      ImmutableList.of("F", "f", "N", "n", "FALSE", "false", "False", "NO", "No", "no");

  private static final List<String> dateFormats =
      Lists.newArrayList("yyyyMMdd", "MM/dd/yyyy", "MM-dd-yyyy", "MM.dd.yyyy",
          "yyyy-MM-dd", "yyyy/MM/dd", "dd/MM/yyyy", "dd-MMM-yyyy",
          "M/d/yyyy", "M/d/yy", "MMM/dd/yyyy", "MMM-dd-yyyy",
          "MMM/dd/yy", "MMM-dd-yy", "MMM/dd/yyyy", "MMM/d/yyyy",
          "MMM-dd-yy", "MMM dd, yyyy", "MMM d, yyyy", "dd-MM-yyyy");

  public static final ImmutableList<Tuple2<String, DateTimeFormatter>> dateFormatters =
      dateFormats.stream()
          .map(fmt -> Tuple2.of(fmt, DateTimeFormatter.ofPattern(fmt)))
          .collect(collectingAndThen(toList(), ImmutableList::copyOf));

  private static final List<String> dateTimeFormats =
      Lists.newArrayList("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS",
          "MM/dd/yyyy hh:mm:ss a", "dd-MMM-yyyy HH:mm",
          "dd-MMM-yyyy HH:mm", "yyyy-MM-dd'T'HH:mm:ss");
  public static final ImmutableList<Tuple2<String, DateTimeFormatter>> dateTimeFormatters =
      dateTimeFormats.stream()
          .map(fmt -> Tuple2.of(fmt, DateTimeFormatter.ofPattern(fmt)))
          .collect(collectingAndThen(toList(), ImmutableList::copyOf));

  private static final List<String> timeFormats =
      Lists.newArrayList("HH:mm:ss", "HH:mm:ss.SSS", "hh:mm:ss a", "h:mm:ss a",
          "hh:mm a", "h:mm a", "HHmm");
  public static final ImmutableList<Tuple2<String, DateTimeFormatter>> timeFormatters =
      timeFormats.stream()
          .map(fmt -> Tuple2.of(fmt, DateTimeFormatter.ofPattern(fmt)))
          .collect(collectingAndThen(toList(), ImmutableList::copyOf));
}
