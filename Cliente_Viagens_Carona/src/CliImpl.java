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
        
        System.out.println("##################################################################################################################");
    	System.out.println("------------------------- AÇÕES ----------------------- # ---------------------- NOTIFICAÇÕES --------------------");
    	System.out.println("\t\t\t\t\t\t\t#");
        System.out.println("\t\t\t\t\t\t\t#\t\tNOTIFICACAO PARA O " + usuario[1]);
        System.out.println("\t\t\t\t\t\t\t#");
        System.out.println("\t\t\t\t\t\t\t#\tOLA " + usuario[1] + " SEGUEM OS DADOS DO " + usuario[0] + ":");
        System.out.println("\t\t\t\t\t\t\t#");
        System.out.println("\t\t\t\t\t\t\t# NOME:\t\t " + nome );
        System.out.println("\t\t\t\t\t\t\t#");
        System.out.println("\t\t\t\t\t\t\t# CONTATO:\t " + contato);
        System.out.println("\t\t\t\t\t\t\t#");
        System.out.println("\t\t\t\t\t\t\t#\t\t(: tenha uma ótima viagem :)");

    }
}
