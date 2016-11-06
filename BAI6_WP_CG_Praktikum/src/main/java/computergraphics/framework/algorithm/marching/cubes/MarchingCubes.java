package computergraphics.framework.algorithm.marching.cubes;

import static computergraphics.framework.datastructures.Pair.pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import computergraphics.framework.datastructures.Pair;
import computergraphics.framework.math.Cuboid;
import computergraphics.framework.math.Vector;
import computergraphics.framework.math.implicit_functions.ImplicitFunction;
import computergraphics.framework.mesh.ITriangleMesh;

public class MarchingCubes {
	private Cuboid _volume;
	private List<Cuboid> _subVolumes;
	private int _resolution;
	
	/**
	 * Instanziiert ein neues Marching-Squares-Algorithmus Objekt
	 * @param volume Das Volumen in dem der Algorithmus angewendet wird
	 * @param resolution Die Auflösung mit der der Algorithmus arbeitet
	 */
	public MarchingCubes(Cuboid volume, int resolution) {
		if(resolution < 1) {
			throw new IllegalArgumentException("Die Auflösung für den MarchingSquares-Algorithmus muss mindestens 1 sein!");
		}
		_volume = Objects.requireNonNull(volume);
		_resolution = resolution;
		_subVolumes = _volume.createSubCuboids(_resolution);
	}
	
	/**
	 * Erzeugt das Mesh der übergebenen impliziten Funktion und Isowert.
	 * @param mesh Das Mesh
	 * @param function Die implizite Funktion
	 * @param isowert Der Isowert
	 */
	public void createMesh(ITriangleMesh mesh, ImplicitFunction function, double isowert) {
		for(Cuboid cube : _subVolumes) {
			createMesh(cube, Objects.requireNonNull(mesh), Objects.requireNonNull(function), isowert);
		}
		mesh.computeNormals();
	}
	
	private void createMesh(Cuboid cube, ITriangleMesh mesh, ImplicitFunction function, double isowert) {
		List<Vector> points = cube.getVertices();
		List<Double> values = new ArrayList<>();
		for(int i=0; i<points.size(); i++) {
			values.add(function.getValue(points.get(i)));
		}
		createTriangles(mesh, points, values, isowert);
	}
	
	private void createTriangles(ITriangleMesh mesh, List<Vector> points, List<Double> values, double isowert) {
		int caseIndex = 0;
		for(int i = 0; i < values.size(); i++) {
			double value = values.get(i);
			caseIndex += (value > isowert) ? 1 << i : 0;
		}
		
		int[] currentCase = MarchingCubesLookupTable.getCase(caseIndex);
		for (int i = 0; i < currentCase.length; i += 3) {
			if(currentCase[i] != -1 && currentCase[i+1] != -1 && currentCase[i+2] != -1) {
				Vector p1 = getPointOnEdge(points, values, isowert, currentCase[i]);
				Vector p2 = getPointOnEdge(points, values, isowert, currentCase[i+1]);
				Vector p3 = getPointOnEdge(points, values, isowert, currentCase[i+2]);
				mesh.addTriangle(mesh.addVertex(p1), mesh.addVertex(p2), mesh.addVertex(p3));
			}
		}
	}
	
	private Vector getPointOnEdge(List<Vector> points, List<Double> values, double isowert, int edgeIndex) {
		Pair<Pair<Vector, Double>, Pair<Vector, Double>> verticesValuesPairs = getVerticesOfEdge(points, values, edgeIndex);
		Pair<Vector, Double> pA = verticesValuesPairs.getKey();
		Pair<Vector, Double> pB = verticesValuesPairs.getValue();
		
		// Value zu Punkt Interpolation
		double t = (isowert-pA.getValue()) / (pB.getValue()-pA.getValue());
		Vector p = pA.getKey().multiply(1-t).add(pB.getKey().multiply(t));
		
		return p;
	}

	private Pair<Pair<Vector, Double>, Pair<Vector, Double>> getVerticesOfEdge(List<Vector> points, List<Double> values, int edgeIndex) {
		switch(edgeIndex) {
			case  0: return pair(pair(points.get(0), values.get(0)), pair(points.get(1), values.get(1)));
			case  1: return pair(pair(points.get(1), values.get(1)), pair(points.get(2), values.get(2)));
			case  2: return pair(pair(points.get(2), values.get(2)), pair(points.get(3), values.get(3)));
			case  3: return pair(pair(points.get(3), values.get(3)), pair(points.get(0), values.get(0)));
			case  4: return pair(pair(points.get(4), values.get(4)), pair(points.get(5), values.get(5)));
			case  5: return pair(pair(points.get(5), values.get(5)), pair(points.get(6), values.get(6)));
			case  6: return pair(pair(points.get(6), values.get(6)), pair(points.get(7), values.get(7)));
			case  7: return pair(pair(points.get(7), values.get(7)), pair(points.get(4), values.get(4)));
			case  8: return pair(pair(points.get(0), values.get(0)), pair(points.get(4), values.get(4)));
			case  9: return pair(pair(points.get(1), values.get(1)), pair(points.get(5), values.get(5)));
			// FIXME: klären ob das so richtig ist mit den Kanten oder vertauscht werden soll?!?!?!?
			case 10: return pair(pair(points.get(3), values.get(3)), pair(points.get(7), values.get(7)));
			case 11: return pair(pair(points.get(2), values.get(2)), pair(points.get(6), values.get(6)));
		}
		throw new IllegalArgumentException("Mehr als 12 Kanten werden nicht unterstützt. Schließlich haben Würfel nur genau 12 Kanten.");
	}
	
	public Cuboid getVolume() {
		return _volume;
	}
	
	public List<Cuboid> getSubVolumes() {
		return Collections.unmodifiableList(_subVolumes);
	}
	
	public int getResolution() {
		return _resolution;
	}
}
