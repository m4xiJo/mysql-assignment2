import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
public class Main {
    protected String url;
    protected String user;
    protected String passwd;
    protected Connection conn;

    public Main() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        url = "jdbc:mysql://127.0.0.1:3306/default_schema";
        user = "admin";
        passwd = "minininja123";
        conn = DriverManager.getConnection(url, user, passwd);
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();

        String userName = getInput("Type your firstname: ");
        String userPasswd = getInput("Type your password: ");

        Integer userId = null;
        Integer orderId = null;
        Integer productId = null;

        PreparedStatement findUser = main.prepState("SELECT customer_id, firstname, lastname FROM Customers WHERE firstname = ? AND passwd = ?");
        findUser.setString(1, userName);
        findUser.setString(2, userPasswd);
        ResultSet userResult = findUser.executeQuery();

        while (userResult.next()) {
            userId = userResult.getInt("customer_id");
            System.out.println("Welcome " + userResult.getString("firstname") + " " + userResult.getString("lastname"));
            System.out.println("Customer ID: " + userId);
        }

        PreparedStatement findOrder = main.prepState("SELECT order_id FROM Orders WHERE customer_id = ?");
        findOrder.setInt(1, userId);
        ResultSet orderResult = findOrder.executeQuery();
        while (orderResult.next()) {
            orderId = orderResult.getInt("order_id");
        }

        main.viewProducts();
        main.showCart(userId);
        String make = getInput("Type the make of the product to add to cart: ");
        String model = getInput("Type the model of the product to add to cart: ");
        String size = getInput("Type the size of the product to add to cart: ");

        PreparedStatement findProduct = main.prepState("SELECT product_id, make, model FROM Products WHERE make = ? AND model = ? AND size = ?");
        findProduct.setString(1, make);
        findProduct.setString(2, model);
        findProduct.setString(3, size);
        ResultSet productResult = findProduct.executeQuery();


        while (productResult.next()) {
            productId = productResult.getInt("product_id");
            System.out.println("Adding " + productResult.getString("make") + " " + productResult.getString("model") + " to cart..." );
        }
        PreparedStatement addToCart = main.prepState("CALL AddToCart(?, ?, ?)");
        if (userId == null)addToCart.setString(1, null);
        else addToCart.setInt(1, userId);
        if (orderId == null)addToCart.setString(2, null);
        else addToCart.setInt(2, orderId);
        if (productId == null)addToCart.setString(3, null);
        else addToCart.setInt(3, productId);
        ResultSet addToCartResult = addToCart.executeQuery();

    }

    public static String getInput(String message) throws Exception {
        BufferedReader lineReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(message);
        String input = lineReader.readLine();
        return input;
    }

    public void viewProducts() throws Exception {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Products WHERE instock > 0");
        System.out.println("\n---Product List---\n");
        while (rs.next()) {
            System.out.println(rs.getString("make") + " " + rs.getString("model"));
            System.out.println("Size: " + rs.getInt("size"));
            System.out.println("In stock: " + rs.getInt("instock"));
            System.out.println("Price: " + rs.getInt("price") + "\n");
        }
    }

    public void showCart(Integer customerId) throws Exception {
        System.out.println("\n---Your Cart---\n");
        String sql = "SELECT p.make, p.model, p.size, p.price, pbo.quantity\n"+
                "FROM Orders AS o \n" +
                "JOIN ProductsPerOrders pbo ON pbo.order_id = o.order_id \n" +
                "JOIN Products p ON p.product_id = pbo.product_id \n" +
                "WHERE customer_id = ? \n" +
                "GROUP BY p.model \n";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, customerId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString("make") + " " + rs.getString("model"));
            System.out.println("Size: " + rs.getInt("size"));
            System.out.println("Price: " + rs.getInt("price"));
            System.out.println("Quantity: " + rs.getInt("quantity") + "\n");
        }
    }

    public PreparedStatement prepState(String query) throws Exception {
        return conn.prepareStatement(query);
    }

}
