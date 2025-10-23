package security.servlet;
import dao.UserDAO;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/validateOtp")
public class ValidateOtpServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Object otpObj = session.getAttribute("otp");
        if (otpObj == null) {
            req.setAttribute("error", "OTP expired or not generated. Try again.");
            req.getRequestDispatcher("login.jsp").forward(req, res);
            return;
        }
        int generatedOtp = (int) otpObj;
        String entered = req.getParameter("otp");
        int enteredOtp = Integer.parseInt(entered);
        if (generatedOtp == enteredOtp) {
            try {
                int userId = (int) session.getAttribute("userId");
                UserDAO udao = new UserDAO();
                User user = udao.findById(userId);
                session.setAttribute("loggedUser", user);
                session.removeAttribute("otp");
                res.sendRedirect("dashboard");
            } catch (Exception e) {
                throw new ServletException(e);
            }
        } else {
            req.setAttribute("error", "Invalid OTP, please try again.");
            req.getRequestDispatcher("otp.jsp").forward(req, res);
        }
    }
}
