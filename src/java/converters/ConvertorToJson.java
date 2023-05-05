

package converters;

import entity.User;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


public class ConvertorToJson {
    
    public JsonObject getJsonObjectUser(User user){
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("id", user.getId());
        job.add("firstname", user.getFirstname());
        job.add("lastname", user.getLastname());
        job.add("phone", user.getPhone());
        job.add("login", user.getLogin());
        job.add("roles", getJsonArrayRoles(user.getRoles()));
        return job.build();
    }
    public JsonArray getJsonArrayRoles(List<String> roles){
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (int i = 0; i < roles.size(); i++) {
            String role = roles.get(i);
            jab.add(role);
        }
        return jab.build();
    }
   
    public JsonArray getJsonArrayUsers(List<User>listUsers){
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (int i = 0; i < listUsers.size(); i++) {
            User user = listUsers.get(i);
            jab.add(getJsonObjectUser(user));
        }
        return jab.build();
    }
}
