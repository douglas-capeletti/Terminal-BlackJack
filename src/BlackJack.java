import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
public class BlackJack implements java.io.Serializable {
	private static final long serialVersionUID = 5719706909038162841L;
	private Baralho baralho;
	private Player player;
	private Dealer dealer;
	private Scanner in;
	boolean primeiraRodada;
	boolean partidaFinalizada;
	String nomePasta = "BlackJackGame"; // Pasta onde os jogos sao salvos. No Windows, fica em %AppData%, em outros sistemas fica em $HOME
	
	public BlackJack() {
		in = new Scanner(System.in);
	}
	
	public void principal() {
		int op = -1;
		while(op != 0) {
			limpaTela();
			op = -1;
			System.out.println(".------..------..------..------..------..------..------..------..------.");
			System.out.println("|B.--. ||L.--. ||A.--. ||C.--. ||K.--. ||J.--. ||A.--. ||C.--. ||K.--. |");
			System.out.println("| :(): || :/\\: || (\\/) || :/\\: || :/\\: || :(): || (\\/) || :/\\: || :/\\: |");
			System.out.println("| ()() || (__) || :\\/: || :\\/: || :\\/: || ()() || :\\/: || :\\/: || :\\/: |");
			System.out.println("| '--'B|| '--'L|| '--'A|| '--'C|| '--'K|| '--'J|| '--'A|| '--'C|| '--'K|");
			System.out.println("`------'`------'`------'`------'`------'`------'`------'`------'`------'");
			System.out.println("");
			System.out.println("1 - Novo jogo");
			System.out.println("2 - Carregar jogo");
			System.out.println("0 - Sair");
			
			while(op < 0 || op > 2) {
				// Previne que seja lancada uma excecao java.util.InputMismatchException
				while(!in.hasNextInt()) {
					in.nextLine();
				}
				op = in.nextInt();
			}
			switch(op) {
				case 0:
					return;
				case 1:
					novoJogo();
					break;
				case 2:
					carregarJogo();
					break;
			}
		}
	}
	
