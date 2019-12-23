import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
* Classe que representa uma conexão.
* @author Nícolas Denadai, Nícolas Oliveira, Bruno Franchi
*/
public class Conexao {

	/**
	 * O servidor ao qual este cliente será conectado.
	 */
	private Parceiro servidor = null;
	
	/**
	 * A conexão;
	 */
	private Socket conexao;
	
	/**
	 * Construtor de classe.
	 * 
	 * @param ip O IP da conexão;
	 * @param port A porta da conexão.
	 * @param user O nome de usuário.
	 */
	public Conexao(String ip, int port, String user) throws Exception
	{
		System.out.println("Conectando...");
		
		conexao = new Socket (ip, port);
		
		ObjectOutputStream transmissor = null;
		transmissor =
				new ObjectOutputStream(
				conexao.getOutputStream());

		ObjectInputStream receptor = null;
		try
		{
		    receptor =
		    new ObjectInputStream(
		    conexao.getInputStream());
		}
		catch (Exception erro)
		{
			try
			{
				transmissor.close();
			}
			catch(Exception e) {}
			throw erro;
		}

		servidor = new Parceiro (conexao, receptor, transmissor);
		
		System.out.println("Conectado! \n");
		System.out.println("Aguardando um oponente...");
		ComunicadoComecar podeIr = (ComunicadoComecar)servidor.envie();
		System.out.println("recebeu");

		try
		{
			servidor.receba(new PedidoDeNome(user));		
		}
		catch(Exception ex)
		{
		    throw new Exception("Este nome já está em uso");
		}
		
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		System.out.println(" ~~ Tetris ~~ \n");
		System.out.println("Usuário: " + user);
	}

	/**
	 * Retorna o servidor.
	 * @return O servidor.
	 */
	public Parceiro getServidor()
	{
		return servidor;
	}
}
