package services;

import model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для управления клиентами.
 * <p>
 * Этот класс предоставляет методы для добавления, обновления, получения, удаления и вывода информации о клиентах.
 * Клиенты хранятся в карте, где ключом является уникальный идентификатор клиента.
 * </p>
 */
public class ClientService {
    /**
     * Карта, хранящая клиентов по их уникальным идентификаторам.
     */
    private final Map<Integer, User> clients = new HashMap<>();

    /**
     * Добавляет нового клиента в сервис.
     * <p>
     * Если клиент с таким же идентификатором уже существует, он будет заменен.
     * </p>
     *
     * @param client Клиент, который необходимо добавить.
     */
    public void addClient(User client) {
        clients.put(client.getId(), client);
    }

    /**
     * Обновляет информацию о клиенте.
     * <p>
     * Изменяет имя и контактную информацию клиента, если они указаны и не пусты.
     * Если клиента с указанным идентификатором не существует, метод ничего не делает.
     * </p>
     *
     * @param id              Уникальный идентификатор клиента.
     * @param newName         Новое имя клиента.
     * @param newContactInfo  Новая контактная информация клиента.
     */
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

    /**
     * Получает клиента по его уникальному идентификатору.
     *
     * @param clientId Уникальный идентификатор клиента.
     * @return Клиент с указанным идентификатором или {@code null}, если клиент не найден.
     */
    public User getUserById(int clientId) {
        return clients.get(clientId);
    }

    /**
     * Удаляет клиента по его уникальному идентификатору.
     *
     * @param id Уникальный идентификатор клиента.
     */
    public void deleteClient(int id) {
        clients.remove(id);
    }

    /**
     * Выводит информацию о всех клиентах.
     * <p>
     * Этот метод печатает в консоль информацию о каждом клиенте, хранящемся в сервисе.
     * </p>
     */
    public void getAllClient() {
        for (Map.Entry<Integer, User> entry : clients.entrySet()) {
            Integer id = entry.getKey();
            User user = entry.getValue();
            System.out.println("ID: " + id + ", User: " + user);
        }
    }
}
