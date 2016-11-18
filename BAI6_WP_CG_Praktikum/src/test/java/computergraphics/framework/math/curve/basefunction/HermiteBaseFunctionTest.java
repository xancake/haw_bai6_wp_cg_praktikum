package computergraphics.framework.math.curve.basefunction;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class HermiteBaseFunctionTest {
	@Test
	public void testHermiteFunctions() {
		List<BaseFunction> functions = BaseFunctions.createHermiteFunctions3();
		assertEquals(4, functions.size());
		assertStartEnd(functions);
	}
	
	/**
	 * Prüft, ob die Basisfunktionen am Start und Ende die zu erwartenden Werte haben.
	 * @param functions Die zu prüfenden Basisfunktionen
	 */
	private void assertStartEnd(List<BaseFunction> functions) {
		int n = functions.size();
		assertPolinom(functions.get(0), 1, 0);
		for(int i=1; i<n-1; i++) {
			assertPolinom(functions.get(i), 0, 0);
		}
		assertPolinom(functions.get(n-1), 0, 1);
	}
	
	private void assertPolinom(BaseFunction function, double expectedT0, double expectedT1) {
		assertEquals("Die Basisfunktion entspricht am Punkt t=0 dem erwarteten Wert!", expectedT0, function.solve(0), 0.0);
		assertEquals("Die Basisfunktion entspricht am Punkt t=1 dem erwarteten Wert!", expectedT1, function.solve(1), 0.0);
	}
}
