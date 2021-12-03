package main.engine.util.graphic;

import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL33.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL33.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL33.GL_FLOAT;
import static org.lwjgl.opengl.GL33.GL_NEAREST;
import static org.lwjgl.opengl.GL33.GL_RGBA;
import static org.lwjgl.opengl.GL33.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL33.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL33.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL33.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL33.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL33.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL33.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL33.glBindTexture;
import static org.lwjgl.opengl.GL33.glGenTextures;
import static org.lwjgl.opengl.GL33.glGenerateMipmap;
import static org.lwjgl.opengl.GL33.glTexImage2D;
import static org.lwjgl.opengl.GL33.glTexParameteri;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.system.MemoryStack;

/** Represent a texture. Could use slick-utils-3 Texture instead of this class. */
public class Texture {

  private final int id;
  private int width;
  private int height;
  private int pixelFormat;

  /**
   * Create empty texture.
   *
   * @param width width of tx
   * @param height height of tx
   * @param pixelFormat format of pixel data/color (rgba, rgb, etc)
   * @throws Exception
   */
  public Texture(int width, int height, int pixelFormat) throws Exception {
    this.id = glGenTextures();
    this.width = width;
    this.height = height;
    this.pixelFormat = pixelFormat;
  }

  public Texture() {
    this.id = glGenTextures();
  }

  public static Texture createTexture(int width, int height, ByteBuffer data) {
    Texture texture = new Texture();
    texture.setWidth(width);
    texture.setHeight(height);
    texture.bind();
    glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
    // Upload texture data
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
    // Generate mip map
    glGenerateMipmap(GL_TEXTURE_2D);
    return texture;
  }

  public static Texture loadTexture(String path) {
    ByteBuffer image;
    int width, height;
    try (MemoryStack stack = MemoryStack.stackPush()) {
      /* Prepare image buffers */
      IntBuffer w = stack.mallocInt(1);
      IntBuffer h = stack.mallocInt(1);
      IntBuffer comp = stack.mallocInt(1);

      /* Load image */
      stbi_set_flip_vertically_on_load(true);
      image = stbi_load(path, w, h, comp, 4);
      if (image == null) {
        throw new RuntimeException(
            "Failed to load a texture file!" + System.lineSeparator() + stbi_failure_reason());
      }

      /* Get width and height of image */
      width = w.get();
      height = h.get();
    }
    return createTexture(width, height, image);
  }

  private void bind() {
    glBindTexture(GL_TEXTURE_2D, this.id);
  }

  public int getId() {
    return id;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getPixelFormat() {
    return pixelFormat;
  }

  public void setPixelFormat(int pixelFormat) {
    this.pixelFormat = pixelFormat;
  }

  private void initSettings() {
    glBindTexture(GL_TEXTURE_2D, this.id);
    glTexImage2D(
        GL_TEXTURE_2D,
        0,
        GL_DEPTH_COMPONENT,
        this.width,
        this.height,
        0,
        pixelFormat,
        GL_FLOAT,
        (ByteBuffer) null);
    glTexParameteri(
        GL_TEXTURE_2D,
        GL_TEXTURE_MIN_FILTER,
        GL_NEAREST); // may change to linear for better performance ?
    glTexParameteri(
        GL_TEXTURE_2D,
        GL_TEXTURE_MAG_FILTER,
        GL_NEAREST); // may change to linear for better performance ?
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
  }
}
