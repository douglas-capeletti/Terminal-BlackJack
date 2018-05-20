public class Player implements java.io.Serializable {
	private static final long serialVersionUID = 2973749462531281181L;
	private String nome;
	private double aposta;
	private double creditos;
	private Mao mao;
	private static final int creditosIniciais = 1000;
	
	public Player(String nome) {
		this.nome = nome;
		this.aposta = 0;
		this.creditos = creditosIniciais;
		this.mao = new Mao();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getAposta() {
		return aposta;
	}

	public void setAposta(double aposta) {
		this.aposta = aposta;
		creditos -= aposta;
	}
	
	public double getCreditos() {
		return creditos;
	}
	
	public void setCreditos(double creditos) {
		this.creditos = creditos;
	}

	public Mao getMao() {
		return mao;
	}

	public void setMao(Mao mao) {
		this.mao = mao;
	}
	
	public void adicionarCarta(Carta carta) {
		carta.setVisivel(true);
		mao.adicionarCarta(carta);
	}

	public void ganhaAposta(int tipoVitoria) {
		// tipoVitoria:
		// 1 - Normal
		// 2 - Blackjack (A + carta valor 10)
		switch(tipoVitoria) {
			case 1:
				creditos += aposta * 2;
				break;
			case 2:
				creditos += aposta * 2.5;
				break;
		}
	}
	
	public void devolveAposta() {
		creditos += aposta;
	}
	
	public String toString() {
		return String.format("Nome:     %s\n", nome
						   + "Creditos: %.2f\n", creditos
						   + "Aposta:   %.2f\n", aposta
						   + "Mao:      %s\n", mao);
	}

	//public void perdeAposta() {
		//creditos -= aposta;
	//}
}
