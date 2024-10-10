package services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.User;
import repositories.UserRepository;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
public class UserService {
    private UserRepository userRepository;
    public User registerUser(User user){
       userRepository.register(user);
    }

    public User loginUser(String name, String password){
       return userRepository.login(name,password);
    }

    public User getUserId(int id){
        return userRepository.findById(id);
    }

    public Collection<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void deleteUser(int id){
        userRepository.remove(id);
    }


    public boolean isAdmin(User user){
        return "admin".equalsIgnoreCase(user.getRole());
    }

    public boolean isManager(User user) {
        return "manager".equalsIgnoreCase(user.getRole());
    }

    public boolean isClient(User user) {
        return "client".equalsIgnoreCase(user.getRole());
    }

}
