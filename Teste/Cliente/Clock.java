/** 
 * A classe Clock reponsável por rastrear números de ciclos.
 * É reponsável por rastrear tudo em torno dos ciclos que decorreram ao longo do jogo.
 * @author Nícolas Denadai, Nícolas Oliveira.
 * @since 2019
 */
public class Clock {
	
	/**
	 * O número de milissegundos que compõem um ciclo.
	 */
	private float millisPerCycle;
	
	/**
	 * A última vez que o relógio foi atualizado, usado para calcular o delta
	 * (diferença) de tempo entre dois updates.
	 */
	private long lastUpdate;
	
	/**
	 * A quantidade de ciclos decorridos.
	 */
	private int elapsedCycles;
	
	/**
	 * A quantidade de tempo excedente em relação ao próximo ciclo.
	 */
	private float excessCycles;
	
	/**
	 * Se o relógio está pausado. Caso esteja, os ciclos não decorrem.
	 */
	private boolean isPaused;
	
	/**
	 * Cria um novo relógio.
	 * @param cyclesPerSecond Números de ciclos por segundo.
	 */
	public Clock(float cyclesPerSecond) {
		setCyclesPerSecond(cyclesPerSecond);										
		reset();
	}
	
	/**
	 * Define o número de ciclos por segundo.
	 * @param cyclesPerSecond Número de ciclos por segundo.
	 */
	public void setCyclesPerSecond(float cyclesPerSecond) {
		this.millisPerCycle = (1.0f / cyclesPerSecond) * 1000;
	}
	
	/**
	 * Reseta o relógio, definido todos os parâmetros como seus valores iniciais.
	 */
	public void reset() {
		this.elapsedCycles = 0;
		this.excessCycles = 0.0f;
		this.lastUpdate = getCurrentTime();
		this.isPaused = false;
	}
	
	/**
	 * Atualiza o relógio. <br>
	 * O número de ciclos decorridos, bem como o excesso de ciclo, 
	 * serão calculados apenas se o relógio não estiver em pausa.
	 */
	public void update() {
		// Obtém o tempo atual e calcula o tempo delta.
		long currUpdate = getCurrentTime();
		float delta = (float)(currUpdate - lastUpdate) + excessCycles;
		
		// Atualiza o número de ticks decorridos e excessivos se não estivermos em pausa.
		if(!isPaused) {
			this.elapsedCycles += (int)Math.floor(delta / millisPerCycle);
			this.excessCycles = delta % millisPerCycle;
		}
		
		// Define o horário da última atualização para o próximo ciclo de atualização.
		this.lastUpdate = currUpdate;
	}
	
	/**
	 * Pausa ou despausa o relógio. 
	 * Enquanto pausado, um relógio não atualizará ciclos decorridos 
	 * ou excesso de ciclos.
	 * @param paused Se o relógio deve pausar ou não (continuar).
	 */
	public void setPaused(boolean paused) {
		this.isPaused = paused;
	}
	
	/**
	 * Verifica se o relógio está pausado.
	 * @return Se o relógio está pausado.
	 */
	public boolean isPaused() {
		return isPaused;
	}
	
	/**
	 * Verifica se um ciclo passou para este relógio.
	 * O número de ciclos decorridos será diminuído em um.
	 * @return Se um ciclo decorreu ou não.
	 * @see peekElapsedCycle
	 */
	public boolean hasElapsedCycle() {
		if(elapsedCycles > 0) {
			this.elapsedCycles--;
			return true;
		}
		return false;
	}
	
	/**
	 * Verifica se um ciclo já passou para este relógio.
	 * Igual ao hasElapsedCycle, só que número de ciclos decorridos não será diminuído em um.
	 * @return Se um ciclo decorreu ou não.
	 * @see hasElapsedCycle
	 */
	public boolean peekElapsedCycle() {
		return (elapsedCycles > 0);
	}
	
	/**
	 * Calcula o tempo atual.
	 * @return O tempo atual em milissegundos.
	 */
	public static final long getCurrentTime() {
		return (System.nanoTime() / 1000000L);
	}

}
