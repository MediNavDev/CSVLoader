package com.scienaptic.csvloader;

import com.scienaptic.csvloader.utils.Tuple2;
import com.scienaptic.csvloader.utils.TypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.scienaptic.csvloader.utils.TypeUtil.*;


public class TypeInferer {
  private static final Logger logger = LoggerFactory.getLogger(TypeInferer.class);

  private TypeInferer() { }

  public static List<Field> inferFieldTypes(List<Column> columnData) {
    List<Field> fields = new ArrayList<>(columnData.size());
    for (int i = 0; i < columnData.size(); i++) {
      Column column = columnData.get(i);
      FieldType fieldType = infer(column.values);
      Field field = new Field(column.name, fieldType);
      fields.add(field);
    }
    return fields;
  }

  public static FieldType infer(List<String> values) {
    List<String> notEmptyValues = values.stream()
        .filter(s -> TypeUtil.isNotEmpty(s) && !TypeUtil.MISSING_INDICATORS.contains(s))
        .collect(Collectors.toList());
    FieldType category = FieldTypes.CATEGORY$.MODULE$;

    if (notEmptyValues.isEmpty()) return category;

    // ewwww!!!
    if (notEmptyValues.stream().allMatch(isBoolean)) return FieldTypes.BOOLEAN$.MODULE$;
    if (notEmptyValues.stream().allMatch(isShort)) return FieldTypes.SHORT_INT$.MODULE$;
    if (notEmptyValues.stream().allMatch(isInteger)) return FieldTypes.INTEGER$.MODULE$;
    if (notEmptyValues.stream().allMatch(isLong)) return FieldTypes.LONG_INT$.MODULE$;
    if (notEmptyValues.stream().allMatch(isFloat)) return FieldTypes.FLOAT$.MODULE$;

    Optional<FieldTypes.LOCAL_DATE_TIME> dtf = dateParser(
        notEmptyValues,
        dateTimeFormatters,
        FieldTypes.LOCAL_DATE_TIME::new);
    if (dtf.isPresent()) return dtf.get();

    Optional<FieldTypes.LOCAL_TIME> tf = dateParser(
        notEmptyValues,
        timeFormatters,
        FieldTypes.LOCAL_TIME::new);
    if (tf.isPresent()) return tf.get();

    Optional<FieldTypes.LOCAL_DATE> df = dateParser(
        notEmptyValues,
        dateFormatters,
        FieldTypes.LOCAL_DATE::new);
    if (df.isPresent()) return df.get();


    return category;
  }

  static <T extends FieldType> Optional<T> dateParser(List<String> values,
                                                      List<Tuple2<String, DateTimeFormatter>> formatters,
                                                      Function<String, T> constructor) {
    String aValue = values.get(0);
    List<Tuple2<String, DateTimeFormatter>> tuple2s =
        formatters
            .stream()
            .filter(pair -> parseable(aValue, pair.right))
            .collect(Collectors.toList());

    if (tuple2s.isEmpty()) {return Optional.empty();}

    logger.info("Date formats: {}", tuple2s.stream().map(x -> x.left).collect(Collectors.joining(",", "[", "]")));

    return tuple2s.stream()
        .filter(tuple2 -> values.stream().allMatch(value -> parseable(value, tuple2.right)))
        .findFirst()
        .map(xs -> constructor.apply(xs.left));
  }

  private static boolean parseable(String probablyDate, DateTimeFormatter fmt) {
    try {
      LocalDate.parse(probablyDate, fmt);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  private static final Predicate<String> isBoolean = s ->
      TypeUtil.TRUE_STRINGS_FOR_DETECTION.contains(s) || TypeUtil.FALSE_STRINGS_FOR_DETECTION.contains(s);

  private static final Predicate<String> isLong = s -> {
    try {
      Long.parseLong(s);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  };

  private static Predicate<String> isInteger = s -> {
    try {
      Integer.parseInt(s);
      return true;
    } catch (NumberFormatException e) {
      // it's all part of the plan
      return false;
    }
  };

  private static Predicate<String> isFloat = s -> {
    try {
      Float.parseFloat(s);
      return true;
    } catch (NumberFormatException e) {
      // it's all part of the plan
      return false;
    }
  };

  private static Predicate<String> isShort = s -> {
    try {
      Short.parseShort(s);
      return true;
    } catch (NumberFormatException e) {
      // it's all part of the plan
      return false;
    }
  };

}
