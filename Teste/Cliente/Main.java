public class Main {
	public static void main (String[] args) {
		
		String ip = null;
		String usuario = null;
		int porta = 0;
		
		Conexao conexao = null;
		
		try
		{
			System.out.print("Digite o host: ");
			ip = Teclado.getUmString();
			System.out.print("Digite a porta: ");
			porta = Teclado.getUmInt();	
			System.out.print("Digite seu nome: ");
			usuario = Teclado.getUmString().trim();
			if (usuario == null)
				throw new Exception("Nome de usuário inválido");
			conexao = new Conexao(ip, porta, usuario);
			System.out.println(1);
		}
		catch(Exception err)
		{
			System.err.println("Ocorreu um erro, verifique as informações.\nErro: " + err.getMessage());
			System.exit(1);
		}
		
		Tetris tetris = new Tetris(conexao);
		tetris.startGame();
	}
	
}
