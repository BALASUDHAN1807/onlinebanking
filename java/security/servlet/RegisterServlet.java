package security.servlet;
import dao.UserDAO;
import dao.AccountDAO;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String username = req.getParameter("username");
        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");
        try {
            UserDAO udao = new UserDAO();
            if (udao.findByPhone(phone) != null) {
                req.setAttribute("error", "Phone already registered!");
                req.getRequestDispatcher("register.jsp").forward(req, res);
                return;
            }
            User u = new User();
            u.setUsername(username); u.setFullName(fullName); u.setPhone(phone); u.setEmail(email);
            int userId = udao.create(u);

            // create starter accounts
            AccountDAO adao = new AccountDAO();
            adao.createAccount(userId, "SAV" + (100000 + userId), "SAVINGS", new java.math.BigDecimal("1000.00"));
            adao.createAccount(userId, "CUR" + (100000 + userId), "CURRENT", new java.math.BigDecimal("500.00"));

            res.sendRedirect("login.jsp?registered=1");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
