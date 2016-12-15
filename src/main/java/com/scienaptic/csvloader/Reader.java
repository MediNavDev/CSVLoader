package com.scienaptic.csvloader;

import com.google.common.base.Stopwatch;
import org.simpleflatmapper.csv.CsvParser;
import org.simpleflatmapper.util.CloseableIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public final class Reader {
  private static final Logger logger = LoggerFactory.getLogger(Reader.class);
  private Reader() { }

  public static List<Column> sample(Path path,
                                    CsvParserConfig config,
                                    float threshold) throws IOException {
    int rowCount = 0;
    int rowsRead = 0;

    Stopwatch stopwatch = Stopwatch.createStarted();
    List<Column.Builder> builders = headers(path, config);

    Random r = new Random(31012010L);

    try (CloseableIterator<String[]> iterator = CsvParser.separator(config.fieldSeparator)
        .quote(config.fieldDelimiter)
        .skip(config.hasHeader ? 1 : 0)
        .iterator(path.toFile())) {

      while (iterator.hasNext()) {
        String[] row = iterator.next();

        if (r.nextFloat() < threshold) {
          rowsRead++;
          for (int i = 0; i < row.length; i++) {
            String col = row[i].trim();
            builders.get(i).add(col);
          }
        }

        rowCount++;
      }
    }

    logger.info("File: {}, rows read: {} / {}", path, rowsRead, rowCount);

    List<Column> list = builders.stream()
        .map(Column.Builder::build)
        .collect(Collectors.toList());
    stopwatch.stop();
    logger.info("Time taken to read sample: {}", stopwatch.toString());

    return list;
  }


  private static List<Column.Builder> generateColumns(int numColumns) {
    List<Column.Builder> result = new ArrayList<>(numColumns);
    for (int i = 0; i < numColumns; i++) result.add(Column.newBuilder("COLUMN_" + i));
    return result;
  }

  private static List<Column.Builder> createColumns(String[] header) {
    List<Column.Builder> result = new ArrayList<>(header.length);
    for (String col : header) result.add(Column.newBuilder(col.toUpperCase()));
    return result;
  }

  private static List<Column.Builder> headers(Path path,
                                              CsvParserConfig config)
      throws IOException {
    try (CloseableIterator<String[]> iterator = CsvParser.separator(config.fieldSeparator)
        .quote(config.fieldDelimiter)
        .iterator(path.toFile())) {
      if (iterator.hasNext()) {
        String[] header = iterator.next();
        return config.hasHeader ? createColumns(header) : generateColumns(header.length);
      }
    }

    return Collections.emptyList();
  }
}
