package security.servlet;
import java.io.IOException;
import java.util.Random;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

@WebServlet("/sendOtp")
public class OTPServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String phone = req.getParameter("phone");
        try {
            UserDAO udao = new UserDAO();
            User user = udao.findByPhone(phone);
            if (user == null) {
                req.setAttribute("error", "Phone not registered!");
                req.getRequestDispatcher("login.jsp").forward(req, res);
                return;
            }
            int otp = 100000 + new Random().nextInt(900000);
            HttpSession session = req.getSession();
            session.setAttribute("otp", otp);
            session.setAttribute("userId", user.getId());
            // attempt to send SMS
            String sms = "Your KDBank OTP is: " + otp;
            boolean sent;
            try {
                sent = util.SmsSender.sendSms(phone, sms);
            } catch (Exception ex) {
                // swallow and fallback to showing OTP on page for development
                sent = false;
            }
            if (sent) {
                req.setAttribute("message", "An OTP has been sent to your mobile number.");
            } else {
                // fallback for dev: show OTP on page
                req.setAttribute("message", "Your OTP is: " + otp + " (sms not sent)");
            }
            req.getRequestDispatcher("otp.jsp").forward(req, res);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
