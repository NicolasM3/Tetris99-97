import java.io.*;
import java.net.*;
import java.util.*;

public class SupervisoraDeConexao extends Thread
{
    private double              pontos;
    private Parceiro            jogador;
    private Socket              conexao;
    private ArrayList<Parceiro> jogadores;

    public SupervisoraDeConexao
    (Socket conexao, ArrayList<Parceiro> jogadores)
    throws Exception
    {
        if (conexao==null)
            throw new Exception ("Conexao ausente");

        if (jogadores==null)
            throw new Exception ("Usuarios ausentes");

        this.conexao  = conexao;
        this.jogadores = jogadores;
        this.pontos = 0;
    }

    public void run ()
    {
        ObjectInputStream receptor=null;
        try
        {
            receptor=
            new ObjectInputStream(
            this.conexao.getInputStream());
        }
        catch (Exception erro)
        {
            return;
        }

        ObjectOutputStream transmissor;
        try
        {
            transmissor =
            new ObjectOutputStream(
            this.conexao.getOutputStream());
        }
        catch (Exception erro)
        {
            try
            {
                receptor.close ();
            }
            catch (Exception falha)
            {} // so tentando fechar antes de acabar a thread

            return;
        }

        try
        {
            this.jogador =
            new Parceiro (this.conexao,
                          receptor,
                          transmissor);
        }
        catch (Exception erro)
        {} // sei que passei os parametros corretos

        try
        {
            /*synchronized (this.jogadores)
            {
                this.jogadores.add (this.jogador);
                this.qtdJogadores++;
                if(this.qtdJogadores == 2)
                	for(Parceiro jogador: this.jogadores)
                	{
						jogador.receba(new ComunicadoComecar(true));
					}
            }*/

            for(;;)
            {
                Comunicado comunicado = this.jogador.envie ();

                if(comunicado==null)
                    return;

                if(comunicado instanceof PedidoParaAdicionarPontos)
                {
                    PedidoParaAdicionarPontos a = (PedidoParaAdicionarPontos) comunicado;
                    double ponto = a.getPontosParaAdicionar();
                    //pontos = pontos + ponto;
                }
			}
        }
        catch (Exception erro)
        {
            try
            {
                transmissor.close ();
                receptor   .close ();
            }
            catch (Exception falha)
            {} // so tentando fechar antes de acabar a thread

            return;
        }
    }

    private String quemGanhou()
    {
		/*JogadaJogada jogada1 = this.jogadores.get(0).getJogada();
		Jogada jogada2 = this.jogadores.get(1).getJogada();

		int comp = jogada1.compareTo(jogada2);

		if(comp == 0)
			return "empate";
		if(comp > 0)
			return this.jogadores.get(0).getNome();*/
		return "a";
	}
}
