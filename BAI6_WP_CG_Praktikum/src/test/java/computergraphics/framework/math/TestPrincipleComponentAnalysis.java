package computergraphics.framework.math;

public class TestPrincipleComponentAnalysis {

  public static void testPCA() {
    Vector x = new Vector(2, 0, 0);
    Vector y = new Vector(0, 1, 0);
    Vector z = new Vector(0, 0, 0.5f);
    int numberOfSamples = 100;
    PrincipalComponentAnalysis pca = new PrincipalComponentAnalysis();
    for (int i = 0; i < numberOfSamples; i++) {
      Vector data =
          x.multiply(Math.random()).add(y.multiply(Math.random()))
              .add(z.multiply(Math.random()));
      pca.add(data);
    }
    pca.applyPCA();
    System.out.println("centroid: " + pca.getCentroid());
    System.out.println("eigenvalues: " + pca.getEigenValues());
    for (int i = 0; i < 3; i++) {
      System.out.println("eigenvector " + i + ": " + pca.getEigenVector(i));
    }
  }
  
  public static void main(String [] args){
    TestPrincipleComponentAnalysis.testPCA();
  }
}
