import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CliImpl extends UnicastRemoteObject implements InterfaceCli{
    
    InterfaceServ referenciaServidor;

    public CliImpl(InterfaceServ referenciaServidor) throws RemoteException{
        this.referenciaServidor = referenciaServidor;
    }

    public void notificar(String nome, String contato, int tipo) throws RemoteException{
        String usuario;
        if(tipo == 0)
            usuario = "passageiro";
        else
            usuario = "motorista";
        System.out.println("NOTIFICACAO\nSeguem os dados do " + usuario + ":");
        System.out.println("Nome: " + nome + "\nContato: " + contato);
        
    }
}
