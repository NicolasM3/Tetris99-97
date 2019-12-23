import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Classe que gerencia conexões de clientes e partidas.
 * @author Nícolas Denadai, Nícolas Oliveira, Bruno Franchi
 *
 */
public class SupervisoraDeConexao extends Thread
{
	/**
	 * O cliente conectado.
	 */
    private Parceiro            jogador;
    
    /**
     * A conexão usada.
     */
    private Socket              conexao;
    
    /**
     * A lista de conexões.
     */
    private ArrayList<Parceiro> jogadores;
    
    /**
     * A quantidade de pontuações já recebidas nessa partida.
     */
    private static int nmrJogadas = 0;
    
    /**
     * A quantidade de jogadores conectados.
     */
    private static int qtdJogadores = 0;

    /**
     * Construtor de classe.
     * @param conexao A conexão a ser utilizada.
     * @param jogadores A lista de jogadores.
     * @throws Exception Caso haja parâmetros ausentes.
     */
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
    }

    /**
     * Inicia essa instância da classe.
     */
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
            {}

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
            synchronized (this.jogadores)
            {
                this.jogadores.add (this.jogador);
                SupervisoraDeConexao.qtdJogadores++;
                if(SupervisoraDeConexao.qtdJogadores == 2)
                	for(Parceiro jogador: this.jogadores)
                	{
						jogador.receba(new ComunicadoComecar(true));
					}
            }
            
            for(;;)
            {
                Comunicado comunicado = this.jogador.envie();
                
                if(comunicado==null)
                    return;
                
                if(comunicado instanceof PedidoDeNome) {
                	this.jogador.setNome(((PedidoDeNome)comunicado).getNome());
                	System.out.println("[" + ((PedidoDeNome)comunicado).getNome() + "] CONECTOU-SE");
                }

                if(comunicado instanceof PedidoEnviarPontos)
                {
                    this.jogador.setPontos(((PedidoEnviarPontos)comunicado).getPontos());
                    for(Parceiro jogador:this.jogadores)
                    {
                        if(jogador != this.jogador)
                            jogador.receba(new PedidoDeLixo(((PedidoEnviarPontos)comunicado).getPontos()));
                    }
                }

                /*if(comunicado instanceof PedidoDePontuacao) {
                	jogador.setPontos(((PedidoDePontuacao) comunicado).getPontuacao());
                	nmrJogadas++;
                	if (nmrJogadas == 2) {
                		System.out.println("GANHADOR: [" + quemGanhou() + "]");
                		for(Parceiro jogador:this.jogadores)
                        {
                            String nome = quemGanhou();
                            jogador.receba(new Resultado(nome));
                        } 
                	}
                }*/
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
            {}

            return;
        }
    }

    /**
     * Verifica quem foi o vencedor da partida.
     * @return O nome do vencedor.
     */
    private String quemGanhou()
    {
    	if (this.jogadores.get(0).getPontos() > this.jogadores.get(1).getPontos()) {
    		return this.jogadores.get(0).getNome();
    	} else
    	if (this.jogadores.get(0).getPontos() < this.jogadores.get(1).getPontos()) {
    		return this.jogadores.get(1).getNome();
    	} else {
    		return "EMPATE";
    	}
	}
}
