package computergraphics.framework.mesh;

import computergraphics.framework.math.Cuboid;
import computergraphics.framework.math.Vector;

public class TriangleMeshFactory {
	/**
	 * Create sphere.
	 */
	public static void createSphere(ITriangleMesh mesh, float radius, int resolution) {
		mesh.clear();
		
		// Create vertices
		float dTheta = (float) (Math.PI / (resolution + 1));
		float dPhi = (float) (Math.PI * 2.0 / resolution);
		// 0-180 degrees: i, theta
		for(int i=0; i<resolution; i++) {
			// 0-360 degres: j, phi
			for(int j=0; j<resolution; j++) {
				Vector p0 = evaluateSpherePoint((i+1) * dTheta, j * dPhi, radius);
				mesh.addVertex(p0);
			}
		}
		
		// Add triangles
		for(int i=0; i<resolution - 1; i++) {
			for(int j=0; j<resolution; j++) {
				mesh.addTriangle(
						getSphereIndex(  i,   j, resolution),
						getSphereIndex(i+1,   j, resolution),
						getSphereIndex(i+1, j+1, resolution));
				mesh.addTriangle(
						getSphereIndex(  i,   j, resolution),
						getSphereIndex(i+1, j+1, resolution),
						getSphereIndex(  i, j+1, resolution));
			}
		}
		int leftIndex = mesh.addVertex(new Vector(0, 0, radius));
		int rightIndex = mesh.addVertex(new Vector(0, 0, -radius));
		for(int j=0; j<resolution; j++) {
			mesh.addTriangle(
					getSphereIndex(0, j, resolution),
					getSphereIndex(0, (j+1) % resolution, resolution),
					leftIndex);
			mesh.addTriangle(
					getSphereIndex(resolution-1, j, resolution),
					rightIndex,
					getSphereIndex(resolution-1, (j+1) % resolution, resolution));
		}

		mesh.computeNormals();
	}

	private static Vector evaluateSpherePoint(float theta, float phi, float radius) {
		float x = (float) (radius * Math.sin(theta) * Math.cos(phi));
		float y = (float) (radius * Math.sin(theta) * Math.sin(phi));
		float z = (float) (radius * Math.cos(theta));
		return new Vector(x, y, z);
	}

	private static int getSphereIndex(int i, int j, int resolution) {
		return (i % resolution) * resolution + (j % resolution);
	}

	public static void createSquare(ITriangleMesh mesh, float extend) {
		mesh.clear();
		mesh.addVertex(new Vector(-extend, 0, -extend));
		mesh.addVertex(new Vector( extend, 0, -extend));
		mesh.addVertex(new Vector( extend, 0,  extend));
		mesh.addVertex(new Vector(-extend, 0,  extend));
		mesh.addTriangle(0, 2, 1);
		mesh.addTriangle(0, 3, 2);
		mesh.computeNormals();
	}
	
	/**
	 * Erzeugt einen Würfel, der sich von {@code -size} bis {@code size} erstreckt.
	 * @param mesh Das Mesh
	 * @param size Die Größe
	 */
	public static void createCube(ITriangleMesh mesh, double size) {
		createCuboid(mesh, new Cuboid(-size, size));
	}
	
	/**
	 * Erzeugt ein Mesh aus dem übergebenen Quader
	 * @param mesh Das Mesh
	 * @param cuboid Der Quader
	 */
	public static void createCuboid(ITriangleMesh mesh, Cuboid cuboid) {
		createCuboid(mesh, cuboid.getBase(), cuboid.getSpanningVector());
	}
	
	/**
	 * Erzeugt das Mesh eines Quaders, der durch die übergebenen Vektoren beschrieben wird. 
	 * @param mesh Das Mesh
	 * @param base Der Stützvektor
	 * @param spanningVector Der Vektor, der das Volumen des Quaders aufspannt
	 */
	public static void createCuboid(ITriangleMesh mesh, Vector base, Vector spanningVector) {
		mesh.clear();
		
		Vector spannX = new Vector(spanningVector.x(), 0, 0);
		Vector spannY = new Vector(0, spanningVector.y(), 0);
		Vector spannZ = new Vector(0, 0, spanningVector.z());
		
		mesh.addVertex(new Vector(base));
		mesh.addVertex(base.add(spannX));
		mesh.addVertex(base.add(spannX).add(spannY));
		mesh.addVertex(base.add(spannY));
		mesh.addVertex(base.add(spannZ));
		mesh.addVertex(base.add(spannX).add(spannZ));
		mesh.addVertex(base.add(spannX).add(spannY).add(spannZ));
		mesh.addVertex(base.add(spannY).add(spannZ));
		
		mesh.addTriangle(2, 1, 0);
		mesh.addTriangle(0, 3, 2);
		mesh.addTriangle(4, 5, 6);
		mesh.addTriangle(6, 7, 4);
		mesh.addTriangle(0, 1, 5);
		mesh.addTriangle(5, 4, 0);
		mesh.addTriangle(6, 5, 1);
		mesh.addTriangle(1, 2, 6);
		mesh.addTriangle(7, 6, 2);
		mesh.addTriangle(2, 3, 7);
		mesh.addTriangle(3, 0, 4);
		mesh.addTriangle(4, 7, 3);
		
		mesh.computeNormals();
	}
	
	/**
	 * Erzeugt ein Mesh, bei dem alle Facetten des Blueprint-Meshes invertiert sind (in die andere Richtung zeigen).
	 * @param mesh Das Mesh (das Ergebnis der Invertierung)
	 * @param blueprint Das Blueprint-Mesh (das Ausgangs-Mesh)
	 */
	public static void createInvertedMesh(ITriangleMesh mesh, ITriangleMesh blueprint) {
		mesh.clear();
		for(int i=0; i<blueprint.getNumberOfVertices(); i++) {
			mesh.addVertex(new Vector(blueprint.getVertex(i).getPosition()));
		}
		for(int i=0; i<blueprint.getNumberOfTriangles(); i++) {
			Triangle t = blueprint.getTriangle(i);
			mesh.addTriangle(t.getVertexIndex(2), t.getVertexIndex(1), t.getVertexIndex(0));
		}
		mesh.computeNormals();
	}
}
