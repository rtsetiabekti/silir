package com.kahl.silir.entity;

import java.util.Random;

/**
 * Created by Paskahlis Anjas Prabowo on 01/08/2017.
 */
public class DataGenerator {
  private final int PEAK_INHALE_INDEX = 1700;
  private final int PEAK_INHALE_VALUE = 400;
  private final int PEAK_EXHALE_INDEX = 3500;
  private final int PEAK_EXHALE_VALUE = 430;
  Random random = new Random();

  public double[] generate() {
    double[] data = new double[6000];
    int index = 0;
    data[index] = 0;
    double delta = (double) PEAK_INHALE_VALUE / PEAK_INHALE_INDEX;
    while (index < PEAK_INHALE_INDEX) {
      index++;
      data[index] = data[index - 1] - delta;
    }
    delta = (double) PEAK_INHALE_VALUE / (3000 - PEAK_INHALE_INDEX);
    while (index < 3000) {
      index++;
      data[index] = data[index - 1] + delta;
    }
    delta = (double) PEAK_EXHALE_VALUE / (PEAK_EXHALE_INDEX - 3000);
    while (index < PEAK_EXHALE_INDEX) {
      index++;
      data[index] = data[index - 1] + delta;
    }
    delta = (double) PEAK_EXHALE_VALUE / (6000 - PEAK_EXHALE_INDEX);
    while (index < 5999) {
      index++;
      data[index] = data[index - 1] - delta;
    }
    return data;
  }
}
