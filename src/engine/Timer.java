package engine;

public class Timer {

  /*
  public Timer() {
    init();
  }
   */

  private double lastLoopTime;

  public void init() {
    lastLoopTime = getTime();
  }

  public double getTime() {
    return System.nanoTime() / 1_000_000_000.0;
  }

  public int getDisplayTime() {
    return (int) (getTime() - lastLoopTime);
  }

  public float getElapsedTime() {
    double time = getTime();
    float elapsedTime = (float) (time - lastLoopTime);
    lastLoopTime = time;
    return elapsedTime;
  }

  public double getLastLoopTime() {
    return lastLoopTime;
  }
}