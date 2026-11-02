package padroescomportamentais.integracao;

/** ICMS: alíquota única de 18% sobre a base de cálculo. */
public class CalculoIcms implements CalculoTributo {

    private static final double ALIQUOTA = 0.18;

    @Override
    public double calcular(double baseCalculo) {
        return baseCalculo * ALIQUOTA;
    }
}
