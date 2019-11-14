public class PedidoDePontuacao extends Comunicado
{
    private double pontuacao;

    public PedidoDePontuacao (double pontuacao)
    {
        this.pontuacao = pontuacao;
    }

    public double getPontuacao ()    
    {
        return this.pontuacao;
    }
}