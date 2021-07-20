import java.rmi.*;

public interface InterfaceServ extends Remote{

    String cadastrarUsuario(Usuario usuario) throws RemoteException;

    String consultarCaronas(String origem, String destino, String data) throws RemoteException;

    int registrarInteresse(Usuario usuario, Carona carona) throws RemoteException;
     
    String cancelarRegistroInteresse(int idRegistro) throws RemoteException;
}
