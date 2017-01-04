package com.scienaptic.csvloader;

import com.google.common.collect.Lists;
import com.scienaptic.csvloader.utils.Tuple2;
import com.scienaptic.csvloader.utils.TypeUtil;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class TestDateParsing {
  private final String pattern = "dd-MM-yyyy";
  private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern(pattern);
  private final List<String> dates = Lists.newArrayList("04-12-2015", "08-12-2015", "10-12-2015", "11-12-2015", "12-12-2015", "01-01-2016", "07-01-2016", "11-01-2016", "16-01-2016", "26-01-2016", "19-02-2016", "20-02-2016", "27-02-2016", "28-02-2016", "02-03-2016", "11-03-2016", "13-03-2016", "22-03-2016", "23-03-2016", "29-03-2016", "30-03-2016", "11-04-2016", "14-04-2016", "07-05-2016", "15-06-2016", "30-06-2016", "12-07-2016", "15-07-2016", "20-07-2016", "21-07-2016", "28-07-2016", "29-07-2016", "02-08-2016", "09-08-2016", "25-08-2016", "30-08-2016", "09-09-2016", "15-09-2016", "23-09-2016", "30-09-2016", "30-10-2016", "08-11-2016", "13-11-2016", "18-11-2016", "23-11-2016", "10-12-2016", "09-01-2017", "10-01-2017", "12-01-2017", "06-02-2017", "07-02-2017", "14-02-2017", "17-02-2017", "20-02-2017", "10-03-2017", "10-04-2017", "11-04-2017", "17-04-2017", "20-04-2017", "23-04-2017", "28-04-2017", "04-05-2017", "08-05-2017", "09-05-2017", "25-05-2017", "09-06-2017", "11-06-2017", "29-06-2017", "03-07-2017", "08-07-2017", "10-07-2017", "15-07-2017", "19-07-2017", "22-07-2017", "26-07-2017", "01-08-2017", "02-08-2017", "09-08-2017", "10-08-2017", "16-08-2017", "18-08-2017", "28-08-2017", "15-09-2017", "06-10-2017", "25-10-2017", "07-11-2017", "17-11-2017", "25-11-2017", "05-12-2017", "13-12-2017", "22-12-2017", "25-12-2017", "28-12-2017", "06-01-2018", "24-01-2018", "27-01-2018", "12-02-2018", "13-02-2018", "15-02-2018", "08-03-2018", "19-03-2018", "10-04-2018", "14-04-2018", "21-04-2018", "11-05-2018", "13-05-2018", "15-05-2018", "17-05-2018", "18-05-2018", "19-05-2018", "30-05-2018", "06-06-2018", "10-06-2018", "02-07-2018", "10-07-2018", "23-07-2018", "11-08-2018", "12-08-2018", "13-08-2018", "15-08-2018");

  @Test
  public void test() {
    dates.stream()
        .map(date -> Tuple2.of(date, tryParse(date)))
        .filter(pair -> !pair.right)
        .forEach(System.out::println);

    Optional<FieldTypes.LOCAL_DATE> dateFormat = TypeInferer.dateParser(dates,
        TypeUtil.dateFormatters,
        FieldTypes.LOCAL_DATE::new, LocalDate::parse);
    assertNotNull(dateFormat);
    assertTrue(dateFormat.isPresent());
    FieldTypes.LOCAL_DATE localDate = dateFormat.get();
    assertNotNull(localDate);
    assertEquals("Expected pattern", pattern, localDate.dateFormat());
  }

  private boolean tryParse(String probablyDate) {
    try {
      LocalDate.parse(probablyDate, fmt);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }

}
