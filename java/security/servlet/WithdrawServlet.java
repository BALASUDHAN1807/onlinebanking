package security.servlet;
import dao.TransactionDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/withdraw")
public class WithdrawServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int accountId = Integer.parseInt(req.getParameter("accountId"));
        BigDecimal amount = new BigDecimal(req.getParameter("amount"));
        try {
            TransactionDAO dao = new TransactionDAO();
            boolean ok = dao.withdraw(accountId, amount);
            if (!ok) {
                req.setAttribute("error", "Insufficient balance!");
                req.getRequestDispatcher("dashboard").forward(req, res);
                return;
            }
            res.sendRedirect("dashboard");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
