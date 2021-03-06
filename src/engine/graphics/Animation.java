package engine.graphics;

import engine.*;

public class Animation {
  private Texture[] frames;
  private Mesh[] meshes;
  private int texturePointer;

  private double elapsedTime;
  private double currentTime;
  private double lastTime;
  private double fps;
  private Timer timer;
  private Timer distantTime; // calculate time to next animation
  private double timeToNext; // distance between 2 animation loop 

  /**
   * Animation object to store sprites
   * @param amount number of sprites
   * @param fps frames to render in a second
   * @param filename PREFIX of sprites
   * @param timeToNext distance between 2 animation loop 
   * @throws Exception
   */
  public Animation(int amount, int fps, double timeToNext, String filename, float[] positions, float[] textCoords, int[] indices) throws Exception {
    this.texturePointer = 0;
    this.elapsedTime = 0;
    this.currentTime = 0;

    this.timeToNext = timeToNext;
    distantTime = new Timer();
    distantTime.init();

    timer = new Timer();
    timer.init();

    this.lastTime = timer.getTime();
    this.fps = 1.0 / fps;

    this.frames = new Texture[amount];
    this.meshes = new Mesh[amount];
    for (int i = 0; i < amount; i++) {
      // frame names: name_i.png
      this.frames[i] = new Texture(filename + "/" + i + ".png");
      this.meshes[i] = new Mesh(positions, textCoords, indices, this.frames[i]);
    }
  }

  //public void bind() {
    //bind(0);
  //}

  /*
  public void bind(int sampler) {
    this.currentTime = timer.getTime();
    this.elapsedTime += currentTime - lastTime;

    if (elapsedTime >= fps) {
      elapsedTime = 0;
      texturePointer++;
    }

    if (texturePointer >= frames.length) texturePointer = 0;

    this.lastTime = currentTime;

    frames[texturePointer].bind(sampler);
  }

   */

  public Mesh getCurrentMesh() {

    if (texturePointer == meshes.length - 1) {
      if (distantTime.getTime() - distantTime.getLastLoopTime() < timeToNext) {
        return meshes[texturePointer];
      } else {
        distantTime.init();
      }
    }

    this.currentTime = timer.getTime();
    this.elapsedTime += currentTime - lastTime;

    if (elapsedTime >= fps) {
      elapsedTime = 0;
      texturePointer++;
    }

    if (texturePointer >= frames.length) texturePointer = 0;

    this.lastTime = currentTime;

    return meshes[texturePointer];
  }
}