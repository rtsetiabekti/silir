package com.kahl.silir.entity;

/**
 * Created by Paskahlis Anjas Prabowo on 01/08/2017.
 */
public class RiemmanIntegrator {

  public double integrate(double arg1, double arg2) {
    double delta = arg2 - arg1;
    double result = 0;
    double startPoint = arg1;
    double endPoint = startPoint + delta;
    while (startPoint < arg2) {
      result += (startPoint + endPoint) * delta / 2f;
      startPoint = endPoint;
      endPoint += delta;
    }
    return result;
  }
}
