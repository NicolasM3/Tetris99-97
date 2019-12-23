import java.util.Random;

/**
 * A classe GameLogic é responsável por gerenciar a lógica do jogo.
 * @author Nícolas Dendai, Nícolas Oliveira 
 * @since 2019
 */
public class GameLogic {

	/**
	 * O número de milissegundos por frame.
	 */
	private static final long FRAME_TIME = 1000L / 50L;
	
	/**
	 * O número de peças que existem.
	 */
	private static final int TYPE_COUNT = TileType.values().length;
	
	/**
	 * O número de ciclos para esperar após o ladrilho atingir o fundo, para que possamos mover a peça depois disso.
	 */
	private static final int WAIT_CYCLES = 3;
	
	/**
	 * A instância do BoardPanel.
	 */
	private BoardPanel board;
	
	/**
	 * A instância do SidePanel.
	 */
	private SidePanel side;
	
	/**
	 * Se o jogo está pausado.
	 */
	private boolean isPaused;
	
	/**
	 * Se o jogo ainda não iniciou.
	 * É definido como true inicialmente e depois definido como false quando o jogo é iniciado.
	 */
	private boolean isNewGame;
	
	/**
	 * Se já perdeu ou não.
	 */
	private boolean isGameOver;
	
	/**
	 * Nivel atual do jogo.
	 */
	private int level;
	
	/**
	 * A pontuação atual do jogador.
	 */
	private int score;
	
	/**
	 * O gerador de números aleatórios. 
	 * Isso é usado para escolher peças aleatoriamente.
	 */
	private Random random;
	
	/**
	 * O relógio que lida com a lógica de atualização.
	 */
	private Clock logicTimer;
				
	/**
	 * A peça atual.
	 */
	private TileType currentType;
	
	/**
	 * A próxima peça.
	 */
	private TileType nextType;
		
	/**
	 * A coluna atual da peça.
	 */
	private int currentCol;
	
	/**
	 * A linha atual da peça.
	 */
	private int currentRow;
	
	/**
	 * A rotação atual da peça.
	 */
	private int currentRotation;
		
	/**
	 * Delay para proxima jogada.
	 * Garante que um certo período de tempo passe após a 
	 * geração de uma peça antes que possamos movê-la.
	 */
	private int dropCooldown;
	
	/**
	 * A velocidade do jogo.
	 */
	private float gameSpeed;
	
	/**
	 * A espera de ciclos ao ocorrer uma colisão.
	 */
	private int waitingCycles = WAIT_CYCLES;
	
	/**
	 * A peça que estamos segurando no momento.
	 */
	private TileType hold = null;
	
	/**
	 * Se já segurou algo nessa jogada.
	 */
	private boolean didHold = false;
	
	/**
	 * A conexão desta partida.
	 */
	private Conexao conexao;
	
	/**
	 * Construtor da classe.
	 * Define as janelas que o jogo usará.
	 * @param tetris Objeto que recebe os inputs.
	 */
	public GameLogic (Conexao conexao) {
		this.board = new BoardPanel(this);
		this.side = new SidePanel(this);
		this.conexao = conexao;
	}
	
