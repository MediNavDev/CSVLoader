package com.scienaptic.csvloader;

import com.typesafe.config.Config;

public final class CsvParserConfig {
  public static final CsvParserConfig defaultConfig =
      new CsvParserConfig(true, ',', '\"', "\r");
  public static final CsvParserConfig.Builder defaultBuilder =
      newBuilder(defaultConfig);

  public final boolean hasHeader;
  public final char fieldSeparator;
  public final char fieldDelimiter;
  public final String lineSeparator;

  private CsvParserConfig(boolean hasHeader,
                          char fieldSeparator,
                          char fieldDelimiter,
                          String lineSeparator) {
    this.hasHeader = hasHeader;
    this.fieldSeparator = fieldSeparator;
    this.fieldDelimiter = fieldDelimiter;
    this.lineSeparator = lineSeparator;
  }

  public static CsvParserConfig fromHocon(Config config) {
    // default - if no header then true
    boolean header = !config.hasPath("header") || config.getBoolean("header");
    char fs = config.hasPath("fieldSeparator") ? config.getString("fieldSeparator").charAt(0) : ',';
    char fd = config.hasPath("fieldDelimiter") ? config.getString("fieldDelimiter").charAt(0) : '\"';
    String ls = config.hasPath("lineSeparator") ? config.getString("lineSeparator") : "\r";
    return new CsvParserConfig(header, fs, fd, ls);
  }

  public static Builder newBuilder() { return new Builder(); }
  public static Builder newBuilder(CsvParserConfig config) { return new Builder(config); }

  public static final class Builder {
    private boolean hasHeader = true;
    private char fieldSeparator = ',';
    private char fieldDelimiter = '\"';
    private String lineSeparator = "\r";

    public Builder(CsvParserConfig config) {
      this.hasHeader = config.hasHeader;
      this.fieldSeparator = config.fieldSeparator;
      this.fieldDelimiter = config.fieldDelimiter;
      this.lineSeparator = config.lineSeparator;
    }

    public Builder() { }

    public Builder hasHeader(boolean hasHeader) {
      this.hasHeader = hasHeader;
      return this;
    }

    public Builder fieldSeparator(char fieldSeparator) {
      this.fieldSeparator = fieldSeparator;
      return this;
    }

    public Builder fieldDelimiter(char fieldDelimiter) {
      this.fieldDelimiter = fieldDelimiter;
      return this;
    }

    public Builder lineSeparator(String lineSeparator) {
      this.lineSeparator = lineSeparator;
      return this;
    }

    public CsvParserConfig build() {
      return new CsvParserConfig(hasHeader, fieldSeparator, fieldDelimiter, lineSeparator);
    }
  }
}
