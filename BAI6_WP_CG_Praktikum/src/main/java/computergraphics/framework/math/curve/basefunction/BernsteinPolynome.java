package computergraphics.framework.math.curve.basefunction;

public class BernsteinPolynome implements BaseFunction {
	private int _n;
	private int _i;
	
	public BernsteinPolynome(int n, int i) {
		_n = n;
		_i = i;
	}
	
	@Override
	public double solve(double t) {
		return ueber(_n, _i) * Math.pow(t, _i) * Math.pow(1-t, _n-_i);
	}
	
	/**
	 * Berechnet die "Ueber"-Notation wie im Foliensatz 5 (Kurven) auf Seite 36 definiert.
	 * @param oben Der obere Parameter
	 * @param unten Der untere Parameter
	 * @return Der berechnete Wert
	 */
	private double ueber(int oben, int unten) {
		return fact(oben) / (double)(fact(unten) * fact(oben-unten));
	}
	
	/**
	 * Berechnet die Fakultät der übergebenen Zahl.
	 * @param n Die Zahl von der die Fakultät berechnet werden soll
	 * @return Die Fakultät
	 */
	private int fact(int n) {
		int fact = 1;
		for(int i=1; i<=n; i++) {
			fact *= i;
		}
		return fact;
	}
}
