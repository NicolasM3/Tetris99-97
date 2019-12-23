import java.awt.Color;

/**
 * A TileType é uma classe enum que descreve as propiedades das peças do Tetris.
 * @author Nícolas Denadai, Nícolas Oliveira.
 * @since 2019
 */
public enum TileType {

	/**
	 * Peça do tipo I, também chamada de Hero.
	 */
	TypeI(new Color(0, 255, 255), 4, 4, 1, new boolean[][] {
		{
			false,	false,	false,	false,
			true,	true,	true,	true,
			false,	false,	false,	false,
			false,	false,	false,	false,
		},
		{
			false,	false,	true,	false,
			false,	false,	true,	false,
			false,	false,	true,	false,
			false,	false,	true,	false,
		},
		{
			false,	false,	false,	false,
			false,	false,	false,	false,
			true,	true,	true,	true,
			false,	false,	false,	false,
		},
		{
			false,	true,	false,	false,
			false,	true,	false,	false,
			false,	true,	false,	false,
			false,	true,	false,	false,
		}
	}),
	
	/**
	 * Peça do tipo J, também chamada de Blue Ricky.
	 */
	TypeJ(new Color(0, 0, 255), 3, 3, 2, new boolean[][] {
		{
			true,	false,	false,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	true,
			false,	true,	false,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	true,
			false,	false,	true,
		},
		{
			false,	true,	false,
			false,	true,	false,
			true,	true,	false,
		}
	}),
	
	/**
	 * Peça do tipo L, também chamada de Orange Ricky.
	 */
	TypeL(new Color(255, 127, 0), 3, 3, 2, new boolean[][] {
		{
			false,	false,	true,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	false,
			false,	true,	true,
		},
		{
			false,	false,	false,
			true,	true,	true,
			true,	false,	false,
		},
		{
			true,	true,	false,
			false,	true,	false,
			false,	true,	false,
		}
	}),
	
	/**
	 * Peça do tipo O, também chamada de Smashboy.
	 */
	TypeO(new Color(255, 255, 0), 2, 2, 2, new boolean[][] {
		{
			true,	true,
			true,	true,
		},
		{
			true,	true,
			true,	true,
		},
		{	
			true,	true,
			true,	true,
		},
		{
			true,	true,
			true,	true,
		}
	}),
	
	/**
	 * Peça do tipo S, também chamada de Rhode Island.
	 */
	TypeS(new Color(0, 255, 0), 3, 3, 2, new boolean[][] {
		{
			false,	true,	true,
			true,	true,	false,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	true,
			false,	false,	true,
		},
		{
			false,	false,	false,
			false,	true,	true,
			true,	true,	false,
		},
		{
			true,	false,	false,
			true,	true,	false,
			false,	true,	false,
		}
	}),
	
	/**
	 * Peça do tipo T, também chamada de Teewee.
	 */
	TypeT(new Color(128, 0, 128), 3, 3, 2, new boolean[][] {
		{
			false,	true,	false,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	true,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	true,
			false,	true,	false,
		},
		{
			false,	true,	false,
			true,	true,	false,
			false,	true,	false,
		}
	}),
	
	/**
	 * Peça do tipo Z, também chamada de Cleveland.
	 */
	TypeZ(new Color(255, 0, 0), 3, 3, 2, new boolean[][] {
		{
			true,	true,	false,
			false,	true,	true,
			false,	false,	false,
		},
		{
			false,	false,	true,
			false,	true,	true,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	false,
			false,	true,	true,
		},
		{
			false,	true,	false,
			true,	true,	false,
			true,	false,	false,
		}
	});
		
	/**
	 * A cor da peça.
	 * Cada peça tem uma cor principal.
	 */
	private Color baseColor;
	
	/**
	 * O efeito da luz sobre a peça.
	 * A cor da peça um pouco mais clara.
	 */
	private Color lightColor;
	
	/**
	 * O efeito da sombra sobre a peça.
	 * A cor da peça um pouco mais escura.
	 */
	private Color darkColor;
	
	/**
	 * Coluna do spawn da peça.
	 * A coluna em que essa peça irá spawnar.
	 */
	private int spawnCol;
	
	/**
	 * Linha do spawn da peça.
	 * A linha em que essa peça irá spawnar.
	 */
	private int spawnRow;
	
	/**
	 * Tamanho da Matriz.
	 * Tamanho que é necessária para a peça.
	 */
	private int dimension;
	
	/**
	 * O número de linhas necessárias. 
	 * O número de linhas necessárais para peça.
	 */
	private int rows;
	
	/**
	 * O número de colunas necessárias. 
	 * O número de colunas necessárais para peça.
	 */
	private int cols;
	
	/**
	 * Os blocos das peças.
	 * Cada peça tem uma matriz de peças para cada rotação.
	 */
	private boolean[][] tiles;
	
