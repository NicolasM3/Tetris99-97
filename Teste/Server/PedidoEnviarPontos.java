/**
 * Classe que contém o nome escolhido por um parceiro.
 * @author Nícolas Denadai, Nícolas Oliveira, Bruno Franchi
 *
 */
public class PedidoEnviarPontos extends Comunicado
{
	/**
	 * UID da versão serial.
	 */
	private static final long serialVersionUID = 1095947842468663545L;
	
	/**
	 * O nome escolhido.
	 */
	private int pontos;

	/**
	 * Construtor de classe.
	 * @param pontos os Pontos a serem enviados.
	 */
	public PedidoEnviarPontos(int pontos)
	{
		this.pontos = pontos;
	}

	/**
	 * Retorna o nome escolhido.
	 * @return O nome escolhido.
	 */
	public int getPontos()
	{
		return this.pontos;
	}
}