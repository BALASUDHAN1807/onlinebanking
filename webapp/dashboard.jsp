<%@ page import="java.util.*, model.User, model.Account, model.Transaction" %>
<%@ page session="true" %>
<%
  User user = (User) session.getAttribute("loggedUser");
  if (user == null) { response.sendRedirect("login.jsp"); return; }
  List<Account> accounts = (List<Account>) request.getAttribute("accounts");
  Map<Integer, List<Transaction>> transactions = (Map<Integer, List<Transaction>>) request.getAttribute("transactions");
%>
<!doctype html>
<html>
  <head>
    <meta charset="utf-8">
    <title>KDBank Dashboard</title>
    <link rel="stylesheet" href="css/styles.css">
  </head>
  <body>
    <%@ include file="WEB-INF/jspf/header.jspf" %>
    <div class="container" style="padding:18px 0">
      <div class="card">
        <div style="display:flex;justify-content:space-between;align-items:center">
          <div>
            <h2>Welcome, <%= user.getFullName() %>!</h2>
            <div class="muted">Phone: <%= user.getPhone() %> | Email: <%= user.getEmail() %></div>
          </div>
          <div class="top-actions">
            <a href="logout.jsp">Logout</a>
          </div>
        </div>
      </div>

      <div class="grid">
        <div>
          <div class="card">
            <h3>Accounts</h3>
            <table>
              <tr><th>Account No</th><th>Type</th><th>Balance</th><th>Actions</th></tr>
              <% for(Account a : accounts){ %>
                <tr>
                  <td><%= a.getAccountNumber() %></td>
                  <td><%= a.getType() %></td>
                  <td>₹ <%= a.getBalance() %></td>
                  <td style="white-space:nowrap">
                    <form action="deposit" method="post" style="display:inline;">
                      <input type="hidden" name="accountId" value="<%= a.getId() %>"/>
                      <input type="number" step="0.01" name="amount" placeholder="Amt" required style="width:100px;display:inline-block"/>
                      <button type="submit">Deposit</button>
                    </form>
                    <form action="withdraw" method="post" style="display:inline;">
                      <input type="hidden" name="accountId" value="<%= a.getId() %>"/>
                      <input type="number" step="0.01" name="amount" placeholder="Amt" required style="width:100px;display:inline-block"/>
                      <button type="submit">Withdraw</button>
                    </form>
                  </td>
                </tr>
                <tr><td colspan="4">
                  <b>Recent Transactions:</b>
                  <ul>
                    <% List<Transaction> txs = transactions.get(a.getId()); if (txs != null) {
                         for(Transaction t : txs) { %>
                           <li><%= t.getType() %> ₹<%= t.getAmount() %> on <%= t.getCreatedAt() %></li>
                    <%   } } %>
                  </ul>
                </td></tr>
              <% } %>
            </table>
          </div>
        </div>

        <div>
          <div class="card">
            <h3>Transfer Money</h3>
            <form action="transfer" method="post">
              <label>From</label>
              <select name="fromAccountId">
                <% for(Account a : accounts){ %>
                  <option value="<%= a.getId() %>"><%= a.getAccountNumber() %> (₹<%= a.getBalance() %>)</option>
                <% } %>
              </select>
              <label>To (choose your account or enter destination account number)</label>
              <select name="toAccountId">
                <option value="">-- Select your account --</option>
                <% for(Account a : accounts){ %>
                  <option value="<%= a.getId() %>"><%= a.getAccountNumber() %></option>
                <% } %>
              </select>
              <div style="margin-top:8px">
                <input type="text" name="toAccountNumber" placeholder="Or enter destination account number" />
              </div>
              <label>Amount</label>
              <input type="number" step="0.01" name="amount" required/>
              <button type="submit">Transfer</button>
            </form>
            <% if(request.getAttribute("error") != null){ %>
              <p style="color:red"><%= request.getAttribute("error") %></p>
            <% } %>
          </div>
        </div>
      </div>
    </div>
    <%@ include file="WEB-INF/jspf/footer.jspf" %>
  </body>
</html>
