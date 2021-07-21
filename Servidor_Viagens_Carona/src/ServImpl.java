import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Objects;

public class ServImpl extends UnicastRemoteObject implements InterfaceServ{

    private ArrayList<Usuario> usuarios;                // guarda os usuarios
    private ArrayList<Carona> caronas;                  // guarda as caronas
    private int idCarona;                               // id da carona adicionada

    public ServImpl() throws RemoteException{
        usuarios = new ArrayList<Usuario>();
        caronas = new ArrayList<Carona>();
        idCarona = 1;
    }

    public String cadastrarUsuario(Usuario usuario) throws RemoteException{
        
        if(usuarios.size() > 0){
            // verifica se o usuário já possui cadastro e, se sim, atualiza o cadastro já existente
            for(Usuario usu : usuarios){
                if(usu.getNome().equals(usuario.getNome())){
                    usu.setContato(usuario.getContato());
                    usu.setchavePublica(usuario.getchavePublica());
                    return "Usuario atualizado pois ja estava cadastrado.";
                }
            }
        }

        // cadastra novo usuário
        boolean cadastro = usuarios.add(usuario);
        if(cadastro)
            return "\tCADASTRO REALIZADO COM SUCESSO. :)\t\t#\n";
        else
            return "\tERRO AO REALIZAR O CADASTRO. :(\t\t#\n";
    }

    public String consultarCaronas(String origem, String destino, String data) throws RemoteException{
        int caronasDisponiveis = 0;
        for(Carona carona : caronas){
            if(carona.getOrigem().equals(origem) & carona.getDestino().equals(destino) & carona.getData().equals(data) & carona.getTipo() == 1)
                caronasDisponiveis++;
        }
        return "Existem " + caronasDisponiveis + " caronas disponiveis.";
    }

    private Usuario validacaoUsuarioExistente(Carona carona){
        Usuario usuario = new Usuario();
        if(usuarios.size() > 0){
            for(Usuario usu : usuarios){
                if(usu.getNome().equals(carona.getNome())){
                    usuario = usu;
                    return usuario;
                }
            }
        }else
            return null;
        
        return null;
    }

    private static String criaMensagem(Carona carona){
        String msg = carona.getNome() + " " + carona.getOrigem() + " " + carona.getDestino() + " " + carona.getData();
		return msg;
    }

    private boolean validaAssinatura(byte[] assinatura, String msg, Usuario usuario){
        try{
        	Signature caronaSig = Signature.getInstance("DSA");
			caronaSig.initVerify(usuario.getchavePublica());
			caronaSig.update(msg.getBytes());
			
			if(!caronaSig.verify(assinatura))
				return false;
            else
                return true;
		}catch(NoSuchAlgorithmException | InvalidKeyException | SignatureException e){
			e.printStackTrace();
            return false;
		}
    }

    private static void preencheCarona(Carona novaCarona, Carona carona, int idCarona){
        novaCarona.setId(idCarona);
        novaCarona.setNome(carona.getNome());
        novaCarona.setOrigem(carona.getOrigem());
        novaCarona.setDestino(carona.getDestino());
        novaCarona.setData(carona.getData());
        novaCarona.setNumPassageiros(carona.getNumPassageiros());
        novaCarona.setReferenciaCliente(carona.getReferenciaCliente());
        novaCarona.setTipo(carona.getTipo());
    }