	public void iniciarPartida() {
		baralho = new Baralho();
		int op = 0;
		String opSave = "";
		primeiraRodada = true;
		partidaFinalizada = false;
		
		// Inicio da partida
		player.getMao().limpar();
		dealer.getMao().limpar();
		dealer.setMostrouCarta(false);
		baralho.embaralhar();
		player.adicionarCarta(baralho.pegarCarta());
		player.adicionarCarta(baralho.pegarCarta());
		dealer.adicionarCarta(baralho.pegarCarta(), true);
		dealer.adicionarCarta(baralho.pegarCarta(), false);
		
		while(!partidaFinalizada) {
			limpaTela();
			exibeTelaJogo();
			
			// Parte da partida onde o dealer ainda nao mostrou a carta (nao foi selecionada a opcao "Jogar")
			if(!dealer.getMostrouCarta()) {
				// Na primeira rodada, caso o valor da mao do player seja 21, a vitoria por Blackjack imediatamente, e ganha o valor da aposta + 50%
				if(primeiraRodada) {
					if(player.getMao().valor() == 21) {
						playerVence(2);
						break;
					}
				}
				
				if(player.getMao().valor() > 21) {
					dealerVence();
					break;
				}
				
				if(dealer.getMao().valor() > 21) {
					playerVence(1);
					break;
				}
				
				op = -1;
				System.out.println("1 - + Carta    2 - Jogar    0 - Sair");
				while(op < 0 || op > 2) {
					// Previne que seja lancada uma excecao java.util.InputMismatchException
					while(!in.hasNextInt()) {
						in.nextLine();
					}
					op = in.nextInt();
				}
				primeiraRodada = false; // Sinaliza o final da primeira rodada
				switch(op) {
					case 1:
						pedirCarta();
						break;
					case 2:
						dealer.mostrarCarta();
						break;
					case 0:
						partidaFinalizada = true;
						return;
				}
			// Parte da partida onde o dealer mostrou a carta. Aqui, o player nao joga mais, apenas espera o dealer pegar mais cartas ate o valor da sua mao ultrapassar 17
			} else {
				if(dealer.getMao().valor() > 21) {
					playerVence(1);
					break;
				}
				
				// Caso o dealer tenha mais de 17, e feita a comparacao final da partida
				if(dealer.getMao().valor() > 17) {
					if(player.getMao().valor() > dealer.getMao().valor()) {
						playerVence(1);
						break;
					} else if(player.getMao().valor() < dealer.getMao().valor()) {
						dealerVence();
						break;
					} else {
						empate();
						break;
					}
				}
				
				dealerPegaCarta();
				sleep(1000); // Para dar tempo de visualizar a carta que o dealer pegou =)
			}
		}
		
		// Enquanto o player tiver creditos, o jogo continua
		if(player.getCreditos() > 0) {	
			op = -1;
			System.out.println("\n1 - Continuar  2 - Mudar aposta  3 - Salvar jogo  4 - Carregar jogo  0 - Sair");
			while(op < 0 || op > 4) {
				// Previne que seja lancada uma excecao java.util.InputMismatchException
				while(!in.hasNextInt()) {
					in.nextLine();
				}
				op = in.nextInt();
				switch(op) {
					case 1:
						manterAposta();
						iniciarPartida();
						break;
					case 2:
						mudarAposta();
						iniciarPartida();
						break;
					case 3:
						salvarJogo();
						System.out.print("Manter aposta? [S/N] ");
						while(!opSave.equalsIgnoreCase("S") && !opSave.equalsIgnoreCase("N")) {
							opSave = in.next();
						}
						if(opSave.equalsIgnoreCase("S")) {
							manterAposta();
						} else {
							mudarAposta();
						}
						iniciarPartida();
						break;
					case 4:
						carregarJogo();
						break;
					case 0:
						return;
				}
			}
		// Os creditos acabaram. GAME OVER =(
		} else {
			System.out.println("Voce ficou sem creditos!");
			System.out.println("   ____     _     __  __ U _____ u       U  ___ __     __U _____ u  ____     ");
			System.out.println("U /\"___|U  /\"\\  U|' \\/ '|\\| ___\"|/        \\/\"_ \\\\ \\   /\"/\\| ___\"|U |  _\"\\ u  ");
			System.out.println("\\| |  _ /\\/ _ \\/\\| |\\/| |/|  _|\"          | | | |\\ \\ / // |  _|\"  \\| |_) |/  ");
			System.out.println(" | |_| | / ___ \\ | |  | | | |___      .-,_| |_| |/\\ V /_,-| |___   |  _ <    ");
			System.out.println("  \\____|/_/   \\_\\|_|  |_| |_____|      \\_)-\\___/U  \\_/-(_/|_____|  |_| \\_\\   ");
			System.out.println("  _)(|_  \\\\    ><<,-,,-.  <<   >>           \\\\    //      <<   >>  //   \\\\_  ");
			System.out.println(" (__)__)(__)  (__(./  \\.)(__) (__)         (__)  (__)    (__) (__)(__)  (__)");
			pause();
			return;
		}
	}

	public void playerVence(int tipoVitoria) {
		System.out.print("Voce ganhou!");
		if(tipoVitoria == 2) {
			System.out.print(" Blackjack!");
		}
		System.out.println("");
		player.ganhaAposta(tipoVitoria);
		partidaFinalizada = true;
	}

	public void dealerVence() {
		System.out.println("Voce perdeu!");
		partidaFinalizada = true;
	}

	public void empate() {
		System.out.println("Empate!");
		player.devolveAposta();
		partidaFinalizada = true;
	}
	
