import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * A classe BoardPanel é responsável por mostrar o tabuleiro (grade) do jogo e
 * tratar da lógica relacionada a ele.
 * @author Nícolas Denadai, Nícolas Oliveira 
 * @since 2019
 */
public class BoardPanel extends JPanel {

	/**
	 * Versão serial UID.
	 * Controla explicitamente a compatibilidade entre o .class usado para serializar
	 * e o .class que será utilizado na desserialização.
	 */
	private static final long serialVersionUID = 5055679736784226108L;
	
	/**
	 * A largura ao redor do tabuleiro.
	 */
	private static final int BORDER_WIDTH = 5;
	
	/**
	 * O número de colunas do tabuleiro.
	 */
	public static final int COL_COUNT = 10;
		
	/**
	 * O número de linhas visíveis do tabuleiro.
	 */
	private static final int VISIBLE_ROW_COUNT = 20;
	
	/**
	 * O número de linhas não-visíveis do tabuleiro. <br>
	 * Essas colunas extras são usadas para cumprir uma regra de design do Tetris que
	 * define que as peças devem surgir fora do tabuleiro visível e descer até ele, ao
	 * invés de surgir já nas linhas visíveis.
	 */
	private static final int HIDDEN_ROW_COUNT = 2;
	
	/**
	 * O número total de linhas.
	 */	
	public static final int ROW_COUNT = VISIBLE_ROW_COUNT + HIDDEN_ROW_COUNT;
	
	/**
	 * O número de pixels que um bloco ocupa.
	 */
	public static final int TILE_SIZE = 24;
	
	/**
	 * A cordenada central x do tabuleiro.
	 */
	private static final int CENTER_X = COL_COUNT * TILE_SIZE / 2;
	
	/**
	 * A cordenada central y do tabuleiro.
	 */
	private static final int CENTER_Y = VISIBLE_ROW_COUNT * TILE_SIZE / 2;
		
	/**
	 * A largura total do tabuleiro.
	 */
	public static final int PANEL_WIDTH = COL_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
	
	/**
	 * A altura total do tabuleiro.
	 */
	public static final int PANEL_HEIGHT = VISIBLE_ROW_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
	
