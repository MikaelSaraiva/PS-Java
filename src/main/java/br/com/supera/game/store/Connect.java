package br.com.supera.game.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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

    public static void createProductTable(String db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + db + " (\n" + "id	    INTEGER PRIMARY KEY NOT NULL,\n"
                + "name		TEXT UNIQUE NOT NULL,\n" + "price 	    DOUBLE NOT NULL,\n"
                + "score	 	INTEGER NOT NULL,\n" + "image 	TEXT NOT NULL\n" + ");";

        // if (db == "cart") {
        // sql = sqlCreateCartTable;
        // } else {
        // sql = sqlCreateProductTable;
        // }

        try {
            Connection dbConnection = connect(db);
            Statement statement = dbConnection.createStatement();
            statement.execute(sql);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void insertProduct(String db, Product product) {
        String sql = "INSERT INTO " + db + "(id, name, price, score, image)VALUES (?, ?, ?, ?, ?)";

        // if (db == "cart") {
        // sql = sqlInsertCart;
        // } else {
        // sql = sqlInsertProduct;
        // }

        try (Connection dbConnection = connect(db); PreparedStatement statement = dbConnection.prepareStatement(sql);) {

            statement.setLong(1, product.id);
            statement.setString(2, product.name);
            statement.setBigDecimal(3, product.price);
            statement.setShort(4, product.score);
            statement.setString(5, product.image);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteProduct(String db, int id) {
        String sql = "DELETE FROM " + db + " WHERE product.id = ?";

        try (Connection dbConnection = connect(db); PreparedStatement statement = dbConnection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public ArrayList<Product> selectAll(String db) {
        String sql = "SELECT * FROM " + db;

        ArrayList<Product> products = new ArrayList<>();

        try (Connection dbConnection = connect(db);
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
        String sql = "SELECT * " + "FROM products WHERE id IN " + ids;
        ArrayList<Product> products = new ArrayList<Product>();

        try (Connection dbConnection = connect("products");
                PreparedStatement statement = dbConnection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();

            // loop through the result set
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