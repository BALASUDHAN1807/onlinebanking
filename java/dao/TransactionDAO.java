package dao;
import model.Transaction;
import java.sql.*;
import java.util.*;
import java.math.BigDecimal;

public class TransactionDAO {
    public List<Transaction> getTransactionsByAccount(int accountId) throws Exception {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id=? ORDER BY created_at DESC LIMIT 10";
        try (Connection con = DB.get(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setId(rs.getInt("id"));
                t.setAccountId(rs.getInt("account_id"));
                t.setType(rs.getString("type"));
                t.setAmount(rs.getBigDecimal("amount"));
                t.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(t);
            }
        }
        return list;
    }

    public void deposit(int accountId, BigDecimal amount) throws Exception {
        try (Connection con = DB.get()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps1 = con.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE id=?")) {
                ps1.setBigDecimal(1, amount);
                ps1.setInt(2, accountId);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = con.prepareStatement("INSERT INTO transactions(account_id, amount, type) VALUES(?,?,?)")) {
                ps2.setInt(1, accountId);
                ps2.setBigDecimal(2, amount);
                ps2.setString(3, "CREDIT");
                ps2.executeUpdate();
            }
            con.commit();
        }
    }

    public boolean withdraw(int accountId, BigDecimal amount) throws Exception {
        try (Connection con = DB.get()) {
            con.setAutoCommit(false);
            BigDecimal balance = BigDecimal.ZERO;
            try (PreparedStatement ps = con.prepareStatement("SELECT balance FROM accounts WHERE id=?")) {
                ps.setInt(1, accountId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) balance = rs.getBigDecimal("balance");
            }
            if (balance.compareTo(amount) < 0) return false;
            try (PreparedStatement ps1 = con.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE id=?")) {
                ps1.setBigDecimal(1, amount);
                ps1.setInt(2, accountId);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = con.prepareStatement("INSERT INTO transactions(account_id, amount, type) VALUES(?,?,?)")) {
                ps2.setInt(1, accountId);
                ps2.setBigDecimal(2, amount);
                ps2.setString(3, "DEBIT");
                ps2.executeUpdate();
            }
            con.commit();
            return true;
        }
    }

    public boolean transfer(int fromAccountId, int toAccountId, BigDecimal amount) throws Exception {
        try (Connection con = DB.get()) {
            con.setAutoCommit(false);
            BigDecimal balance = BigDecimal.ZERO;
            try (PreparedStatement ps = con.prepareStatement("SELECT balance FROM accounts WHERE id=?")) {
                ps.setInt(1, fromAccountId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) balance = rs.getBigDecimal("balance");
            }
            if (balance.compareTo(amount) < 0) return false;
            try (PreparedStatement ps1 = con.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE id=?")) {
                ps1.setBigDecimal(1, amount);
                ps1.setInt(2, fromAccountId);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = con.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE id=?")) {
                ps2.setBigDecimal(1, amount);
                ps2.setInt(2, toAccountId);
                ps2.executeUpdate();
            }
            try (PreparedStatement ps3 = con.prepareStatement("INSERT INTO transactions(account_id, amount, type) VALUES(?,?,?)")) {
                ps3.setInt(1, fromAccountId);
                ps3.setBigDecimal(2, amount);
                ps3.setString(3, "TRANSFER_OUT");
                ps3.executeUpdate();
            }
            try (PreparedStatement ps4 = con.prepareStatement("INSERT INTO transactions(account_id, amount, type) VALUES(?,?,?)")) {
                ps4.setInt(1, toAccountId);
                ps4.setBigDecimal(2, amount);
                ps4.setString(3, "TRANSFER_IN");
                ps4.executeUpdate();
            }
            con.commit();
            return true;
        }
    }
}
