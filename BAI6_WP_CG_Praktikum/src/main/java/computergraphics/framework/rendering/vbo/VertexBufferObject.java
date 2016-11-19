package computergraphics.framework.rendering.vbo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import com.jogamp.opengl.GL2;
import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.Shader;
import computergraphics.framework.rendering.ShaderAttributes;

/**
 * Rendering vie vertex buffer objects (VBO).
 */
public class VertexBufferObject {
	/**
	 * List containing the fragment vertices to be rendered
	 */
	private List<RenderVertex> renderVertices = null;

	/**
	 * Use this primitive type for rendering. Attentions: This implies the
	 * number of vertices, normals and colors required; e.g. triangles require
	 * three vertices each.
	 */
	private int primitiveType = GL2.GL_TRIANGLES;

	private static final int FLOAT_SIZE_IN_BYTES = 4;
	private static final int INT_SIZE_IN_BYTES = 4;

	private FloatBuffer positionBuffer = null;
	private FloatBuffer normalBuffer = null;
	private FloatBuffer colorBuffer = null;
	private IntBuffer indexBuffer = null;

	public VertexBufferObject(RenderVertex... renderVertices) {
		this(GL2.GL_TRIANGLES, renderVertices);
	}
	
	public VertexBufferObject(List<RenderVertex> renderVertices) {
		this(GL2.GL_TRIANGLES, renderVertices);
	}
	
	public VertexBufferObject(int primitiveType, RenderVertex... renderVertices) {
		this(primitiveType, Arrays.asList(renderVertices));
	}

	/**
	 * Set the data for the Buffer. The format is described together with the
	 * vertices, normals and colors attributes.
	 */
	public VertexBufferObject(int primitiveType, List<RenderVertex> renderVertices) {
		this.renderVertices = Objects.requireNonNull(renderVertices);
		this.primitiveType = primitiveType;
	}

	/**
	 * Init VBO, called only once (or if the date changed).
	 */
	private void init(GL2 gl) {
		if (renderVertices == null || renderVertices.size() == 0) {
			return;
		}

		positionBuffer = createPositionBuffer(gl);
		normalBuffer = createNormalBuffer(gl);
		colorBuffer = createColorBuffer(gl);
		indexBuffer = createIndexBuffer();
		Shader.checkGlError(gl);
		System.out.println("Created VBO buffers (vertex, normal, color, index).");
	}

	/**
	 * Create position buffer from data.
	 */
	private FloatBuffer createPositionBuffer(GL2 gl) {
		int dataLength = renderVertices.size() * 3 * FLOAT_SIZE_IN_BYTES;
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(dataLength);
		float[] data = new float[renderVertices.size() * 3];
		for (int i = 0; i < renderVertices.size(); i++) {
			data[i * 3] = (float) renderVertices.get(i).position.x();
			data[i * 3 + 1] = (float) renderVertices.get(i).position.y();
			data[i * 3 + 2] = (float) renderVertices.get(i).position.z();
		}
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		floatBuffer.put(data);
		floatBuffer.position(0);
		return floatBuffer;
	}

	/**
	 * Create normal buffer from data.
	 */
	private FloatBuffer createNormalBuffer(GL2 gl) {
		int dataLength = renderVertices.size() * 3 * FLOAT_SIZE_IN_BYTES;
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(dataLength);
		float[] data = new float[renderVertices.size() * 3];
		for (int i = 0; i < renderVertices.size(); i++) {
			data[i * 3] = (float) renderVertices.get(i).normal.x();
			data[i * 3 + 1] = (float) renderVertices.get(i).normal.y();
			data[i * 3 + 2] = (float) renderVertices.get(i).normal.z();
		}
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		floatBuffer.put(data);
		floatBuffer.position(0);
		return floatBuffer;
	}

	/**
	 * Create color buffer from data.
	 */
	private FloatBuffer createColorBuffer(GL2 gl) {
		int dataLength = renderVertices.size() * 4 * FLOAT_SIZE_IN_BYTES;
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(dataLength);
		float[] data = new float[renderVertices.size() * 4];
		for (int i = 0; i < renderVertices.size(); i++) {
			data[i * 4] = (float) renderVertices.get(i).color.x();
			data[i * 4 + 1] = (float) renderVertices.get(i).color.y();
			data[i * 4 + 2] = (float) renderVertices.get(i).color.z();
			data[i * 4 + 3] = (float) renderVertices.get(i).color.w();
		}
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		floatBuffer.put(data);
		floatBuffer.position(0);
		return floatBuffer;
	}

	private IntBuffer createIndexBuffer() {
		ByteBuffer ibb = ByteBuffer.allocateDirect(renderVertices.size() * INT_SIZE_IN_BYTES);
		ibb.order(ByteOrder.nativeOrder());
		IntBuffer indicesBuf = ibb.asIntBuffer();
		for (int i = 0; i < renderVertices.size(); i++) {
			indicesBuf.put(i);
		}
		indicesBuf.position(0);
		return indicesBuf;
	}

	/**
	 * Draw using the VBO
	 */
	public void draw(GL2 gl) {
		if (positionBuffer == null || normalBuffer == null || colorBuffer == null) {
			init(gl);
		}

		gl.glEnableVertexAttribArray(ShaderAttributes.getInstance().getVertexLocation());
		gl.glEnableVertexAttribArray(ShaderAttributes.getInstance().getNormalLocation());
		gl.glEnableVertexAttribArray(ShaderAttributes.getInstance().getColorLocation());

		try {
			gl.glVertexAttribPointer(ShaderAttributes.getInstance().getVertexLocation(), 3, GL2.GL_FLOAT, false, 0,positionBuffer);
			gl.glVertexAttribPointer(ShaderAttributes.getInstance().getNormalLocation(), 3, GL2.GL_FLOAT, false, 0,normalBuffer);
			gl.glVertexAttribPointer(ShaderAttributes.getInstance().getColorLocation(), 4, GL2.GL_FLOAT, false, 0, colorBuffer);

			gl.glDrawElements(primitiveType, renderVertices.size(), GL2.GL_UNSIGNED_INT, indexBuffer);
		} catch (Exception e) {
			System.out.println("Fehler: " + e);
		}

		Shader.checkGlError(gl);
	}

	/**
	 * Delete all buffers.
	 */
	public void invalidate() {
		positionBuffer = null;
		normalBuffer = null;
		colorBuffer = null;

		System.out.println("TODO: Free allocated mem.");
	}
	
	public void setColor(Vector color) {
		for(RenderVertex vertex : renderVertices) {
			vertex.color = color;
		}
		invalidate();
	}
}
