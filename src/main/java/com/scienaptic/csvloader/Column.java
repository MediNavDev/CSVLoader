package com.scienaptic.csvloader;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 * case class Column(name: String, values: List[String])
 */
public final class Column {
  public final String name;
  public final ImmutableList<String> values;

  public Column(String name, ImmutableList<String> values) {
    this.name = name;
    this.values = values;
  }

  public static Builder newBuilder(String name) { return new Builder(name); }

  public static final class Builder {
    private final String name;
    private final List<String> values = new ArrayList<>();

    public Builder(String name) {
      this.name = name;
    }

    public Builder add(String value) {
      values.add(value);
      return this;
    }

    public Column build() {
      return new Column(name, ImmutableList.copyOf(values));
    }
  }
}
