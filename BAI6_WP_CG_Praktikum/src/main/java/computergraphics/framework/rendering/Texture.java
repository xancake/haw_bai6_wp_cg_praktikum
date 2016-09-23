package computergraphics.framework.rendering;

import java.io.File;
import java.io.IOException;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.TextureIO;

import computergraphics.framework.AssetPath;

public class Texture {

  private String filename;

  private com.jogamp.opengl.util.texture.Texture texture = null;

  public Texture(String filename) {
    this.filename = filename;
  }

  public boolean isLoaded() {
    return texture != null;
  }

  /**
   * Load texture image from file and create GL texture object.
   */
  public void load(GL2 gl) {
    load(gl, filename);
  }

  /**
   * Load texture image from file and create GL texture object.
   */
  public void load(GL2 gl, String filename) {

    if (texture != null) {
      // TODO: delete
    }
    try {
      texture = TextureIO.newTexture(new File(AssetPath.getPathToAsset(filename)), true);
    } catch (GLException | IOException e) {
      System.out.println("Failed to load texture.");
    }
    
    gl.glEnable(GL2.GL_TEXTURE_2D);
    gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject(gl));
    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);

//    if (doMipMap) {
//      gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
//      gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
//    }

    System.out.println("Texture " + filename + " loaded.");
  }

  /**
   * Bind the texture as current texture.
   */
  public void bind(GL2 gl) {
    texture.bind(gl);
  }
}
