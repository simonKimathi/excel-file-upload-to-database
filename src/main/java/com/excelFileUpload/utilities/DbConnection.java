package com.excelFileUpload.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    String url;
    String user;
    String pass;

    public DbConnection(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    public Connection connect(){
        Connection connection=null;


        try {

            connection= DriverManager.getConnection(url,user,pass);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }
}
