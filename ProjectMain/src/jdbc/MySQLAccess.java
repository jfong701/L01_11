package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class MySQLAccess {

	// JDBC Driver Name and Database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://mathlab.utsc.utoronto.ca:3306/";
	static final String dbName = "cscc43f17_manogar7_sakila";
	// Property variables
	static final String USER = "manogar7";
	static final String PASS = "manogar7";
	static final String USESSL = "true";
	static final String VERIFYSC = "false";
	
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement prepstmt = null;
	private Properties props = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MySQLAccess a = new MySQLAccess();
		a.start();
		a.close();
	}

	public void start() {
		loadAndConnect(dbName);
	}
	
	public void close() {
		try {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void loadAndConnect(String db_name){
		
		try {
			// Register JDBC Driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String new_URL = DB_URL + db_name;
			
			// Set properties if needed
			if (props == null) {
				setProperties();
			}
			
			// Open a connection
			conn = DriverManager.getConnection(new_URL, props);
			stmt = conn.createStatement();
			System.out.println("Connected to " +  new_URL +  " successfully.");
		} catch (Exception ex) {
			ex.printStackTrace();
			close();
		}
	}
	
	private void setProperties() {
		props = new Properties();
		props.setProperty("user", USER);
		props.setProperty("password", PASS);
		props.setProperty("verifyServerCertificate", VERIFYSC);
		props.setProperty("useSSL", USESSL);
	}
	
	public Connection getConn() {
		return conn;
	}
	
	public ResultSet execute(String query) throws SQLException {
		ResultSet result = null;
		try {
			start();
			Statement stmt = conn.createStatement();
			result = stmt.executeQuery(query);
			//close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void prepExecute(String query, Object... values) throws SQLException {
		try {
			start();
			PreparedStatement prepStmt = conn.prepareStatement(query);
			int i = 1;
			for (Object obj:values) {
				prepStmt.setObject(i++, obj);
			}
			prepStmt.execute();
			close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createTable(String tableName, String... sqlAttributes ) {
		String sql = "CREATE TABLE " + tableName + " " + concatValues(sqlAttributes);
		System.out.println("Create table " + tableName + " successfully.");
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void dropTable(String tableName) {
		try {
			System.out.println(tableName);
			stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=0");
			stmt.executeUpdate("DROP TABLE " + tableName);
			stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=1");
			System.out.println("Dropped table " + tableName + " successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String concatValues(String...strings) {
		String values = "(";
		for (String string : strings) {
			values += string + ", ";
		}		
		values = values.substring(0, values.length() - 2) + ")";
		return values;
	}
	
}