    private void notificaClientes(Carona carona, ArrayList<Carona> caronasNotificadas, Carona novaCarona, Usuario usuario){
        int tipo_busca = 1 - carona.getTipo();
        
        try {
	        for(Carona caronaTemp : caronas){
	            if(caronaTemp.getOrigem().equals(carona.getOrigem()) & caronaTemp.getDestino().equals(carona.getDestino()) & caronaTemp.getData().equals(carona.getData()) & caronaTemp.getTipo() == tipo_busca){
	                
						caronaTemp.getReferenciaCliente().notificar(carona.getNome(), usuario.getContato(), carona.getTipo());
					 //notifica quem já havia registrado interesse
	                String nome = caronaTemp.getNome();
	                for(Usuario usu : usuarios){
	                    if(usu.getNome().equals(nome))
	                        carona.getReferenciaCliente().notificar(usu.getNome(), usu.getContato(), caronaTemp.getTipo()); //notifica quem está registrando interesse    
	                }
	
	                if(caronaTemp.getTipo() == 1)
	                    caronaTemp.setNumPassageiros(caronaTemp.getNumPassageiros() - 1);
	                else{
	                    for(Carona caronaRegistrada : caronas){
	                        if(caronaRegistrada.getId() == novaCarona.getId())
	                            caronaRegistrada.setNumPassageiros(caronaRegistrada.getNumPassageiros() - 1);
	                    }
	                }
	                caronasNotificadas.add(caronaTemp);
	                caronasNotificadas.add(novaCarona);
	            }
	        } 
        } catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void removeCaronasNotificadasCheias(Carona carona, ArrayList<Carona> caronasNotificadas){
        for(Carona caronaNotificada : caronasNotificadas){
            if(caronaNotificada.getTipo() == 0)
                caronas.remove(caronaNotificada);
            else if(caronaNotificada.getNumPassageiros() == 0)
                caronas.remove(caronaNotificada);   
        }
    }

    public int registrarInteresse(Carona carona, byte[] assinatura) throws RemoteException{


        Usuario usuario = new Usuario();
        
        usuario = validacaoUsuarioExistente(carona);
/*      if(usuarios.size() > 0){
            for(Usuario usu : usuarios){
                if(usu.getNome().equals(carona.getNome())){
                    usuario = usu;
                    break;
                }
            }
        }else
            return 0;
*/


        if(Objects.isNull(usuario.getNome()))
            return 0;

        // validando a assinatura digital
       // try{
            String msg = criaMensagem(carona);
            if(!validaAssinatura(assinatura, msg, usuario))
                return 0;

/*	        Signature caronaSig = Signature.getInstance("DSA");
			caronaSig.initVerify(usuario.getchavePublica());
			caronaSig.update(msg.getBytes());
			
			if(!caronaSig.verify(assinatura))
				return 0;
		}catch(NoSuchAlgorithmException | InvalidKeyException | SignatureException e){
			e.printStackTrace();
            return 0;
		}
*/

        Carona novaCarona = new Carona();
/*      novaCarona.setId(idCarona);
        novaCarona.setNome(carona.getNome());
        novaCarona.setOrigem(carona.getOrigem());
        novaCarona.setDestino(carona.getDestino());
        novaCarona.setData(carona.getData());
        novaCarona.setNumPassageiros(carona.getNumPassageiros());
        novaCarona.setReferenciaCliente(carona.getReferenciaCliente());
        novaCarona.setTipo(carona.getTipo());
*/

        preencheCarona(novaCarona, carona, idCarona);

        boolean registro = caronas.add(novaCarona);

        if(!registro)
            return 0;

        idCarona++;
        
        ArrayList<Carona> caronasNotificadas = new ArrayList<Carona>();

        notificaClientes(carona, caronasNotificadas, novaCarona, usuario);
/*        int tipo_busca = 1 - carona.getTipo();
        for(Carona caronaTemp : caronas){
            if(caronaTemp.getOrigem().equals(carona.getOrigem()) & caronaTemp.getDestino().equals(carona.getDestino()) & caronaTemp.getData().equals(carona.getData()) & caronaTemp.getTipo() == tipo_busca){
                caronaTemp.getReferenciaCliente().notificar(carona.getNome(), usuario.getContato(), carona.getTipo()); //notifica quem já havia registrado interesse
                String nome = caronaTemp.getNome();
                for(Usuario usu : usuarios){
                    if(usu.getNome().equals(nome))
                        carona.getReferenciaCliente().notificar(usu.getNome(), usu.getContato(), caronaTemp.getTipo()); //notifica quem está registrando interesse    
                }

                if(caronaTemp.getTipo() == 1)
                    caronaTemp.setNumPassageiros(caronaTemp.getNumPassageiros() - 1);
                else{
                    for(Carona caronaRegistrada : caronas){
                        if(caronaRegistrada.getId() == novaCarona.getId())
                            caronaRegistrada.setNumPassageiros(caronaRegistrada.getNumPassageiros() - 1);
                    }
                }
                caronasNotificadas.add(caronaTemp);
                caronasNotificadas.add(novaCarona);
            }
        }
*/

        removeCaronasNotificadasCheias(carona, caronasNotificadas);
/*        for(Carona caronaNotificada : caronasNotificadas){
            if(caronaNotificada.getTipo() == 0)
                caronas.remove(caronaNotificada);
            else if(caronaNotificada.getNumPassageiros() == 0)
                caronas.remove(caronaNotificada);   
        }
*/
        return novaCarona.getId();

    }

    public String cancelarRegistroInteresse(int idRegistro, String nome) throws RemoteException{
        for(Carona carona : caronas){
            if(carona.getId() == idRegistro){
                if(carona.getNome().equals(nome)){
                    caronas.remove(carona);
                    return "Cancelamento realizado com sucesso.";
                }
                return "Este registro corresponde a outro usuario e, portanto, nao pode ser cancelado.";
            }
        }
        return "Registro de interesse nao encontrado.";
    }

}
