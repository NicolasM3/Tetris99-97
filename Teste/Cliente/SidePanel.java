import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * A classe do painel lateral.
 * A classe que mostra no painel lateral várias informações sobre o jogo, 
 * como a próxima peça, a pontuação, o nível atual e os controles.
 * @author Nícolas Denadai, Nícolas Oliveira.
 * @since 2019
 */
public class SidePanel extends JPanel {
	
	/**
	 * Versão serial UID.
	 * Controla explicitamente a compatibilidade entre o .class usado para serializar e o .class que será utilizado na desserialização.
	 */
	private static final long serialVersionUID = 2181495598854992747L;

	/**
	 * As dimensões da cada peça.
	 * As dimensões da próxima peça que aparecerá no previw.
	 */
	private static final int TILE_SIZE = BoardPanel.TILE_SIZE >> 1;
	
	/**
	 * O numero de colunas e linha no preview. 
	 * Defina para 5 porque podemos mostrar qualquer peça com esse tamanho.
	 */
	private static final int TILE_COUNT = 5;
	
	/**
	 * O centro X da peça no preview.
	 * O centro X da preoxima peça, que esta no preview.
	 */
	private static final int SQUARE_CENTER_X = 130;
	
	/**
	 * O centro Y da peça no preview.
	 * O centro Y da preoxima peça, que esta no preview.
	 */
	private static final int SQUARE_CENTER_Y = 65;
	
	/**
	 * Tamanho da peça no preview box.
	 * Tamanho da próxima peça, que esta no preview.
	 */
	private static final int SQUARE_SIZE = (TILE_SIZE * TILE_COUNT >> 1);
	
	/**
	 * O número mínimo de pixel.
	 * O número de pixels usados ​​em pequenas inserções.
	 */
	private static final int SMALL_INSET = 20;
	
	/**
	 * O número máximo de pixel.
	 * O número de pixels usados ​​em grandes inserções.
	 */
	private static final int LARGE_INSET = 40;
	
	/**
	 * Cordenada y da categoria de status.
	 */
	private static final int STATS_INSET = 180;
	
	/**
	 * Cordenada y da categoria de controles.
	 */
	private static final int CONTROLS_INSET = 300;
	
	/**
	 * Número de pixel entre strings.
	 */
	private static final int TEXT_STRIDE = 20;
	