	/**
	 * A fonte grande usada.
	 */
	private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 16);

	/**
	 * A fonte pequena usada.
	 */
	private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 12);

	/**
	 * A instância da classe GameLogic.
	 */
	private GameLogic game;
	
	/**
	 * A matriz das peças que estão no tabuleiro.
	 */
	private TileType[][] tiles;
	
	/**
	 * Se já foi mostrada a mensagem de game over (derrota).
	 */
	private boolean shownGameOver = false;
	
	private String winner;
		
	/**
	 * Cria um painel novo para o um novo jogo.
	 * @param game A instância da classe GameLogic que controlará o tabuleiro.
	 */
	public BoardPanel(GameLogic game) {
		this.game = game;
		this.tiles = new TileType[ROW_COUNT][COL_COUNT];
		
		// Define propriedades do tabuleiro.
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setBackground(Color.BLACK);
	}
	
	/**
	 * Limpa todas as peças do tabuleiro.
	 */
	public void clear() {
		// Percorre o tabuleiro definindo cada casa como null.
		for(int i = 0; i < ROW_COUNT; i++) {
			for(int j = 0; j < COL_COUNT; j++) {
				tiles[i][j] = null;
			}
		}
	}
	
	/**
	 * Verifica se a posição (x, y), usando a rotação passada não está
	 * em uso ou fora dos limites do tabuleiro.
	 * @param type Tipo da peça.
	 * @param x Cordenada x da peça.
	 * @param y Cordenada y da peça.
	 * @param rotation A rotação da peça.
	 * @return Se o movimento é valido ou não.
	 */
	public boolean isValidAndEmpty(TileType type, int x, int y, int rotation) {
				
		// Verifica se a peça está em uma coluna válida.
		if(x < -type.getLeftInset(rotation) || x + type.getDimension() - type.getRightInset(rotation) >= COL_COUNT) {
			return false;
		}
		
		// Verifica se a peça está em uma linha válida.
		if(y < -type.getTopInset(rotation) || y + type.getDimension() - type.getBottomInset(rotation) >= ROW_COUNT) {
			return false;
		}
		
		// Percorre todos os blocos da peça e verifica se ele entra em conflito com um bloco existente.
		for(int col = 0; col < type.getDimension(); col++) {
			for(int row = 0; row < type.getDimension(); row++) {
				if(type.isTile(col, row, rotation) && isOccupied(x + col, y + row)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Adiciona uma nova peça no painel, sem verificar se a posição é valida
	 * @param type Tipo da peça.
	 * @param x Cordenada x da peça.
	 * @param y Cordenada y da peça.
	 * @param rotation A rotação da peça.
	 */
	public void addPiece(TileType type, int x, int y, int rotation) {
		/*
		 * Percorre todos os blocos da peça e adiciona-os
		 * para o quadro somente se o valor que representa esse
		 * bloco está definido como true.
		 */
		for(int col = 0; col < type.getDimension(); col++) {
			for(int row = 0; row < type.getDimension(); row++) {
				if(type.isTile(col, row, rotation)) {
					setTile(col + x, row + y, type);
				}
			}
		}
	}
	
	/**
	 * Verifica se alguma linha deve ser limpa.
	 * Uma linha é limpa caso todas as suas colunas estejam preenchidas.
	 * @return O número de linhas que foram limpas.
	 */
	public int checkLines() {
		int completedLines = 0;
		
		/*
		 * Percorre todas as linhas e verifica se
		 * foi limpa ou não. Se sim, incrementa o
		 * número de linhas concluídas.
		 */
		for(int row = 0; row < ROW_COUNT; row++) {
			if(checkLine(row)) {
				completedLines++;
			}
		}
		return completedLines;
	}
			
	/**
	 * Verifica se a linha está cheia ou não.
	 * @param line Linha a ser verificada.
	 * @return Se está ou não preenchida.
	 */
	private boolean checkLine(int line) {
		/*
		* Percorre todas as colunas nesta linha. Se alguma delas estiver
		* vazia, então a linha não está cheia.
		*/
		for(int col = 0; col < COL_COUNT; col++) {
			if(!isOccupied(col, line)) {
				return false;
			}
		}
		
		/*
		 * A linha está cheia, precisamos removê-la do jogo.
		 * Para fazer isso, simplesmente movemos todas as linhas acima
		 * dessa uma posição para baixo.
		 */
		for(int row = line - 1; row >= 0; row--) {
			for(int col = 0; col < COL_COUNT; col++) {
				setTile(col, row + 1, getTile(col, row));
			}
		}
		return true;
	}
	
	
	/**
	 * Verifica se uma posição (x, y) está ocupada
	 * @param x Cordenada x da peça.
	 * @param y Cordenada y da peça.
	 * @return Se está ou não ocupada.
	 */	
	private boolean isOccupied(int x, int y) {
		return tiles[y][x] != null;							
	}
	
	/**
	 * Coloca um bloco na posição (x, y)
	 * @param x A coluna.
	 * @param y A linha.
	 * @param type Tipo da peça que será colocada.
	 */
	private void setTile(int x, int y, TileType type) {
		tiles[y][x] = type;
	}
		
	/**
	 * Retorna a peça da posição (x, y)
	 * @param x A coluna.
	 * @param y A linha.
	 * @return O tipo da peça.
	 */
	private TileType getTile(int x, int y) {
		//  Retorna a peça da posição y, x
		return tiles[y][x];
	}
	
	/**
	 * Desenha os componentes no tabuleiro.
	 * @param g As propiedades gráficas do objeto.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Ajuda a simplificar o posicionamento das coisas.
		g.translate(BORDER_WIDTH, BORDER_WIDTH);
		
		
		// Desenha o tabuleiro de maneira diferente, dependendo do estado do jogo.
		if(game.isPaused()) {
			g.setFont(LARGE_FONT);
			g.setColor(Color.WHITE);
			String msg = "PAUSADO";
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, CENTER_Y);
			g.setFont(SMALL_FONT);
			msg = "[P] Continuar - [R] Reiniciar";
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 300);
		} else if(game.isNewGame() || game.isGameOver()) {
			g.setFont(LARGE_FONT);
			g.setColor(Color.WHITE);
			
			/*
			 * Lidamos com as telas e usamos um operador ternário para mudar
			 * as mensagens que são exibidas.
			 */
			String msg = game.isNewGame() ? "TETRIS" : "GAME OVER";
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 150);
			g.setFont(SMALL_FONT);
			msg = (this.winner != null) ? ("Ganhador: " + this.winner):(game.isNewGame() ? "Aperte [Enter] para jogar":"Esperando o outro jogador acabar");
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 300);
			if (game.isGameOver() && !shownGameOver) {
				shownGameOver = true;
			}
		} else {
			// Desenha as peças no tabuleiro.
			for(int x = 0; x < COL_COUNT; x++) {
				for(int y = HIDDEN_ROW_COUNT; y < ROW_COUNT; y++) {
					TileType tile = getTile(x, y);
					if(tile != null) {
						drawTile(tile, x * TILE_SIZE, (y - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
					}
				}
			}
			
			
			/*
			 * Desenha a peça atual. Esta não pode ser desenhada como o resto das
			 * peças porque ainda não faz parte do tabuleiro do jogo.
			*/
			TileType type = game.getPieceType();
			int pieceCol = game.getPieceCol();
			int pieceRow = game.getPieceRow();
			int rotation = game.getPieceRotation();
			
			// Desenha as peças no tabuleiro.
			for(int col = 0; col < type.getDimension(); col++) {
				for(int row = 0; row < type.getDimension(); row++) {
					if(pieceRow + row >= 2 && type.isTile(col, row, rotation)) {
						drawTile(type, (pieceCol + col) * TILE_SIZE, (pieceRow + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
					}
				}
			}
			
			/*
			 * Desenha a peça fantasma (peça semi-transparente que mostra onde a peça atual vai cair).
			 * Simplesmente desce a partir da posição atual
			 * até atingir uma linha que causaria uma colisão.
			 */
			Color base = type.getBaseColor();
			base = new Color(base.getRed(), base.getGreen(), base.getBlue(), 30);
			for(int lowest = pieceRow; lowest < ROW_COUNT; lowest++) {
				// Se nenhuma colisão for detectada, tente a próxima linha.
				if(isValidAndEmpty(type, pieceCol, lowest, rotation)) {					
					continue;
				}
				
				// Volta uma linha acima daquela em que a colisão ocorreu. 
				lowest--;
				
				// Desenha a peça fantama.
				for(int col = 0; col < type.getDimension(); col++) {
					for(int row = 0; row < type.getDimension(); row++) {
						if(lowest + row >= 2 && type.isTile(col, row, rotation)) {
							drawTile(base, (pieceCol + col) * TILE_SIZE, (lowest + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
						}
					}
				}
				
				break;
			}
			

			// Desenha a grade na frente das peças.
			g.setColor(Color.DARK_GRAY);
			for(int x = 0; x < COL_COUNT; x++) {
				for(int y = 0; y < VISIBLE_ROW_COUNT; y++) {
					g.drawLine(0, y * TILE_SIZE, COL_COUNT * TILE_SIZE, y * TILE_SIZE);
					g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, VISIBLE_ROW_COUNT * TILE_SIZE);
				}
			}
		}
		
		//Desenha a borda
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, TILE_SIZE * COL_COUNT, TILE_SIZE * VISIBLE_ROW_COUNT);
	}
	
	/**
	 * Desenha o bloco na posição.
	 * Desenha um bloco da posição (x, y).
	 * @param type O tipo a ser desanhado.
	 * @param x A coluna.
	 * @param y A linha.
	 * @param g O estilo da peça.
	 */
	private void drawTile(TileType type, int x, int y, Graphics g) {
		drawTile(type.getBaseColor(), x, y, g);
	}
	
	/**
	 * Desenha o bloco na posição.
	 * Desenha um bloco da posição (x, y).
	 * @param base A cor padrão.
	 * @param light A cor com luz.
	 * @param dark A cor sombreada.
	 * @param x A coluna.
	 * @param y A linha.
	 * @param g O estilo das peça.
	 */
	private void drawTile(Color base, int x, int y, Graphics g) {
		g.setColor(base);
		g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
	}
	
	public void showWinner(String winner) {
		this.winner = winner;
		this.repaint();
	}
}
