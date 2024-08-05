package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Представляет автомобиль с его характеристиками.
 * <p>
 * Этот класс содержит информацию о различных атрибутах автомобиля, таких как марка, модель, год выпуска и состояние.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    /**
     * Уникальный идентификатор автомобиля.
     */
    private int id;

    /**
     * Марка автомобиля.
     */
    private String brand;

    /**
     * Модель автомобиля.
     */
    private String model;

    /**
     * Год выпуска автомобиля.
     */
    private int year;

    /**
     * Цена автомобиля.
     */
    private double price;

    /**
     * Состояние автомобиля.
     * <p>
     * Может быть "new" (новый) или "used" (б/у).
     * </p>
     */
    private String condition;

    /**
     * Статус автомобиля.
     */
    private String status;

    /**
     * Конструктор для создания объекта автомобиля с минимальными данными.
     * <p>
     * Этот конструктор используется, когда необходимо создать объект автомобиля, указывая только идентификатор,
     * марку, модель и год выпуска.
     * </p>
     *
     * @param id    Уникальный идентификатор автомобиля.
     * @param brand Марка автомобиля.
     * @param model Модель автомобиля.
     * @param year  Год выпуска автомобиля.
     */
    public Car(int id, String brand, String model, int year) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
    }
}
