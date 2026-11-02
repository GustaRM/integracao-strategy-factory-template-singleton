package padroescomportamentais.integracao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** SINGLETON + FACTORY: a fábrica única e a criação das guias. */
class FabricaGuiasTest {

    private final FabricaGuias fabrica = FabricaGuias.getInstance();

    // ---------- Singleton ----------

    @Test
    void getInstanceSempreRetornaAMesmaFabrica() {
        assertSame(FabricaGuias.getInstance(), FabricaGuias.getInstance());
    }

    @Test
    void construtorEhPrivado() throws NoSuchMethodException {
        assertTrue(java.lang.reflect.Modifier.isPrivate(
                FabricaGuias.class.getDeclaredConstructor().getModifiers()));
    }

    // ---------- Factory ----------

    @Test
    void criaGuiaDoTipoCorreto() {
        assertEquals(GuiaIss.class, fabrica.criar(TipoTributo.ISS).getClass());
        assertEquals(GuiaIcms.class, fabrica.criar(TipoTributo.ICMS).getClass());
        assertEquals(GuiaIrpf.class, fabrica.criar(TipoTributo.IRPF).getClass());
    }

    @Test
    void cadaChamadaRetornaUmaNovaGuia() {
        assertNotSame(fabrica.criar(TipoTributo.ISS), fabrica.criar(TipoTributo.ISS));
    }

    @Test
    void guiasCriadasSaoIndependentes() {
        GuiaTributo a = fabrica.criar(TipoTributo.ISS);
        GuiaTributo b = fabrica.criar(TipoTributo.ISS);
        a.emitir("Primeiro", 1000.0);
        assertNull(b.getContribuinte());
    }

    @Test
    void todosOsTiposDoEnumSaoSuportados() {
        for (TipoTributo tipo : TipoTributo.values()) {
            assertNotNull(fabrica.criar(tipo), "Tipo sem guia: " + tipo);
        }
    }

    @Test
    void tipoNuloLancaExcecao() {
        assertThrows(NullPointerException.class, () -> fabrica.criar(null));
    }
}
