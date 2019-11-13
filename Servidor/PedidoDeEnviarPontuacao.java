public class PedidoDeEnviarPontuacao extends Comunicado
{
    private double valorParaAdicionar;
    
    public PedidoDeEnviarPontuacao (double valorParaAdicionar)
    {
        this.valorParaAdicionar = valorParaAdicionar;
    }
    
    public double getValorParaEnviar ()
    {
        return this.valorParaAdicionar;
    }
}
