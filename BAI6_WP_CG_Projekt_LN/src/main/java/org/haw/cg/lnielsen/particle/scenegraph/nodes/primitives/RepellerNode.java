package org.haw.cg.lnielsen.particle.scenegraph.nodes.primitives;

import java.util.Objects;
import org.haw.cg.lnielsen.particle.physics.Repeller;
import com.jogamp.opengl.GL2;
import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.mesh.TriangleMeshFactory;
import computergraphics.framework.mesh.halfedge.HalfEdgeTriangleMesh;
import computergraphics.framework.scenegraph.nodes.InnerNode;
import computergraphics.framework.scenegraph.nodes.primitives.TriangleMeshNode;
import computergraphics.framework.scenegraph.nodes.transformation.TranslationNode;

public class RepellerNode extends InnerNode {
	private Repeller _repeller;
	private boolean _drawRepeller;
	
	private InnerNode _locationNode;
	private Vector _oldLocation = new Vector(0,0,0);
	
	private InnerNode _rangeNode;
	private double _oldRange = Double.NaN;
	
	public RepellerNode(Repeller repeller) {
		setRepeller(repeller);
	}
	
	@Override
	public void traverse(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		if(isDrawRepeller()) {
			drawLocation(gl, mode, modelMatrix);
			drawRange(gl, mode, modelMatrix);
		}
	}
	
	private void drawLocation(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		if(_locationNode == null || !_oldLocation.equals(_repeller.getLocation())) {
			_oldLocation.copy(_repeller.getLocation());
			removeChild(_locationNode);
			_locationNode = createLocationNode();
			addChild(_locationNode);
		}
		_locationNode.traverse(gl, mode, modelMatrix);
	}
	
	private void drawRange(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		if(_rangeNode == null || _oldRange != _repeller.getRange()) {
			_oldRange = _repeller.getRange();
			removeChild(_rangeNode);
			_rangeNode = createRangeNode();
			addChild(_rangeNode);
		}
		_rangeNode.traverse(gl, mode, modelMatrix);
	}
	
	public Repeller getRepeller() {
		return _repeller;
	}
	
	public void setRepeller(Repeller repeller) {
		_repeller = Objects.requireNonNull(repeller);
	}
	
	public boolean isDrawRepeller() {
		return _drawRepeller;
	}
	
	public void setDrawRepeller(boolean draw) {
		_drawRepeller = draw;
	}
	
	private InnerNode createLocationNode() {
		ITriangleMesh mesh = new HalfEdgeTriangleMesh();
		TriangleMeshFactory.createCube(mesh, 0.01);
		TriangleMeshNode meshNode = new TriangleMeshNode(mesh, new Vector(1,1,1));
		TranslationNode translation = new TranslationNode(_repeller.getLocation());
		translation.addChild(meshNode);
		return translation;
	}
	
	private InnerNode createRangeNode() {
		ITriangleMesh mesh = new HalfEdgeTriangleMesh();
		TriangleMeshFactory.createSphere(mesh, (float)_repeller.getRange(), 10);
		TriangleMeshNode meshNode = new TriangleMeshNode(mesh, new Vector(1,1,1,0.25));
		TranslationNode translation = new TranslationNode(_repeller.getLocation());
		translation.addChild(meshNode);
		return translation;
	}
}
