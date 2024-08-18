package services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.User;
import repositories.ClientRepository;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
public class ClientService {
    private  ClientRepository clientRepository;

    public void addClient(User client) {
        clientRepository.add(client);
    }

    public void updateClient(int id, String newName, String newContactInfo) {
        clientRepository.update(id,  newName,newContactInfo);
    }
    public User getUserById(int clientId) {
        return clientRepository.getUserById(clientId);
    }

    public void deleteClient(int id) {
      clientRepository.delete(id);
    }

    public Collection<User> getAllClient(){
        return clientRepository.findAll();
    }

}
