import java.io.*;
import java.net.*;

/**
 * Classe que representa o "outro lado" da conexão. <br>
 * Nesse caso, representa um cliente conectado ao servidor.
 * @author Nícolas Denadai, Nícolas Oliveira, Bruno Franchi
 *
 */
public class Parceiro
{
	/**
	 * A conexão.
	 */
    private Socket             conexao;
    
    /**
     * O objeto receptor de dados. 
     */
    private ObjectInputStream  receptor;
    
    /**
     * O objeto transmissor de dados;
     */
    private ObjectOutputStream transmissor;
    
    /**
     * A pontuação do Parceiro.
     */
    private double pontos;
    
    /**
     * O nome do Parceiro.
     */
    private String nome;

    
    /**
     * Construtor de classe.
     * @param conexao A conexão a ser usada.
     * @param receptor O objeto receptor a ser usado.
     * @param transmissor O objeto transmissor a ser usado.
     * @throws Exception Caso algum parâmetro esteja ausente.
     */
    public Parceiro (Socket             conexao,
                     ObjectInputStream  receptor,
                     ObjectOutputStream transmissor)
                     throws Exception // se parametro nulos
    {
        if (conexao==null)
            throw new Exception ("Conexao ausente");

        if (receptor==null)
            throw new Exception ("Receptor ausente");

        if (transmissor==null)
            throw new Exception ("Transmissor ausente");

        this.conexao     = conexao;
        this.receptor    = receptor;
        this.transmissor = transmissor;
        this.pontos = 0;
    }

    /**
     * Envia uma informação para esse parceiro
     * @param x A informação.
     * @throws Exception Caso o envio não seja finalizado.
     */
    public void receba (Comunicado x) throws Exception
    {
        try
        {
            this.transmissor.writeObject (x);
            this.transmissor.flush       ();
        }
        catch (IOException erro)
        {
            throw new Exception ("Erro de transmissao");
        }
    }

    /**
     * Espera até esse parceiro enviar uma informação.
     * @return A informação recebida.
     * @throws Exception Caso a recepção não seja finalizada.
     */
    public Comunicado envie () throws Exception
    {
        try
        {
            return (Comunicado)this.receptor.readObject();
        }
        catch (Exception erro)
        {
            throw new Exception ("Erro de recepcao");
        }
    }

    /**
     * Define o nome do parceiro.
     * @param nome O nome.
     * @throws Exception Caso o nome esteja ausente.
     */
	public void setNome(String nome) throws Exception
	{
		if(nome == null)
			throw new Exception("Nome inv�lido!");
		this.nome = nome;
	} 				

	/**
	 * Retorna o nome do parceiro.
	 * @return O nome do parceiro.
	 */
	public String getNome()
	{
		return this.nome;
    }
    
	/**
	 * Retorna a pontuação do parceiro.
	 * @return A pontuação do parceiro.
	 */
    public double getPontos()
    {
        return pontos;
    }

    /**
     * Define a pontuação do parceiro.
     * @param pts A pontuação do parceiro.
     */
    public void setPontos(double pts)
    {
        this.pontos += pts;
    }

    /**
     * Desconecta esse parceiro.
     * @throws Exception Caso a desconexão não seja finalizada.
     */
    public void adeus () throws Exception
    {
        try
        {
            this.transmissor.close();
            this.receptor   .close();
            this.conexao    .close();
        }
        catch (Exception erro)
        {
            throw new Exception ("Erro de desconexao");
        }
    }
}