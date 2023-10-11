package com.example.gitgrub;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConn {
    public static Connection connectDB(){
        Connection connection = null;
        try {

            // below two lines are used for connectivity.
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://gitgrub-db.csrfrll6c2yx.us-east-2.rds.amazonaws.com:3306/RecipeDevOpsDB",
                    "admin", "Jupiter123");
            return connection;

        }
        catch (Exception exception) {
            System.out.println(exception);
        }
        return null;
    }
}
