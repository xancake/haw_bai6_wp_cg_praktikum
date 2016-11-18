package computergraphics.framework.math.curve.basefunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BaseFunctions {
	private BaseFunctions() {}
	
	/**
	 * Erzeugt die Bernsteinpolynome für den übergebenen Grad.
	 * Die Liste der Bernsteinpolynome ist immer um eins größer, als der übergebene Grad.
	 * @param grad Der übergebene Grad
	 * @return Eine Liste der Bernsteinpolynome
	 */
	public static List<BernsteinPolynome> createBernsteinPolynome(int grad) {
		List<BernsteinPolynome> polynome = new ArrayList<>();
		for(int i=0; i<=grad; i++) {
			polynome.add(new BernsteinPolynome(grad, i));
		}
		return polynome;
	}
	
	/**
	 * Erzeugt die Hermite-Basisfunktionen für den Grad 3.
	 * @return Eine Liste der Basisfunktionen mit der Länge 4
	 */
	public static List<BaseFunction> createHermiteFunctions3() {
		BaseFunction h0 = (t) -> Math.pow(1-t, 2) * (1+2*t);
		BaseFunction h1 = (t) -> t*Math.pow(1-t, 2);
		BaseFunction h2 = (t) -> Math.pow(-t, 2) * (1-t);
		BaseFunction h3 = (t) -> (3-2*t) * Math.pow(t, 2);
		return Arrays.asList(h0, h1, h2, h3);
	}
}
