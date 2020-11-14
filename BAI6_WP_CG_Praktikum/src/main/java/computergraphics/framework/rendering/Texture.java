package computergraphics.framework.rendering;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.TextureIO;

import computergraphics.framework.AssetPath;

public class Texture {
	private File file;
	private com.jogamp.opengl.util.texture.Texture texture;
	
	public Texture(String filename) {
		this(new File(AssetPath.getPathToAsset(filename)));
	}
	
	public Texture(File file) {
		this.file = Objects.requireNonNull(file);
	}
	
	public boolean isLoaded() {
		return texture != null;
	}
	
	/**
	 * Load texture image from file and create GL texture object.
	 */
	public void load(GL2 gl) {
		texture = load(gl, file);
	}
	
	/**
	 * Load texture image from file and create GL texture object.
	 */
	public static com.jogamp.opengl.util.texture.Texture load(GL2 gl, File file) {
		try {
			com.jogamp.opengl.util.texture.Texture texture = TextureIO.newTexture(file, true);
			
			gl.glEnable(GL2.GL_TEXTURE_2D);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject(gl));
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
			
			//      if (doMipMap) {
			//        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
			//        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
			//      }
			
			System.out.println("Texture successfully loaded from " + file.getAbsolutePath());
			return texture;
		} catch (GLException | IOException e) {
			System.out.println("Failed to load texture from " + file.getAbsolutePath());
			return null;
		}
	}
	
	/**
	 * Bind the texture as current texture.
	 */
	public void bind(GL2 gl) {
		texture.bind(gl);
	}
}
