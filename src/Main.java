import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
public class Main {
    public Main() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/default_schema";
        String user = "admin";
        String passwd = "minininja123";
        Connection conn = DriverManager.getConnection(url, user, passwd);
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        String userName = getInput("Type your username: ");
        String userPasswd = getInput("Type your password: ");

        String make = getInput("Type the make of the product to add to cart: ");
        String model = getInput("Type the model of the product to add to cart: ");
        String size = getInput("Type the size: ");

        PreparedStatement prepStmt = conn.prepareStatement("SELECT namn, adress, nummer FROM Personuppgifter where namn = ?");
        prepStmt.setString(1, "Kalle");
        stmt.executeQuery();

        ResultSet rs = stmt.executeQuery("SELECT customer_id FROM Customers WHERE firstName = ");
        while (rs.next()) {
            System.out.println(rs.getString("make")+ " " +rs.getString("model"));
            System.out.println("Size: " + rs.getInt("size"));
            System.out.println("In stock: " + rs.getInt("instock"));
            System.out.println("Price: " + rs.getInt("price") + "\n");
        }

        stmt.executeQuery("CALL AddToCart");
    }

    public static String getInput(String message) throws Exception {
        BufferedReader lineReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(message);
        String input = lineReader.readLine();
        return input;
    }

    public void viewProducts(String message) throws Exception {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Products WHERE instock > 0");
        System.out.println("\n---Product List---\n");
        while (rs.next()) {
            System.out.println(rs.getString("make")+ " " +rs.getString("model"));
            System.out.println("Size: " + rs.getInt("size"));
            System.out.println("In stock: " + rs.getInt("instock"));
            System.out.println("Price: " + rs.getInt("price") + "\n");
        }
    }

}
