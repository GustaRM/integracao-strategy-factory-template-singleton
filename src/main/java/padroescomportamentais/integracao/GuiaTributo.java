package padroescomportamentais.integracao;

import java.util.Locale;

/**
 * Classe abstrata que une dois padrões:
 *
 *  - TEMPLATE METHOD: emitir() é final e fixa a sequência de passos
 *    (validar -> criar cálculo -> calcular -> arredondar -> gerar documento).
 *  - FACTORY METHOD: criarCalculo() é o método-fábrica que cada subclasse
 *    implementa para decidir qual STRATEGY de cálculo será usada.
 */
public abstract class GuiaTributo {

    private String contribuinte;
    private double baseCalculo;
    private double valorImposto;

    // ---------- método-template ----------

    public final String emitir(String contribuinte, double baseCalculo) {
        validar(contribuinte, baseCalculo);                    // passo concreto
        CalculoTributo calculo = criarCalculo();               // passo abstrato (Factory Method)
        double imposto = calculo.calcular(baseCalculo);        // uso da Strategy
        this.contribuinte = contribuinte;
        this.baseCalculo = baseCalculo;
        this.valorImposto = arredondar(imposto);               // passo concreto
        return gerarDocumento();                               // passo concreto
    }

    // ---------- passos concretos ----------

    private void validar(String contribuinte, double baseCalculo) {
        if (contribuinte == null || contribuinte.trim().isEmpty()) {
            throw new IllegalArgumentException("O contribuinte é obrigatório");
        }
        if (baseCalculo <= 0) {
            throw new IllegalArgumentException("A base de cálculo deve ser positiva");
        }
    }

    private double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    private String gerarDocumento() {
        return String.format(Locale.ROOT,
                "GUIA %s | Contribuinte: %s | Base: R$ %.2f | Imposto: R$ %.2f | Vencimento: %d dias",
                getNomeTributo(), contribuinte, baseCalculo, valorImposto, getPrazoVencimentoDias());
    }

    // ---------- passos abstratos ----------

    /** Factory Method: cada subclasse decide qual estratégia de cálculo criar. */
    protected abstract CalculoTributo criarCalculo();

    protected abstract String getNomeTributo();

    // ---------- gancho (hook) ----------

    protected int getPrazoVencimentoDias() {
        return 30;
    }

    // ---------- consultas ----------

    public String getContribuinte() {
        return contribuinte;
    }

    public double getBaseCalculo() {
        return baseCalculo;
    }

    public double getValorImposto() {
        return valorImposto;
    }
}