	public void mudarAposta() {
		System.out.println("Creditos: " + player.getCreditos());
		boolean apostaOK = false;
		while(!apostaOK) {
			System.out.println("Digite o valor da aposta: ");
			// Previne que seja lancada uma excecao java.util.InputMismatchException
			while(!in.hasNextDouble()) {
				in.nextLine();
			}
			double aposta = in.nextDouble();
			if(aposta > player.getCreditos()) {
				System.out.println("Creditos insuficientes!");
			} else if(aposta < 1) {
				System.out.println("Aposta invalida!");
			} else {
				player.setAposta(aposta);
				apostaOK = true;
			}
		}
	}
	
	// Utilizado na opcao Continuar, que mantem a aposta
	public void manterAposta() {
		if(player.getAposta() > player.getCreditos()) {
			System.out.println("Creditos insuficientes!");
			mudarAposta();
		} else {
			player.setAposta(player.getAposta());
		}
	}

	public void novoJogo() {
		System.out.print("Digite seu nome: ");
		in.nextLine();
		String nome = in.nextLine(); 
		player = new Player(nome);
		dealer = new Dealer();
		
		// =)
		if(nome.equalsIgnoreCase("yamaguti")) {
			System.out.print(new EtEgg().yama());
		}
		
		mudarAposta();
		iniciarPartida();
	}
	
	public void carregarJogo() {
		int op = 0;
		String pasta = getPastaSave();
		if(pasta == "") {
			System.out.println("Pasta de saves nao encontrada.");
			sleep(1000);
			return;
		}
		File objPasta = new File(pasta);
		if(!objPasta.exists()) {
			System.out.println("Nenhum save encontrado.");
			pause();
			return;
		}
		File[] files = objPasta.listFiles();
		if(files.length < 1) {
			System.out.println("Nenhum arquivo de save encontrado!");
			pause();
			return;
		}
		System.out.println("Jogos salvos:");
		// String.format("%0$-20s", xxx) fixa o tamanho da string em 20, para exibir os dados como em uma tabela
		System.out.println("N\t" + String.format("%0$-20s", "Nome") + " Creditos");
		for(int i = 0; i < files.length; i++) {
			Player dados = getDetalhesSave(files[i]);
			if(dados != null) {
				System.out.println(i+1 + "\t" + String.format("%0$-20s", dados.getNome()) + " " + dados.getCreditos());
			} else {
				System.out.println(i+1 + "\t" + String.format("%0$-20s", files[i].getName()) + " " + "Save incompativel");
			}
        }
		
		while(op < 1 || op > files.length) {
			System.out.print("Digite o numero do jogo desejado: ");
			// Previne que seja lancada uma excecao java.util.InputMismatchException
			while(!in.hasNextInt()) {
				in.nextLine();
			}
			op = in.nextInt();
			if(op < 1 || op > files.length) {
				System.out.println("Opcao invalida!");
			}
		}
		
		// https://stackoverflow.com/questions/19784628/saving-game-state
		try {
			FileInputStream fileStream = new FileInputStream(files[op-1].getPath());
			ObjectInputStream objectStream = new ObjectInputStream(fileStream);
			
			player = (Player) objectStream.readObject();
			dealer = (Dealer) objectStream.readObject();
			
			objectStream.close();
			fileStream.close();
			
			mudarAposta();
			iniciarPartida();
			
		} catch(IOException e) {
			System.out.println("Falha ao carregar o arquivo: " + e.toString());
			pause();
			return;
		} catch(ClassNotFoundException c) {
			System.out.println("Classe nao encontrada: " + c.toString());
			pause();
			return;
		} catch(ClassCastException cc) {
			System.out.println("Save incompativel.");
			pause();
			return;
		}
	}
	