	/**
	 * Construtor um TileType.
	 * @param color cor da peça.
	 * @param dimension A dimensão da matriz necessária para a peça.
	 * @param cols O número de colunas.
	 * @param rows O número de linhas.
	 * @param tiles A matriz da peça.
	 */
	private TileType(Color color, int dimension, int cols, int rows, boolean[][] tiles) {
		// Define algumas propiedades da peça.
		this.baseColor = color;
		this.lightColor = color.brighter();
		this.darkColor = color.darker();
		this.dimension = dimension;
		this.tiles = tiles;
		this.cols = cols;
		this.rows = rows;
		
		this.spawnCol = 5 - (dimension >> 1);
		this.spawnRow = getTopInset(0);
	}
	
	/**
	 * Get a cor básica da peça.
	 * @return A cor.
	 */
	public Color getBaseColor() {
		return baseColor;
	}
	
	/**
	 * Get a cor iluminada da peça.
	 * @return cor iluminada da peça.
	 */
	public Color getLightColor() {
		return lightColor;
	}
	
	/**
	 * Get a cor sombreda da peça.
	 * @return cor sombreada da peça.
	 */
	public Color getDarkColor() {
		return darkColor;
	}
	
	/**
	 * Get a dimensão da peça.
	 * @return A dimensão.
	 */
	public int getDimension() {
		return dimension;
	}
	
	/**
	 * Get o coluna de spawn da peça.
	 * @return coluna de spawn da peça.
	 */
	public int getSpawnColumn() {
		return spawnCol;
	}
	
	/**
	 * Get o linha de spawn da peça.
	 * @return linha de spawn da peça.
	 */
	public int getSpawnRow() {
		return spawnRow;
	}
	
	/**
	 * Get o número de linhas da peça.
	 * Pega o número de linhas que a peça necessita.
	 * @return O número de linhas.
	 */
	public int getRows() {
		return rows;
	}
	
	/**
	 * Get O número de colunas da peça.
	 * Pega o número de colunas que a peça necessita.
	 * @return O número de colunas.
	 */
	public int getCols() {
		return cols;
	}
	
	/**
	 * Verifica se as coordenadas e a rotação fornecidas contêm um bloco.
	 * @param x A cordenada X da peça.
	 * @param y A cordenada Y da peça.
	 * @param rotation A rotação pedida.
	 * @return Se pode ou não girar.
	 */
	public boolean isTile(int x, int y, int rotation) {
		return tiles[rotation][y * dimension + x];
	}
	
	/**
	 * A inserção esquerda é representada pelo número de colunas vazias no lado esquerdo da matriz para a rotação especificada.
	 * @param rotation A rotação.
	 * @return A inserção a esquerda.
	 */
	public int getLeftInset(int rotation) {
		/*
		 * Faz um loop da esquerda para direita até encontrar um bloco e retorna
		 * a coluna.
		 */
		for(int x = 0; x < dimension; x++) {
			for(int y = 0; y < dimension; y++) {
				if(isTile(x, y, rotation)) {
					return x;
				}
			}
		}
		return -1;
	}
	
	/**
	 * A inserção a direita é representada pelo número de colunas vazias no lado direita da matriz para a rotação especificada.
	 * @param rotation A rotação.
	 * @return A inserção a direita.
	 */
	public int getRightInset(int rotation) {
		/*
		 * Faz um loop da direita para esquerda até encontrar um bloco e retorna
		 * a coluna.
		 */
		for(int x = dimension - 1; x >= 0; x--) {
			for(int y = 0; y < dimension; y++) {
				if(isTile(x, y, rotation)) {
					return dimension - x;
				}
			}
		}
		return -1;
	}
	
	/**
	 * A inserção esquerda é representada pelo número de linhas vazias no lado superior da matriz para a rotação especificada.
	 * @param rotation A rotação.
	 * @return A rotação para cima.
	 */
	public int getTopInset(int rotation) {
		/*
		 * Faz um loop de cima para baixo até encontrar um bloco e retorna
		 * a coluna.
		 */
		for(int y = 0; y < dimension; y++) {
			for(int x = 0; x < dimension; x++) {
				if(isTile(x, y, rotation)) {
					return y;
				}
			}
		}
		return -1;
	}
	
	/**
	 * A inserção inferior é representada pelo número de linhas vazias na parte inferior da matriz para a rotação especificada.
	 * @param rotation A rotação.
	 * @return A rotação para baixo.
	 */
	public int getBottomInset(int rotation) {
		/*
		 * Faça um loop de baixo para cima até encontrar um bloco e retorna
		 * a coluna.
		 */
		for(int y = dimension - 1; y >= 0; y--) {
			for(int x = 0; x < dimension; x++) {
				if(isTile(x, y, rotation)) {
					return dimension - y;
				}
			}
		}
		return -1;
	}
	
}
