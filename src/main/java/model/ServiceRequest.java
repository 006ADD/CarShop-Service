package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Представляет запрос на обслуживание автомобиля.
 * <p>
 * Этот класс содержит информацию о запросе на обслуживание, включая идентификатор запроса,
 * пользователя, который сделал запрос, автомобиль, описание запроса и его статус.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequest {
    /**
     * Уникальный идентификатор запроса.
     */
    private int id;

    /**
     * Пользователь, который сделал запрос на обслуживание.
     */
    private User user;

    /**
     * Автомобиль, который требуется обслужить.
     */
    private Car car;

    /**
     * Описание запроса на обслуживание.
     */
    private String description;

    /**
     * Статус запроса на обслуживание.
     */
    private String status;

    /**
     * Конструктор для создания объекта запроса на обслуживание с минимальным набором данных.
     * <p>
     * Этот конструктор используется, когда необходимо создать объект запроса, указывая только идентификатор и описание запроса.
     * </p>
     *
     * @param id          Уникальный идентификатор запроса.
     * @param description Описание запроса на обслуживание.
     */
    public ServiceRequest(int id, String description) {
        this.id = id;
        this.description = description;
    }
}
