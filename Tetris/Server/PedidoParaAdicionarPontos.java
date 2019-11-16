public class PedidoParaAdicionarPontos extends Comunicado
{
    private double pontosAdicionar;

    public PedidoParaAdicionarPontos(double pontosAdicionar)
    {
        this.pontosAdicionar = pontosAdicionar;
    }

    public double getPontosParaAdicionar()    
    {
        return this.pontosAdicionar;
    }
}