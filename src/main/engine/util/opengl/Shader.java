package main.engine.util.opengl;

import java.io.File;
import main.engine.util.io.FileUtils;
import org.lwjgl.opengl.GL33;

/**
 * Shader object runs on GPU, manipulates game objects and show it on screen.
 */
public class Shader {
  // Mesh is created with vertices. To draw vertices, we use Vertex Shader (described in vertexFile).
  // To draw the area of that geometric object (made by these vertices), we use Fragment Shader
  // or Pixel Shader (described in fragmentFile).
  private String vertexFile;
  private String fragmentFile;

  // Pointer to vertex shader
  private int vertexID;
  // Pointer to fragment shader
  private int fragmentID;
  // Pointer to current program
  private int programID;

  public Shader(String vertexGLSL, String fragmentGLSL) {
    vertexFile = FileUtils.loadAsString(vertexGLSL);
    fragmentFile = FileUtils.loadAsString(fragmentGLSL);
  }

  public void create() {
    programID = GL33.glCreateProgram();
    vertexID = GL33.glCreateShader(GL33.GL_VERTEX_SHADER);

    // Binding vertex shader
    GL33.glShaderSource(vertexID, vertexFile);
    GL33.glCompileShader(vertexID);

    if (GL33.glGetShaderi(vertexID, GL33.GL_COMPILE_STATUS) == GL33.GL_FALSE) {
      System.err.println("Vertex shader err: " + GL33.glGetShaderInfoLog(vertexID));
      return;
    }

    // Binding fragment shader
    GL33.glShaderSource(fragmentID, fragmentFile);
    GL33.glCompileShader(fragmentID);

    if (GL33.glGetShaderi(fragmentID, GL33.GL_COMPILE_STATUS) == GL33.GL_FALSE) {
      System.err.println("Fragment shader err: " + GL33.glGetShaderInfoLog(fragmentID));
      return;
    }

    // Attach to current program
    GL33.glAttachShader(programID, vertexID);
    GL33.glAttachShader(programID, fragmentID);

    // Link to gpu to use those shaders
    GL33.glLinkProgram(programID);

    if (GL33.glGetProgrami(programID, GL33.GL_LINK_STATUS) == GL33.GL_FALSE) {
      System.err.println("Linking fail: " + GL33.glGetProgramInfoLog(programID));
      return;
    }

    // Checks to see whether the executables contained in program can execute given the current OpenGL state
    GL33.glValidateProgram(programID);
    if (GL33.glGetProgrami(programID, GL33.GL_VALIDATE_STATUS) == GL33.GL_FALSE) {
      System.err.println("Validation fail: " + GL33.glGetProgramInfoLog(programID));
      return;
    }

    // After binding those shaders to program object, release them
    GL33.glDeleteShader(vertexID);
    GL33.glDeleteShader(fragmentID);
  }

  public void bind() {
    GL33.glUseProgram(programID);
  }

  public void unbind() {
    GL33.glUseProgram(0);
  }

  public void destroy() {
    GL33.glDeleteProgram(programID);
  }
}
