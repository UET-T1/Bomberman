package engine.graphics;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.util.HashMap;
import java.util.Map;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public class ShaderProgram {

  private final int programId;
  private final Map<String, Integer> uniforms;
  private int vertexShaderId;
  private int fragmentShaderId;

  public ShaderProgram() throws Exception {
    programId = glCreateProgram();
    if (programId == 0) {
      throw new Exception("Could not create Shader");
    }
    uniforms = new HashMap<>();
  }

  public void createUniform(String uniformName) throws Exception {
    int uniformLocation = glGetUniformLocation(programId, uniformName);
    if (uniformLocation < 0) {
      throw new Exception("Could not find uniform:" + uniformName);
    }
    uniforms.put(uniformName, uniformLocation);
  }

  public void createPointLightUniform(String uniformName) throws Exception {
    createUniform(uniformName + ".colour");
    createUniform(uniformName + ".position");
    createUniform(uniformName + ".intensity");
    createUniform(uniformName + ".att.constant");
    createUniform(uniformName + ".att.linear");
    createUniform(uniformName + ".att.exponent");
  }

  public void createMaterialUniform(String uniformName) throws Exception {
    createUniform(uniformName + ".ambient");
    createUniform(uniformName + ".diffuse");
    createUniform(uniformName + ".specular");
    createUniform(uniformName + ".hasTexture");
    createUniform(uniformName + ".reflectance");
  }

  public void setUniform(String uniformName, Matrix4f value) {
    // Dump the matrix into a float buffer
    try (MemoryStack stack = MemoryStack.stackPush()) {
      glUniformMatrix4fv(uniforms.get(uniformName), false,
          value.get(stack.mallocFloat(16)));
    }
  }

  public void setUniform(String uniformName, int value) {
    glUniform1i(uniforms.get(uniformName), value);
  }

  public void setUniform(String uniformName, float value) {
    glUniform1f(uniforms.get(uniformName), value);
  }

  public void setUniform(String uniformName, Vector3f value) {
    glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
  }

  public void setUniform(String uniformName, Vector4f value) {
    glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
  }


  public void setUniform(String uniformName, Material material) {
    setUniform(uniformName + ".ambient", material.getAmbientColour());
    setUniform(uniformName + ".diffuse", material.getDiffuseColour());
    setUniform(uniformName + ".specular", material.getSpecularColour());
    setUniform(uniformName + ".hasTexture", material.isTextured() ? 1 : 0);
    setUniform(uniformName + ".reflectance", material.getReflectance());
  }

  public void createVertexShader(String shaderCode) throws Exception {
    vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
  }

  public void createFragmentShader(String shaderCode) throws Exception {
    fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
  }

  protected int createShader(String shaderCode, int shaderType) throws Exception {
    int shaderId = glCreateShader(shaderType);
    if (shaderId == 0) {
      throw new Exception("Error creating shader. Type: " + shaderType);
    }

    glShaderSource(shaderId, shaderCode);
    glCompileShader(shaderId);

    if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
      throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
    }

    glAttachShader(programId, shaderId);

    return shaderId;
  }

  public void link() throws Exception {
    glLinkProgram(programId);
    if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
      throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
    }

    if (vertexShaderId != 0) {
      glDetachShader(programId, vertexShaderId);
    }
    if (fragmentShaderId != 0) {
      glDetachShader(programId, fragmentShaderId);
    }

    glValidateProgram(programId);
    if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
      System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
    }
  }

  public void bind() {
    glUseProgram(programId);
  }

  public void unbind() {
    glUseProgram(0);
  }

  public void cleanup() {
    unbind();
    if (programId != 0) {
      glDeleteProgram(programId);
    }
  }
}