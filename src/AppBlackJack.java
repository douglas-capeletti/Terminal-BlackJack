import java.io.IOException;

public class AppBlackJack {

	public static void main(String[] args) {
		setCorConsole();
		BlackJack jogo = new BlackJack();
		jogo.principal();
	}
	
	private static void setCorConsole() {
		if(System.getProperty("os.name").contains("Windows")) {
			try {
				new ProcessBuilder("cmd", "/c", "color", "f0").inheritIO().start().waitFor();
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				//new ProcessBuilder("/bin/bash", "-c", "setterm", "--background", "white", "--foreground", "black");.inheritIO().start().waitFor();
				new ProcessBuilder("/bin/bash", "-c", "export PS1=\"\\[$(tput bold)\\]\\[\\e[38;5;0m\\]\\[\\e[48;5;15m\\]\\u@\\h:\\[$(tput sgr0)\\]\\[\\e[38;5;0m\\]\\[\\e[48;5;15m\\]\\w$ \"").inheritIO().start().waitFor();
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
			//System.out.print("\033[H\033[2J");
		    //System.out.flush();
		}
	}
}
