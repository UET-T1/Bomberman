package main.engine;

public class Timer {
  private double lastLoop;

  public void init() {
    lastLoop = getTime();
  }

  public double getTime() {
    return System.nanoTime() / 1_000_000_000.0;
  }

  public float getTimeElapsed() {
    double time = getTime();
    float elapsed = (float) (time - lastLoop);
    lastLoop = time;
    return elapsed;
  }


}
