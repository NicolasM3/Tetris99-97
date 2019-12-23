import java.net.*;
import java.util.*;

/**
 * A classe {@code AceitadoraDeConexao} recebe pedidos de conexão
 * e os aceita, contabilizando a quantidade de jogadores.
 * @author Nícolas Denadai, Nícolas Oliveira, Bruno Franchi
 *
 */
public class AceitadoraDeConexao extends Thread
{
    /**
     * A porta que será usada caso nenhuma tenha
     * sido escolhida.
     */
	private static final int PORTA_PADRAO = 3000;

	/**
	 * O pedido de conexão que foi enviado pelo
	 * cliente.
	 */
    private ServerSocket        pedido;
    
    /**
     * A lista de clientes conectados.
     */
    private ArrayList<Parceiro> jogadores;

    /**
     * Construtor de classe.
     * @param escolha A porta da conexão.
     * @param jogadores A lista de clientes.
     * @throws Exception Caso a porta seja inválida.
     * @throws Exception Caso a lista esteja ausente.
     */
    public AceitadoraDeConexao
    (String escolha, ArrayList<Parceiro> jogadores)
    throws Exception
    {
        int porta = AceitadoraDeConexao.PORTA_PADRAO;

        if (escolha!=null)
        {
			porta = Integer.parseInt(escolha);
		}

       	try
        {
            this.pedido = new ServerSocket (porta);
        }
        catch (Exception erro)
        {
            throw new Exception ("Porta invalida");
		}

        if (jogadores==null)
            throw new Exception ("Lista de usuarios ausente");

        this.jogadores = jogadores;
    }

    /**
     * Inicia essa instância da classe.
     */
    public void run ()
    {
        for(;;)
        {
            Socket conexao=null;
            try
            {
                conexao = this.pedido.accept();
            }
            catch (Exception erro)
            {
                continue;
            }

            SupervisoraDeConexao supervisoraDeConexao=null;
            try
            {
                supervisoraDeConexao =
                new SupervisoraDeConexao (conexao, jogadores);
            }
            catch (Exception erro)
            {} // sei que passei parametros corretos para o construtor
            supervisoraDeConexao.start();
        }
    }
}
