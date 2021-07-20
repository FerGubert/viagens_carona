import java.io.Serializable;

public class Carona implements Serializable{

    private int id;
    private int idUsuario;
	private String origem;
    private String destino;
    private String data;
    private int numPassageiros;
    private InterfaceCli referenciaCliente;
	private int tipo; //0 para passageiro e 1 para motorista

    public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

    public int getIdUsuario() {
		return idUsuario;
	}
	
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

    public String getOrigem() {
		return origem;
	}
	
	public void setOrigem(String origem) {
		this.origem = origem;
	}
	
	public String getDestino() {
		return destino;
	}

    public void setDestino(String destino) {
		this.destino = destino;
	}

    public String getData() {
		return data;
	}

    public void setData(String data) {
		this.data = data;
	}

    public int getNumPassageiros() {
		return numPassageiros;
	}
	
	public void setNumPassageiros(int numPassageiros) {
		this.numPassageiros = numPassageiros;
	}

    public InterfaceCli getReferenciaCliente() {
		return referenciaCliente;
	}
	
	public void setReferenciaCliente(InterfaceCli referenciaCliente) {
		this.referenciaCliente = referenciaCliente;
	}

	public int getTipo() {
		return tipo;
	}
	
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
}
