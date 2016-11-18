package computergraphics.framework.math.curve.basefunction;

import java.util.ArrayList;
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
}
