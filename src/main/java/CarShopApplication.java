import model.*;
import services.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class CarShopApplication {
    private static final UserService userService = new UserService();
    private static final CarService carService = new CarService();
    private static final OrderService orderService = new OrderService();
    private static final ServiceRequest serviceRequestService = new ServiceRequest();

    private static final ClientService clientService = new ClientService();
    private static final AuditService auditService = new AuditService();


    private static final EmployeeService employeeService = new EmployeeService();

    private static User currentUser;

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

    private static void viewCars(Scanner scanner) {
        System.out.println("Available cars:");
        for (Car car : carService.getAllCars()) {
            System.out.println(car.getId() + ": " + car.getBrand() + " " + car.getModel() + " (" + car.getYear() + ") - $" + car.getPrice() + " [" + car.getStatus() + "]" + " [" + car.getCondition()+"] ");
        }
    }

    private static void viewAuditLogs(Scanner scanner) {
        System.out.println("Audit logs:");
        for (AuditLog log : auditService.getLogs()) {
            System.out.println(log.getTimestamp() + ": " + log.getUser().getName() + " - " + log.getAction());
        }
    }

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

    private static void viewAllServiceRequests() {
        Collection<SerRequest> requests = serviceRequestService.getAllServiceRequests();
        if (requests.isEmpty()) {
            System.out.println("There are no requests.");
            return;
        }
        for (SerRequest request : requests) {
            System.out.println(request);
        }
    }

    private static void updateServiceRequestStatus(Scanner scanner) {
        System.out.print("Enter the update request ID: ");
        try {
            int requestId = scanner.nextInt();
            scanner.nextLine(); // consume newline

            SerRequest request = serviceRequestService.getServiceRequestById(requestId);
            if (request == null) {
                System.out.println("The request was not found.");
                return;
            }

            System.out.print("Enter a new status (in progress, completed, canceled): ");
            String status = scanner.nextLine();
            serviceRequestService.updateServiceRequestStatus(requestId, status);
            System.out.println("The status of the request has been updated.");
            auditService.logAction(currentUser, "The status of the request has been updated: " + requestId + " на " + status);
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }
    }

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

            SerRequest request = new SerRequest(serviceRequestService.getAllServiceRequests().size() + 1, description);
            serviceRequestService.createServiceRequest(request);
            System.out.println("The service request has been successfully created.");
            auditService.logAction(currentUser, "A request for car ID service has been created: " + carId);
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }
    }
    private static void cancelServiceRequest(Scanner scanner) {
        System.out.print("Enter the request ID to cancel: ");
        try {
            int requestId = scanner.nextInt();
            scanner.nextLine(); // consume newline

            SerRequest request = serviceRequestService.getServiceRequestById(requestId);
            if (request == null) {
                System.out.println("The request was not found.");
                return;
            }

            serviceRequestService.updateServiceRequestStatus(requestId, "cancelled");
            System.out.println("The request has been canceled.");
            auditService.logAction(currentUser, "Request cancelled: " + requestId);
        } catch (Exception e) {
            System.out.println("Incorrect input, try again.");
            scanner.nextLine();
        }
    }


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

    private static void viewAllClients() {
        clientService.getAllClient();
    }

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

        User client = new User(userService.getAllUsers().size() + 1, username, "клиент", contactInfo, password);
        userService.registerUser(client);
        System.out.println("The client has been successfully added.");
        auditService.logAction(currentUser, "A new client has been added: " + username);
    }

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

    private static void viewAllEmployees() {
        employeeService.getAllEmployees();
    }

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