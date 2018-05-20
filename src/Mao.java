public class Mao implements java.io.Serializable {
	private static final long serialVersionUID = 4022972226898171574L;
	private static final int maxCartas = 11; //Maximo possivel sem passar do valor 21 (A, A, A, A, 2, 2, 2, 2, 3, 3, 3)
	private int tamanho;
	private Carta[] cartas;
	
	public Mao() {
		cartas = new Carta[maxCartas];
		tamanho = 0;
	}

	public Carta[] getCartas() {
		return cartas;
	}
	
	public void setCartas(Carta[] cartas) {
		this.cartas = cartas;
	}
	
	public String toString() {
		String str = "";
		for(int i = 0; i < tamanho; i++) {
			str += "Carta " + i + ": " + cartas[i].toString();
		}
		str += "Valor da mao: " + valor();
		return str;
	}

	public int valor() {
		int valor = 0;
		for(int i = 0; i < tamanho; i++) {
			if(cartas[i].getVisivel()) {
				valor += cartas[i].getValor();
			}
		}
		return valor;
	}
	
	public int length() {
		return tamanho;
	}
	
	public Carta getCarta(int index) {
		if(index >= 0 && index < tamanho) {
			return cartas[index];
		}
		return null;
	}
	
	public void adicionarCarta(Carta carta) {
		if(tamanho >= maxCartas) {
			return;
		}
		
		cartas[tamanho] = carta;
		tamanho++;
		// Caso passe de 21 e tiver cartas A, substitui o valor da carta A de 11 para 1
		int valor = valor();
		if(valor > 21) {
			for(int i = 0; i < tamanho; i++) {
				if(cartas[i].getNome() == "A") {
					cartas[i].setValor(1);
					valor -= 10;
				}
				if(valor <= 21) {
					i = tamanho;
				}
			}
		}
	}
	
	public void mostrarCartaOculta() {
		for(int i = 0; i < tamanho; i++) {
			if(!cartas[i].getVisivel()) {
				cartas[i].setVisivel(true);
			}
		}
	}
	
	public void limpar() {
		cartas = new Carta[maxCartas];
		tamanho = 0;
	}
}
