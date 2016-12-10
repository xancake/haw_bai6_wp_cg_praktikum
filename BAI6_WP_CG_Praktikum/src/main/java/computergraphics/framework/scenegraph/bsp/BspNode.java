package computergraphics.framework.scenegraph.bsp;

import java.util.ArrayList;
import java.util.List;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.vbo.RenderVertex;
import computergraphics.framework.rendering.vbo.VertexBufferObject;
import computergraphics.framework.scenegraph.bsp.BspTreeNode.Orientation;
import computergraphics.framework.scenegraph.nodes.LeafNode;

/**
 * Scene graph node to display a BSP tree.
 * 
 * @author Philipp Jenke
 */
public class BspNode extends LeafNode {
	/**
	 * List of points used to create the tree.
	 */
	private List<Vector> points;
	
	/**
	 * Observer position
	 */
	private Vector eye;
	
	/**
	 * Flags controlling the visualized content.
	 */
	public boolean showPoints = true;
	public boolean showPlanes = true;
	public boolean showBackToFront = true;
	public boolean showElements = true;
	
	private VertexBufferObject vboPoints;
	private VertexBufferObject vboPlanes;
	private VertexBufferObject vboBack2FrontPath;
	private VertexBufferObject vboElements;
	
	public BspNode(BspTreeNode rootNode, List<Vector> points, List<Integer> back2FrontSorted, Vector eye) {
		this.points = points;
		this.eye = eye;
		vboPoints = new VertexBufferObject(GL.GL_POINTS, createVBOPoints());
		vboBack2FrontPath = new VertexBufferObject(GL.GL_LINE_STRIP, createVBOBack2Front(back2FrontSorted));
		vboPlanes = new VertexBufferObject(GL.GL_LINES, createVBOPlanes(rootNode, 0.7f));
		vboElements = new VertexBufferObject(GL.GL_LINES, createVBOElements(rootNode));
	}
	
	@Override
	public void drawGL(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		if(mode == RenderMode.REGULAR) {
			if(showPlanes) {
				vboPlanes.draw(gl);
			}
			
			if(showPoints) {
				vboPoints.draw(gl);
			}
			
			if(showBackToFront) {
				vboBack2FrontPath.draw(gl);
			}
			
			if(showElements) {
				vboElements.draw(gl);
			}
		}
	}
	
	@Override
	public void timerTick(int counter) {
		
	}
	
	/**
	 * Create the VBO render vertices for the data points.
	 */
	private List<RenderVertex> createVBOPoints() {
		List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
		for(Vector p : points) {
			renderVertices.add(new RenderVertex(p, new Vector(0, 0, 1), new Vector(0, 1, 0, 1)));
		}
		renderVertices.add(new RenderVertex(eye, new Vector(0, 0, 1), new Vector(1, 1, 0, 1)));
		return renderVertices;
	}
	
	/**
	 * Create the VBO render vertices for the back-to-front sorting lines.
	 */
	private List<RenderVertex> createVBOBack2Front(List<Integer> sortedPoints) {
		List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
		for(int index : sortedPoints) {
			renderVertices.add(new RenderVertex(points.get(index), new Vector(0, 0, 1), new Vector(1, 1, 0, 1)));
		}
		renderVertices.add(new RenderVertex(eye, new Vector(0, 0, 1), new Vector(1, 1, 0, 1)));
		return renderVertices;
	}
	
	/**
	 * Create the VBO render vertices for the BSP plane lines.
	 */
	private List<RenderVertex> createVBOPlanes(BspTreeNode node, float scale) {
		List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
		if(node == null) {
			return renderVertices;
		}
		Vector tangent = new Vector(node.getPlaneNormal().get(1), -node.getPlaneNormal().get(0), 0).multiply(scale);
		renderVertices.add(new RenderVertex(node.getPlaneBase().add(tangent), new Vector(0, 0, 1), new Vector(1, 1, 1, 1)));
		renderVertices.add(new RenderVertex(node.getPlaneBase().subtract(tangent), new Vector(0, 0, 1), new Vector(1, 1, 1, 1)));
		renderVertices.add(new RenderVertex(node.getPlaneBase(), new Vector(0, 0, 1), new Vector(1, 1, 1, 1)));
		renderVertices.add(new RenderVertex(node.getPlaneBase().add(node.getPlaneNormal().multiply(scale * 0.3f)), new Vector(0, 0, 1),
				new Vector(1, 1, 1, 1)));
		
		renderVertices.addAll(createVBOPlanes(node.getChild(BspTreeNode.Orientation.POSITIVE), scale * 0.5f));
		renderVertices.addAll(createVBOPlanes(node.getChild(BspTreeNode.Orientation.NEGATIVE), scale * 0.5f));
		
		return renderVertices;
	}
	
	/**
	 * Create the VBO for the data points in the different BSP regions.
	 */
	private List<RenderVertex> createVBOElements(BspTreeNode node) {
		List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
		if(node == null) {
			return renderVertices;
		}
		for(int or = 0; or < 2; or++) {
			Orientation orientation = Orientation.values()[or];
			Vector color = (orientation == Orientation.NEGATIVE) ? new Vector(0, 1, 1, 1) : new Vector(1, 0, 1, 1);
			for(int i = 0; i < node.getNumberOfElements(orientation); i++) {
				int index = node.getElement(orientation, i);
				renderVertices.add(new RenderVertex(node.getPlaneBase(), new Vector(0, 0, 1), color));
				renderVertices.add(new RenderVertex(points.get(index), new Vector(0, 0, 1), color));
			}
		}
		renderVertices.addAll(createVBOElements(node.getChild(BspTreeNode.Orientation.POSITIVE)));
		renderVertices.addAll(createVBOElements(node.getChild(BspTreeNode.Orientation.NEGATIVE)));
		return renderVertices;
	}
}
