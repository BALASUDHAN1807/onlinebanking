<!doctype html>
<html>
  <head>
    <meta charset="utf-8">
    <title>Enter OTP</title>
    <link rel="stylesheet" href="css/styles.css">
  </head>
  <body>
    <%@ include file="WEB-INF/jspf/header.jspf" %>
    <div class="container">
      <div class="card" style="max-width:420px;margin:28px auto">
        <h2>One-Time Password</h2>
        <p style="color:green"><%= request.getAttribute("message") != null ? request.getAttribute("message") : "" %></p>
        <form action="validateOtp" method="post">
          <input type="text" name="otp" placeholder="6-digit OTP" required/>
          <button type="submit">Verify</button>
        </form>
        <% if (request.getAttribute("error") != null) { %>
          <p style="color:red"><%= request.getAttribute("error") %></p>
        <% } %>
      </div>
    </div>
    <%@ include file="WEB-INF/jspf/footer.jspf" %>
  </body>
</html>
