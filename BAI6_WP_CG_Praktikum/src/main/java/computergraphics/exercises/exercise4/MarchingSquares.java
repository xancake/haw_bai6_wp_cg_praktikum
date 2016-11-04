package computergraphics.exercises.exercise4;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import computergraphics.framework.datastructures.Pair;
import computergraphics.framework.datastructures.implicit_functions.ImplicitFunction;
import computergraphics.framework.math.Vector;
import computergraphics.framework.mesh.ITriangleMesh;
import computergraphics.framework.mesh.Triangle;

public class MarchingSquares {
	
	private ImplicitFunction _function;
	private double _iso = 0;
	private ITriangleMesh _mesh;
	
	
	
	
	
	
	
	public MarchingSquares(ImplicitFunction function, double isowert, ITriangleMesh mesh) {
		_function = Objects.requireNonNull(function);
		_iso = isowert;
		_mesh = Objects.requireNonNull(mesh);
	}
	
	
	public void createMesh(double size) {
		List<Vector> points = Arrays.asList(
				new Vector(-size, -size, -size),
				new Vector( size, -size, -size),
				new Vector( size,  size, -size),
				new Vector(-size,  size, -size),
				new Vector(-size, -size,  size),
				new Vector( size, -size,  size),
				new Vector( size,  size,  size),
				new Vector(-size,  size,  size)
		);
		List<Double> values = Arrays.asList(
//				-2.,  1.,  2.,  3.,  6.,  0., -4., -2.
//				-1., -1., -1., -1., -1., -1., -1.,  1.
				 2.,  7.,  3.,  1.,  7.,  2.,  7.,  6.
		);
		createTriangles(points, values);
	}
	
	private void createTriangles(List<Vector> points, List<Double> values) {
		int caseIndex = 0;
		for(int i = 0; i < values.size(); i++) {
			double value = values.get(i);
			caseIndex += (value > _iso) ? 1 << i : 0;
		}
		
		int[] currentCase = MarchingSquaresLookupTable.getCase(caseIndex);
		System.out.println(caseIndex + " " + Arrays.toString(currentCase));
		for (int i = 0; i < currentCase.length; i += 3) {
			Vector p1 = getPointOnEdge(points, values, currentCase[i]);
			Vector p2 = getPointOnEdge(points, values, currentCase[i+1]);
			Vector p3 = getPointOnEdge(points, values, currentCase[i+2]);
			
			if(p1 != null && p2 != null && p3 != null) {
				_mesh.addTriangle(_mesh.addVertex(p1), _mesh.addVertex(p2), _mesh.addVertex(p3));
			}
		}
	}
	
	private Vector getPointOnEdge(List<Vector> points, List<Double> values, int edgeIndex) {
		Pair<Pair<Vector, Double>, Pair<Vector, Double>> verticesValuesPairs = getVerticesOfEdge(points, values, edgeIndex);
		if(verticesValuesPairs == null) {
			return null;
		}
		
		Pair<Vector, Double> pA = verticesValuesPairs.getKey();
		Pair<Vector, Double> pB = verticesValuesPairs.getValue();
		
		// Value zu Punkt Interpolation
		double t = (_iso-pA.getValue()) / (pB.getValue()-pA.getValue());
		Vector p = pA.getKey().multiply(1-t).add(pB.getKey().multiply(t));
		
		return p;
	}

	private Pair<Pair<Vector, Double>, Pair<Vector, Double>> getVerticesOfEdge(List<Vector> points, List<Double> values, int edgeIndex) {
		switch(edgeIndex) {
			case -1: return null;
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
	
	private <K, V> Pair<K, V> pair(K k, V v) {
		return new Pair<K, V>(k, v);
	}
	
}
