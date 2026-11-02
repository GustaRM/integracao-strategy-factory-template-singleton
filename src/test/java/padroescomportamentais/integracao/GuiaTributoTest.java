package padroescomportamentais.integracao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/** TEMPLATE METHOD + FACTORY METHOD: sequência de passos e escolha da estratégia. */
class GuiaTributoTest {

    private static final double DELTA = 0.001;

    /** Subclasse de teste que registra a ordem dos passos do template. */
    private static class GuiaEspia extends GuiaTributo {
        final List<String> chamadas = new ArrayList<>();
        private final double impostoRetornado;

        GuiaEspia(double impostoRetornado) {
            this.impostoRetornado = impostoRetornado;
        }

        @Override
        protected CalculoTributo criarCalculo() {
            chamadas.add("criarCalculo");
            return base -> {
                chamadas.add("calcular");
                return impostoRetornado;
            };
        }

        @Override
        protected String getNomeTributo() {
            chamadas.add("nome");
            return "TESTE";
        }
    }

    // ---------- documento emitido por tipo ----------

    @Test
    void emiteGuiaDeIss() {
        GuiaTributo guia = new GuiaIss();
        assertEquals("GUIA ISS | Contribuinte: Padaria Pão Quente Ltda | Base: R$ 10000.00 | Imposto: R$ 500.00 | Vencimento: 30 dias",
                guia.emitir("Padaria Pão Quente Ltda", 10000.0));
    }

    @Test
    void emiteGuiaDeIcmsComVencimentoDeDezDias() {
        GuiaTributo guia = new GuiaIcms();
        assertEquals("GUIA ICMS | Contribuinte: Distribuidora Alfa S.A. | Base: R$ 25000.00 | Imposto: R$ 4500.00 | Vencimento: 10 dias",
                guia.emitir("Distribuidora Alfa S.A.", 25000.0));
    }

    @Test
    void emiteGuiaDeIrpfProgressivo() {
        GuiaTributo guia = new GuiaIrpf();
        assertEquals("GUIA IRPF | Contribuinte: Maria Souza | Base: R$ 5000.00 | Imposto: R$ 575.00 | Vencimento: 30 dias",
                guia.emitir("Maria Souza", 5000.0));
    }

    @Test
    void guiaGuardaOsDadosDaEmissao() {
        GuiaTributo guia = new GuiaIss();
        guia.emitir("Fulano", 2000.0);
        assertEquals("Fulano", guia.getContribuinte());
        assertEquals(2000.0, guia.getBaseCalculo(), DELTA);
        assertEquals(100.0, guia.getValorImposto(), DELTA);
    }

    @Test
    void ganchoDePrazoTemPadraoDeTrintaDiasEIcmsSobrescreve() {
        assertTrue(new GuiaIss().emitir("A", 100.0).endsWith("Vencimento: 30 dias"));
        assertTrue(new GuiaIrpf().emitir("A", 5000.0).endsWith("Vencimento: 30 dias"));
        assertTrue(new GuiaIcms().emitir("A", 100.0).endsWith("Vencimento: 10 dias"));
    }

    @Test
    void emitirNovamenteSubstituiOsDadosAnteriores() {
        GuiaTributo guia = new GuiaIss();
        guia.emitir("Primeiro", 1000.0);
        guia.emitir("Segundo", 2000.0);
        assertEquals("Segundo", guia.getContribuinte());
        assertEquals(100.0, guia.getValorImposto(), DELTA);
    }

    // ---------- ordem dos passos do template ----------

    @Test
    void templateExecutaOsPassosNaOrdemCorreta() {
        GuiaEspia guia = new GuiaEspia(10.0);
        guia.emitir("Teste", 100.0);
        assertEquals(List.of("criarCalculo", "calcular", "nome"), guia.chamadas);
    }

    @Test
    void factoryMethodEhChamadoUmaVezPorEmissao() {
        GuiaEspia guia = new GuiaEspia(10.0);
        guia.emitir("Teste", 100.0);
        guia.emitir("Teste", 100.0);
        long criacoes = guia.chamadas.stream().filter("criarCalculo"::equals).count();
        assertEquals(2, criacoes);
    }

    @Test
    void subclasseDefineQualEstrategiaSeraUsada() {
        // o resultado vem da estratégia criada pelo Factory Method, não da base
        GuiaEspia guia = new GuiaEspia(42.0);
        guia.emitir("Teste", 100.0);
        assertEquals(42.0, guia.getValorImposto(), DELTA);
    }

    @Test
    void valorDoImpostoEhArredondadoParaCentavos() {
        GuiaEspia guia = new GuiaEspia(10.126);
        guia.emitir("Teste", 100.0);
        assertEquals(10.13, guia.getValorImposto(), 0.0001);
    }

    // ---------- Factory Method em cada subclasse ----------

    @Test
    void cadaGuiaCriaASuaEstrategia() {
        assertTrue(new GuiaIss().criarCalculo() instanceof CalculoIss);
        assertTrue(new GuiaIcms().criarCalculo() instanceof CalculoIcms);
        assertTrue(new GuiaIrpf().criarCalculo() instanceof CalculoIrpf);
    }

    // ---------- validações ----------

    @Test
    void contribuinteInvalidoLancaExcecao() {
        GuiaTributo guia = new GuiaIss();
        assertThrows(IllegalArgumentException.class, () -> guia.emitir(null, 100.0));
        assertThrows(IllegalArgumentException.class, () -> guia.emitir("", 100.0));
        assertThrows(IllegalArgumentException.class, () -> guia.emitir("   ", 100.0));
    }

    @Test
    void baseDeCalculoInvalidaLancaExcecao() {
        GuiaTributo guia = new GuiaIss();
        assertThrows(IllegalArgumentException.class, () -> guia.emitir("Fulano", 0.0));
        assertThrows(IllegalArgumentException.class, () -> guia.emitir("Fulano", -10.0));
    }

    @Test
    void validacaoOcorreAntesDeCriarAEstrategiaENaoAlteraEstado() {
        GuiaEspia guia = new GuiaEspia(10.0);
        assertThrows(IllegalArgumentException.class, () -> guia.emitir("Teste", -1.0));
        assertTrue(guia.chamadas.isEmpty());
        assertEquals(0.0, guia.getValorImposto(), DELTA);
        assertNull(guia.getContribuinte());
    }
}
