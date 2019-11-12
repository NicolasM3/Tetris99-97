public class PedidoDeAdicao extends Comunicado
{
    private double valorParaAdicionar;
    
    public PedidoDeAdicao (double valorParaAdicionar)
    {
        this.valorParaAdicionar = valorParaAdicionar;
    }
    
    public double getValorParaAdicionar ()
    {
        return this.valorParaAdicionar;
    }
}
