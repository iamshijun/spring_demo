package cjava.walker.testcase.etl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
 
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
 
/**
 * from http://java-success.blogspot.com.au/2012/02/hibernate-interview-questions-and.html
 * FIXME use my format and parse method!
 */
public class EmployeeParser {
 
 private static final String DROP_SQL = "drop table tbl_employee";
    private static final String CREATE_SQL =
            "create table tbl_employee(employee_id varchar(12), emp_code varchar(12), " + 
            "manager_code  varchar(12), type  varchar(12), base_salary decimal";
 
 
    private static final String INSERT_SQL =
            "insert into tbl_employee (employee_id, emp_code, manager_code, type, base_salary) values (";
 
    
    public void parseEmployee(Session session) throws IOException {
        createDatabaseTable(session);
        BufferedReader file = findFile(getFileName());
        String[] data = readLine(file);
        while (data != null) {
            Query query =
                    session.createSQLQuery(INSERT_SQL + "'" + data[0] + "','" + data[1] + "','"
                            + data[2] + "'," + data[3] +  "','" + data[4] + ")");
            query.executeUpdate();
            data = readLine(file); // read next line from the file
        }
    }
     
     
    protected String[] readLine(BufferedReader file) throws IOException {
     String[] data = null;
        String line = file.readLine();
        while (line != null && line.startsWith("#")) {
            line = file.readLine();
        }
        if (line != null) {
         data =  line.split("\\|"); //split by "|" 
        }
       return data;
    }
 
 
    private void createDatabaseTable(Session session) {
        Query query = session.createSQLQuery(DROP_SQL);
        try {
            query.executeUpdate();
        } catch (HibernateException e) {
        }
        query =  session.createSQLQuery(CREATE_SQL);
        query.executeUpdate();
    }
     
    protected BufferedReader findFile(String fileName) {
        final InputStreamReader file =
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream(fileName));
        BufferedReader stream = new BufferedReader(file);
        return stream;
    }
 
   
 
    public String getFileName() {
        return "employeeSearch.txt";
    }
 
}