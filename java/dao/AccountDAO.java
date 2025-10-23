package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Account;

public class AccountDAO {
    public List<Account> getAccountsByUser(int userId) throws Exception {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE user_id=?";
        try (Connection con = DB.get(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Account a = new Account();
                a.setId(rs.getInt("id"));
                a.setUserId(rs.getInt("user_id"));
                a.setAccountNumber(rs.getString("account_number"));
                a.setType(rs.getString("type"));
                a.setBalance(rs.getBigDecimal("balance"));
                list.add(a);
            }
        }
        return list;
    }

    public Account findById(int id) throws Exception {
        String sql = "SELECT * FROM accounts WHERE id=?";
        try (Connection con = DB.get(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Account a = new Account();
                a.setId(rs.getInt("id"));
                a.setUserId(rs.getInt("user_id"));
                a.setAccountNumber(rs.getString("account_number"));
                a.setType(rs.getString("type"));
                a.setBalance(rs.getBigDecimal("balance"));
                return a;
            }
            return null;
        }
    }

    public Account findByAccountNumber(String accountNumber) throws Exception {
        String sql = "SELECT * FROM accounts WHERE account_number=? LIMIT 1";
        try (Connection con = DB.get(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Account a = new Account();
                a.setId(rs.getInt("id"));
                a.setUserId(rs.getInt("user_id"));
                a.setAccountNumber(rs.getString("account_number"));
                a.setType(rs.getString("type"));
                a.setBalance(rs.getBigDecimal("balance"));
                return a;
            }
            return null;
        }
    }

    public int createAccount(int userId, String accNumber, String type, java.math.BigDecimal balance) throws Exception {
        String sql = "INSERT INTO accounts(user_id, account_number, type, balance) VALUES(?,?,?,?)";
        try (Connection con = DB.get(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setString(2, accNumber);
            ps.setString(3, type);
            ps.setBigDecimal(4, balance);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }
}
