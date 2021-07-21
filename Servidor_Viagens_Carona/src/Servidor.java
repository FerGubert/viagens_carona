import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor{

    public static void main(String args[]) throws RemoteException{

        int portaSN = 1099;                                                         // porta padrão, poderia ser ocultada
        Registry referenciaServicoNomes = LocateRegistry.createRegistry(portaSN);   // cria serviço de nomes na porta

        InterfaceServ referenciaServidor = new ServImpl();                          // referência remota do servidor
        referenciaServicoNomes.rebind("Carona", referenciaServidor);                // dá nome a aplicação e o servidor dessa aplicação

    }

}
