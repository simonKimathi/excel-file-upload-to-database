package com.excelFileUpload.User.servlet;

import com.excelFileUpload.User.model.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/file")
public class userServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext scx = getServletContext();
        Connection dbConnection = (Connection) scx.getAttribute("dbConnection");

        String name = req.getParameter("name");
        int age = Integer.parseInt(req.getParameter("age"));
        String town = req.getParameter("town");

        try {
            PreparedStatement statement = dbConnection.prepareStatement("insert into user(name,age,town) values(?,?,?)");
            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, town);
            statement.executeUpdate();

            resp.getWriter().print("OK");

        }catch (SQLException sqlEx){
            sqlEx.printStackTrace();
        }

    }
}
