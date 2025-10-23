package dao;
import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
    public static Connection get() throws Exception {
        String url = "jdbc:mysql://localhost:3306/kdbank";
        String user = "root";       // <-- change to your DB username
        String pass = "root";   // <-- change to your DB password
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, pass);
    }
}
