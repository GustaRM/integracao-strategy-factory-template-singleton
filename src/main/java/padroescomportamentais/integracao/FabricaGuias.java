package padroescomportamentais.integracao;

import java.util.Objects;

/**
 * Padrão SINGLETON: existe uma única fábrica de guias na aplicação.
 * (Mesmo estilo dos outros exemplos: construtor privado + instância estática.)
 *
 * Também centraliza a escolha da guia a criar a partir do TipoTributo,
 * isolando o cliente das classes concretas.
 */
public class FabricaGuias {

    private static final FabricaGuias instance = new FabricaGuias();

    private FabricaGuias() {}

    public static FabricaGuias getInstance() {
        return instance;
    }

    public GuiaTributo criar(TipoTributo tipo) {
        Objects.requireNonNull(tipo, "O tipo de tributo é obrigatório");
        switch (tipo) {
            case ISS:
                return new GuiaIss();
            case ICMS:
                return new GuiaIcms();
            case IRPF:
                return new GuiaIrpf();
            default:
                throw new IllegalArgumentException("Tipo de tributo não suportado: " + tipo);
        }
    }
}
