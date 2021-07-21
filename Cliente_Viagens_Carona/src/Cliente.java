import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Scanner;

public class Cliente{

    public static void main(String args[]) throws RemoteException, NotBoundException, NoSuchAlgorithmException{

        Registry referenciaServicoNomes = LocateRegistry.getRegistry(); // assume localhost e porta default 1099.
        InterfaceServ referenciaServidor = (InterfaceServ) referenciaServicoNomes.lookup("Carona");
        CliImpl referenciaCliente = new CliImpl(referenciaServidor);
        Usuario usuario = new Usuario();
        Carona carona = new Carona();

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
        SecureRandom secRan = new SecureRandom();
        kpg.initialize(512, secRan);
        KeyPair keyP = kpg.generateKeyPair();
        PublicKey pubKey = keyP.getPublic();
        PrivateKey priKey = keyP.getPrivate();
        byte[] assinatura;

        Scanner input = new Scanner(System.in);
        int op = -1;
        while(op != 0){
            op = menu(input);
        
            if(op == 1){
                pedirDadosUsuario(input, usuario);
                usuario.setchavePublica(pubKey);

                String retorno = referenciaServidor.cadastrarUsuario(usuario);
                System.out.println(retorno);

            }else if(op == 2){
                pedirDadosCarona(input, carona);
                carona.setReferenciaCliente(referenciaCliente);

                String msg = criarMensagem(carona);
				assinatura = assinarMensagem(priKey, msg);

                int retorno = referenciaServidor.registrarInteresse(carona, assinatura);
                System.out.println(retorno);
                if(retorno == 0)
                    System.out.println("Erro ao registrar interesse.");
                else
                    System.out.println("Registro de interesse realizado com sucesso. Caso queira cancelar seu ID e " + retorno + ".");
            
            }else if(op == 3){
                pedirDadosConsultaCarona(input, carona);
                String retorno = referenciaServidor.consultarCaronas(carona.getOrigem(), carona.getDestino(), carona.getData());
                System.out.println(retorno);

            }else if(op == 4){
                pedirDadosCancelamento(input, carona);
                String retorno = referenciaServidor.cancelarRegistroInteresse(carona.getId(), carona.getNome());
                System.out.println(retorno);
            
            }else if(op != 0)
                System.out.println("Opcao invalida.");

        }
        input.close();
        System.exit(0);

    }

    public static int menu(Scanner input){
        System.out.println("MENU\n1 - Quero me cadastrar");
        System.out.println("2 - Quero registrar interesse");
        System.out.println("3 - Quero consultar caronas disponiveis");
        System.out.println("4 - Quero cancelar um registro de interesse");
        System.out.println("0 - Sair");
        int op = input.nextInt();
        return op;
    }

    public static void pedirDadosUsuario(Scanner input, Usuario usuario){
        System.out.println("CADASTRO DE USUARIO");
		
		System.out.println("Nome:");
		usuario.setNome(input.next());
		
		System.out.println("Contato:");
		usuario.setContato(input.next());
	}

    public static void pedirDadosCarona(Scanner input, Carona carona){
        System.out.println("REGISTRO DE INTERESSE");
		
        System.out.println("Nome:");
		carona.setNome(input.next());

		System.out.println("Origem:");
		carona.setOrigem(input.next());
		
		System.out.println("Destino:");
		carona.setDestino(input.next());

        System.out.println("Data:");
		carona.setData(input.next());

        System.out.println("Esta interessado em carona ou passageiro:");
        if(input.next().equals("carona"))
		    carona.setTipo(0);
        else{
            carona.setTipo(1);
            System.out.println("Numero de passageiros:");
		    carona.setNumPassageiros(Integer.parseInt(input.next()));
        }
	}

    public static void pedirDadosConsultaCarona(Scanner input, Carona carona){
        System.out.println("CONSULTA DE CARONAS");
		
		System.out.println("Origem:");
		carona.setOrigem(input.next());
		
		System.out.println("Destino:");
		carona.setDestino(input.next());

        System.out.println("Data:");
		carona.setData(input.next());
	}

    public static void pedirDadosCancelamento(Scanner input, Carona carona){
        System.out.println("CANCELAMENTO DE REGISTRO DE INTERESSE");
		
		System.out.println("Nome:");
		carona.setNome(input.next());
		
		System.out.println("ID:");
		carona.setId(Integer.parseInt(input.next()));
	}

    public static String criarMensagem(Carona carona){
    	String msg = carona.getNome() + " " + carona.getOrigem() + " " + carona.getDestino() + " " + carona.getData();
    	return msg;
    }
    
    public static byte[] assinarMensagem(PrivateKey priKey, String msg){
    	byte[] assinatura = null;
        try{
    		Signature sign;
			sign = Signature.getInstance("DSA");
			sign.initSign(priKey);
			sign.update(msg.getBytes());
			assinatura = sign.sign();
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			e.printStackTrace();
            return null;
		}
		return assinatura;
    }
}
