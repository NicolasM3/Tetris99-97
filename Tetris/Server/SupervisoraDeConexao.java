import java.io.*;
import java.net.*;
import java.util.*;

public class SupervisoraDeConexao extends Thread
{
    private static double       pontos;
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
            for(;;)
            {
                Comunicado comunicado = this.jogador.envie();
                
                if(comunicado==null)
                    return;


                if(comunicado instanceof PedidoDeNome)
                    this.jogador.setNome(((PedidoDeNome)comunicado).getNome());
                
                if(comunicado instanceof PedidoParaAdicionarPontos)
                {
                    PedidoParaAdicionarPontos a = (PedidoParaAdicionarPontos) comunicado;
                    double ponto = a.getPontosParaAdicionar();
                    pontos = pontos + ponto;
                    jogador.receba(new PedidoDePontuacao(pontos));
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
		return pontos + " ";
	}
}
