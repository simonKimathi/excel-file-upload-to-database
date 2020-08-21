package com.excelFileUpload.User.servlet;


import com.excelFileUpload.User.model.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@WebServlet(name = "FileHandler", urlPatterns = {"/FileHandler"})
public class userServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String DATA_DIRECTORY = "data";
    private static final int MAX_MEMORY_SIZE = 1024 * 1024 * 2;
    private static final int MAX_REQUEST_SIZE = 1024 * 1024;
    String path;
    public void init() throws ServletException {
        path = getServletContext().getInitParameter("file-upload");
        File f = new File("uploads");
        if (!f.exists()) {
            System.out.println("");
            f.mkdir();
        }
        path = f.getPath();
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        doPost(request,response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext scx = getServletContext();
        Connection dbConnection = (Connection) scx.getAttribute("dbConnection");
        //processRequest(request, response);
        //PrintWriter out = response.getWriter();
        // Check that we have a file upload request
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (!isMultipart) {
            return;
        }

        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Sets the size threshold beyond which files are written directly to
        // disk.
        factory.setSizeThreshold(MAX_MEMORY_SIZE);

        // Sets the directory used to temporarily store files that are larger
        // than the configured size threshold. We use temporary directory for
        // java
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        // constructs the folder where uploaded file will be stored
        String uploadFolder = getServletContext().getRealPath("")
                + File.separator + DATA_DIRECTORY;

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);


        // Set overall request size constraint
        upload.setSizeMax(MAX_REQUEST_SIZE);

        try {
            // Parse the request
            List items = upload.parseRequest(request);
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();

                if (!item.isFormField()) {
                    String fileName = new File(item.getName()).getName();
                    String filePath = path + File.separator + fileName;
                    File uploadedFile = new File(filePath);
                    System.out.println(filePath);
                    // saves the file to upload directory
                    item.write(uploadedFile);
                    File file = new File(filePath);
                    FileInputStream fis = new FileInputStream(file);
                    Workbook workbook = new XSSFWorkbook(fis);

                    Sheet sheet=workbook.getSheetAt(0);

                    // Row row;
                    Iterator<org.apache.poi.ss.usermodel.Row> itr = sheet.iterator();
                    List<User> models = new ArrayList<>();
                    int numRow = 0;
                    while (itr.hasNext()) {
                        if (numRow != 0) {
                            org.apache.poi.ss.usermodel.Row row = itr.next();
                            User userModel = new User(
                                    row.getCell(0).getStringCellValue(),
                                    row.getCell(2).getStringCellValue(),
                                    row.getCell(1).getNumericCellValue()
                            );
                            models.add(userModel);
                        } else {
                            itr.next();
                            numRow++;
                        }
                    }
                    models.forEach(list -> {
                        addToDb(list);
                    });



                    /*DataFormatter formatter = new DataFormatter(Locale.US);
                    for(int i=1; i<=sheet.getLastRowNum(); i++)
                    {
                        try {
                            PreparedStatement ps = dbConnection.prepareStatement("insert into user(name,age,town) values(?,?,?)");
                            row = (Row) sheet.getRow(i);
                            ps.setString(1, "" + formatter.formatCellValue(sheet.getRow(i).getCell(0)));
                            ps.setString(2, "" + formatter.formatCellValue(sheet.getRow(i).getCell(1)));
                            ps.setString(3, "" + formatter.formatCellValue(sheet.getRow(i).getCell(2)));
                            ps.executeUpdate();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }*/
                }
            }

            PrintWriter out = response.getWriter();
            out.println("data uploaded successfully");


        } catch (FileUploadException ex) {
            throw new ServletException(ex);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
    private void addToDb(User list) {
        try {
            ServletContext scx = getServletContext();
            Connection dbConnection = (Connection) scx.getAttribute("dbConnection");
            PreparedStatement ps = dbConnection.prepareStatement("insert into user(name,age,town) values(?,?,?)");

            ps.setString(1, list.getName());
            ps.setDouble(2, list.getAge());
            ps.setString(3, list.getTown());
            ps.executeUpdate();;

        } catch (SQLException ex) {

        }
    }
}

