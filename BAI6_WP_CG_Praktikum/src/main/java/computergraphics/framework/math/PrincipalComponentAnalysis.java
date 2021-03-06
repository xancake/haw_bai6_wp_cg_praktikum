/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package computergraphics.framework.math;

import java.util.ArrayList;
import java.util.List;

import Jama.EigenvalueDecomposition;

/**
 * Apply a principle component analysis on a list of points in 3-space.
 * @author Philipp Jenke
 */
public class PrincipalComponentAnalysis {
	private Vector centroid = null;
	private Vector eigenValues = null;
	private Vector[] eigenVectors = new Vector[3];
	
	private List<Vector> points;
	
	public PrincipalComponentAnalysis() {
		points = new ArrayList<Vector>();
	}
	
	/**
	 * Add an additional point.
	 */
	public void add(Vector point) {
		points.add(point);
	}
	
	public void addAll(List<Vector> points) {
		this.points.addAll(points);
	}
	
	/**
	 * Apply the PCA, compute tangentU, tangentV and normal.
	 */
	public void applyPCA() {
		if(points.size() < 3) {
			System.out.println("Need a least 3 points for PCA");
			return;
		}
		
		// Compute centroid
		centroid = new Vector(0, 0, 0);
		for(Vector p : points) {
			centroid = centroid.add(p);
		}
		centroid = centroid.multiply(1.0 / points.size());
		
		// Compute the covariance matrix
		Matrix M = new Matrix(0, 0, 0, 0, 0, 0, 0, 0, 0);
		for(Vector p : points) {
			Vector d = p.subtract(centroid);
			M = M.add(d.innerProduct(d));
		}
		
		// Singular value decomposition
		Jama.Matrix jamaM = new Jama.Matrix(3, 3);
		for(int rowIndex=0; rowIndex<3; rowIndex++) {
			for(int colIndex=0; colIndex<3; colIndex++) {
				jamaM.set(colIndex, rowIndex, M.get(colIndex, rowIndex));
			}
		}
		EigenvalueDecomposition e = jamaM.eig();
		Jama.Matrix V = e.getV();
		Jama.Matrix D = e.getD();
		
		eigenVectors[0] = new Vector(V.get(0, 0), V.get(1, 0), V.get(2, 0));
		eigenVectors[1] = new Vector(V.get(0, 1), V.get(1, 1), V.get(2, 1));
		eigenVectors[2] = new Vector(V.get(0, 2), V.get(1, 2), V.get(2, 2));
		eigenValues     = new Vector(D.get(0, 0), D.get(1, 1), D.get(2, 2));
		
	}
	
	/**
	 * Getter for eigenvectors; ascending order
	 */
	public Vector getEigenVector(int index) {
		assert (index >= 0 && index < 3);
		return eigenVectors[index];
	}
	
	/**
	 * Return vector of eigenvalues; smallest first.
	 */
	public Vector getEigenValues() {
		return eigenValues;
	}
	
	public Vector getCentroid() {
		return centroid;
	}
	
	public Vector getNormal() {
		int maxIndex = 0;
		double maxValue = -Double.MAX_VALUE;
		for(int i=0; i<eigenVectors.length; i++) {
			double v = eigenValues.get(i);
			if(v > maxValue) {
				maxIndex = i;
				maxValue = v;
			}
		}
		return eigenVectors[maxIndex];
	}
	
	/**
	 * Clear list of points.
	 */
	public void clear() {
		points.clear();
	}
}