	public void salvarJogo() {
		String pasta = getPastaSave();
		if(pasta == "") {
			System.out.println("Pasta de saves nao encontrada.");
			pause();
			return;
		}
		new File(pasta).mkdir();
		
		File save = new File(pasta + File.separator + player.getNome());
		
		if(save.exists()) {
			System.out.print("Deseja sobreescrever o arquivo? [S/N] ");
			String op = in.next();
			while(!op.equalsIgnoreCase("S") && !op.equalsIgnoreCase("N")) {
				op = in.next();
			}
			if(op.equalsIgnoreCase("N")) {
				return;
			}
		}
		
		// https://stackoverflow.com/questions/19784628/saving-game-state
		try {
			FileOutputStream fileStream = new FileOutputStream(save);
			ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
			
			objectStream.writeObject(player);
			objectStream.writeObject(dealer);
			
			objectStream.close();
			fileStream.close();
			
			System.out.println("Jogo salvo com sucesso!");
			
		} catch(Exception e) {
			System.out.println("Falha ao salvar o jogo: " + e.toString());
		}
	}
	
	public void pedirCarta() {
		player.adicionarCarta(baralho.pegarCarta());
	}
	
	public void dealerPegaCarta() {
		dealer.adicionarCarta(baralho.pegarCarta(), true);
	}
	
	public String getPastaSave() {
		String l;
		if(System.getProperty("os.name").contains("Windows")) {
			l = System.getenv("APPDATA");
		} else {
			l = System.getenv("HOME");
		}
		
		if(l.length() < 1) {
			return "";
		}
		
		l += File.separator + nomePasta;
		
		return l;
	}
	
	public Player getDetalhesSave(File arquivoSave) {
		Player dados;
		// https://stackoverflow.com/questions/19784628/saving-game-state
		try {
			FileInputStream fileStream = new FileInputStream(arquivoSave.getPath());
			ObjectInputStream objectStream = new ObjectInputStream(fileStream);
			
			dados = (Player) objectStream.readObject();
			
			objectStream.close();
			fileStream.close();
			
		} catch(IOException e) {
			//System.out.println("Falha ao carregar o arquivo: " + e.toString());
			return null;
		} catch(ClassNotFoundException c) {
			//System.out.println("Classe nao encontrada: " + c.toString());
			return null;
		} catch(ClassCastException cc) {
			//System.out.println("Save " + arquivoSave.getName() + " nao compativel.");
			return null;
		}
		
		return dados;
	}
	
	// Tela principal da partida, onde sao exibidas as cartas, valores das maos, creditos e aposta
	public void exibeTelaJogo() {		
		// Nestes loops for, o "i" eh a linha do desenho da carta (que tem 6 linhas) e o "j" eh o numero da carta.
		// Desta forma, eh possivel desenhar as cartas lado a lado.
		System.out.println(player.getNome() + " - valor " + player.getMao().valor());
		for(int i=0; i<6; i++) {
			for(int j=0; j<player.getMao().length(); j++) {
				System.out.print(player.getMao().getCarta(j).toAsciiArt()[i] + " ");
			}
			System.out.print("\n");
		}
		System.out.println("");
		System.out.println("Dealer - valor " + dealer.getMao().valor());
		for(int i=0; i<6; i++) {
			for(int j=0; j<dealer.getMao().length(); j++) {
				System.out.print(dealer.getMao().getCarta(j).toAsciiArt()[i] + " ");
			}
			System.out.print("\n");
		}
		System.out.println("");
		System.out.println("Creditos: " + player.getCreditos());
		System.out.println("Aposta:   " + player.getAposta());
		System.out.print("\n");
	}
	
	public void limpaTela() {
		if(System.getProperty("os.name").contains("Windows")) {
			try {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} catch (InterruptedException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		} else {
		    System.out.print("\033[H\033[2J");  
		    System.out.flush();  
		}
	}
	
	// Pausa a execucao pelo tempo especificado em milissegundos
	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
	}
	
	// Equivalente ao comando PAUSE do prompt de comando do Windows
	public void pause() {
		System.out.println("Pressione ENTER para continuar");
		try {
			System.in.read();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
	

}
