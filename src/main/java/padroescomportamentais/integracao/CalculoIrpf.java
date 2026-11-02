package padroescomportamentais.integracao;

/**
 * IRPF: tabela progressiva SIMPLIFICADA (didática, não é a tabela oficial).
 *   até 2.000,00          -> isento
 *   de 2.000,01 a 4.000   -> 15% sobre o que exceder 2.000
 *   acima de 4.000        -> 300,00 + 27,5% sobre o que exceder 4.000
 */
public class CalculoIrpf implements CalculoTributo {

    @Override
    public double calcular(double baseCalculo) {
        if (baseCalculo <= 2000.0) {
            return 0.0;
        }
        if (baseCalculo <= 4000.0) {
            return (baseCalculo - 2000.0) * 0.15;
        }
        return 300.0 + (baseCalculo - 4000.0) * 0.275;
    }
}
