package jdbc;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class OldMySQLAccess {
	
	// JDBC Driver Name and Database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://mathlab.utsc.utoronto.ca:3306/";
	
	// Property variables
	static final String USER = "manogar7";
	static final String PASS = "manogar7";
	static final String USESSL = "true";
	static final String VERIFYSC = "false";
	
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement prepstmt = null;
	private Properties props = null;
	
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
	
	public Connection getConn() {
		return conn;
	}
	
	public int createDatabase(String db_name){
		try {
			loadAndConnect("");
			if (checkDatabaseExists(db_name)) {
				close();
				return 0;
			} else {
				stmt.executeUpdate("CREATE DATABASE " + db_name);
				System.out.println(db_name + " created successfully.");
				close();
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 2;
		}
		
	}
	
	public void executeSQL(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ResultSet executeSQLQuery(String sql) {
		ResultSet rs = null;
		try {
			System.out.println(sql);

			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	public boolean checkDatabaseExists(String db_name) throws SQLException {
		ResultSet rs = conn.getMetaData().getCatalogs();
		
		while (rs.next()) {
			if (rs.getString(1).equals(db_name)) {
				return true;
			}
		}
		return false;
	}
	
	public void deleteDatabase(String db_name){
		try {
			loadAndConnect("");
			stmt.executeUpdate("DROP DATABASE " + db_name);
			System.out.println(db_name + " dropped successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
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
	
	public void insertRecords(String tableName, String... values) {
		try {
			String sql = insertSQL(tableName, values);
			System.out.println(sql);
			stmt.executeUpdate(sql);
			System.out.println(sql + " completely successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String preparedRecordsSQL(String tableName, int length, String... values) {
		String sql = "INSERT INTO " + tableName + " " + concatValues(values) + " VALUES " + concatQuestionMarks(length);
		return sql;
	}
	
	public void updateRecords(String tableName, String update, String requisite) {
		try {
			String sql = "UPDATE " + tableName + " SET " + update + " WHERE " + requisite;
			int rowsUpdated =stmt.executeUpdate(sql);
			System.out.println(sql + " completely successfully. " + rowsUpdated + " rows updated." );
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public ResultSet selectRecords(String tableName, String...columns) {
		ResultSet resultSet = null;
		try {
			String sql = selectSQL(tableName, columns);
			resultSet = stmt.executeQuery(sql);
			System.out.println(sql + " completely successfully.\n");
			
			printResultSet(resultSet, columns);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	public ResultSet selectRecordsWhere(String tableName, String where, String...columns) {
		ResultSet resultSet = null;
		try {
			String sql = selectSQL(tableName, columns) + " WHERE " + where;
			System.out.println(sql);
			resultSet = stmt.executeQuery(sql);
			System.out.println(sql + " completely successfully.\n");
			
			printResultSet(resultSet, columns);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	public ResultSet selectRecordsWhereLike(String tableName, String where, String like, String...columns) {
		ResultSet resultSet = null;
		try {
			String sql = selectSQL(tableName, columns) + " WHERE " + where + " LIKE " + like;
			resultSet = stmt.executeQuery(sql);
			System.out.println(sql + " completely successfully.\n");
			
			printResultSet(resultSet, columns);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	public ResultSet selectRecordsOrderBy(String tableName, String orderBy, String order, String...columns) {
		ResultSet resultSet = null;
		try {
			String sql = selectSQL(tableName, columns) + " ORDER BY " + orderBy + " " + order.toUpperCase();
			resultSet = stmt.executeQuery(sql);
			System.out.println(sql + " completely successfully.\n");
			
			printResultSet(resultSet, columns);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	public void deleteRecords(String tableName, String requisite) {
		try {
			String sql = "DELETE FROM " + tableName + " WHERE " + requisite;
			int rowsDeleted = stmt.executeUpdate(sql);
			System.out.println(sql + " completely successfully. " + rowsDeleted + " rows deleted.");
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public void printResultSet(ResultSet resultSet, String... columns) throws SQLException {
		
		int colLength = columns.length;
		Object[] colNames = (Object[]) columns;
		ResultSetMetaData rsmd = resultSet.getMetaData();

		if (columns[0].equals("*")) {
			colLength = rsmd.getColumnCount();
			colNames = new Object[colLength];
			for (int i = 1; i <= colLength; i++) {
				colNames[i-1] = rsmd.getColumnLabel(i);
			}
		}
				
		String format = String.format("%0" + colLength + "d", 0).replace("0", "%-20s") + "\n";
		System.out.format(format + "\n", colNames);
		Object[] row = new Object[colLength];
		
		
		
		
		while(resultSet.next()) {
			for (int i = 1; i <= colLength; i++) {
				row[i-1] = resultSet.getObject(i);
			}
			System.out.format(format, row);
		}
		
		resultSet.beforeFirst();
	}
			
	private void setProperties() {
		props = new Properties();
		props.setProperty("user", USER);
		props.setProperty("password", PASS);
		props.setProperty("verifyServerCertificate", VERIFYSC);
		props.setProperty("useSSL", USESSL);
	}
	
	private String insertSQL(String tableName, String... values) {
		String sql = "INSERT INTO " + tableName + " VALUES " + concatValues(values);
		System.out.println(sql);
		return sql;
	}
	
	private String concatValues(String...strings) {
		String values = "(";
		for (String string : strings) {
			values += string + ", ";
		}		
		values = values.substring(0, values.length() - 2) + ")";
		return values;
	}
	
	private String concatQuestionMarks(int num) {
		String values = "(";
		for (int i = 0; i < num ; i++) {
			values += "?, ";
		}		
		values = values.substring(0, values.length() - 2) + ")";
		return values;
	}
	
	private String selectSQL(String tableName, String... columns) {
		String sql = "SELECT " + concatValues(columns).replace("(", "") .replace(")", "") + " FROM " + tableName;
		return sql;
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
}
