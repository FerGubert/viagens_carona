import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor{

    public static void main(String args[]) throws RemoteException{

        int portaSN = 1099;
        Registry referenciaServicoNomes = LocateRegistry.createRegistry(portaSN);

        InterfaceServ referenciaServidor = new ServImpl();
        referenciaServicoNomes.rebind("Carona", referenciaServidor);

    }

}
