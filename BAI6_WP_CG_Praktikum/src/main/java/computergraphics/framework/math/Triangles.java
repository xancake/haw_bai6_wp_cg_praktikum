package computergraphics.framework.math;

import java.util.Objects;

/**
 * Hilfsklasse zur Berechnung von Dreieckseigenschaften mit Vektoren.
 */
public class Triangles {
	private Triangles() {}
	
	/**
	 * Berechnet die Normale des durch die übergebenen Eckpunkte beschriebenen Dreiecks.
	 * @param v0 Der erste Eckpunkt
	 * @param v1 Der zweite Eckpunkt
	 * @param v2 Der dritte Eckpunkt
	 * @return Die normierte Normale des Dreiecks
	 * @throws NullPointerException, wenn {@code v0}, {@code v1} oder {@code v2} {@code null} ist
	 * @throws IllegalArgumentException, wenn {@code v0.dimension == v1.dimension == v2.dimension} <b>nicht</b> gilt
	 */
	public static Vector calculateNormal(Vector v0, Vector v1, Vector v2) {
		checkTriangleValid(v0, v1, v2);
		Vector v01 = v1.subtract(v0);
		Vector v02 = v2.subtract(v0);
		return v01.cross(v02).getNormalized();
	}
	
	/**
	 * Prüft, ob alle der übergebenen Vektoren valide sind, das heißt:
	 * <ul>
	 * 	<li>Keiner der Vektoren ist {@code null}
	 * 	<li>Alle Vektoren haben dieselbe Dimension
	 * </ul>
	 * @param v0 Ein Vektor
	 * @param v1 Ein Vektor
	 * @param v2 Ein Vektor
	 * @throws NullPointerException, wenn {@code v0}, {@code v1} oder {@code v2} {@code null} ist
	 * @throws IllegalArgumentException, wenn {@code v0.dimension == v1.dimension == v2.dimension} <b>nicht</b> gilt
	 */
	private static void checkTriangleValid(Vector v0, Vector v1, Vector v2) {
		checkTriangleNotNull(v0, v1, v2);
		checkTriangleDimension(v0, v1, v2);
	}
	
	/**
	 * Prüft, ob einer der der übergebenen Vektoren {@code null} ist.
	 * @param v0 Ein Vektor
	 * @param v1 Ein Vektor
	 * @param v2 Ein Vektor
	 * @throws NullPointerException, wenn {@code v0}, {@code v1} oder {@code v2} {@code null} ist
	 */
	private static void checkTriangleNotNull(Vector v0, Vector v1, Vector v2) {
		Objects.requireNonNull(v0);
		Objects.requireNonNull(v1);
		Objects.requireNonNull(v2);
	}
	
	/**
	 * Prüft, ob alle der übergebenen Vektoren dieselbe Dimension haben.
	 * @param v0 Ein Vektor
	 * @param v1 Ein Vektor
	 * @param v2 Ein Vektor
	 * @throws IllegalArgumentException, wenn {@code v0.dimension == v1.dimension == v2.dimension} <b>nicht</b> gilt
	 */
	private static void checkTriangleDimension(Vector v0, Vector v1, Vector v2) {
		if(v0.getDimension() != v1.getDimension()
				|| v0.getDimension() != v2.getDimension()
				|| v1.getDimension() != v2.getDimension()) {
			throw new IllegalArgumentException(String.format(
					"Alle Vektoren müssen die gleiche Dimension haben (v0: %d, v1: %d, v2: %d)!",
					v0.getDimension(), v1.getDimension(), v2.getDimension()));
		}
	}
}
