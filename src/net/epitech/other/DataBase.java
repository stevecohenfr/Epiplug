package net.epitech.other;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.configuration.file.FileConfiguration;

public class DataBase {

	private static Connection 	connexion 	= null;
	private static String		addr;
	private static String		port;
	private static String		bdd;
	private static String		user;
	private static String		pass;
	
	
	public static boolean DataBaseConnect(FileConfiguration config) {
		addr = config.getString("MySQL.Host");
		port = config.getString("MySQL.Port");
		bdd = config.getString("MySQL.Bdd");
		user = config.getString("MySQL.User");
		pass = config.getString("MySQL.Pass");
		if (addr == null || port == null || bdd == null || user == null || pass == null)
			return false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connexion = DriverManager.getConnection(
					"jdbc:mysql://" + addr + ":" + port + "/" + bdd + "?autoReconnect=true",
					user, pass);
			if (connexion.isValid(5000))
				return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static void modifyUserList(String username, int value) {
		/* Modify user list on mysql database */
		if (connexion == null)
			return ;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "UPDATE `vose_users` SET `inUse` = '" + value + "' WHERE `vose_users`.`pseudo` = '" + username + "'";
			stm.executeUpdate(SQL);
			System.out.println("MYSQL => " + SQL);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static int readUserList(String username) {
		/* Get user list on mysql database */
		if (connexion == null)
			return -1;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "SELECT `inUse` FROM `vose_users` WHERE `pseudo` = '" + username + "'";
			ResultSet res = stm.executeQuery(SQL);
			if (res.next())
			{
				int inUse = res.getInt(1);
				System.out.println("MYSQL => " + SQL);
				return inUse;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void resetUserList() {
		if (connexion == null)
			return;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "UPDATE `vose_users` SET `inUse` = 0, `id` = 0 WHERE `inUse` != 0 OR `id` != 0";
			stm.executeUpdate(SQL);
			System.out.println("MYSQL => " + SQL);
			return;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	public static void resetSpecificUser(String user) {
		if (connexion == null)
			return;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "UPDATE `vose_users` SET `inUse` = 0, `id` = 0 WHERE `pseudo` = '" + user + "'";
			stm.executeUpdate(SQL);
			System.out.println("MYSQL => " + SQL);
			return;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	public static boolean closeSession() {
		try {
			connexion.close();
			if (connexion.isClosed())
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}