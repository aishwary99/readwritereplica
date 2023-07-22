package manager;
import service.*;
import model.*;
import java.util.*;
import java.sql.*;

public class DepartmentManager {
    private static volatile DepartmentManager instance = null;
    private DepartmentService departmentService;

    private DepartmentManager() {
        this.departmentService = new DepartmentService();
    }

    public static DepartmentManager getInstance() {
        // double-checked
        if (instance == null) {
            synchronized (DepartmentManager.class) {
                if (instance == null) instance = new DepartmentManager();
            }
        }
        return instance;
    }

    public synchronized void add(String name) {
        Department department = new Department(name);
        departmentService.add(department);        
    }

    public synchronized List<Department> getDepartments() {
        return departmentService.getDepartments();
    }

    public static void main(String args[]) {
        try {
            ConnectionManager connectionManager = ConnectionManager.getInstance();
        } catch (Exception e) {
            System.out.println(e);
        }

        DepartmentManager departmentManager = DepartmentManager.getInstance();

        Scanner scanner = new Scanner(System.in);     
        System.out.println("-------------Department Management System--------------");
        while (true) {
            System.out.println(">> 1. Add Department");
            System.out.println(">> 2. List Department(s)");
            System.out.println(">> 3. Exit");
            System.out.print(">> Enter your choice : ");
            Integer choice = scanner.nextInt();
            if (choice < 1 && choice > 3) {
                System.out.println(">> Invalid choice, try again");
                continue;
            }

            if (choice == 1) {
                System.out.print(">> Enter Department name :: ");
                String departmentName = scanner.next();
                departmentManager.add(departmentName);
            } else if (choice == 2) {
                System.out.println("--------------------");
                List<Department> departments = departmentManager.getDepartments();
                for (Department department : departments) System.out.println(">> Code :: " + department.getCode() + " | Name :: " + department.getName());
                System.out.println("--------------------");
            } else if (choice == 3) {
                System.out.println(">> Bye !");
                break;
            }
        }
    }
}