package computergraphics.framework.math;

import java.util.Objects;

public class HessescheEbene {
	private Vector _base;
	private Vector _normal;
	
	public HessescheEbene(Vector base, Vector normal) {
		Vector.checkDimensionEqual(base, normal);
		_base = Objects.requireNonNull(base);
		_normal = Objects.requireNonNull(normal);
	}
	
	/**
	 * Gibt den Basisvektor dieser Ebene zurück.
	 * @return Der Basisvektor
	 */
	public Vector getBase() {
		return _base;
	}
	
	/**
	 * Gibt den Normalenvektor dieser Ebene zurück.
	 * @return Der Normalenvektor
	 */
	public Vector getNormal() {
		return _normal;
	}
	
	/**
	 * Berechnet das Lambda für den Schnitt eines Strahls mit dieser Ebene.
	 * @param origin Der Ursprungspunkt des Strahls
	 * @param direction Der Richtungsvektor des Strahls
	 * @return Das berechnete Lambda oder {@link Double#NaN}, wenn der Strahl diese Ebene nicht schneidet
	 * @throws IllegalArgumentException Wenn die übergebenen Vektoren nicht dieselbe
	 *         {@link Vector#getDimension() Dimension} haben, wie diese Ebene
	 */
	public double calculateLambda(Vector origin, Vector direction) {
		Vector.checkDimensionEqual(_base, origin, direction);
		double divisor = _normal.multiply(direction);
		if(divisor == 0) {
			return Double.NaN;
		}
		return (_normal.multiply(_base) - _normal.multiply(origin)) / divisor;
	}
	
	/**
	 * Berechnet den Schnittpunkt des durch die übergebenen Vektoren beschriebenen Strahls mit dieser Ebene.
	 * @param origin Der Ursprungspunkt des Strahls
	 * @param direction Der Richtungsvektor des Strahls
	 * @return Der Schnittpunkt oder {@code null}, wenn es keinen Schnittpunkt gibt
	 */
	public Vector calculateIntersection(Vector origin, Vector direction) {
		double lambda = calculateLambda(origin, direction);
		if(Double.isNaN(lambda)) {
			return null;
		}
		return origin.add(direction.multiply(lambda));
	}
}
