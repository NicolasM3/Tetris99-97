import java.net.*;
import java.io.*;

public class Cliente
{
	public static void main (String[] args)
	{
		int pontos = 0;

		String host = null;
		int porta = 0;
		try
		{
			System.out.println("Ip:");
			host = Teclado.getUmString();
			System.out.println("Porta:");
			porta = Teclado.getUmInt();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println("Valores inválidos");
			return;
		}

		Socket conexao=null;
		try
		{
		    conexao = new Socket (host, porta);
		}
		catch (Exception erro)
		{
		    erro.printStackTrace();

		    System.err.println ("Indique o servidor e a porta corretos!\n");
		    return;
		}

		ObjectOutputStream transmissor=null;
		try
		{
		    transmissor =
		    new ObjectOutputStream(
		    conexao.getOutputStream());
		}
		catch (Exception erro)
		{
		    erro.printStackTrace();

		    System.err.println ("Indique o servidor e a porta corretos!\n");
		    return;
		}

		ObjectInputStream receptor=null;
		try
		{
		    receptor =
		    new ObjectInputStream(
		    conexao.getInputStream());
		}
		catch (Exception erro)
		{
		    erro.printStackTrace();
		    System.err.println ("Indique o servidor e a porta corretos!\n");
		    return;
		}

		Parceiro servidor=null;
		try
		{
		    servidor = new Parceiro (conexao, receptor, transmissor);
		}
		catch (Exception erro)
		{
		    erro.printStackTrace();
		    System.err.println ("Indique o servidor e a porta corretos!\n");
		    return;
		}
		for(;;)
		{

			String opcao = Teclado.getUmString();

			if(opcao == "a")
			{

				try {
					servidor.receba(new PedidoParaAdicionarPontos(50));
					//System.out.println("pontos adicionado");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1
					.printStackTrace();
				}

			}
			else
			{
				try
				{
					PedidoDePontuacao pontosA = (PedidoDePontuacao) servidor.envie();
					pontos = pontos + (int)pontosA.getPontuacao();
				}
				catch(Exception ex)
				{
					System.err.println("Erro de conexão com o servidor");
				}
			}
			System.out.println(pontos);
		}
	}
}