
import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorSpecies;

//--source 21 --add-modules jdk.incubator.vector --enable-preview
interface App {

  VectorSpecies<Float> SPECIES = FloatVector.SPECIES_PREFERRED;

  static void vectorCalculation(float[] a, float[] b, float[] c) {
    int upperBound = SPECIES.loopBound(a.length);
    for (int i = 0; i < upperBound; i += SPECIES.length()) {
      var va = FloatVector.fromArray(SPECIES, a, i);
      var vb = FloatVector.fromArray(SPECIES, b, i);
      var vc = va.mul(vb);
      vc.intoArray(c, i);
    }
  }

  static void calculation(float[] a, float[] b, float[] c) {
    for (int i = 0; i < a.length; i++) {
      c[i] = (a[i] * b[i]);
    }
  }

  // --add-modules jdk.incubator.vector
  static void main(String... args) {

    float[] a = { 1.0f, 2.0f, 3.0f, 4.0f };
    float[] b = { 5.0f, 6.0f, 7.0f, 8.0f };
    float[] result = new float[a.length];

    vectorCalculation(a, b, result);
    for (var f : result) {
      System.out.println(f);
    }
  }
}