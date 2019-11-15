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
			System.err.println("Valores inv�lidos");
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

		String nome = null;
		try
		{
			System.out.println("Digite seu nome:\n");
			nome = Teclado.getUmString();
			servidor.receba(new PedidoDeNome(nome));
		}
		catch(Exception ex)
		{
		    System.err.println ("Nome inválido!\n");
		    return;
		}

		char opcao = ' ';
		do
		{
			System.out.println("A = somar pontos/B = mostrar pontos/ Z = sair");
			try
		    {
				opcao = Character.toUpperCase(Teclado.getUmChar());
		    }
		    catch (Exception erro)
		    {
				System.err.println ("Opcao invalida!\n");
				continue;
		    }
		   if ("ABZ".indexOf(opcao)==-1)
		   {
				System.err.println ("Opcao invalida!\n");
				continue;
		   }

		   try
			{
				if ("AB".indexOf(opcao)!=-1)
				{
					switch (opcao)
					{
						case 'A':
							servidor.receba(new PedidoParaAdicionarPontos(50.0));
							//PedidoDePontuacao pontosA = (PedidoDePontuacao) servidor.envie();		//erro
							pontos = pontos + (int)pontosA.getPontuacao();
							break;
						case 'B':
							System.out.println(pontos);
							break;
					}
				}
				else if(opcao == 'Z')
				{
					//servidor.receba(new PedidoParaSair());
					System.out.println("Saindo...");
				}
			}
			catch (Exception erro)
			{
				System.err.println(erro);
				/*
				System.err.println ("Erro de comunicacao com o servidor;");
				System.err.println ("Tente novamente!");
				System.err.println ("Caso o erro persista, termine o programa");
				System.err.println ("e volte a tentar mais tarde!");*/
			}
		}
		while (opcao != 'Z');
	}
}