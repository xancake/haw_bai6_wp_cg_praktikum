package computergraphics.framework.datastructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import computergraphics.framework.math.Vector;

public class Cube {
	private Vector _base;
	private Vector _spanningVector;
	
	public Cube(double from, double to) {
		this(new Vector(from, from, from), new Vector(to-from, to-from, to-from));
	}
	
	public Cube(Vector base, Vector spanningVector) {
		_base = Objects.requireNonNull(base);
		_spanningVector = Objects.requireNonNull(spanningVector);
	}
	
	/**
	 * Erzeugt Würfel innerhalb dieses Würfels, wobei die Auflösung angibt, wieviele Würfel pro Achse erzeugt werden.
	 * @param resolution Die Auflösung
	 * @return Eine Liste der erzeugten Würfel
	 */
	public List<Cube> createSubCubes(int resolution) {
		Vector spannX = new Vector(_spanningVector.x(), 0, 0);
		Vector spannY = new Vector(0, _spanningVector.y(), 0);
		Vector spannZ = new Vector(0, 0, _spanningVector.z());
		Vector spannR = _spanningVector.multiply(1./resolution);
		
		List<Cube> cubes = new ArrayList<>();
		for(int x=0; x<resolution; x++) {
			for(int y=0; y<resolution; y++) {
				for(int z=0; z<resolution; z++) {
					
					Vector newBase = _base
							.add(spannX.multiply((double)x/resolution))
							.add(spannY.multiply((double)y/resolution))
							.add(spannZ.multiply((double)z/resolution));
					
					cubes.add(new Cube(newBase, spannR));
				}
			}
		}
		return cubes;
	}
	
	public List<Vector> createVertexVectors() {
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
