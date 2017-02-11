/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */

package computergraphics.exercises.exercise7;

import java.util.List;
import com.jogamp.opengl.GL2;
import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.Shader;
import computergraphics.framework.scenegraph.Scene;
import computergraphics.framework.scenegraph.bsp.BspTreeNode;
import computergraphics.framework.scenegraph.bsp.BspTreeTools;
import computergraphics.framework.scenegraph.nodes.INode.RenderMode;
import computergraphics.framework.scenegraph.nodes.primitives.BspNode;

/**
 * Scene and application for the BSP tree example.
 * 
 * @author Philipp Jenke
 */
public class BspScene extends Scene {
	private static final long serialVersionUID = 6506789797991105075L;
	
	/**
	 * Scene graph BSP node
	 */
	private BspNode node;
	
	public BspScene(List<Vector> points) {
		super(100, Shader.ShaderMode.PHONG, RenderMode.REGULAR);
		getRoot().setLightPosition(new Vector(1, 1, 1));
		getRoot().setAnimated(true);
		getRoot().setBackgroundColor(new Vector(0.25, 0.25, 0.25, 1));
		
		// Create tree
		BspTreeTools tools = new BspTreeTools();
		BspTreeNode rootNode = tools.createBspTree(points);
		
		// Add result to scene graph
		if(rootNode != null) {
			Vector observer = new Vector(1, 1, 0);
			List<Integer> back2FrontSorted = tools.getBackToFront(rootNode, observer);
			node = new BspNode(rootNode, points, back2FrontSorted, observer);
			getRoot().addChild(node);
		}
	}
	
	@Override
	public void init(GL2 gl) {
		super.init(gl);
		gl.glLineWidth(5);
		gl.glPointSize(5);
	}
	
	@Override
	public void keyPressed(int keyCode) {
		switch(keyCode) {
			case 'p':
				node.showPoints = !node.showPoints;
				break;
			case 'e':
				node.showElements = !node.showElements;
				break;
			case 'l':
				node.showPlanes = !node.showPlanes;
				break;
			case 'b':
				node.showBackToFront = !node.showBackToFront;
				break;
		}
	}
	
	@Override
	public void timerTick(int counter) {
	}
}
