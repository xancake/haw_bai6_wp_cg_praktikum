package computergraphics.framework.scenegraph.nodes.primitives;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.jogamp.opengl.GL2;
import computergraphics.framework.algorithm.marching.cubes.MarchingCubes;
import computergraphics.framework.math.Cuboid;
import computergraphics.framework.math.Matrix;
import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.mesh.TriangleMeshFactory;
import computergraphics.framework.mesh.halfedge.HalfEdgeTriangleMesh;
import computergraphics.framework.rendering.vbo.RenderVertex;
import computergraphics.framework.rendering.vbo.VertexBufferObject;
import computergraphics.framework.scenegraph.nodes.LeafNode;

public class MarchingCubesVisualizationNode extends LeafNode {
	private MarchingCubes _marchingSquares;
	
	private TriangleMeshNode _volumeNode;
	private Vector _volumeColor = new Vector(1, 0, 0, 1);
	private boolean _drawVolume;
	
	private VertexBufferObject _subVolumes;
	private Vector _subVolumesColor = new Vector(1, 1, 1, 0.4);
	private boolean _drawSubVolumes;
	
	public MarchingCubesVisualizationNode(MarchingCubes marchingSquares) {
		_marchingSquares = Objects.requireNonNull(marchingSquares);
		setDrawVolume(true);
	}
	
	@Override
	public void drawGL(GL2 gl, RenderMode mode, Matrix modelMatrix) {
		if(isDrawVolume()) {
			_volumeNode.drawGL(gl, mode, modelMatrix);
		}
		
		if(isDrawSubVolumes()) {
			_subVolumes.draw(gl);
		}
	}
	
	public boolean isDrawVolume() {
		return _drawVolume;
	}
	
	public void setDrawVolume(boolean draw) {
		if(_drawVolume != draw) {
			initMSVolumeMesh(false);
			_drawVolume = draw;
		}
	}
	
	public Vector getVolumeColor() {
		return _volumeColor;
	}
	
	public void setVolumeColor(Vector color) {
		if(!_volumeColor.equals(color)) {
			_volumeColor = checkColorVektor(color);
			initMSVolumeMesh(true);
		}
	}
	
	public boolean isDrawSubVolumes() {
		return _drawSubVolumes;
	}
	
	public void setDrawSubVolumes(boolean draw) {
		if(_drawSubVolumes != draw) {
			initMSSubVolumeMesh(false);
			_drawSubVolumes = draw;
		}
	}
	
	public Vector getSubVolumesColor() {
		return _subVolumesColor;
	}
	
	public void setSubVolumesColor(Vector color) {
		if(!_subVolumesColor.equals(color)) {
			_subVolumesColor = checkColorVektor(color);
			initMSSubVolumeMesh(true);
		}
	}
	
	/**
	 * Prüft ob der übergebene Vektor einen gültigen Farbvektor beschreibt und gibt eine Kopie des Vektors zurück,
	 * wenn das der Fall ist. Ansonsten wird eine {@link IllegalArgumentException} geworfen.
	 * <p>Ein gültiger Farbvektor hat entweder drei (R,G,B) oder vier (R,G,B,A) Dimensionen.
	 * @param color Der zu prüfende Farbvektor
	 * @return Eine Kopie des erfolgreich geprüften Vektors
	 * @throws IllegalArgumentException Wenn der Vektor nicht drei- oder vierdimensional ist
	 */
	private Vector checkColorVektor(Vector color) {
		switch(Objects.requireNonNull(color).getDimension()) {
			case 3: return new Vector(color.x(), color.y(), color.z(), 1);
			case 4: return new Vector(color);
			default:
				throw new IllegalArgumentException("Die Farbe muss als drei- oder vierdimensionaler Vektor übergeben werden!");
		}
	}
	
	private void initMSVolumeMesh(boolean recalculate) {
		if(recalculate || _volumeNode == null) {
			_volumeNode = new TriangleMeshNode(createMSVolumeMesh(_marchingSquares.getVolume()), _volumeColor);
		}
	}
	
	private ITriangleMesh createMSVolumeMesh(Cuboid volume) {
		ITriangleMesh cubeMesh = new HalfEdgeTriangleMesh();
		TriangleMeshFactory.createCuboid(cubeMesh, volume);
		ITriangleMesh cubeMeshInverted = new HalfEdgeTriangleMesh();
		TriangleMeshFactory.createInvertedMesh(cubeMeshInverted, cubeMesh);
		return cubeMeshInverted;
	}
	
	private void initMSSubVolumeMesh(boolean recalculate) {
		if(recalculate || _subVolumes == null) {
			_subVolumes = createMSSubVolumesMeshB(_marchingSquares.getVolume(), _marchingSquares.getResolution());
		}
	}
	
	private VertexBufferObject createMSSubVolumesMeshB(Cuboid volume, double resolution) {
		Vector base = volume.getBase();
		Vector spannR = volume.getSpanningVector();
		Vector spannX = new Vector(spannR.x(), 0, 0);
		Vector spannY = new Vector(0, spannR.y(), 0);
		Vector spannZ = new Vector(0, 0, spannR.z());
		Vector normal = new Vector(1, 0, 0);
		
		List<RenderVertex> renderVertices = new ArrayList<>();
		for(int x=1; x<resolution; x++) {
			for(int y=1; y<resolution; y++) {
				Vector b = new Vector(base.x()+(spannR.x()*x/resolution), base.y()+(spannR.y()*y/resolution), base.z());
				renderVertices.add(new RenderVertex(b, normal, _subVolumesColor));
				renderVertices.add(new RenderVertex(b.add(spannZ), normal, _subVolumesColor));
			}
		}
		for(int x=1; x<resolution; x++) {
			for(int z=1; z<resolution; z++) {
				Vector b = new Vector(base.x()+(spannR.x()*x/resolution), base.y(), base.z()+(spannR.z()*z/resolution));
				renderVertices.add(new RenderVertex(b, normal, _subVolumesColor));
				renderVertices.add(new RenderVertex(b.add(spannY), normal, _subVolumesColor));
			}
		}
		for(int y=1; y<resolution; y++) {
			for(int z=1; z<resolution; z++) {
				Vector b = new Vector(base.x(), base.y()+(spannR.y()*y/resolution), base.z()+(spannR.z()*z/resolution));
				renderVertices.add(new RenderVertex(b, normal, _subVolumesColor));
				renderVertices.add(new RenderVertex(b.add(spannX), normal, _subVolumesColor));
			}
		}
		
		return new VertexBufferObject(GL2.GL_LINES, renderVertices);
	}
}
