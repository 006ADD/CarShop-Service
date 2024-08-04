package services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class UserService {



    private Map<String, User> users = new HashMap<>();

    public void registerUser(User user){
        if(!users.containsKey(user.getName())){
            users.put(user.getName(), user);
        }else{
            System.out.println("Пользователь не существует.");
        }
    }

    public User loginUser(String name, String password){
        User user = users.get(name);
        if(user != null && user.getPassword().equals(password)){
            return user;
        }else{
            return null;
        }
    }

    public Collection<User> getAllUsers(){
        return users.values();
    }

    public void deleteUser(int id){
        users.remove(id);
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
