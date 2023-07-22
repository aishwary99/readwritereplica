package service;
import manager.*;
import model.*;
import java.sql.*;
import java.util.*;

public class DepartmentService {
    private static ConnectionManager connectionManager = null;
    private static Integer departmentId = 99;
    private final String INSERT_STATEMENT = "INSERT INTO department VALUES (?,?)";
    private final String SELECT_QUERY = "SELECT * FROM department";
    

    public DepartmentService() {
        connectionManager = ConnectionManager.getInstance();
    }

    public void add(Department department) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnectionForWriting();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STATEMENT);
            departmentId = departmentId + 1;
            preparedStatement.setString(1, String.valueOf(departmentId));
            preparedStatement.setString(2, department.getName());
            preparedStatement.executeUpdate();
            department.setCode(departmentId);

            System.out.println("Department :: " + department.getName() + " :: " + department.getCode() + " inserted");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Department> getDepartments() {
        List<Department> departments = new ArrayList<>();
        Connection connection = null;
        try {
            connection = connectionManager.getConnectionForReading();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_QUERY);            

            while (resultSet.next()) {
                Integer departmentCode = resultSet.getInt("code");
                String departmentName = resultSet.getString("name");
                departments.add(new Department(departmentCode, departmentName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return departments;
    }
}