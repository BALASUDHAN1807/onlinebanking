package security.servlet;
import java.io.IOException;
import java.math.BigDecimal;

import dao.TransactionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/transfer")
public class TransferServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int fromAccountId = Integer.parseInt(req.getParameter("fromAccountId"));
        String toAccountIdParam = req.getParameter("toAccountId");
        String toAccountNumber = req.getParameter("toAccountNumber"); // new field: accept destination acc number
        BigDecimal amount = new BigDecimal(req.getParameter("amount"));
        try {
            int toAccountId;
            // prefer explicit id if provided
            if (toAccountIdParam != null && !toAccountIdParam.isEmpty()) {
                toAccountId = Integer.parseInt(toAccountIdParam);
            } else if (toAccountNumber != null && !toAccountNumber.isEmpty()) {
                dao.AccountDAO accDao = new dao.AccountDAO();
                model.Account dest = accDao.findByAccountNumber(toAccountNumber.trim());
                if (dest == null) {
                    req.setAttribute("error", "Destination account not found: " + toAccountNumber);
                    req.getRequestDispatcher("dashboard").forward(req, res);
                    return;
                }
                toAccountId = dest.getId();
            } else {
                req.setAttribute("error", "No destination account specified.");
                req.getRequestDispatcher("dashboard").forward(req, res);
                return;
            }

            TransactionDAO dao = new TransactionDAO();
            boolean ok = dao.transfer(fromAccountId, toAccountId, amount);
            if (!ok) {
                req.setAttribute("error", "Insufficient balance for transfer!");
                req.getRequestDispatcher("dashboard").forward(req, res);
                return;
            }
            res.sendRedirect("dashboard");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
