package com.excelFileUpload.utilities.listener;

import com.excelFileUpload.utilities.DbConnection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@WebListener
public class DatabaseBootstrap implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        DbConnection dbConnection = new DbConnection("jdbc:mysql://localhost:3306/","root",
                "camoncxair");

        System.out.println("INFO: Creating database if it does not exist....");
        //create statements
        Statement createDbStatement = null;
        Statement tableStatement = null;

        try {
            createDbStatement = dbConnection.connect().createStatement();
            createDbStatement.execute("CREATE DATABASE IF NOT EXISTS excelFile");

            System.out.println("INFO: db created or updated successfully...");

            System.out.println("INFO: Connection to database just created or existing");
            DbConnection dbConnection2 = new DbConnection("jdbc:mysql://localhost:3306/excelFile","root",
                    "camoncxair");

            //connect statement to connection
            tableStatement = dbConnection2.connect().createStatement();

            //create tables if not exists
            System.out.println("INFO: Creating tables");

            tableStatement.execute("create table if not exists user(name varchar(255),age int, town varchar(255))");


            sce.getServletContext().setAttribute("dbConnection", dbConnection2.connect());


        }catch (SQLException sqEx){
            sqEx.printStackTrace();
        }finally {
            try {
                if (createDbStatement != null)
                    createDbStatement.close();

                if (tableStatement != null)
                    tableStatement.close();

            }catch (SQLException sqlEx2){
                sqlEx2.printStackTrace();
            }
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        Connection connection = (Connection) sce.getServletContext().getAttribute("dbConnection");

        if (connection != null){
            try{
                connection.close();
            }catch (SQLException sqlEx){
                sqlEx.printStackTrace();
            }
        }

    }
}
