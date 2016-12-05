package com.scienaptic.csvloader;

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

    public Builder setHasHeader(boolean hasHeader) {
      this.hasHeader = hasHeader;
      return this;
    }

    public Builder setFieldSeparator(char fieldSeparator) {
      this.fieldSeparator = fieldSeparator;
      return this;
    }

    public Builder setFieldDelimiter(char fieldDelimiter) {
      this.fieldDelimiter = fieldDelimiter;
      return this;
    }

    public Builder setLineSeparator(String lineSeparator) {
      this.lineSeparator = lineSeparator;
      return this;
    }

    public CsvParserConfig build() {
      return new CsvParserConfig(hasHeader, fieldSeparator, fieldDelimiter, lineSeparator);
    }
  }
}
