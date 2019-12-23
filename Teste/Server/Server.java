import java.util.*;
import java.net.*;

/**
 * Classe que contém o método main. <br>
 * Executa e gerencia o servidor.
 * @author Nícolas Denadai, Nícolas Oliveira, Bruno Franchi
 *
 */
public class Server
{
	public static void main(String[] args)
	{
		System.out.println("Indique a porta desejada:");
		String porta = null;
		try
		{
			porta = Teclado.getUmString();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println("Valor inv�lido");
			return;
		}
		ArrayList<Parceiro> jogadores = new ArrayList<Parceiro>();

		AceitadoraDeConexao aceitadoraDeConexao = null;
		try
		{
			aceitadoraDeConexao = new AceitadoraDeConexao(porta, jogadores);
			try
			{
				System.out.println("Ip para jogo: "+InetAddress.getLocalHost().getHostAddress());
			}
			catch(Exception e)
			{}
			aceitadoraDeConexao.start();
		}
		catch(Exception ex)
		{
			System.err.println("Escolha uma porta livre e apropriada para o uso!\n");
			return;
		}

		for(;;)
		{
			System.out.println ("O servidor esta ativo! Para desativa-lo,");
			System.out.println ("use o comando \"desativar\"\n");

            String comando = null;
            try
            {
				comando = Teclado.getUmString();
			}
			catch(Exception ex)
			{}

			if(comando.toLowerCase().equals("desativar"))
			{
				synchronized(jogadores)
				{
					for(Parceiro jogador: jogadores)
					{
						ComunicadoDeDesligamento com = new ComunicadoDeDesligamento();
						try
						{
							jogador.receba(com);
							jogador.adeus();
						}
						catch(Exception ex)
						{}
					}
				}
				System.out.println("O servidor foi desativado!\n");
				System.exit(0);
			}
			else
				System.out.println("Comando inv�lido!\n");
		}
	}
}