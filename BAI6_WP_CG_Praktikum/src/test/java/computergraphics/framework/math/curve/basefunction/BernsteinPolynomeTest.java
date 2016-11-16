package computergraphics.framework.math.curve.basefunction;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Test;

public class BernsteinPolynomeTest {
	@Test
	public void testPolynome() {
		List<BernsteinPolynome> polynome = BernsteinPolynome.createPolynome(3);
		assertEquals(4, polynome.size());
		assertStartEnd(polynome);
		assertSum(polynome, 0.1);
		assertSymmetry(polynome, 0.1);
	}
	
	/**
	 * Prüft, ob die Polynome am Start und Ende die zu erwartenden Werte haben.
	 * @param polynome Die zu prüfenden Polynome
	 */
	private void assertStartEnd(List<BernsteinPolynome> polynome) {
		int n = polynome.size();
		assertPolinom(polynome.get(0), 1, 0);
		for(int i=1; i<n-1; i++) {
			assertPolinom(polynome.get(i), 0, 0);
		}
		assertPolinom(polynome.get(n-1), 0, 1);
	}
	
	private void assertPolinom(BernsteinPolynome polinom, double expectedT0, double expectedT1) {
		assertEquals("Das Polynom entspricht am Punkt t=0 dem erwarteten Wert!", expectedT0, polinom.solve(0), 0.0);
		assertEquals("Das Polynom entspricht am Punkt t=1 dem erwarteten Wert!", expectedT1, polinom.solve(1), 0.0);
	}
	
	/**
	 * Prüft ob die übergebenen Polynome die Eigenschaft der "Partition der Eins" erfüllen.
	 * @param polynome Die zu prüfenden Polynome
	 * @param tStep Die Schrittgröße in der geprüft werden soll
	 */
	private void assertSum(List<BernsteinPolynome> polynome, double tStep) {
		for(double t=0; t<1; t+=tStep) {
			double sum = 0;
			for(BernsteinPolynome polynom : polynome) {
				sum += polynom.solve(t);
			}
			assertEquals("Die Summe der Polynome an t=" + t + " entspricht nicht 1!", 1, sum, 0.00001);
		}
	}
	
	/**
	 * Prüft ob die übergebenen Polynome die Eigenschaft der "Symmetrie" erfüllen.
	 * @param polynome Die zu prüfenden Polynome
	 * @param tStep Die Schrittgröße in der geprüft werden soll
	 */
	private void assertSymmetry(List<BernsteinPolynome> polynome, double tStep) {
		int n = polynome.size();
		for(int i=0; i<n/2; i++) {
			for(double t=0; t<1; t+=tStep) {
				int iMirror = n-1-i;
				BernsteinPolynome b = polynome.get(i);
				BernsteinPolynome bMirror = polynome.get(iMirror);
				assertEquals("Die Polynome '" + i + "' und '" + iMirror + "' haben an t" + i + "=" + t + " und t"+ iMirror + "=" + (1-t) + " nicht den selben Wert!", b.solve(t), bMirror.solve(1-t), 0.00001);
			}
		}
	}
}
