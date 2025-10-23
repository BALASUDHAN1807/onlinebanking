<!doctype html>
<html>
  <head>
    <meta charset="utf-8">
    <title>KDBank Register</title>
    <link rel="stylesheet" href="css/styles.css">
  </head>
  <body>
    <%@ include file="WEB-INF/jspf/header.jspf" %>
    <div class="container">
      <div class="card" style="max-width:520px;margin:28px auto">
        <h2>Create your KDBank account</h2>
        <form action="register" method="post">
          <input type="text" name="username" placeholder="Username" required/>
          <input type="text" name="fullName" placeholder="Full name" required/>
          <input type="text" name="phone" placeholder="Mobile number" required/>
          <input type="email" name="email" placeholder="Email (optional)"/>
          <button type="submit">Register</button>
        </form>
        <% if (request.getAttribute("error") != null) { %>
          <p style="color:red"><%= request.getAttribute("error") %></p>
        <% } %>
        <p style="margin-top:8px"><a href="login.jsp">Back to login</a></p>
      </div>
    </div>
    <%@ include file="WEB-INF/jspf/footer.jspf" %>
  </body>
</html>