	/**
	 * Inicializa tudo e entra no ciclo do jogo.
	 */
	public void startGame(){
		
		/*
		 * Inicializa o gerador de números aleatórios.
		 */
		this.random = new Random();
		this.isNewGame = true;
		this.gameSpeed = 1.0f;
		
		/*
		 * Configura o timer e pausa-o, pois o jogo ainda não começou.
		 */
		this.logicTimer = new Clock(gameSpeed);
		logicTimer.setPaused(true);
		
		while(true) {
			// Recebe o tempo de início do frame.
			long start = System.nanoTime();
			
			// Atualiza o clock.
			logicTimer.update();
			
			/*
			 * Se um ciclo do clock decorreu, podemos atualizar o jogo, movendo a peça atual
			 * para baixo e fazendo todos os testes necessários.
			 */
			if(logicTimer.hasElapsedCycle()) {
				updateGame();
			}
		
			// Caso necessário, decrementa o cooldown para mover a peça.
			if(dropCooldown > 0) {
				dropCooldown--;
			}
			
			// Mostra a janela para o usuário
			renderGame();
			
			/*
			 * Espera até completar o tempo do frame.
			 */
			long delta = (System.nanoTime() - start) / 1000000L;
			if(delta < FRAME_TIME) {
				try {
					Thread.sleep(FRAME_TIME - delta);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Atualiza o jogo e lida com a maior parte de sua lógica.
	 */
	public void updateGame(){
		Parceiro servidor = conexao.getServidor();
		try 
		{
			servidor.receba(new PedidoEnviarPontos(0));
		}
		catch(Exception ex)
		{
			System.out.println("erro");
		}
		try
		{
			PedidoDeLixo lixo = (PedidoDeLixo)servidor.envie();	
			
		}
		catch(Exception ex)
		{
			System.out.println("erro");
		}
		
		// Verifica se a peça pode descer para a próxima linha.
		if(board.isValidAndEmpty(currentType, currentCol, currentRow + 1, currentRotation)) {
			// Se puder, desce.
			currentRow++;
		} else {
			if (waitingCycles != 0)
				waitingCycles--;
			else {
				// A peça colidiu com o fundo do tabuleiro ou com outra peça.
				// Devemos, então, adicioná-la ao tabuleiro.
				board.addPiece(currentType, currentCol, currentRow, currentRotation);
				
				/*
				 * Verifica se a adição desta peça limpou alguma linha.
				 * Se sim, aumenta a pontuação do jogador usando a tabela:
				 * 
				 * Limpou |	Pontos
				 * 1		100
				 * 2		200
				 * 3		400
				 * 4		800
				 * 
				 * Não é possível limpar mais de 4 linhas usando uma só peça.
				 */
				int cleared = board.checkLines();
				if(cleared > 0) {
					score += 50 << cleared;
				}
				
				/*
				 * Aumenta levemente a velocidade para a próxima peça.
				 * Usado para deixar o jogo mais difícil conforme mais peças são colocadas.
				 */
				gameSpeed += 0.035f;
				logicTimer.setCyclesPerSecond(gameSpeed);
				logicTimer.reset();
				
				/*
				 * Define um cooldown para que a próxima peça não se mova imediatamente. Isso
				 * previne que o jogador não tenha tempo de soltar uma tecla, e a próxima peça
				 * receba esse comando.
				 * Não é algo recomendado para gameplays profissionais de Tetris, mas para jogadores
				 * comuns, isso deixa o jogo mais confortável.
				 */
				dropCooldown = 25;
				
				/*
				 * Aumenta o nível de dificuldade. Isso não muda nada no jogo, só é um jeito
				 * de mostrar a dificuldade atual para o jogador.
				 */
				level = (int)(gameSpeed * 1.70f);
				
				/*
				 * Spawna a próxima peça.
				 */
				spawnPiece();
				waitingCycles = WAIT_CYCLES;
				didHold = false;
			}
		}
	}
	
	/**
	 * Atualiza o jogo.
	 * Força o BoardPanel e o SidePanel a se redesenharem.
	 */
	private void renderGame() {
		board.repaint();
		side.repaint();
	}
	
	/**
	 * Reseta o jogo.
	 * Redefine as variáveis ​​do jogo para seus valores padrão no início de um novo jogo.
	 */
	private void resetGame() {
		this.level = 1;
		this.score = 0;
		this.gameSpeed = 1.0f;
		this.nextType = TileType.values()[random.nextInt(TYPE_COUNT)];
		this.hold = null;
		this.didHold = false;
		this.isNewGame = false;
		this.isGameOver = false;
		board.clear();
		logicTimer.reset();
		logicTimer.setCyclesPerSecond(gameSpeed);
		spawnPiece();
	}
		
	/**
	 * Spawna a próxima peça e redefine as variáveis ​​da nossa peça para seus valores padrão.
	 */
	private void spawnPiece() {
		// Recebe a próxima peça do jogo e reseta suas variáveis
		this.currentType = nextType;
		this.currentCol = currentType.getSpawnColumn();
		this.currentRow = currentType.getSpawnRow();
		this.currentRotation = 0;
		this.nextType = TileType.values()[random.nextInt(TYPE_COUNT)];
		
		// Se o spawn foi inválido, significa que o jogador perdeu.
		if(!board.isValidAndEmpty(currentType, currentCol, currentRow, currentRotation)) {
			this.isGameOver = true;
			logicTimer.setPaused(true);
		}
	}
	
	/**
	 * Spawna uma nova peça (respeitando o tipo definido) e redefine as variáveis ​​da nossa
	 * peça para seus valores padrão.
	 * @param type Tipo da peça.
	 */
	private void spawnPiece(TileType type) {
		// Spawna a peça definida e reseta suas variáveis
		this.currentType = type;
		this.currentCol = currentType.getSpawnColumn();
		this.currentRow = currentType.getSpawnRow();
		this.currentRotation = 0;
		
		// Se o spawn foi inválido, significa que o jogador perdeu.
		if(!board.isValidAndEmpty(currentType, currentCol, currentRow, currentRotation)) {
			this.isGameOver = true;
			logicTimer.setPaused(true);
		}
	}

	/**
	 * Tenta atualizar a rotação da peça atual.
	 * @param newRotation A nova rotação da peça.
	 */
	private void rotatePiece(int newRotation) {
		// Usados para mover a peça caso a rotação provoque uma colisão com as bordas.
		int newColumn = currentCol;
		int newRow = currentRow;
		
		// Recebe as sobras de cada lado
		int left = currentType.getLeftInset(newRotation);
		int right = currentType.getRightInset(newRotation);
		int top = currentType.getTopInset(newRotation);
		int bottom = currentType.getBottomInset(newRotation);
		
		// Se a peça estiver muito perto das bordas laterais, move-a para longe.
		if(currentCol < -left) {
			newColumn -= currentCol - left;
		} else if(currentCol + currentType.getDimension() - right >= BoardPanel.COL_COUNT) {
			newColumn -= (currentCol + currentType.getDimension() - right) - BoardPanel.COL_COUNT + 1;
		}
		
		// Se a peça estiver muito perto das bordas verticais, move-a para longe.
		if(currentRow < -top) {
			newRow -= currentRow - top;
		} else if(currentRow + currentType.getDimension() - bottom >= BoardPanel.ROW_COUNT) {
			newRow -= (currentRow + currentType.getDimension() - bottom) - BoardPanel.ROW_COUNT + 1;
		}
		
		// Verifica se a nova rotação e posição são válidas
		if(board.isValidAndEmpty(currentType, newColumn, newRow, newRotation)) {
			// Se sim, atualiza a rotação da peça
			currentRotation = newRotation;
			currentRow = newRow;
			currentCol = newColumn;
		}
	}
	
	/**
	 * Segura a peça para ser usada depois.
	 */
	private void holdPiece() {
		didHold = true;
		if (hold == null) {
			hold = currentType;
			spawnPiece();
		} else {
			TileType tempCurrType = currentType;
			spawnPiece(hold);
			hold = tempCurrType;
		}
	}
	
	/* INPUTS */
	
    /** 
     * Começa a mover a peça para baixo mais rapidamente, aumentando
     * a velocidade do clock. Movimento conhecido como "soft drop".
	 */
	public void startSoftDrop() {
		if(!isPaused && !isGameOver && dropCooldown == 0) {
			logicTimer.setCyclesPerSecond(25.0f);
		}
	}
	
	/** 
	 * Para de mover a peça para baixo mais rapidamente, resetando o
	 * clock.
	 */
	public void stopSoftDrop() {
		logicTimer.setCyclesPerSecond(gameSpeed);
		logicTimer.reset();
	}
	
	/**
	 * Move para a esquerda.
	 */
	public void moveLeft() {
		if(!isPaused && !isGameOver && board.isValidAndEmpty(currentType, currentCol - 1, currentRow, currentRotation)) {
			currentCol--;
		}
	}
	
		
	/**
	 * Move para a direita.
	 */
	public void moveRight() {
		if(!isPaused && !isGameOver && board.isValidAndEmpty(currentType, currentCol + 1, currentRow, currentRotation)) {
			currentCol++;
		}
	}
	
		
	/**
	 * Gira no sentido anti-horário.
	 */
	public void rotateAnticlock() {
		if(!isPaused && !isGameOver) {
			rotatePiece((currentRotation == 0) ? 3 : currentRotation - 1);
		}
	}
	
	/**
	 * Gira no sentido horário.
	 */
	public void rotateClock() {
		if(!isPaused && !isGameOver) {
			rotatePiece((currentRotation == 3) ? 0 : currentRotation + 1);
		}
	}
	
	/**
	 * Pausa o jogo.
	 */
	public void pause() {
		if(!isGameOver && !isNewGame) {
			stopSoftDrop();
			isPaused = !isPaused;
			logicTimer.setPaused(isPaused);
		}
	}
	
	/**
	 * Começa um novo jogo.
	 */
	public void start() {
		if(isGameOver || isNewGame) {
			resetGame();
		}
	}
	
	/**
	 * Reinicia o jogo se estiver pausado.
	 */
	public void restart() {
		if(isPaused) {
			resetGame();
			isPaused = false;
			logicTimer.setPaused(isPaused);
		}
	}
	
	/**
	 * Segura a peça atual, caso já não tenha segurado nessa jogada.
	 */
	public void hold() {
		if (!didHold && !isGameOver) {
			holdPiece();
		}
	}
	/**
	 * Move a peça ao máximo para baixo possível, movimento conhecido como
	 * "hard drop".
	 */
	public void hardDrop() {
		if (isGameOver || isPaused) return;
		/*
		 * Usa uma lógica parecida com a de desenhar a peça-fantasma mas, ao
		 * invés disso, "move" a peça atual até seu fantasma.
		 */
		for(int lowest = currentRow; lowest < BoardPanel.ROW_COUNT; lowest++) {
			if(board.isValidAndEmpty(currentType, currentCol, lowest, currentRotation)) {					
				continue;
			}
			
			lowest--;
			
			board.addPiece(currentType, currentCol, lowest, currentRotation);
			break;
		}
		int cleared = board.checkLines();
		if(cleared > 0) {
			score += 50 << cleared;
		}
		
		gameSpeed += 0.035f;
		logicTimer.setCyclesPerSecond(gameSpeed);
		logicTimer.reset();
		
		dropCooldown = 25;
		
		level = (int)(gameSpeed * 1.70f);

		spawnPiece();
		waitingCycles = WAIT_CYCLES;
		didHold = false;
	}
	
	/* GETTERS */
	
	/**
	 * Get se o jogo está ou não em pausa.
	 * @return Se o jogo esta ou não em pausa.
	 */
	public boolean isPaused() {
		return isPaused;
	}
	
	/**
	 * Get se o jogo está ou não perdido.
	 * @return Se o jogo esta ou não em perdido.
	 */
	public boolean isGameOver() {
		return isGameOver;
	}
	
	/**
	 * Get se é ou não um novo jogo.
	 * @return Se e o não um novo jogo.
	 */
	public boolean isNewGame() {
		return isNewGame;
	}
	
	/**
	 * Get o score atual.
	 * @return O score.
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Get o level atual.
	 * @return O level atual.
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Get a peça atual.
	 * @return A peça atual.
	 */
	public TileType getPieceType() {
		return currentType;
	}
	
	/**
	 * Get A próxima peça.
	 * @return A próxima peça.
	 */
	public TileType getNextPieceType() {
		return nextType;
	}
	
	/**
	 * Get a coluna da peça.
	 * @return A coluna da peça.
	 */
	public int getPieceCol() {
		return currentCol;
	}
	
	/**
	 * Get a linha da peça.
	 * @return A linha da peça.
	 */
	public int getPieceRow() {
		return currentRow;
	}
	
	/**
	 * Get a rotação atual da peça.
	 * @return a rotação da peça.
	 */
	public int getPieceRotation() {
		return currentRotation;
	}
	
	/**
	 * Get o tipo da peça em hold.
	 */
	public TileType getHoldType() {
		return hold;
	}
	
	/**
	 * Get o BoardPanel usado.
	 */
	public BoardPanel getBoard() {
		return this.board;
	}
	
	/**
	 * Get o SidePanel usado.
	 */
	public SidePanel getSide() {
		return this.side;
	}
}
