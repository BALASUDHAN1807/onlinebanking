package security.servlet;
import dao.AccountDAO;
import dao.TransactionDAO;
import model.Account;
import model.Transaction;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            res.sendRedirect("login.jsp");
            return;
        }
        try {
            AccountDAO adao = new AccountDAO();
            TransactionDAO tdao = new TransactionDAO();
            List<Account> accounts = adao.getAccountsByUser(user.getId());
            Map<Integer, List<Transaction>> txMap = new HashMap<>();
            for (Account a : accounts) {
                txMap.put(a.getId(), tdao.getTransactionsByAccount(a.getId()));
            }
            req.setAttribute("accounts", accounts);
            req.setAttribute("transactions", txMap);
            req.getRequestDispatcher("dashboard.jsp").forward(req, res);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
