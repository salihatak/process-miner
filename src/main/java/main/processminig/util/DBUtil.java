package main.processminig.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import javax.naming.InitialContext;
import javax.sql.DataSource;

//import com.sap.engine.services.dbpool.deploy.DataSourceManager;

public class DBUtil {
	public static boolean inPortal = true;

	public static Connection getConnection() {
		Connection conn = null;
		// try {
		// 	InitialContext context = new InitialContext();
		// 	final DataSourceManager dsm = (DataSourceManager) context.lookup("dbpool");
		// 	String lookUpName = dsm.getSystemDataSourceDescriptor().getDataSourceName();
		// 	DataSource ds = (DataSource) context.lookup("jdbc/" + lookUpName);
		// 	conn = ds.getConnection();
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
		return conn;
	}

	public static void close(Connection con, PreparedStatement stmt, ResultSet rset) {
		close(con);
		close(stmt);
		close(rset);
	}

	public static void close(Connection con, Statement stmt, ResultSet rset) {
		close(con);
		close(stmt);
		close(rset);
	}

	public static void close(Connection con, PreparedStatement stmt) {
		close(con);
		close(stmt);
	}

	public static void close(Connection con, Statement stmt) {
		close(con);
		close(stmt);
	}

	public static void close(Connection resource) {
		try {
			if (resource != null) {
				resource.close();
				resource = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void close(PreparedStatement resource) {
		try {
			if (resource != null) {
				resource.close();
				resource = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void close(Statement resource) {
		try {
			if (resource != null) {
				resource.close();
				resource = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void close(ResultSet resource) {
		try {
			if (resource != null) {
				resource.close();
				resource = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static long nextId(String table, String col) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		long maxid = 0;
		try {
			conn = DBUtil.getConnection();
			stmt = conn.createStatement();
			rset = stmt.executeQuery("select max(" + col + ") as maxid from " + table + "");
			if (rset.next()) {
				maxid = rset.getLong("maxid");
				maxid++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, stmt);
		}
		return maxid;
	}

	public static long nextLongId(String table, String col) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		long maxid = 0;
		try {
			conn = DBUtil.getConnection();
			stmt = conn.createStatement();
			rset = stmt.executeQuery("select max(" + col + ") as maxid from " + table + "");
			if (rset.next()) {
				maxid = rset.getLong("maxid");
				maxid++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, stmt);
		}
		return maxid;
	}

	public static int nextIntVersion(String table, String col, String idString, String fileName) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		int maxid = 1;

		String findId = "select max(" + col + ") as maxid from " + table + " where name = ? and idstring = ?";
		try {
			int x = 1;
			conn = DBUtil.getConnection();
			stmt = conn.prepareStatement(findId);
			DBUtil.setString(stmt, x++, fileName);
			DBUtil.setString(stmt, x++, idString);
			rset = stmt.executeQuery();

			if (rset.next()) {
				maxid = rset.getInt("maxid");
				maxid++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, stmt);
		}
		return maxid;
	}

	public static long nextIdmas(String table, String col, long task) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		long maxid = 0;

		try {
			conn = DBUtil.getConnection();
			stmt = conn.createStatement();
			rset = stmt
					.executeQuery("select max(" + col + ") as maxid from " + table + " where masrafid =" + task + "");
			if (rset.next()) {
				maxid = rset.getLong("maxid");
				maxid++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, stmt);
		}

		return maxid;
	}

	public static void setString(PreparedStatement prst, int index, String value) {
		try {
			if (value != null && !value.trim().equals(""))
				prst.setString(index, value.trim());
			else
				prst.setNull(index, Types.VARCHAR);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		getConnection();
		System.out.println("connected !");
	}
}
