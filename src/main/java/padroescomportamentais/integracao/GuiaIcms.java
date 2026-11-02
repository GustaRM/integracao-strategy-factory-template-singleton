package padroescomportamentais.integracao;

public class GuiaIcms extends GuiaTributo {

    @Override
    protected CalculoTributo criarCalculo() {
        return new CalculoIcms();
    }

    @Override
    protected String getNomeTributo() {
        return "ICMS";
    }

    @Override
    protected int getPrazoVencimentoDias() {
        return 10;
    }

}
