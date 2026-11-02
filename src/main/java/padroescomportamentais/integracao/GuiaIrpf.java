package padroescomportamentais.integracao;

public class GuiaIrpf extends GuiaTributo {

    @Override
    protected CalculoTributo criarCalculo() {
        return new CalculoIrpf();
    }

    @Override
    protected String getNomeTributo() {
        return "IRPF";
    }

}
