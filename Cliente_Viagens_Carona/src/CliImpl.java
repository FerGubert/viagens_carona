import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CliImpl extends UnicastRemoteObject implements InterfaceCli{
    
    InterfaceServ referenciaServidor;

    public CliImpl(InterfaceServ referenciaServidor) throws RemoteException{
        this.referenciaServidor = referenciaServidor;
    }

    public void notificar(String nome, String contato, int tipo) throws RemoteException{
        String[] usuario = new String[2];
        
        
        if(tipo == 0) {
        	usuario[0] = "PASSAGEIRO";
            usuario[1] = "MOTORISTA";
        }
        else {
        	usuario[0] = "MOTORISTA";
            usuario[1] = "PASSAGEIRO";
        }
        
        System.out.println("#################################################");
        System.out.println();
        System.out.println("\tNOTIFICACAO PARA O " + usuario[1]);
        System.out.println();
        System.out.println("OLA " + usuario[1] + " SEGUEM OS DADOS DO " + usuario[0] + ":");
        System.out.println();
        System.out.println("\tNOME:\t\t " + nome );
        System.out.println();
        System.out.println("\tCONTATO:\t " + contato);
        System.out.println();
        System.out.println("\t(: tenha uma ótima viagem :)");
        System.out.println("#################################################");

    }
}
