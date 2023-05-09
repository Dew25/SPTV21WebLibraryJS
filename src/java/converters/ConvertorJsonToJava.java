

package converters;

import entity.User;
import java.io.PrintWriter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import servlets.UserServlet;
import session.UserFacade;
import tools.PasswordEncrypt;


public class ConvertorJsonToJava {
    private PasswordEncrypt pe = new PasswordEncrypt();

    public User getUser(JsonObject jsonObject){
        JsonObjectBuilder job = Json.createObjectBuilder();
                String firstname = jsonObject.getString("firstname");
                String lastname = jsonObject.getString("lastname");
                String phone = jsonObject.getString("phone");
                String login = jsonObject.getString("login");
                String password = jsonObject.getString("password");
                if(firstname == null || firstname.isEmpty() 
                        || lastname == null || lastname.isEmpty() 
                        || phone == null || phone.isEmpty()
                        || login == null || login.isEmpty()
                        || password == null || password.isEmpty()){
                    return null;
                }
                User user = new User();
                user.setFirstname(firstname);
                user.setLastname(lastname);
                user.setPhone(phone);
                user.setLogin(login);
                user.setSalt(pe.getSalt());
                password = pe.getProtectedPassword(password, user.getSalt());
                user.setPassword(password);
                user.getRoles().add(UserServlet.role.USER.toString());
                return user;
    }
    public User getUser(JsonObject jsonObject,UserFacade userFacade){
        String userId = jsonObject.getString("userId");
        String firstname = jsonObject.getString("firstname");
        String lastname = jsonObject.getString("lastname");
        String phone = jsonObject.getString("phone");
        String login = jsonObject.getString("login");
        String password = jsonObject.getString("password");
        if(firstname == null || firstname.isEmpty() 
                || lastname == null || lastname.isEmpty() 
                || phone == null || phone.isEmpty()
                || login == null || login.isEmpty()
                || userId == null || userId.isEmpty()){
            return null;
        }
        // Находим по идентификатору пользователя в базе и инициируем новыми значениями
        User user = userFacade.find(Long.parseLong(userId));
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setPhone(phone);
        user.setLogin(login);
        if(password != null && !password.isEmpty()){
            //если пароль заполнен, то меняем на указанный
            password = pe.getProtectedPassword(password, user.getSalt());
            user.setPassword(password);
        }
        return user;
    }
    
}
