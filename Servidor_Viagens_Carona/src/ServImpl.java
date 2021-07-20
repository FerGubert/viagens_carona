import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.util.ArrayList;

public class ServImpl extends UnicastRemoteObject implements InterfaceServ{

    private ArrayList<Usuario> usuarios;
    private ArrayList<Carona> caronas;
    private int idCarona;

    public ServImpl() throws RemoteException{
        usuarios = new ArrayList<Usuario>();
        caronas = new ArrayList<Carona>();
        idCarona = 1;
    }

    public String cadastrarUsuario(Usuario usuario) throws RemoteException{
        
        int proxId;
        if(usuarios.size() > 0){
            // verifica se o usuário já possui cadastro e, se sim, atualiza o cadastro já existente
            for(Usuario usu : usuarios){
                if(usu.getNome().equals(usuario.getNome())){
                    usu.setContato(usuario.getContato());
                    usu.setchavePublica(usuario.getchavePublica());
                    return "Usuario atualizado pois ja estava cadastrado.";
                }
            }
            // se for usuário novo especifica o id, que é sempre o incremento em 1 do último cadastrado
            proxId = usuarios.get(usuarios.size() - 1).getId() + 1;
        }else
            proxId = 1;

        // cadastra novo usuário
        usuario.setId(proxId);
        boolean cadastro = usuarios.add(usuario);
        if(cadastro)
            return "Cadastro realizado com sucesso.";
        else
            return "Erro ao realizar o cadastro.";
    }

    public String consultarCaronas(String origem, String destino, String data) throws RemoteException{
        int caronasDisponiveis = 0;
        for(Carona carona : caronas){
            if(carona.getOrigem().equals(origem) & carona.getDestino().equals(destino) & carona.getData().equals(data) & carona.getTipo() == 1)
                caronasDisponiveis++;
        }
        return "Existem " + caronasDisponiveis + " disponiveis.";
    }

    public int registrarInteresse(Usuario usuario, Carona carona) throws RemoteException{
        int idUsuario = 0;
        for(Usuario usu : usuarios){
            if(usu.getNome().equals(usuario.getNome()) & usu.getContato().equals(usuario.getContato())){
                idUsuario = usu.getId();
                break;
            }
        }
        if(idUsuario == 0)
            return 0;
        
        Carona nova_carona = new Carona();
        nova_carona.setId(idCarona);
        nova_carona.setOrigem(carona.getOrigem());
        nova_carona.setDestino(carona.getDestino());
        nova_carona.setData(carona.getData());
        nova_carona.setNumPassageiros(carona.getNumPassageiros());
        nova_carona.setReferenciaCliente(carona.getReferenciaCliente());
        nova_carona.setTipo(carona.getTipo());
        nova_carona.setIdUsuario(idUsuario);
        boolean registro = caronas.add(nova_carona);
        if(!registro)
            return 0;

        idCarona++;
        
        idUsuario = 0;
        int tipo_busca = 1 - carona.getTipo();
        for(Carona caronaTemp : caronas){
            if(caronaTemp.getOrigem().equals(carona.getOrigem()) & caronaTemp.getDestino().equals(carona.getDestino()) & caronaTemp.getData().equals(carona.getData()) & caronaTemp.getTipo() == tipo_busca){
                caronaTemp.getReferenciaCliente().notificar(usuario.getNome(), usuario.getContato(), carona.getTipo()); //notifica quem já havia registrado interesse
                idUsuario = caronaTemp.getIdUsuario();
                for(Usuario usu : usuarios){
                    if(usu.getId() == idUsuario)
                        carona.getReferenciaCliente().notificar(usu.getNome(), usu.getContato(), caronaTemp.getTipo()); //notifica quem está registrando interesse
                }
            }
        }

        return nova_carona.getId();

    }

    public String cancelarRegistroInteresse(int idRegistro) throws RemoteException{
        for(Carona carona : caronas){
            if(carona.getId() == idRegistro){
                caronas.remove(carona);
                return "Cancelamento realizado com sucesso.";
            }
        }
        return "Registro de interesse nao encontrado.";
    }

}
