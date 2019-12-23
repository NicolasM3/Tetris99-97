/**
 * Classe que contém o nome escolhido por um parceiro.
 * @author Nícolas Denadai, Nícolas Oliveira, Bruno Franchi
 *
 */
public class PedidoDeLixo extends Comunicado
{
	/**
	 * UID da versão serial.
	 */
	private static final long serialVersionUID = 1095947842468663545L;
	
	/**
	 * O nome escolhido.
	 */
	private int lixo;

	/**
	 * Construtor de classe.
	 * @param pontos os Pontos a serem enviados.
	 */
	public PedidoDeLixo(int linhasLixo)
	{
		this.lixo = linhasLixo;
	}

	/**
	 * Retorna o nome escolhido.
	 * @return O nome escolhido.
	 */
	public double getLixo()
	{
		return this.lixo;
	}
}