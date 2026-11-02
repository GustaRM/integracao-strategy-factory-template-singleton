package padroescomportamentais.integracao;

/** ISS: alíquota única de 5% sobre a base de cálculo. */
public class CalculoIss implements CalculoTributo {

    private static final double ALIQUOTA = 0.05;

    @Override
    public double calcular(double baseCalculo) {
        return baseCalculo * ALIQUOTA;
    }
}
