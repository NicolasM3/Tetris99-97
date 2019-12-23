/**
 * Comunicado para iniciar a partida.
 * @author Nícolas Denadai, Nícolas Oliveira, Bruno Franchi
 *
 */
public class ComunicadoComecar extends Comunicado
{
	/**
	 * UID da versão serial.
	 */
	private static final long serialVersionUID = -1741368386457253768L;
	/**
	 * Se pode iniciar a partida.
	 */
	private boolean podeIr;
	
	/**
	 * Construtor da classe.
	 * @param pode Se é para iniciar a partida.
	 */
	public ComunicadoComecar(boolean pode)
	{
		this.podeIr = pode;
	}
	
	/**
	 * Retorna se pode iniciar a partida.
	 * @return Se pode iniciar.
	 */
	public boolean getPodeComecar()
	{
		return this.podeIr;
	}
}