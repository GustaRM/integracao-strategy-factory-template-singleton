package padroescomportamentais.integracao;

public class GuiaIss extends GuiaTributo {

    @Override
    protected CalculoTributo criarCalculo() {
        return new CalculoIss();
    }

    @Override
    protected String getNomeTributo() {
        return "ISS";
    }

}
