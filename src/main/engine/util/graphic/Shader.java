package main.engine.util.graphic;

import main.engine.util.math.*;
import main.engine.util.math.*;
import main.engine.util.io.FileUtils;
import java.io.IOException;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL33.*;

public class Shader {
  private String vertexFile, fragmentFile;
  private int programID, vertexID, fragmentID;

  public Shader(String vertexPath, String fragmentPath) throws Exception {
    vertexFile = FileUtils.loadResource(vertexPath);
    fragmentFile = FileUtils.loadResource(fragmentPath);
  }

  public void create() {
    programID = glCreateProgram();
    vertexID = glCreateShader(GL_VERTEX_SHADER);
    fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

    // Set the source code to shaders
    glShaderSource(vertexID, vertexFile);
    glShaderSource(fragmentID, fragmentFile);

    // Compile shaders
    glCompileShader(vertexID);
    glCompileShader(fragmentID);

    // Check compilation
    if (glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE || glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL_FALSE) {
      System.err.println("Shader compilation err: \n" + glGetShaderInfoLog(vertexID) + "\n" + glGetShaderInfoLog(fragmentID));
      return;
    }

    // Attach to program
    glAttachShader(programID, vertexID);
    glAttachShader(programID, fragmentID);

    // Link to gpu
    glLinkProgram(programID);

    // check linking
    if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
      System.err.println("Program linking err: " + glGetProgramInfoLog(programID));
      return;
    }

    // validate
    glValidateProgram(programID);
    if (glGetProgrami(programID, GL_VALIDATE_STATUS) == GL_FALSE) {
      System.err.println("Program validation err: " + glGetProgramInfoLog(programID));
      return;
    }

  }

  public void bind() {
    glUseProgram(programID);
  }

  public void unbind() {
    glUseProgram(0);
  }

  public void destroy() {
    glDetachShader(programID, vertexID);
    glDetachShader(programID, fragmentID);
    glDeleteShader(vertexID);
    glDeleteShader(fragmentID);
    glDeleteProgram(programID);
  }

  public int getUniformLocation(String name) {
    return GL20.glGetUniformLocation(programID, name);
  }

  // 6 prototypes
  public void setUniform(String name, float value) {
    GL20.glUniform1f(getUniformLocation(name), value);
  }
  public void setUniform(String name, int value) {
    GL20.glUniform1i(getUniformLocation(name), value);
  }
  public void setUniform(String name, Vector2f value) {
    GL20.glUniform2f(getUniformLocation(name), value.x, value.y);
  }
  public void setUniform(String name, Vector3f value) {
    GL20.glUniform3f(getUniformLocation(name), value.x, value.y, value.z);
  }
  public void setUniform(String name, Matrix4f value) {
    FloatBuffer buffer = MemoryUtil.memAllocFloat(4 * 4); // 4x4 matrix
    value.toBuffer(buffer);
    GL20.glUniformMatrix4fv(getUniformLocation(name), true, buffer);
  }
  public void setUniform(String name, boolean value) {
    GL20.glUniform1i(getUniformLocation(name), value ? 1:0);
  }
}
