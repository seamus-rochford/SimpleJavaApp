package com.trandonsystems.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import com.trandonsystems.database.UtilDAL;
import com.trandonsystems.model.User;

public class UserDAL {

	static Logger log = Logger.getLogger(UserDAL.class);
	static ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();

	public UserDAL() {
		log.trace("Constructor");

		// Configure password Encrypter
		passwordEncryptor.setAlgorithm("SHA-1");
		passwordEncryptor.setPlainDigest(true);
	}

	public static User getBySQL(int id) {

		log.info("UserDAL.getBySQL");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			log.error("ERROR: Can't create instance of driver" + ex.getMessage());
		}

		String sql = "SELECT * FROM users WHERE id = " + id;
		log.info("SQL: " + sql);
		
		User user = new User();
		
		log.debug("Connection String: " + UtilDAL.connUrl);
		log.debug("Username: " + UtilDAL.username);
		
		try (Connection conn = DriverManager.getConnection(UtilDAL.connUrl, UtilDAL.username, UtilDAL.password);
				PreparedStatement pst = conn.prepareStatement(sql);
				ResultSet rs = pst.executeQuery()) {

			user.setId(id);
			if (rs.next()) {
				user.setName(rs.getString("name"));
				user.setAddr1(rs.getString("addr1"));
				user.setAddr2(rs.getString("addr2"));
			}

		} catch (SQLException ex) {
			log.error(ex.getMessage());
		}

		return user;
	}

	public static User get(int id) {

		log.info("UserDAL.get(id)");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			log.error("ERROR: Can't create instance of driver" + ex.getMessage());
		}

		String spCall = "{ call GetUserById(?) }";
		log.info("SP Call: " + spCall);
		
		User user = new User();
		
		try (Connection conn = DriverManager.getConnection(UtilDAL.connUrl, UtilDAL.username, UtilDAL.password);
				CallableStatement spStmt = conn.prepareCall(spCall)) {

			spStmt.setInt(1, id);
			ResultSet rs = spStmt.executeQuery();

			user.setId(id);
			if (rs.next()) {
				user.setName(rs.getString("name"));
				user.setAddr1(rs.getString("addr1"));
				user.setAddr2(rs.getString("addr2"));
			}

		} catch (SQLException ex) {
			log.error(ex.getMessage());
		}

		return user;
	}

}
