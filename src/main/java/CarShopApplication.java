import model.*;
import model.ServiceRequest;
import services.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
/**
 * Главный класс приложения для управления автосалоном.
 * Обеспечивает интерфейс командной строки для управления автомобилями, заказами, клиентами,
 * сотрудниками, журналами аудита и запросами на обслуживание.
 * <p>
 * Приложение поддерживает следующие операции:
 * <ul>
 *   <li>Управление автомобилями: добавление, редактирование, удаление и просмотр автомобилей.</li>
 *   <li>Управление заказами: просмотр заказов, изменение статуса заказа и оформление новых заказов.</li>
 *   <li>Управление клиентами: просмотр и удаление клиентов.</li>
 *   <li>Управление сотрудниками: просмотр и удаление сотрудников.</li>
 *   <li>Журналы аудита: просмотр журналов действий, выполненных в системе.</li>
 *   <li>Управление запросами на обслуживание: просмотр запросов на обслуживание и изменение их статуса.</li>
 * </ul>
 * </p>
 *
 * Использование:
 * <pre>
 * {@code
 * java CarShopApplication
 * }
 * </pre>
 *
 * Зависимости:
 * <ul>
 *   <li>CarService: Сервис для управления данными об автомобилях.</li>
 *   <li>OrderService: Сервис для управления данными о заказах.</li>
 *   <li>ClientService: Сервис для управления данными о клиентах.</li>
 *   <li>EmployeeService: Сервис для управления данными о сотрудниках.</li>
 *   <li>AuditService: Сервис для регистрации аудита действий.</li>
 *   <li>ServiceRequestService: Сервис для управления запросами на обслуживание.</li>
 * </ul>
 *
 * Обработка исключений:
 * Обрабатывает неверный ввод и возможные исключения во время операций, предлагая
 * пользователям повторить попытку.
 *
 */
public class CarShopApplication {
    private static final UserService userService = new UserService();
    private static final CarService carService = new CarService();
    private static final OrderService orderService = new OrderService();
    private static final RequestService SERVICE_REQUEST_SERVICE = new RequestService();

    private static final ClientService clientService = new ClientService();
    private static final AuditService auditService = new AuditService();


    private static final EmployeeService employeeService = new EmployeeService();

    private static User currentUser;

