<!doctype html>
<html>
  <head>
    <meta charset="utf-8">
    <title>KDBank Login</title>
    <link rel="stylesheet" href="css/styles.css">
  </head>
  <body>
    <%@ include file="WEB-INF/jspf/header.jspf" %>
    <div class="container">
      <div class="card" style="max-width:420px;margin:28px auto">
        <h2>Login with OTP</h2>
        <p class="muted">Enter your registered mobile number to receive a one-time password.</p>
        <form action="sendOtp" method="post">
          <input type="text" name="phone" placeholder="Enter phone number" required/>
          <button type="submit">Get OTP</button>
        </form>
        <% if (request.getParameter("registered") != null) { %>
          <p style="color:green">Registration successful. Login with OTP.</p>
        <% } %>
        <% if (request.getAttribute("error") != null) { %>
          <p style="color:red"><%= request.getAttribute("error") %></p>
        <% } %>
        <p style="margin-top:8px"><a href="register.jsp">Create an account</a></p>
      </div>
    </div>
    <%@ include file="WEB-INF/jspf/footer.jspf" %>
  </body>
</html>
