package services;

import model.User;

import java.util.HashMap;
import java.util.Map;

public class ClientService {
    private final Map<Integer, User> clients = new HashMap<>();

    public void addClient(User client) {
        clients.put(client.getId(), client);
    }

    public void updateClient(int id, String newName, String newContactInfo) {
        User user = clients.get(id);
        if (user != null) {
            if (!newName.isEmpty()) {
                user.setName(newName);
            }
            if (!newContactInfo.isEmpty()) {
                user.setContactInfo(newContactInfo);
            }
            clients.put(id, user);  // Обновляем клиента в коллекции
        }
    }
    public User getUserById(int clientId) {
        return clients.get(clientId);
    }

    public void deleteClient(int id) {
        clients.remove(id);
    }

    public void getAllClient(){
        for (Map.Entry<Integer, User> entry : clients.entrySet()) {
            Integer id = entry.getKey();
            User user = entry.getValue();
            System.out.println("ID: " + id + ", User: " + user);
        }
    }

}
