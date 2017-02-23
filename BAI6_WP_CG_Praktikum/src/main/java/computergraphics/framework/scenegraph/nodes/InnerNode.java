package computergraphics.framework.scenegraph.nodes;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

import computergraphics.framework.math.Matrix;

public class InnerNode extends INode {
	private List<INode> children = new ArrayList<INode>();

	@Override
	public void traverse(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		for (INode child : children) {
			child.traverse(gl, mode, modelMatrix);
		}
	}

	@Override
	public void timerTick(int counter) {
		for (INode child : children) {
			child.timerTick(counter);
		}
	}

	/**
	 * Add new child node.
	 **/
	public void addChild(INode child) {
		child.setParentNode(this);
		children.add(child);
	}
	
	/**
	 * Remove child node from this node.
	 * @param child The child node
	 */
	public void removeChild(INode child) {
		int index = children.indexOf(child);
		if(index != -1) {
			child.setParentNode(null);
			children.remove(index);
		}
	}
}
