public class Dealer implements java.io.Serializable {
	private static final long serialVersionUID = -6393141820690836607L;
	private Mao mao;
	private boolean mostrouCarta;
	
	public Dealer() {
		mao = new Mao();
		mostrouCarta = false;
	}
	
	public Mao getMao() {
		return mao;
	}

	public void setMao(Mao mao) {
		this.mao = mao;
	}
	
	public boolean getMostrouCarta() {
		return mostrouCarta;
	}
	
	public void setMostrouCarta(boolean mostrouCarta) {
		this.mostrouCarta = mostrouCarta;
	}
	
	public String toString() {
		return String.format("Mao do croupier:\n%s", mao);
	}

	public void mostrarCarta() {
		mao.mostrarCartaOculta();
		mostrouCarta = true;
	}
	
	public void adicionarCarta(Carta carta, boolean visivel) {
		carta.setVisivel(visivel);
		mao.adicionarCarta(carta);
	}
}
