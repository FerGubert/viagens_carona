import java.io.Serializable;
import java.security.PublicKey;

public class Usuario implements Serializable{
    
    private String nome;
	private String contato;
    private PublicKey chavePublica;

    public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

	public PublicKey getchavePublica() {
		return chavePublica;
	}

    public void setchavePublica(PublicKey chavePublica) {
		this.chavePublica = chavePublica;
	}

}
