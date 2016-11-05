package computergraphics.framework.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Beschreibt einen Quader.
 */
public class Cuboid {
	private Vector _base;
	private Vector _spanningVector;
	
	/**
	 * Erzeugt einen Würfel.
	 * @param from Die XYZ-Koordinaten des Stützvektors
	 * @param to Die XYZ-Koordinaten des gegenüberliegenden Eckpunktes vom Stützvektor
	 */
	public Cuboid(double from, double to) {
		this(new Vector(from, from, from), new Vector(to-from, to-from, to-from));
	}
	
	/**
	 * Erzeugt einen Quader, der durch den übergebenen Stütz- und Spannvektor beschrieben wird.
	 * @param base Der Stützvektor
	 * @param spanningVector Der Spanvektor
	 */
	public Cuboid(Vector base, Vector spanningVector) {
		_base = Objects.requireNonNull(base);
		_spanningVector = Objects.requireNonNull(spanningVector);
	}
	
	public Vector getBase() {
		return new Vector(_base);
	}
	
	public Vector getSpanningVector() {
		return new Vector(_spanningVector);
	}
	
	/**
	 * Erzeugt Quader innerhalb dieses Quader, wobei die Auflösung angibt, wieviele Quader pro Achse erzeugt werden.
	 * @param resolution Die Auflösung
	 * @return Eine Liste der erzeugten Quader
	 */
	public List<Cuboid> createSubCuboids(int resolution) {
		Vector spannX = new Vector(_spanningVector.x(), 0, 0);
		Vector spannY = new Vector(0, _spanningVector.y(), 0);
		Vector spannZ = new Vector(0, 0, _spanningVector.z());
		Vector spannR = _spanningVector.multiply(1./resolution);
		
		List<Cuboid> cubes = new ArrayList<>();
		for(int x=0; x<resolution; x++) {
			for(int y=0; y<resolution; y++) {
				for(int z=0; z<resolution; z++) {
					
					Vector newBase = _base
							.add(spannX.multiply((double)x/resolution))
							.add(spannY.multiply((double)y/resolution))
							.add(spannZ.multiply((double)z/resolution));
					
					cubes.add(new Cuboid(newBase, spannR));
				}
			}
		}
		return cubes;
	}
	
	/**
	 * Erzeugt eine Liste der Eckpunkte dieses Quaders und gibt sie zurück.
	 * @return Die Eckpunkte dieses Quaders
	 */
	public List<Vector> getVertices() {
		Vector spannX = new Vector(_spanningVector.x(), 0, 0);
		Vector spannY = new Vector(0, _spanningVector.y(), 0);
		Vector spannZ = new Vector(0, 0, _spanningVector.z());
		
		List<Vector> vertices = new ArrayList<>();
		vertices.add(new Vector(_base));
		vertices.add(new Vector(_base.add(spannX)));
		vertices.add(new Vector(_base.add(spannX).add(spannY)));
		vertices.add(new Vector(_base.add(spannY)));
		vertices.add(new Vector(_base.add(spannZ)));
		vertices.add(new Vector(_base.add(spannX).add(spannZ)));
		vertices.add(new Vector(_base.add(spannX).add(spannY).add(spannZ)));
		vertices.add(new Vector(_base.add(spannY).add(spannZ)));
		return vertices;
	}
	
	@Override
	public String toString() {
		return "[" + _base.toString() + ", " + _spanningVector.toString() + "]";
	}
}
