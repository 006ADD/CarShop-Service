package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import services.AuditService;
import services.UserService;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserService userService = new UserService();
    private final AuditService auditService = new AuditService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String action = req.getParameter("action");

        if("register".equals(action)){
            registerUser(req,resp);
        } else if ("login".equals(action)) {
            loginUser(req,resp,username,password);
        }else{
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void registerUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String role = req.getParameter("role");
        String contactInfo = req.getParameter("contactInfo");

        User user = new User(userService.getAllUsers().size() + 1,username,password,role,contactInfo);
        userService.registerUser(user);
        resp.sendRedirect("login?action=login");
    }

    private void loginUser(HttpServletRequest req, HttpServletResponse resp,String username, String password) throws IOException {
        User currentUser = userService.loginUser(username,password);

        if(currentUser != null){
            req.getSession().setAttribute("user", currentUser);
            auditService.logAction(currentUser, "Logged in");
            resp.sendRedirect("carshop?action=main");
        }else{
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
