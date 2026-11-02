package padroescomportamentais.integracao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** STRATEGY: cada algoritmo testado isoladamente. */
class CalculoTributoTest {

    private static final double DELTA = 0.001;

    private final CalculoTributo iss = new CalculoIss();
    private final CalculoTributo icms = new CalculoIcms();
    private final CalculoTributo irpf = new CalculoIrpf();

    @Test
    void issAplicaCincoPorCento() {
        assertEquals(50.0, iss.calcular(1000.0), DELTA);
        assertEquals(500.0, iss.calcular(10000.0), DELTA);
    }

    @Test
    void icmsAplicaDezoitoPorCento() {
        assertEquals(180.0, icms.calcular(1000.0), DELTA);
        assertEquals(4500.0, icms.calcular(25000.0), DELTA);
    }

    @Test
    void irpfIsentoAteDoisMil() {
        assertEquals(0.0, irpf.calcular(1000.0), DELTA);
        assertEquals(0.0, irpf.calcular(2000.0), DELTA);
    }

    @Test
    void irpfSegundaFaixaQuinzePorCentoSobreOExcedente() {
        assertEquals(0.0015, irpf.calcular(2000.01), 0.0001);
        assertEquals(150.0, irpf.calcular(3000.0), DELTA);
        assertEquals(300.0, irpf.calcular(4000.0), DELTA);
    }

    @Test
    void irpfTerceiraFaixaSomaFaixaAnteriorMaisVinteESeteVirgulaCinco() {
        assertEquals(575.0, irpf.calcular(5000.0), DELTA);
        assertEquals(300.0 + 6000.0 * 0.275, irpf.calcular(10000.0), DELTA);
    }

    @Test
    void irpfEhContinuoNasFronteirasDasFaixas() {
        assertEquals(irpf.calcular(2000.0), irpf.calcular(2000.01), 0.01);
        assertEquals(irpf.calcular(4000.0), irpf.calcular(4000.01), 0.01);
    }

    @Test
    void estrategiasSaoIntercambiaveisPelaInterface() {
        CalculoTributo[] todas = {iss, icms, irpf};
        double[] esperado = {250.0, 900.0, 575.0}; // base 5000
        for (int i = 0; i < todas.length; i++) {
            assertEquals(esperado[i], todas[i].calcular(5000.0), DELTA);
        }
    }

    @Test
    void estrategiasNaoGuardamEstado() {
        double primeira = irpf.calcular(3000.0);
        irpf.calcular(9000.0);
        assertEquals(primeira, irpf.calcular(3000.0));
    }
}
