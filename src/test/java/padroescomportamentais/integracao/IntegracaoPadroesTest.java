package padroescomportamentais.integracao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Fluxo completo com os quatro padrões:
 * Singleton (fábrica) -> Factory (guia) -> Template Method (emitir) -> Strategy (cálculo).
 */
class IntegracaoPadroesTest {

    private static final double DELTA = 0.001;

    @Test
    void fluxoCompletoParaCadaTipoDeTributo() {
        FabricaGuias fabrica = FabricaGuias.getInstance();
        double[] esperado = {250.0, 900.0, 575.0}; // base 5000: ISS, ICMS, IRPF
        TipoTributo[] tipos = {TipoTributo.ISS, TipoTributo.ICMS, TipoTributo.IRPF};

        for (int i = 0; i < tipos.length; i++) {
            GuiaTributo guia = fabrica.criar(tipos[i]);
            String documento = guia.emitir("Contribuinte " + tipos[i], 5000.0);

            assertEquals(esperado[i], guia.getValorImposto(), DELTA);
            assertTrue(documento.startsWith("GUIA " + tipos[i].name()));
        }
    }

    @Test
    void mesmaBaseDeCalculoGeraImpostosDiferentesConformeAEstrategia() {
        FabricaGuias fabrica = FabricaGuias.getInstance();
        GuiaTributo iss = fabrica.criar(TipoTributo.ISS);
        GuiaTributo icms = fabrica.criar(TipoTributo.ICMS);
        iss.emitir("X", 1000.0);
        icms.emitir("X", 1000.0);
        assertNotEquals(iss.getValorImposto(), icms.getValorImposto(), DELTA);
    }

    @Test
    void duasReferenciasDaFabricaProduzemResultadosIguais() {
        GuiaTributo a = FabricaGuias.getInstance().criar(TipoTributo.IRPF);
        GuiaTributo b = FabricaGuias.getInstance().criar(TipoTributo.IRPF);
        assertEquals(a.emitir("Maria", 3000.0), b.emitir("Maria", 3000.0));
    }

    @Test
    void clienteTrabalhaApenasComAAbstracao() {
        // o cliente só conhece GuiaTributo e TipoTributo, nunca as classes concretas
        GuiaTributo guia = FabricaGuias.getInstance().criar(TipoTributo.ICMS);
        String documento = guia.emitir("Distribuidora", 1000.0);
        assertEquals("GUIA ICMS | Contribuinte: Distribuidora | Base: R$ 1000.00 | Imposto: R$ 180.00 | Vencimento: 10 dias",
                documento);
    }
}
