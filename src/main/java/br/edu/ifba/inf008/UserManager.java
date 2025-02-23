package br.edu.ifba.inf008;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import br.edu.ifba.inf008.models.User;

public class UserManager implements Serializable{
    private Map<Integer, User> users = new HashMap<>();
    public static User currentUser = null;

    public void addUser(User user) throws Exception{
        if(users.containsKey(user.getId())){
            throw new Exception("This ID has already been registered.");
        }
        if(user.getName() == null || user.getName().trim().isEmpty()){
            throw new Exception("User name cannot be empty.");
        }
        users.put(user.getId(), user);
    }

    public void login(Integer id) throws Exception{
        currentUser = users.get(id);
        if(currentUser == null){
            throw new Exception("ID not found.");
        }
    }
    
}
