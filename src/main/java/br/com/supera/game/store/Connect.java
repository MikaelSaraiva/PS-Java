package br.com.supera.game.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

public class Connect {

    static String DB_PRODUCTS_PATH = "jdbc:sqlite:src/data/products.db";
    static String DB_CART_PATH = "jdbc:sqlite:src/data/cart.db";

    public static Connection connect(String db) {
        Connection connection = null;

        try {
            if (db == "products") {
                connection = DriverManager.getConnection(DB_PRODUCTS_PATH);
            } else {
                connection = DriverManager.getConnection(DB_CART_PATH);
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }

        return connection;
    }

    public static void createProductTable() {
        String sql = "CREATE TABLE IF NOT EXISTS products (\n" + "id	    INTEGER PRIMARY KEY NOT NULL,\n"
                + "name		TEXT UNIQUE NOT NULL,\n" + "price 	    DOUBLE NOT NULL,\n"
                + "score	 	INTEGER NOT NULL,\n" + "image 	TEXT NOT NULL\n" + ");";

        try {
            Connection dbConnection = connect("products");
            Statement statement = dbConnection.createStatement();
            statement.execute(sql);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public static void createCartTable() {
        String sql = "CREATE TABLE IF NOT EXISTS cart (\n" + "id	    INTEGER PRIMARY KEY NOT NULL,\n"
                + "products		BLOB UNIQUE NOT NULL,\n" + "checkout 	    BLOB NOT NULL\n" + ");";

        try {
            Connection dbConnection = connect("cart");
            Statement statement = dbConnection.createStatement();
            statement.execute(sql);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void insertProducts(ArrayList<Product> products, JSONObject checkout, long cartId) {
        String sql = "INSERT INTO cart (id, products, checkout) VALUES (?, ?, ?) ";

        try (Connection dbConnection = connect("cart");
                PreparedStatement statement = dbConnection.prepareStatement(sql);) {
            statement.setLong(1, cartId);
            statement.setObject(2, products);
            statement.setObject(3, checkout);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateCart(JSONObject cart) {
        String sql = "UPDATE cart SET products = ? , checkout = ? WHERE id = ?";

        try (Connection dbConnection = connect("cart");
                PreparedStatement statement = dbConnection.prepareStatement(sql)) {

            // set the corresponding param
            statement.setObject(1, cart.get("products"));
            statement.setObject(2, cart.get("checkout"));
            statement.setLong(3, cart.getLong("id"));
            // update
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Product> selectCartProducts(long cartId) {
        String sql = "SELECT * FROM cart WHERE id = ?";

        List<Product> products = new ArrayList<Product>();

        try {
            Connection dbConnection = connect("cart");
            PreparedStatement statement = dbConnection.prepareStatement(sql);
            statement.setLong(1, cartId);

            ResultSet resultSet = statement.executeQuery(sql);

            products = Arrays.asList((Product[]) resultSet.getObject("products"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return products;
    }

    public ArrayList<Product> selectAll() {
        String sql = "SELECT * FROM products";

        ArrayList<Product> products = new ArrayList<>();

        try (Connection dbConnection = connect("products");
                Statement statement = dbConnection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                products.add(new Product(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getBigDecimal("price"), resultSet.getShort("score"), resultSet.getString("image")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return products;
    }

    public ArrayList<Product> selectProducts(String ids) {
        String sql = "SELECT * FROM products WHERE id = ?";
        ArrayList<Product> products = new ArrayList<Product>();

        try {
            Connection dbConnection = connect("products");
            PreparedStatement statement = dbConnection.prepareStatement(sql);

            statement.setString(1, ids);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                products.add(new Product(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getBigDecimal("price"), resultSet.getShort("score"), resultSet.getString("image")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return products;
    }
}