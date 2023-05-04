/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import converters.ConvertorToJson;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import session.UserFacade;
import tools.PasswordEncrypt;

/**
 *
 * @author user
 */
@WebServlet(name = "UserServlet",loadOnStartup = 1, urlPatterns = {
    "/createUser",
    "/changeUserProfile",
    "/getListUsers",
    
})

public class UserServlet extends HttpServlet {
    @EJB private UserFacade userFacade; 
    static enum role {ADMINISTRATOR, MANAGER, USER};
    private PasswordEncrypt pe = new PasswordEncrypt();

    @Override
    public void init() throws ServletException {
        super.init(); 
        if(userFacade.count() > 0) return;
        User user = new User();
        user.setFirstname("Juri");
        user.setLastname("Melnikov");
        user.setPhone("5659394949");
        user.setLogin("Administrator");
        user.setSalt(pe.getSalt());
        user.setPassword(pe.getProtectedPassword("12345", user.getSalt()));
        user.getRoles().add(UserServlet.role.ADMINISTRATOR.toString());
        user.getRoles().add(UserServlet.role.MANAGER.toString());
        user.getRoles().add(UserServlet.role.USER.toString());
        userFacade.create(user);
    }
    
    
    
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();
        switch (path) {
            case "/createUser":
                JsonReader jsonReader = Json.createReader(request.getReader());
                JsonObject jsonObject = jsonReader.readObject();
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
                    job.add("info", "Не все поля заполнены");
                    job.add("status", false);
                    try (PrintWriter out = response.getWriter()) {
                        out.println(job.build().toString());
                    }
                    break;
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
                try {
                    userFacade.create(user);
                    job.add("info", "Пользователь успешно создан");
                    job.add("status", true);
                } catch (Exception e) {
                    job.add("info", "Пользователя создать не удолось");
                    job.add("status", false);
                }
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                break;
            case "/changeUserProfile":
                jsonReader = Json.createReader(request.getReader());
                jsonObject = jsonReader.readObject();
                job = Json.createObjectBuilder();
                String userId = jsonObject.getString("userId");
                firstname = jsonObject.getString("firstname");
                lastname = jsonObject.getString("lastname");
                phone = jsonObject.getString("phone");
                login = jsonObject.getString("login");
                password = jsonObject.getString("password");
                if(firstname == null || firstname.isEmpty() 
                        || lastname == null || lastname.isEmpty() 
                        || phone == null || phone.isEmpty()
                        || login == null || login.isEmpty()
                        || userId == null || userId.isEmpty()){
                    job.add("info", "Не все поля заполнены");
                    job.add("status", false);
                    try (PrintWriter out = response.getWriter()) {
                        out.println(job.build().toString());
                    }
                    break;
                }
                // Находим по идентификатору пользователя в базе и инициируем новыми значениями
                user = userFacade.find(Long.parseLong(userId));
                user.setFirstname(firstname);
                user.setLastname(lastname);
                user.setPhone(phone);
                user.setLogin(login);
                if(password != null && !password.isEmpty()){
                    //если пароль заполнен, то меняем на указанный
                    password = pe.getProtectedPassword(password, user.getSalt());
                    user.setPassword(password);
                }
                try {
                    userFacade.edit(user);
                    //Состояние пользователя изменилось -> запоминаем его в сессии
                    HttpSession session = request.getSession(false);
                    if(session.getAttribute("authUser")!= null ){
                        session.setAttribute("authUser", user);
                    }
                    job.add("info", "Профиль пользователя успешно изменнен");
                    job.add("user", new ConvertorToJson().getJsonObjectUser(user));
                    job.add("status", true);
                } catch (Exception e) {
                    job.add("info", "Профиль пользователя изменить не удолось");
                    job.add("status", false);
                }
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                break;
            case "/getListUsers":
                List<User> listUsers = userFacade.findAll();
                job = Json.createObjectBuilder();
                job.add("status", true);
                job.add("users", new ConvertorToJson().getJsonArrayUsers(listUsers));
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                break;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
