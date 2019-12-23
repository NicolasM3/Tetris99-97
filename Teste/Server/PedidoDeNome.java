/**
 * Classe que contém o nome escolhido por um parceiro.
 * @author Nícolas Denadai, Nícolas Oliveira, Bruno Franchi
 *
 */
public class PedidoDeNome extends Comunicado
{
	/**
	 * UID da versão serial.
	 */
	private static final long serialVersionUID = 1095947842468663545L;
	
	/**
	 * O nome escolhido.
	 */
	private String nome;

	/**
	 * Construtor de classe.
	 * @param nome O nome escolhido.
	 */
	public PedidoDeNome(String nome)
	{
		this.nome = nome;
	}

	/**
	 * Retorna o nome escolhido.
	 * @return O nome escolhido.
	 */
	public String getNome()
	{
		return this.nome;
	}
}