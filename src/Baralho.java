import java.util.Random;

public class Baralho implements java.io.Serializable {
	private static final long serialVersionUID = -1761342858365635953L;
	private static final int maxCartas = 52;
	private int tamanho;
	private Carta[] cartas;

	public Baralho() {
		cartas = new Carta[maxCartas];
		tamanho = 0;
		String[] naipes = {"Copas", "Ouro", "Espada", "Paus"};
		for (String naipe : naipes) {
			String[] nomes = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
			for (int j = 0; j < nomes.length; j++) {
				int v = j + 2; // valor da carta. Inicia em 2, pois este eh o primeiro elemento do array nomes
				if (v > 10) {
					v = 10;
				} // Valor maximo das cartas
				if (nomes[j].equals("A")) {
					v = 11;
				}
				adicionarCarta(new Carta(naipe, nomes[j], v, false));
			}
		}
	}

	public Carta[] getCartas() {
		return cartas;
	}

	public void setCartas(Carta[] cartas) {
		this.cartas = cartas;
	}
	
	public int length() {
		return tamanho;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < tamanho; i++) {
			str.append(cartas[i].toString());
		}
		str.append("Numero de cartas: ").append(tamanho);
		return str.toString();
	}

	public void embaralhar() {
		// Implementacao do algoritmo de embaralhamento de Fisher-Yates, aperfeicoado por Richard Durstenfeld
		// https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle#The_modern_algorithm
		Random rand = new Random();
		for(int i = tamanho-1; i > 0; i--) {
			int r = rand.nextInt(i+1);
			Carta pivot = cartas[i];
			cartas[i] = cartas[r];
			cartas[r] = pivot;
		}
	}
	
	private void adicionarCarta(Carta c) {
		if(tamanho >= maxCartas) {
			return;
		}
		
		cartas[tamanho] = c;
		tamanho++;
	}
	
	private void removerCarta(int posicao) {
		if(tamanho <= 0 || posicao < 0 || posicao >= tamanho) {
			return;
		}
		
		for(int i = posicao; i < tamanho-1; i++) {
			cartas[i] = cartas[i+1];
		}
		cartas[tamanho-1] = null;
		tamanho--;
	}

	public Carta pegarCarta() {
		if(tamanho == 0) {
			return null;
		}
		
		Carta cartaRetirada = cartas[tamanho-1];
		removerCarta(tamanho-1);
		return cartaRetirada;
	}

}
