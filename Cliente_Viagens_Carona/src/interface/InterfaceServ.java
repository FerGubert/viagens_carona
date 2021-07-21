import java.rmi.*;

public interface InterfaceServ extends Remote{

    String cadastrarUsuario(Usuario usuario) throws RemoteException;

    String consultarCaronas(String origem, String destino, String data) throws RemoteException;

    int registrarInteresse(Carona carona, byte[] assinatura) throws RemoteException;
     
    String cancelarRegistroInteresse(int idRegistro, String nome) throws RemoteException;
}
