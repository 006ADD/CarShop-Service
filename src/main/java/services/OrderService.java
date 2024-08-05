package services;

import model.Order;
import model.ServiceRequest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для управления заказами и запросами на услуги.
 * <p>
 * Этот класс предоставляет методы для создания, обновления, получения и отмены заказов.
 * Запросы на услуги хранятся в отдельной карте, но в текущей реализации не используются.
 * </p>
 */
public class OrderService {
    /**
     * Карта, хранящая заказы по их уникальным идентификаторам.
     */
    private Map<Integer, Order> orders = new HashMap<>();

    /**
     * Карта, хранящая запросы на услуги по их уникальным идентификаторам.
     * <p>
     * В текущей реализации запросы на услуги не используются, но структура данных
     * предусмотрена для будущего расширения функциональности.
     * </p>
     */
    private Map<Integer, ServiceRequest> serviceRequests = new HashMap<>();

    /**
     * Создает новый заказ.
     * <p>
     * Добавляет заказ в сервис. Если заказ с таким же идентификатором уже существует,
     * он будет заменен.
     * </p>
     *
     * @param order Заказ, который необходимо создать.
     */
    public void createOrder(Order order) {
        orders.put(order.getId(), order);
    }

    /**
     * Обновляет статус заказа.
     * <p>
     * Если заказ с указанным идентификатором существует, его статус будет обновлен.
     * </p>
     *
     * @param id     Уникальный идентификатор заказа.
     * @param status Новый статус заказа.
     */
    public void updateOrderStatus(int id, String status) {
        Order order = orders.get(id);
        if (order != null) {
            order.setStatus(status);
        }
    }

    /**
     * Получает все заказы.
     *
     * @return Коллекция всех заказов.
     */
    public Collection<Order> getAllOrders() {
        return orders.values();
    }

    /**
     * Отменяет заказ по его уникальному идентификатору.
     *
     * @param id Уникальный идентификатор заказа.
     */
    public void cancelOrder(int id) {
        orders.remove(id);
    }

    /**
     * Получает заказ по его уникальному идентификатору.
     *
     * @param orderId Уникальный идентификатор заказа.
     * @return Заказ с указанным идентификатором или {@code null}, если заказ не найден.
     */
    public Order getOrderById(int orderId) {
        return orders.get(orderId);
    }
}
