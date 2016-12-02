package computergraphics.framework.scenegraph.nodes.primitives;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.mesh.Triangle;
import computergraphics.framework.mesh.indexlist.TriangleMesh;
import computergraphics.framework.rendering.vbo.RenderVertex;
import computergraphics.framework.rendering.vbo.VertexBufferObject;
import computergraphics.framework.scenegraph.nodes.LeafNode;

public class JenkeTriangleMeshNode extends LeafNode {
	private ITriangleMesh mesh;
	private ITriangleMesh shadowPolygonMesh = new TriangleMesh();
	private JenkeTriangleMeshNode shadowPolygonNode = null;

	/**
	 * Color used for rendering (RGBA)
	 */
	private Vector color = new Vector(0.75, 0.25, 0.25, 1);

	/**
	 * Debugging: Show normals.
	 */
	private boolean showNormals = false;
	private VertexBufferObject vbo;
	private VertexBufferObject vboNormals;

	public JenkeTriangleMeshNode(ITriangleMesh mesh) {
		this.mesh = mesh;
		vbo = new VertexBufferObject(GL2.GL_TRIANGLES, createRenderVertices());
		vboNormals = new VertexBufferObject(GL2.GL_LINES, createRenderVerticesNormals());
	}

	/**
	 * Create vbo data for mesh rendering
	 */
	private List<RenderVertex> createRenderVertices() {
		List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
		for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
			Triangle t = mesh.getTriangle(i);
			for (int j = 0; j < 3; j++) {
				renderVertices.add(new RenderVertex(mesh.getVertex(t.getVertexIndex(j)).getPosition(), t.getNormal(), color));
			}
		}
		return renderVertices;
	}

	/**
	 * Create vbo data for normal rendering.
	 */
	private List<RenderVertex> createRenderVerticesNormals() {
		List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
		double normalScale = 0.1;
		Vector color = new Vector(0.5, 0.5, 0.5, 1);
		for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
			Triangle t = mesh.getTriangle(i);
			Vector p = mesh.getVertex(t.getVertexIndex(0)).getPosition()
					.add(mesh.getVertex(t.getVertexIndex(1)).getPosition())
					.add(mesh.getVertex(t.getVertexIndex(2)).getPosition())
					.multiply(1.0 / 3.0);
			renderVertices.add(new RenderVertex(p, t.getNormal(), color));
			renderVertices.add(new RenderVertex(p.add(t.getNormal().multiply(normalScale)), t.getNormal(), color));
		}
		return renderVertices;
	}

	@Override
	public void drawGL(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		// Use texture if texture object != null
		if (mesh.getTexture() != null) {
			if (!mesh.getTexture().isLoaded()) {
				mesh.getTexture().load(gl);
			}
			mesh.getTexture().bind(gl);
		}

		// Compute transformed light position
		Matrix invertedTransformation = modelMatrix.getInverse();
		invertedTransformation = invertedTransformation.getTransposed();
		Vector light4 = new Vector(
				getRootNode().getLightPosition().x(),
				getRootNode().getLightPosition().y(),
				getRootNode().getLightPosition().z(),
				1
		);
		Vector transformedLight = invertedTransformation.multiply(light4);
		Vector lightPosition = transformedLight.xyz().multiply(1.0f / transformedLight.w());

		if (mode == RenderMode.REGULAR) {
			drawRegular(gl);
		} else if (mode == RenderMode.DEBUG_SHADOW_VOLUME) {
			drawShadowVolume(gl, modelMatrix, lightPosition);
		} else if (mode == RenderMode.SHADOW_VOLUME) {
			drawShadowVolume(gl, modelMatrix, lightPosition);
		}
	}

	/**
	 * Draw mesh regularly.
	 */
	public void drawRegular(GL2 gl) {
		vbo.draw(gl);
		if (showNormals) {
			vboNormals.draw(gl);
		}
	}

	/**
	 * Render the shadow volumes.
	 */
	public void drawShadowVolume(GL2 gl, Matrix modelMatrix, Vector lightPosition) {
		mesh.createShadowPolygons(lightPosition, 500, shadowPolygonMesh);
//		if (shadowPolygonNode == null) {
			shadowPolygonNode = new JenkeTriangleMeshNode(shadowPolygonMesh);
			shadowPolygonNode.setParentNode(this);
			shadowPolygonNode.setColor(new Vector(0.25, 0.25, 0.75, 0.25));
			shadowPolygonNode.vbo = new VertexBufferObject(GL2.GL_TRIANGLES, shadowPolygonNode.createRenderVertices());
//		}
		shadowPolygonNode.traverse(gl, RenderMode.REGULAR, modelMatrix);
	}

	public void setColor(Vector color) {
		this.color = color;
		vbo = new VertexBufferObject(GL2.GL_TRIANGLES, createRenderVertices());
	}
	
	public boolean isShowNormals() {
		return showNormals;
	}

	public void setShowNormals(boolean showNormals) {
		this.showNormals = showNormals;
	}
}
