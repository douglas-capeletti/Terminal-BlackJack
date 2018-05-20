public class Carta implements java.io.Serializable {
	private static final long serialVersionUID = 6400728909184645970L;
	private String naipe;
	private String nome;
	private int valor;
	private boolean visivel;
	
	public Carta(String naipe, String nome, int valor, boolean visivel) {
		this.naipe = naipe;
		this.nome = nome;
		this.valor = valor;
		this.visivel = visivel;
	}

	public String getNaipe() {
		return naipe;
	}

	public void setNaipe(String naipe) {
		this.naipe = naipe;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	public boolean getVisivel() {
		return visivel;
	}

	public void setVisivel(boolean visivel) {
		this.visivel = visivel;
	}

	public String toString() {
		String str = "";
		if(visivel) {
			str += String.format("%s de %s, vale %d", nome, naipe, valor);
		} else {
			str += "Carta virada para baixo";
		}
		return str;
	}
	
	public String[] toAsciiArt() {
		String[] str = new String[6];
		str[0] = ".------.";
		if(visivel) {
			if(nome.equals("10")) {
				str[1] = "|" + nome + "--. |";
			} else {
				str[1] = "|" + nome + ".--. |";
			}
			switch(naipe) {
				case "Copas":
					str[2] = "| (\\/) |";
					str[3] = "| :\\/: |";
					break;
				case "Ouro":
					str[2] = "| :/\\: |";
					str[3] = "| :\\/: |";
					break;
				case "Espada":
					str[2] = "| :/\\: |";
					str[3] = "| (__) |";
					break;
				case "Paus":
					str[2] = "| :(): |";
					str[3] = "| ()() |";
					break;
			}
			if(nome.equals("10")) {
				str[4] = "| '--" + nome + "|";
			} else {
				str[4] = "| '--'" + nome + "|";
			}
		} else {
			for(int i=1; i<str.length-1; i++) {
				str[i] = "|::::::|";
			}
		}
		str[5] = "`------'";
		
		return str;
	}

}
