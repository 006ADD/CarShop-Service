package services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для управления запросами на услуги.
 * <p>
 * Этот класс предоставляет методы для создания, обновления, получения и получения всех запросов на услуги.
 * Запросы на услуги хранятся в карте, где ключом является уникальный идентификатор запроса.
 * </p>
 */
public class RequestService {
    /**
     * Карта, хранящая запросы на услуги по их уникальным идентификаторам.
     */
    private final Map<Integer, model.ServiceRequest> serviceRequests = new HashMap<>();

    /**
     * Создает новый запрос на услугу.
     * <p>
     * Добавляет запрос на услугу в сервис. Если запрос с таким же идентификатором уже существует,
     * он будет заменен.
     * </p>
     *
     * @param request Запрос на услугу, который необходимо создать.
     */
    public void createServiceRequest(model.ServiceRequest request) {
        serviceRequests.put(request.getId(), request);
    }

    /**
     * Обновляет статус запроса на услугу.
     * <p>
     * Если запрос с указанным идентификатором существует, его статус будет обновлен.
     * </p>
     *
     * @param id     Уникальный идентификатор запроса на услугу.
     * @param status Новый статус запроса.
     */
    public void updateServiceRequestStatus(int id, String status) {
        model.ServiceRequest serviceRequest = serviceRequests.get(id);
        if (serviceRequest != null) {
            serviceRequest.setStatus(status);
        }
    }

    /**
     * Получает запрос на услугу по его уникальному идентификатору.
     *
     * @param id Уникальный идентификатор запроса на услугу.
     * @return Запрос на услугу с указанным идентификатором или {@code null}, если запрос не найден.
     */
    public model.ServiceRequest getServiceRequestById(int id) {
        return serviceRequests.get(id);
    }

    /**
     * Получает все запросы на услуги.
     *
     * @return Коллекция всех запросов на услуги.
     */
    public Collection<model.ServiceRequest> getAllServiceRequests() {
        return serviceRequests.values();
    }
}
