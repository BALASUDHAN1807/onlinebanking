package security.servlet;
import dao.TransactionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/deposit")
public class DepositServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int accountId = Integer.parseInt(req.getParameter("accountId"));
        BigDecimal amount = new BigDecimal(req.getParameter("amount"));
        try {
            TransactionDAO dao = new TransactionDAO();
            dao.deposit(accountId, amount);
            res.sendRedirect("dashboard");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
