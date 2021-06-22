package br.com.supera.game.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Connect {

    static String DB_PATH = "jdbc:sqlite:src/data/products.db";

    public static Connection connect() {
        Connection connection = null;

        try {

            connection = DriverManager.getConnection(DB_PATH);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }

        return connection;
    }

    public static void createProductTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Products (\n" 
                + "id	    INTEGER PRIMARY KEY NOT NULL,\n"
                + "name		TEXT UNIQUE NOT NULL,\n" 
                + "price 	    DOUBLE NOT NULL,\n"
                + "score	 	INTEGER NOT NULL,\n" 
                + "image 	TEXT NOT NULL\n" 
                + ");";

        try {
            Connection dbConnection = connect();
            Statement statement = dbConnection.createStatement();
            statement.execute(sql);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public ArrayList<Product> selectAll() {
        String sql = "SELECT * FROM Products";

        ArrayList<Product> products = new ArrayList<>();

        try (Connection conn = connect();
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                products.add(new Product(
                    resultSet.getInt("id"), 
                    resultSet.getString("name"),
                    resultSet.getDouble("price"), 
                    resultSet.getInt("score"), 
                    resultSet.getString("image"))
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return products;
    }

}