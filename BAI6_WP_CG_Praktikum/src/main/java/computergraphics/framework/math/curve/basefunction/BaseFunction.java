package computergraphics.framework.math.curve.basefunction;

/**
 * Schnittstelle für Basisfunktionen.
 */
public interface BaseFunction {
	/**
	 * Berechnet den Wert dieser Basisfunktion an dem Parameter {@code t}.
	 * @param t Der Parameter
	 * @return Der Wert dieser Basisfunktion
	 */
	double solve(double t);
	
	/**
	 * Berechnet den Wert der Ableitung dieser Basisfunktion an dem Parameter {@code t}.
	 * @param t Der Parameter
	 * @return Der Wert der Ableitung dieser Basisfunktion
	 */
	default double derive(double t) {
		double h = 0.00001;
		return (solve(t+h) - solve(t)) / h;
	}
}
