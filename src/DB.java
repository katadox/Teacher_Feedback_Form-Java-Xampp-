import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/teacher_feedback?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true";
	private static final String JDBC_USER = "root"; // XAMPP default
	private static final String JDBC_PASSWORD = ""; // set if you configured a password

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("MySQL Connector/J not found. Add mysql-connector-j.jar to classpath.", e);
		}
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
	}

	public static void initSchema() {
		try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
			st.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
					"username VARCHAR(64) PRIMARY KEY, " +
					"password VARCHAR(128) NOT NULL, " +
					"role ENUM('TEACHER','STUDENT') NOT NULL, " +
					"subject VARCHAR(64) NULL" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");

			StringBuilder fb = new StringBuilder();
			fb.append("CREATE TABLE IF NOT EXISTS feedback (" +
					"id INT AUTO_INCREMENT PRIMARY KEY, " +
					"student_name VARCHAR(128) NOT NULL, " +
					"roll_no VARCHAR(64) NOT NULL, " +
					"subject VARCHAR(64) NOT NULL, ");
			for (int i = 1; i <= 28; i++) {
				fb.append("q" + i + " TINYINT NOT NULL,");
			}
			fb.append("created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
			fb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
			st.executeUpdate(fb.toString());

			// Seed teacher accounts if not present
			seedUsers();
		} catch (SQLException e) {
			throw new RuntimeException("Failed to initialize schema", e);
		}
	}

	private static void seedUsers() throws SQLException {
		try (Connection conn = getConnection()) {
			upsertUser(conn, "Rakesh", "1234567", "TEACHER", "TOC");
			upsertUser(conn, "Happy", "123", "TEACHER", "OOP");
			upsertUser(conn, "Prabha", "909", "TEACHER", "MATHS");
			upsertUser(conn, "Rishika", "678", "TEACHER", "DS");
			upsertUser(conn, "Deepthy", "456", "TEACHER", "DELD");
		}
	}

	private static void upsertUser(Connection conn, String username, String password, String role, String subject) throws SQLException {
		String sql = "INSERT INTO users (username, password, role, subject) VALUES (?,?,?,?) " +
				"ON DUPLICATE KEY UPDATE password=VALUES(password), role=VALUES(role), subject=VALUES(subject)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, role);
			ps.setString(4, subject);
			ps.executeUpdate();
		}
	}

	public static boolean authenticateTeacher(String username, String password) {
		String sql = "SELECT 1 FROM users WHERE username=? AND password=? AND role='TEACHER'";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			ps.setString(2, password);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getTeacherSubject(String username) {
		String sql = "SELECT subject FROM users WHERE username=?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) return rs.getString(1);
			}
			return null;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static void insertFeedback(String studentName, String rollNo, String subject, int[] answers) {
		if (answers == null || answers.length != 28) throw new IllegalArgumentException("answers must have 28 items");
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO feedback (student_name, roll_no, subject");
		for (int i = 1; i <= 28; i++) sb.append(", q" + i);
		sb.append(") VALUES (?,?,?");
		for (int i = 1; i <= 28; i++) sb.append(",?");
		sb.append(")");
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sb.toString())) {
			ps.setString(1, studentName);
			ps.setString(2, rollNo);
			ps.setString(3, subject);
			for (int i = 0; i < 28; i++) {
				ps.setInt(4 + i, answers[i]);
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static double[] getQuestionMeansForSubject(String subject) {
		double[] means = new double[28];
		StringBuilder sb = new StringBuilder("SELECT ");
		for (int i = 1; i <= 28; i++) {
			sb.append("AVG(q" + i + ")");
			if (i < 28) sb.append(", ");
		}
		sb.append(" FROM feedback WHERE subject=?");
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sb.toString())) {
			ps.setString(1, subject);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					for (int i = 0; i < 28; i++) {
						means[i] = rs.getDouble(i + 1);
					}
				}
			}
			return means;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}


