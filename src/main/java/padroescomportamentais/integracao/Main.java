package padroescomportamentais.integracao;

public class Main {

    public static void main(String[] args) {
        FabricaGuias fabrica = FabricaGuias.getInstance();                  // Singleton

        GuiaTributo iss = fabrica.criar(TipoTributo.ISS);                    // Factory
        GuiaTributo icms = fabrica.criar(TipoTributo.ICMS);
        GuiaTributo irpf = fabrica.criar(TipoTributo.IRPF);

        System.out.println(iss.emitir("Padaria Pão Quente Ltda", 10000.0));  // Template + Strategy
        System.out.println(icms.emitir("Distribuidora Alfa S.A.", 25000.0));
        System.out.println(irpf.emitir("Maria Souza", 5000.0));
    }
}