	/**
	 * Fonte 1.
	 * A menor fonte usada.
	 */
	private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 11);
	
	/**
	 * Fonte 2.
	 * A maior fonte usada.
	 */
	private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 13);
	
	/**
	 * Cor usada.
	 * A cor usada para escrever e desenha o preview.
	 */
	private static final Color DRAW_COLOR = new Color(128, 192, 128);

	/**
	 * Instância do GameLogic.
	 * Uma instância de um objeto da classe GameLogic, que a logica de jogo.
	 */
	private GameLogic game;
	
	/**
	 * Construtor da classe.
	 * Cria um novo SidePanel e define suas propriedades de exibição.
	 * @param tetris a instância do jogo usada.
	 */
	public SidePanel(GameLogic game) {
		// Define as propiedades.
		this.game = game;
		
		setPreferredSize(new Dimension(200, BoardPanel.PANEL_HEIGHT));
		setBackground(Color.BLACK);
	}
	
	/**
	 * Pintar componentes.
	 * Desenha os componentes no painel lateral.
	 * @param g As propiedades gráficas do objeto.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Defina a cor para o desenho.
		g.setColor(DRAW_COLOR);
		
		/*
		 * Essa variável armazena a coordenada y atual da string.
		 * Dessa forma, podemos reordenar, adicionar ou remover novas strings, se necessário
		 * sem precisar alterar as outras strings.
		 */
		int offset;
		
		// Desenha a categoria status.
		offset = STATS_INSET;
		g.setFont(LARGE_FONT);
		g.drawString("Estatísticas", SMALL_INSET, offset += TEXT_STRIDE);
		g.setFont(SMALL_FONT);
		g.drawString("Nível: " + game.getLevel(), LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("Pontos: " + game.getScore(), LARGE_INSET, offset += TEXT_STRIDE);
		
		// Desenha a categoria controle.
		g.setFont(LARGE_FONT);
		g.drawString("Controles", SMALL_INSET, offset = CONTROLS_INSET);
		g.setFont(SMALL_FONT);
		g.drawString("A / < - Esquerda", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("D / > - Direita", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("Q - Girar Anti-horário", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("E / Espaço - Girar Horário", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("H / F - Segurar (Hold)", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("S - Cair", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("W - Cair tudo", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("P - Pausar Jogo", LARGE_INSET, offset += TEXT_STRIDE);
		
		// Desenha a proxima peça no preview.
		g.setFont(LARGE_FONT);
		g.drawString("Próx. peça:", SMALL_INSET, 70);
		g.drawRect(SQUARE_CENTER_X - SQUARE_SIZE, SQUARE_CENTER_Y - SQUARE_SIZE, SQUARE_SIZE * 2, SQUARE_SIZE * 2);
		
		/*
		 * Faz uma prévia da próxima peça que será gerada. O código é praticamente
		 * idêntico ao código do desenho no quadro, apenas menor e centralizado,
		 * do que restrito a uma grade.
		 */
		TileType type = game.getNextPieceType();
		if(!game.isGameOver() && type != null) {
			// Pega a propiedade tamanho da peça atual. 
			int cols = type.getCols();
			int rows = type.getRows();
			int dimension = type.getDimension();
		
			// Calcule o canto superior esquerdo (origem) da peça.
			int startX = (SQUARE_CENTER_X - (cols * TILE_SIZE / 2));
			int startY = (SQUARE_CENTER_Y - (rows * TILE_SIZE / 2));
		
			/*
			 * Obtem as inserções para a visualização. O padrão de
			 * rotação é usada para a visualização, então usamos apenas a rotação 0.
			 */
			int top = type.getTopInset(0);
			int left = type.getLeftInset(0);
		

			// Passa pela peça e desenhe suas peças no preview.

			for(int row = 0; row < dimension; row++) {
				for(int col = 0; col < dimension; col++) {
					if(type.isTile(col, row, 0)) {
						drawTile(type, startX + ((col - left) * TILE_SIZE), startY + ((row - top) * TILE_SIZE), g);
					}
				}
			}
		}
		
		g.setColor(DRAW_COLOR);
		g.setFont(SMALL_FONT);
		g.drawString("Peça em hold:", SMALL_INSET, 140);
		g.drawRect(SQUARE_CENTER_X - SQUARE_SIZE, SQUARE_CENTER_Y - SQUARE_SIZE + 70, SQUARE_SIZE * 2, SQUARE_SIZE * 2);
		
		type = game.getHoldType();
		if(!game.isGameOver() && type != null) {
			// Obtem as propriedades de tamanho da peça atual.
			int cols = type.getCols();
			int rows = type.getRows();
			int dimension = type.getDimension();
		
			// Calcula o canto superior esquerdo (origem) da peça.
			int startX = (SQUARE_CENTER_X - (cols * TILE_SIZE / 2));
			int startY = (SQUARE_CENTER_Y - (rows * TILE_SIZE / 2) + 70);
		
			/*
			 * Obtem as inserções para a visualização. O padrão
			 * rotação é usada para a visualização, então usamos apenas a rotação 0.
			 */
			int top = type.getTopInset(0);
			int left = type.getLeftInset(0);
		
			// Passa pela peça e desenha suas peças no preview.
			for(int row = 0; row < dimension; row++) {
				for(int col = 0; col < dimension; col++) {
					if(type.isTile(col, row, 0)) {
						drawTile(type, startX + ((col - left) * TILE_SIZE), startY + ((row - top) * TILE_SIZE), g);
					}
				}
			}
		}
	}
	
	/**
	 * atualizar preview.
	 * Desenha um bloco na janela de visualização.
	 * @param type O tipo da peça.
	 * @param x A cordenada x da peça.
	 * @param y A cordenada y da peça.
	 * @param g As propiedades gráficas do objeto.
	 */
	private void drawTile(TileType type, int x, int y, Graphics g) {
		// Preenche o bloco inteiro com a cor base.
		g.setColor(type.getBaseColor());
		g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
	}
	
}