    /**
     * Главная точка входа для приложения автосалона.
     * Инициализирует приложение и отображает главное меню пользователю.
     *
     * @param args Аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (currentUser == null) {
                showLoginMenu(scanner);
            } else {
                showMainMenu(scanner);
            }
        }
    }

    /**
     * Отображает меню входа в систему и обрабатывает выбор пользователя.
     * <p>
     * Меню предлагает следующие варианты:
     * <ul>
     *   <li>Регистрация нового пользователя.</li>
     *   <li>Авторизация существующего пользователя.</li>
     *   <li>Выход из программы.</li>
     * </ul>
     * </p>
     *
     * Принимает ввод от пользователя и выполняет соответствующие действия на основе выбора.
     * Обрабатывает неверный ввод и сообщает пользователю о необходимости повторного ввода.
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для чтения ввода пользователя.
     * @see #register(Scanner)
     * @see #login(Scanner)
     */
    private static void showLoginMenu(Scanner scanner) {
        System.out.println("Welcome to the car service!");
        System.out.println("1. Registration");
        System.out.println("2. Authorization");
        System.out.println("3. Exit");
        System.out.print("Select the desired option: ");
        try {
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    register(scanner);
                    break;
                case 2:
                    login(scanner);
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("The wrong option.");
            }
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }
    }

    /**
     * Регистрация нового пользователя в системе.
     * <p>
     * Собирает необходимые данные от пользователя, такие как имя, электронная почта, пароль и т.д.,
     * и добавляет нового пользователя в систему.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для чтения ввода пользователя.
     */
    private static void register(Scanner scanner) {
        try {
            System.out.print("Enter the user name: ");
            String username = scanner.nextLine();
            System.out.print("Enter the password: ");
            String password = scanner.nextLine();
            System.out.print("Enter the role (admin, manager, client): ");
            String role = scanner.nextLine();
            System.out.print("Enter the contact information: ");
            String contactInfo = scanner.nextLine();

            User user = new User(userService.getAllUsers().size() + 1, username, role, contactInfo, password);
            userService.registerUser(user);
            System.out.println("The user has been successfully registered.");
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }
    }

    /**
     * Авторизация существующего пользователя в системе.
     * <p>
     * Запрашивает у пользователя учетные данные (например, электронная почта и пароль)
     * и проверяет их правильность для входа в систему.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для чтения ввода пользователя.
     */
    private static void login(Scanner scanner) {
        try {
            System.out.print("Enter the user name: ");
            String username = scanner.nextLine();
            System.out.print("Enter the password: ");
            String password = scanner.nextLine();

            currentUser = userService.loginUser(username, password);

            if (currentUser != null) {
                System.out.println("Authorization is successful.");
                auditService.logAction(currentUser, "Logged in");
            } else {
                System.out.println("Authorization error.");
            }
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }
    }

    /**
     * Отображает главное меню и обрабатывает выбор пользователя в зависимости от его роли.
     * <p>
     * Пункты главного меню варьируются в зависимости от роли пользователя:
     * для администратора, менеджера или клиента доступны различные опции.
     * Метод обрабатывает ввод пользователя и вызывает соответствующие методы управления или просмотра
     * в зависимости от выбора пользователя.
     * </p>
     * <ul>
     * <li>Администраторы имеют доступ к управлению автомобилями, заказами, клиентами, сотрудниками,
     * просмотру журналов аудита и управлению заявками на обслуживание.</li>
     * <li>Менеджеры имеют доступ к управлению автомобилями, заказами и заявками на обслуживание.</li>
     * <li>Клиенты могут просматривать автомобили и оформлять заказы.</li>
     * </ul>
     * <p>
     * В метод также включены опции для выхода из системы и завершения работы приложения.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для чтения ввода пользователя.
     */
    private static void showMainMenu(Scanner scanner) {
        System.out.println("Main Menu:");
        if (userService.isAdmin(currentUser)) {
            System.out.println("    1. Car management");
            System.out.println("    2. Order Management");
            System.out.println("    3. Customer Management");
            System.out.println("    4. Employee Management");
            System.out.println("    5. Viewing audit logs");
            System.out.println("    6. Managing service requests");
        } else if (userService.isManager(currentUser)) {
            System.out.println("    1. Car management");
            System.out.println("    2. Order Management");
            System.out.println("    3. Managing service requests");
        } else if (userService.isClient(currentUser)) {
            System.out.println("    1. View cars");
            System.out.println("    2. Place an order");

        }
        System.out.println("7. Log out");
        System.out.println("8. Exit");
        System.out.print("Select the desired option: ");
        try {
            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {
                case 1:
                    if (userService.isAdmin(currentUser) || userService.isManager(currentUser)) {
                        manageCars(scanner);
                    } else if (userService.isClient(currentUser)) {
                        viewCars(scanner);
                    }
                    break;
                case 2:
                    if (userService.isAdmin(currentUser) || userService.isManager(currentUser)) {
                        manageOrders(scanner);
                    } else if (userService.isClient(currentUser)) {
                        placeOrder(scanner);
                    }
                    break;
                case 3:
                    if (userService.isAdmin(currentUser)) {
                        manageClients(scanner);
                    }
                    break;
                case 4:
                    if (userService.isAdmin(currentUser)) {
                        manageEmployees(scanner);
                    }
                    break;
                case 5:
                    if (userService.isAdmin(currentUser)) {
                        viewAuditLogs(scanner);
                    }
                    break;
                case 6:
                    if (userService.isAdmin(currentUser) || userService.isManager(currentUser)) {
                        manageServiceRequests(scanner);
                    }
                    break;
                case 7:
                    currentUser = null;
                    System.out.println("You are logged out.");
                    break;
                case 8:
                    System.exit(0);
                default:
                    System.out.println("The wrong option.");
            }
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }
    }

    /**
     * Отображает меню управления автомобилями и обрабатывает выбор пользователя.
     * <p>
     * Пункты меню включают добавление, редактирование, удаление и просмотр автомобилей.
     * Метод вызывает соответствующие функции в зависимости от выбора пользователя.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для чтения ввода пользователя.
     */
    private static void manageCars(Scanner scanner) {
        System.out.println("Car Management:");
        System.out.println("1. Add a car");
        System.out.println("2. Edit the car");
        System.out.println("3. Remove the car");
        System.out.println("4. View cars");
        System.out.print("Select the desired option: ");
        try {
            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {
                case 1:
                    addCar(scanner);
                    break;
                case 2:
                    editCar(scanner);
                    break;
                case 3:
                    deleteCar(scanner);
                    break;
                case 4:
                    viewCars(scanner);
                    break;
                default:
                    System.out.println("The wrong option.");
            }
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }
    }

    /**
     * Добавляет новый автомобиль в систему.
     * <p>
     * Метод запрашивает у пользователя данные об автомобиле, такие как бренд, модель,
     * год выпуска, цена, состояние и статус. Затем создает объект {@link Car} и добавляет его в систему.
     * Записывает действие в журнал аудита.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для чтения ввода пользователя.
     */
    private static void addCar(Scanner scanner) {
        try {
            System.out.print("Enter the brand: ");
            String brand = scanner.nextLine();
            System.out.print("Enter the model: ");
            String model = scanner.nextLine();
            System.out.print("Enter the year of release: ");
            int year = scanner.nextInt();
            System.out.print("Enter the price: ");
            double price = scanner.nextDouble();
            scanner.nextLine(); // consume newline
            System.out.print("Enter the status (new, used): ");
            String condition = scanner.nextLine();
            System.out.print("Enter the status (available, sold): ");
            String status = scanner.nextLine();

            Car car = new Car(carService.getAllCars().size() + 1, brand, model, year, price, condition, status);
            carService.addCar(car);
            auditService.logAction(currentUser, "A car has been added: " + brand + " " + model);
            System.out.println("The car has been added successfully.");
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }
    }

    /**
     * Редактирует информацию о существующем автомобиле.
     * <p>
     * Метод запрашивает у пользователя идентификатор автомобиля для редактирования и,
     * если автомобиль найден, предлагает ввести новые значения для бренда, модели,
     * года выпуска, цены, состояния и статуса. Затем обновляет информацию об автомобиле
     * и записывает действие в журнал аудита.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для чтения ввода пользователя.
     */
    private static void editCar(Scanner scanner) {
        System.out.print("Enter the car ID to edit: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine(); // consume newline

            Car car = carService.getCar(id);
            if (car != null) {
                System.out.print("Enter a new brand (current: " + car.getBrand() + "): ");
                String brand = scanner.nextLine();
                System.out.print("Enter a new model (the current one: " + car.getModel() + "): ");
                String model = scanner.nextLine();
                System.out.print("Enter the new release year (current: " + car.getYear() + "): ");
                int year = scanner.nextInt();
                System.out.print("Enter a new price (current: " + car.getPrice() + "): ");
                double price = scanner.nextDouble();
                scanner.nextLine();
                System.out.print("Enter a new state (new, used): ");
                String condition = scanner.nextLine();
                System.out.print("Enter a new status (available, sold): ");
                String status = scanner.nextLine();

                Car updatedCar = new Car(id, brand, model, year, price, condition, status);
                carService.updateCar(id, updatedCar);
                auditService.logAction(currentUser, "The car ID has been edited: " + id);
                System.out.println("The car has been successfully updated.");
            } else {
                System.out.println("The car was not found.");
            }
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }
    }

    /**
     * Удаляет автомобиль из системы.
     * <p>
     * Метод запрашивает у пользователя идентификатор автомобиля для удаления и,
     * если автомобиль найден, удаляет его из системы и записывает действие в журнал аудита.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для чтения ввода пользователя.
     */
    private static void deleteCar(Scanner scanner) {
        System.out.print("Enter the car ID to delete: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine(); // consume newline

            Car car = carService.getCar(id);
            if (car != null) {
                carService.deleteCar(id);
                auditService.logAction(currentUser, "The car ID was deleted: " + id);
                System.out.println("The car was successfully deleted.");
            } else {
                System.out.println("The car was not found.");
            }
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }
    }

    /**
     * Отображает список доступных автомобилей.
     * <p>
     * Метод извлекает все автомобили из {@link CarService} и выводит их информацию, включая
     * идентификатор, бренд, модель, год выпуска, цену, статус и состояние.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для ввода пользователя, хотя в данном методе не используется.
     */
    private static void viewCars(Scanner scanner) {
        System.out.println("Available cars:");
        for (Car car : carService.getAllCars()) {
            System.out.println(car.getId() + ": " + car.getBrand() + " " + car.getModel() + " (" + car.getYear() + ") - $" + car.getPrice() + " [" + car.getStatus() + "]" + " [" + car.getCondition()+"] ");
        }
    }

    /**
     * Отображает журналы аудита.
     * <p>
     * Метод извлекает все записи из {@link AuditService} и выводит их, включая
     * временную метку, имя пользователя, который совершил действие, и описание самого действия.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для ввода пользователя, хотя в данном методе не используется.
     */
    private static void viewAuditLogs(Scanner scanner) {
        System.out.println("Audit logs:");
        for (AuditLog log : auditService.getLogs()) {
            System.out.println(log.getTimestamp() + ": " + log.getUser().getName() + " - " + log.getAction());
        }
    }

    /**
     * Управление заказами.
     * <p>
     * Метод отображает меню управления заказами с возможностью просмотреть все заказы,
     * обновить статус заказа или отменить заказ. В зависимости от выбранного варианта,
     * вызываются соответствующие методы для выполнения операций.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для чтения ввода пользователя.
     */
    private static void manageOrders(Scanner scanner) {
        System.out.println("Order Management:");
        System.out.println("1. View all orders");
        System.out.println("2. Update the order status");
        System.out.println("3. Cancel the order");
        System.out.print("Select an option: ");
        try {
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    viewAllOrders();
                    break;
                case 2:
                    updateOrderStatus(scanner);
                    break;
                case 3:
                    cancelOrder(scanner);
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }
    }

    /**
     * Управление запросами на обслуживание.
     * <p>
     * Метод отображает меню управления запросами на обслуживание с возможностью просмотреть все запросы,
     * обновить статус запроса или отменить запрос. В зависимости от выбранной опции вызываются
     * соответствующие методы для выполнения операций.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для чтения ввода пользователя.
     */
    private static void manageServiceRequests(Scanner scanner) {
        System.out.println("Managing service requests:");
        System.out.println("1. View all queries");
        System.out.println("2. Update the status of the request");
        System.out.println("3. Cancel the request");
        System.out.print("Select an option: ");
        try {
            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {
                case 1:
                    viewAllServiceRequests();
                    break;
                case 2:
                    updateServiceRequestStatus(scanner);
                    break;
                case 3:
                    cancelServiceRequest(scanner);
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }
    }

    /**
     * Просматривает все запросы на обслуживание.
     * <p>
     * Метод извлекает все запросы на обслуживание и выводит их на экран. Если запросов нет, выводится
     * сообщение о том, что запросов нет.
     * </p>
     */
    private static void viewAllServiceRequests() {
        Collection<ServiceRequest> requests = SERVICE_REQUEST_SERVICE.getAllServiceRequests();
        if (requests.isEmpty()) {
            System.out.println("There are no requests.");
            return;
        }
        for (ServiceRequest request : requests) {
            System.out.println(request);
        }
    }

    /**
     * Обновляет статус запроса на обслуживание.
     * <p>
     * Метод запрашивает у пользователя идентификатор запроса, который нужно обновить. Затем метод извлекает
     * запрос по этому идентификатору и позволяет пользователю ввести новый статус запроса. После обновления
     * статуса метод регистрирует действие в аудите.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для получения ввода пользователя.
     */
    private static void updateServiceRequestStatus(Scanner scanner) {
        System.out.print("Enter the update request ID: ");
        try {
            int requestId = scanner.nextInt();
            scanner.nextLine(); // consume newline

            ServiceRequest request = SERVICE_REQUEST_SERVICE.getServiceRequestById(requestId);
            if (request == null) {
                System.out.println("The request was not found.");
                return;
            }

            System.out.print("Enter a new status (in progress, completed, canceled): ");
            String status = scanner.nextLine();
            SERVICE_REQUEST_SERVICE.updateServiceRequestStatus(requestId, status);
            System.out.println("The status of the request has been updated.");
            auditService.logAction(currentUser, "The status of the request has been updated: " + requestId + " на " + status);
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }
    }

    /**
     * Создает запрос на обслуживание автомобиля.
     * <p>
     * Метод запрашивает у пользователя идентификатор автомобиля, который требуется обслужить. Затем
     * извлекает информацию о машине по этому идентификатору. После этого запрашивает описание проблемы
     * или необходимого сервиса и создает новый запрос на обслуживание. Регистрация действия в аудите
     * также выполняется.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для получения ввода пользователя.
     */
    private static void requestService(Scanner scanner) {
        System.out.println("Car Service Request:");
        System.out.print("Enter the ID of the car to be serviced: ");
        try {
            int carId = scanner.nextInt();
            scanner.nextLine(); // consume newline

            Car car = carService.getCar(carId);
            if (car == null) {
                System.out.println("The car was not found.");
                return;
            }

            System.out.print("Enter a description of the problem or service: ");
            String description = scanner.nextLine();

            ServiceRequest request = new ServiceRequest(SERVICE_REQUEST_SERVICE.getAllServiceRequests().size() + 1, description);
            SERVICE_REQUEST_SERVICE.createServiceRequest(request);
            System.out.println("The service request has been successfully created.");
            auditService.logAction(currentUser, "A request for car ID service has been created: " + carId);
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }
    }

    /**
     * Отменяет запрос на обслуживание.
     * <p>
     * Метод запрашивает у пользователя идентификатор запроса, который требуется отменить. Затем
     * извлекает запрос по этому идентификатору и обновляет его статус на "отменен". Регистрация действия
     * в аудите также выполняется.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для получения ввода пользователя.
     */
    private static void cancelServiceRequest(Scanner scanner) {
        System.out.print("Enter the request ID to cancel: ");
        try {
            int requestId = scanner.nextInt();
            scanner.nextLine(); // consume newline

            ServiceRequest request = SERVICE_REQUEST_SERVICE.getServiceRequestById(requestId);
            if (request == null) {
                System.out.println("The request was not found.");
                return;
            }

            SERVICE_REQUEST_SERVICE.updateServiceRequestStatus(requestId, "cancelled");
            System.out.println("The request has been canceled.");
            auditService.logAction(currentUser, "Request cancelled: " + requestId);
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }
    }

    /**
     * Выводит все заказы в консоль.
     * <p>
     * Метод получает все заказы из {@link OrderService} и выводит информацию о каждом заказе в консоль.
     * Если заказы отсутствуют, выводится сообщение о том, что заказов нет.
     * </p>
     */
    private static void viewAllOrders() {
        Collection<Order> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("There are no orders.");
            return;
        }
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    /**
     * Обновляет статус заказа.
     * <p>
     * Метод запрашивает у пользователя идентификатор заказа, статус которого требуется обновить. Затем
     * извлекает заказ по этому идентификатору и обновляет его статус на новый. Регистрация действия в аудите
     * также выполняется.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для получения ввода пользователя.
     */
    private static void updateOrderStatus(Scanner scanner) {
        System.out.print("Enter the order ID to update: ");
        try {
            int orderId = scanner.nextInt();
            scanner.nextLine(); // считываем переход на новую строку

            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                System.out.println("The order was not found.");
                return;
            }

            System.out.print("Enter a new status (in progress, completed, canceled): ");
            String status = scanner.nextLine();
            order.setStatus(status);
            System.out.println("The order status has been updated.");
            auditService.logAction(currentUser, "The order status has been updated: " + orderId + " on " + status);
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }
    }

    /**
     * Отменяет заказ по указанному идентификатору.
     * <p>
     * Метод запрашивает у пользователя идентификатор заказа, который необходимо отменить.
     * Если заказ найден, его статус обновляется на отмененный, и действие записывается в аудит.
     * В случае неверного ввода пользователю предлагается попробовать снова.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для получения ввода пользователя.
     */
    private static void cancelOrder(Scanner scanner) {
        System.out.print("Enter the order ID to cancel: ");
        try {
            int orderId = scanner.nextInt();
            scanner.nextLine(); // считываем переход на новую строку

            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                System.out.println("The order was not found.");
                return;
            }

            orderService.cancelOrder(orderId);
            System.out.println("The order has been cancelled.");
            auditService.logAction(currentUser, "The order has been cancelled. " + orderId);
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }
    }

    /**
     * Размещает новый заказ на автомобиль.
     * <p>
     * Метод отображает список доступных автомобилей и запрашивает у пользователя номер автомобиля для заказа.
     * Если выбранный автомобиль доступен, создается новый заказ и статус автомобиля обновляется на "продан".
     * Метод также записывает действие в аудит.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для получения ввода пользователя.
     */
        private static void placeOrder(Scanner scanner) {
            System.out.println("Available cars:");
            List<Car> availableCars = carService.getAllCars();
            if (availableCars.isEmpty()) {
                System.out.println("There are no cars.");
                return;
            }
    
            for (int i = 0; i < availableCars.size(); i++) {
                Car car = availableCars.get(i);
                System.out.printf("%d: %s %s (%d) - $%.2f [%s]%n",
                        i + 1, car.getBrand(), car.getModel(), car.getYear(), car.getPrice(), car.getStatus());
            }
    
            System.out.print("Select the car number to order: ");
            int carNumber = 0;
            try {
                carNumber = scanner.nextInt();
                scanner.nextLine(); // считываем переход на новую строку
    
                if (carNumber < 1 || carNumber > availableCars.size()) {
                    System.out.println("Wrong choice.");
                    return;
                }
    
            } catch (Exception e) {
                System.out.println("Incorrect input, try again.");
                scanner.nextLine();
            }
            Car selectedCar = availableCars.get(carNumber - 1);
            if (!"available".equalsIgnoreCase(selectedCar.getStatus())) {
                System.out.println("The selected car is not available for purchase.");
                return;
            }
    
            // Создаем новый заказ
            Order order = new Order(orderService.getAllOrders().size() + 1, currentUser, selectedCar, LocalDate.now(), "в процессе");
            orderService.createOrder(order);
            selectedCar.setStatus("sold");
            System.out.println("The order has been successfully placed on " + selectedCar.getBrand() + " " + selectedCar.getModel());
            auditService.logAction(currentUser, "An order for a car has been placed: " + selectedCar.getBrand() + " " + selectedCar.getModel());
        }

    /**
     * Управляет клиентами.
     * <p>
     * Метод отображает меню управления клиентами с возможностью просмотра всех клиентов, добавления нового клиента,
     * редактирования информации о клиенте и удаления клиента.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для получения ввода пользователя.
     */
     private static void manageClients(Scanner scanner) {
            System.out.println("Customer Management:");
            System.out.println("1. View all clients");
            System.out.println("2. Add a new client");
            System.out.println("3. Edit customer information");
            System.out.println("4. Delete the client");
            System.out.print("Select an option: ");
            try {
                int option = scanner.nextInt();
                scanner.nextLine(); // считываем переход на новую строку
    
                switch (option) {
                    case 1:
                        viewAllClients();
                        break;
                    case 2:
                        addNewClient(scanner);
                        break;
                    case 3:
                        editClientInfo(scanner);
                        break;
                    case 4:
                        removeClient(scanner);
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Incorrect input, try again.");
                scanner.nextLine();
            }
        }

    /**
     * Просматривает всех клиентов.
     * <p>
     * Метод запрашивает информацию о всех клиентах через {@link ClientService} и выводит их в консоль.
     * </p>
     */
    private static void viewAllClients() {
        clientService.getAllClient();
    }

    /**
     * Добавляет нового клиента.
     * <p>
     * Метод запрашивает у пользователя имя пользователя, пароль и контактную информацию для нового клиента.
     * Затем создает нового клиента с указанными данными и регистрирует его через {@link UserService}.
     * Добавление нового клиента также регистрируется в аудите.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для получения ввода пользователя.
     */
    private static void addNewClient(Scanner scanner) {
        String username = null;
        String password = null;
        String contactInfo = null;
        try {
            System.out.print("Enter the user name: ");
            username = scanner.nextLine();
            System.out.print("Enter the password: ");
            password = scanner.nextLine();
            System.out.print("Enter the contact information: ");
            contactInfo = scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }

        User client = new User(userService.getAllUsers().size() + 1, username, "client", contactInfo, password);
        userService.registerUser(client);
        System.out.println("The client has been successfully added.");
        auditService.logAction(currentUser, "A new client has been added: " + username);
    }

    /**
     * Редактирует информацию о клиенте.
     * <p>
     * Метод запрашивает у пользователя идентификатор клиента, чью информацию требуется изменить. Затем
     * обновляет имя пользователя и контактную информацию для найденного клиента. Действие также регистрируется
     * в аудите.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для получения ввода пользователя.
     */
    private static void editClientInfo(Scanner scanner) {
        System.out.print("Enter the client ID to edit: ");
        int clientId = 0;
        String username = null;
        String contactInfo = null;
        try {
            clientId = scanner.nextInt();
            scanner.nextLine();

            User client = clientService.getUserById(clientId);
            if (client == null) {
                System.out.println("The client was not found.");
                return;
            }

            System.out.print("Enter a new username: ");
            username = scanner.nextLine();

            System.out.print("Enter your new contact information: ");
            contactInfo = scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }

        clientService.updateClient(clientId, username, contactInfo);

        System.out.println("The information about the client has been updated.");
        auditService.logAction(currentUser, "Updated information about the client: " + clientId);
    }

    /**
     * Удаляет клиента.
     * <p>
     * Метод запрашивает у пользователя идентификатор клиента, которого требуется удалить. Затем удаляет
     * клиента из {@link ClientService}. Действие также регистрируется в аудите.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для получения ввода пользователя.
     */
    private static void removeClient(Scanner scanner) {
        System.out.print("Enter the client ID to delete: ");
        int clientId = 0;
        try {
            clientId = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }

        clientService.deleteClient(clientId);
        System.out.println("The client has been deleted.");
        auditService.logAction(currentUser, "The client was deleted: " + clientId);
    }

    /**
     * Управление сотрудниками.
     * <p>
     * Метод отображает меню управления сотрудниками с различными опциями, такими как просмотр всех сотрудников,
     * добавление нового сотрудника, редактирование информации о сотруднике и удаление сотрудника.
     * Пользователь выбирает опцию, и соответствующий метод вызывается для выполнения выбранного действия.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для получения ввода пользователя.
     */
    private static void manageEmployees(Scanner scanner) {
        System.out.println("Employee Management:");
        System.out.println("1. View all employees");
        System.out.println("2. Add a new employee");
        System.out.println("3. Edit employee information");
        System.out.println("4. Delete an employee");
        System.out.print("Select an option: ");
        try {
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    viewAllEmployees();
                    break;
                case 2:
                    addNewEmployee(scanner);
                    break;
                case 3:
                    editEmployeeInfo(scanner);
                    break;
                case 4:
                    removeEmployee(scanner);
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }
    }

    /**
     * Выводит всех сотрудников в консоль.
     * <p>
     * Метод извлекает всех сотрудников из {@link EmployeeService} и выводит информацию о каждом сотруднике в консоль.
     * Если сотрудники отсутствуют, информация не выводится.
     * </p>
     */
    private static void viewAllEmployees() {
        employeeService.getAllEmployees();
    }

    /**
     * Добавляет нового сотрудника.
     * <p>
     * Метод запрашивает у пользователя имя пользователя, пароль и контактную информацию для нового сотрудника.
     * Затем создается новый объект {@link User} и добавляется в {@link EmployeeService}. Действие также регистрируется
     * в аудите.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для получения ввода пользователя.
     */
    private static void addNewEmployee(Scanner scanner) {
        String username = null;
        String password = null;
        String contactInfo = null;
        try {
            System.out.print("Enter the user name: ");
            username = scanner.nextLine();
            System.out.print("Enter the password: ");
            password = scanner.nextLine();
            System.out.print("Enter the contact information: ");
            contactInfo = scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }

        User employee = new User(userService.getAllUsers().size() + 1, username, "manager", contactInfo, password);
        employeeService.addEmployee(employee);
        System.out.println("The employee has been successfully added.");
        auditService.logAction(currentUser, "A new employee has been added: " + username);
    }

    /**
     * Редактирует информацию о сотруднике.
     * <p>
     * Метод запрашивает у пользователя идентификатор сотрудника, чью информацию требуется изменить. Затем
     * обновляет имя пользователя и контактную информацию для найденного сотрудника. Действие также регистрируется
     * в аудите.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для получения ввода пользователя.
     */
    private static void editEmployeeInfo(Scanner scanner) {
        int employeeId = 0;
        String username = null;
        String contactInfo = null;
        try {
            System.out.print("Enter the employee ID to edit: ");
            employeeId = scanner.nextInt();
            scanner.nextLine(); // считываем переход на новую строку

            User employee = employeeService.getUserById(employeeId);
            if (employee == null) {
                System.out.println("The employee was not found.");
                return;
            }

            System.out.print("Enter a new username: ");
            username = scanner.nextLine();

            System.out.print("Enter your new contact information: ");
            contactInfo = scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }

        employeeService.updateEmployee(employeeId, username, contactInfo);
        System.out.println("Information about the employee has been updated.");
        auditService.logAction(currentUser, "Updated information about employees: " + employeeId);
    }

    /**
     * Удаляет сотрудника.
     * <p>
     * Метод запрашивает у пользователя идентификатор сотрудника, которого требуется удалить. Затем удаляет
     * сотрудника из {@link EmployeeService}. Действие также регистрируется в аудите.
     * </p>
     *
     * @param scanner Экземпляр {@link Scanner}, используемый для получения ввода пользователя.
     */
    private static void removeEmployee(Scanner scanner) {
        System.out.print("Enter the employee ID to delete: ");
        int employeeId = 0;
        try {
            employeeId = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }

        employeeService.deleteEmployee(employeeId);
        System.out.println("The employee has been deleted.");
        auditService.logAction(currentUser, "Dismissed employee: " + employeeId);
    }
}