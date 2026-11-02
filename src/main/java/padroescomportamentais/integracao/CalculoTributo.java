package padroescomportamentais.integracao;

/** Padrão STRATEGY: família de algoritmos de cálculo de tributo, intercambiáveis. */
public interface CalculoTributo {

    double calcular(double baseCalculo);
}
