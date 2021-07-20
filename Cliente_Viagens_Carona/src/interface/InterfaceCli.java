import java.rmi.*;

public interface InterfaceCli extends Remote{

    void notificar(String nome, String contato, int tipo) throws RemoteException;

}