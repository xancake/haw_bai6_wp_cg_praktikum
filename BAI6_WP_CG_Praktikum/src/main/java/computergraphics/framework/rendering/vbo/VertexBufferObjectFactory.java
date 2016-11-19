package computergraphics.framework.rendering.vbo;

import java.util.Objects;
import computergraphics.framework.math.Vector;
import computergraphics.framework.math.curve.Curve;
import computergraphics.framework.mesh.ITriangleMesh;

public abstract class VertexBufferObjectFactory {
	protected VertexBufferObjectFactory() {}
	
	public static MeshVBOFactory forMesh(ITriangleMesh mesh) {
		return new MeshVBOFactory(mesh);
	}
	
	public static CurveVBOFactory forCurve(Curve curve) {
		return new CurveVBOFactory(curve);
	}
	
	protected void checkColor(Vector color) {
		if(Objects.requireNonNull(color).getDimension() != 4) {
			throw new IllegalArgumentException("Die Farbe muss vierdimensional sein (R,G,B,A)");
		}
	}
}
