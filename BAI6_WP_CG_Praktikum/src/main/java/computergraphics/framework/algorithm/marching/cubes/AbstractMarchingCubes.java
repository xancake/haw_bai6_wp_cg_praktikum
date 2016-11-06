package computergraphics.framework.algorithm.marching.cubes;

import static computergraphics.framework.datastructures.Pair.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import computergraphics.framework.datastructures.Pair;
import computergraphics.framework.math.Cuboid;
import computergraphics.framework.math.Vector;
import computergraphics.framework.math.implicit_functions.ImplicitFunction;
import computergraphics.framework.mesh.ITriangleMesh;

public abstract class AbstractMarchingCubes implements MarchingCubes {
	private Cuboid _volume;
	private List<Cuboid> _subVolumes;
	private int _resolution;
	
	public AbstractMarchingCubes(Cuboid volume, int resolution) {
		if(resolution < 1) {
			throw new IllegalArgumentException("Die Auflösung für den MarchingSquares-Algorithmus muss mindestens 1 sein!");
		}
		_volume = Objects.requireNonNull(volume);
		_resolution = resolution;
		_subVolumes = _volume.createSubCuboids(_resolution);
	}
	
	@Override
	public final Cuboid getVolume() {
		return _volume;
	}
	
	@Override
	public final List<Cuboid> getSubVolumes() {
		return Collections.unmodifiableList(_subVolumes);
	}
	
	@Override
	public final int getResolution() {
		return _resolution;
	}
	
	@Override
	public final void createMesh(ITriangleMesh mesh, ImplicitFunction function, double isowert) {
		Objects.requireNonNull(mesh);
		Objects.requireNonNull(function);
		mesh.clear();
		createMesh0(mesh, function, isowert);
		mesh.computeNormals();
	}
	
	/**
	 * Erzeugt das Mesh für die übergebene implizite Funktion und deren Isowert.
	 * <p>Es ist bereits sichergestellt, dass weder das Mesh noch die Funktion {@code null} ist. Das Mesh ist bereits
	 * {@link ITriangleMesh#clear() geleert} und die Normalen müssen nicht berechnet werden.
	 * @param mesh Das Mesh
	 * @param function Die Funktion
	 * @param isowert Der Isowert
	 */
	protected abstract void createMesh0(ITriangleMesh mesh, ImplicitFunction function, double isowert);
	
	protected void doVolume(Cuboid subVolume, ITriangleMesh mesh, ImplicitFunction function, double isowert) {
		List<PointValue> pointValueList = new ArrayList<>();
		for(Vector point : subVolume.getVertices()) {
			pointValueList.add(pv(point, function.getValue(point)));
		}
		createTriangles(mesh, pointValueList, isowert);
	}
	
	private void createTriangles(ITriangleMesh mesh, List<PointValue> pointValueList, double isowert) {
		int caseIndex = 0;
		for(int i = 0; i < pointValueList.size(); i++) {
			double value = pointValueList.get(i).getValue();
			caseIndex += (value > isowert) ? 1 << i : 0;
		}
		
		int[] currentCase = MarchingCubesLookupTable.getCase(caseIndex);
		for (int i = 0; i < currentCase.length; i += 3) {
			if(currentCase[i] != -1 && currentCase[i+1] != -1 && currentCase[i+2] != -1) {
				Vector p1 = calculatePointOnEdge(getEdge(pointValueList, currentCase[i]), isowert);
				Vector p2 = calculatePointOnEdge(getEdge(pointValueList, currentCase[i+1]), isowert);
				Vector p3 = calculatePointOnEdge(getEdge(pointValueList, currentCase[i+2]), isowert);
				addTriangle(mesh, p1, p2, p3);
			}
		}
	}
	
	/**
	 * Berechnet den Punkt auf der Kante, durch den die {@link ImplicitFunction} läuft.
	 * @param edge Die Kante beschrieben durch die beiden Eckpunkte
	 * @param isowert Der Isowert
	 * @return Der Punkt auf dem die Funktion die Kante schneidet
	 */
	private Vector calculatePointOnEdge(Pair<PointValue, PointValue> edge, double isowert) {
		PointValue pA = edge.getKey();
		PointValue pB = edge.getValue();
		
		// Value zu Punkt Interpolation
		double t = (isowert-pA.getValue()) / (pB.getValue()-pA.getValue());
		Vector p = pA.getPoint().multiply(1-t).add(pB.getPoint().multiply(t));
		
		return p;
	}
	
	/**
	 * Ermittelt anhand des Kantenindex aus der {@link MarchingCubesLookupTable Lookup-Tabelle} die zwei Punkte, die
	 * diese Kante beschreiben.
	 * @param pointValueList Eine Liste aller Punkte
	 * @param edgeIndex Der Kantenindex aus der Lookup-Tabelle
	 * @return Die Kante zu dem Kantenindex beschrieben durch die beiden Eckpunkte
	 */
	private Pair<PointValue, PointValue> getEdge(List<PointValue> pointValueList, int edgeIndex) {
		switch(edgeIndex) {
			case  0: return pair(pointValueList.get(0), pointValueList.get(1));
			case  1: return pair(pointValueList.get(1), pointValueList.get(2));
			case  2: return pair(pointValueList.get(2), pointValueList.get(3));
			case  3: return pair(pointValueList.get(3), pointValueList.get(0));
			case  4: return pair(pointValueList.get(4), pointValueList.get(5));
			case  5: return pair(pointValueList.get(5), pointValueList.get(6));
			case  6: return pair(pointValueList.get(6), pointValueList.get(7));
			case  7: return pair(pointValueList.get(7), pointValueList.get(4));
			case  8: return pair(pointValueList.get(0), pointValueList.get(4));
			case  9: return pair(pointValueList.get(1), pointValueList.get(5));
			case 10: return pair(pointValueList.get(3), pointValueList.get(7));
			case 11: return pair(pointValueList.get(2), pointValueList.get(6));
		}
		throw new IllegalArgumentException("Mehr als 12 Kanten werden nicht unterstützt. Schließlich haben Würfel nur genau 12 Kanten.");
	}
	
	/**
	 * Fügt dem übergebenen Mesh ein Dreieck bestehend aus den übergebenen drei Eckpunkten hinzu.
	 * <p>Wird {@code protected} bereitgestellt, damit multithreaded-Varianten des Algorithmus die Möglichkeit den Zugriff auf
	 * das Mesh zu synchronisieren.
	 * @param mesh Das Mesh
	 * @param p1 Der erste Punkt des Dreiecks
	 * @param p2 Der zweite Punkt des Dreiecks
	 * @param p3 Der dritte Punkt des Dreiecks
	 */
	protected void addTriangle(ITriangleMesh mesh, Vector p1, Vector p2, Vector p3) {
		int pi1 = mesh.addVertex(p1);
    	int pi2 = mesh.addVertex(p2);
    	int pi3 = mesh.addVertex(p3);
    	mesh.addTriangle(pi1, pi2, pi3);
	}
	
	private static PointValue pv(Vector point, Double value) {
		return new PointValue(point, value);
	}
	
	private static class PointValue {
		private Pair<Vector, Double> _pair;
		
		public PointValue(Vector point, Double value) {
			_pair = pair(point, value);
		}
		
		public Vector getPoint() {
			return _pair.getKey();
		}
		
		public Double getValue() {
			return _pair.getValue();
		}
	}
}
