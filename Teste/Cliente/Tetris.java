
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
/**
 * Classe Tetris.
 * A classe Tetris é responsável por mostrar a janela e redirecionar inputs do jogo para a classe de lógica.
 * @author Nícolas Denadai, Nícolas Oliveira.
 * @since 2019
 */
public class Tetris extends JFrame {
	
	/**
	 * Versão serial UID.
	 * Controla explicitamente a compatibilidade entre o .class usado para serializar e o .class que será utilizado na desserialização.
	 */
	private static final long serialVersionUID = -4722429764792514382L;

	/**
	 * Objeto BoardPanel.
	 * Instância da classe BoardPanel que desenha o grid.
	 */
	private BoardPanel board;
	
	/**
	 * Objeto SidePanel.
	 * Instância da classe SidePanel que desenha o menu lateral.
	 */
	private SidePanel side;
	
	/**
	 * Objeto GameLogic.
	 * Instância da classe que faz as operações do jogo.
	 */
	private GameLogic game;
	
	/**
	 * Construtor da classe Tetris. 
	 * Define as propiedades da janela de jogo,
	 * e adiciona a lista de controles.
	 */
	public Tetris(Conexao conexao) {
		// Define as propriedades básicas da janela.
		super("Tetris");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		// Inicializa as instâncias BoardPanel e SidePanel.
		this.game = new GameLogic(conexao);
		this.board = game.getBoard();
		this.side = game.getSide();
		
		// Adiciona as instâncias de BoardPanel e SidePanel à janela.
		add(board, BorderLayout.WEST);
		add(side, BorderLayout.EAST);
		
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
								
				switch(e.getKeyCode()) {
				
				/*
				 * Há mais informação sobre os inputs na classe GameLogic, que
				 * os recebe e processa.
				 */
				
				// Começar soft drop.
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
					game.startSoftDrop();
					break;
					
				// Mover pra esquerda.
				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
					game.moveLeft();
					break;

				// Mover pra direita.
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
					game.moveRight();
					break;
					
				// Girar anti-horário.
				case KeyEvent.VK_Q:
					game.rotateAnticlock();
					break;
				
				// Girar horário.
				case KeyEvent.VK_E:
				case KeyEvent.VK_SPACE:
					game.rotateClock();
					break;
				
				// Pausa o game.
				case KeyEvent.VK_P:
					game.pause();
					break;
				
				// Começa o jogo, se já não estiver jogando.
				case KeyEvent.VK_ENTER:
					game.start();
					break;
				
				// Reinicia o jogo, se estiver pausado.
				case KeyEvent.VK_R:
					game.restart();
					break;
				
				// Segura a peça.
				case KeyEvent.VK_F:
				case KeyEvent.VK_H:
					game.hold();
					break;
				
				// Dropa a peça instantaneamente.
				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
					game.hardDrop();
					break;
					
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
				switch(e.getKeyCode()) {
				
				// Parar soft drop
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
					game.stopSoftDrop();
					break;
				}
				
			}
		});
		
		/*
		 * Aqui redimensionamos o quadro para conter as instâncias BoardPanel e SidePanel,
		 * centraliza a janela na tela e mostra ao usuário.
		 */
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
		
	/**
	 * Começa o jogo.
	 */
	public void startGame() {
		game.startGame();
	}

}
