package com.scienaptic.csvloader.utils;

public final class Tuple2<A, B> {
  public final A left;
  public final B right;

  private Tuple2(A left, B right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public String toString() {
    return String.format("Tuple2[left=%s, right=%s]", left, right);
  }

  public static <A, B> Tuple2<A, B> of(A a, B b) {
    return new Tuple2<A, B>(a, b);
  }
}
