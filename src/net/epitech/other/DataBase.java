package net.epitech.other;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
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
			connexion = DriverManager.getConnection("jdbc:mysql://" + addr + ":" + port + "/" + bdd + "?autoReconnect=true", user, pass);
			if (connexion.isValid(5000))
				return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
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

	/*********************************************************************************************/
	/******************************** Users gesture **********************************************/
	/*********************************************************************************************/

	public static void modifyUserList(String username, int value) {
		/* Modify user list on mysql database */
		if (connexion == null || username == null)
			return ;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "UPDATE `vose_users` SET `inUse` = '" + value + "' WHERE `vose_users`.`pseudo` = '" + username + "'";
			stm.executeUpdate(SQL);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static String getUserPrefix() {
		/* Get city user prefix on mysql database */
		if (connexion == null)
			return "disconnected";
		try {
			Statement stm = connexion.createStatement();
			String SQL = "SELECT `user_prefix` FROM `config`";
			ResultSet res = stm.executeQuery(SQL);
			if (res.next())
				return res.getString("user_prefix");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "ko";
	}

	public static int readUserList(String username) {
		/* Get user list on mysql database */
		if (connexion == null || username == null)
			return -1;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "SELECT `inUse` FROM `vose_users` WHERE `pseudo` = '" + username + "'";
			ResultSet res = stm.executeQuery(SQL);
			if (res.next())
			{
				int inUse = res.getInt(1);
				return inUse;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static boolean compareIds(String username) {
		/* Get user data (*) on mysql database */
		if (connexion == null)
			return false;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "SELECT `id`, `last_id` FROM `vose_users` WHERE `pseudo` = '" + username + "'";
			ResultSet res = stm.executeQuery(SQL);
			if (res.next())
			{
				if (res.getString("id").equals("null"))
					return false;
				if (res.getString("id").equals(res.getString("last_id")))
					return true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void modifyIds(String username) {
		/* Change ids in DB */
		if (connexion == null || username == null)
			return;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "UPDATE `vose_users` SET `id` = `last_id` WHERE `pseudo` = '" + username + "'";
			stm.executeUpdate(SQL);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}

	public static void resetUserList() {
		/* Reset ALL fields in vose_users */
		if (connexion == null)
			return;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "UPDATE `vose_users` SET `inUse` = 0, `id` = 'null' WHERE `inUse` != 0 OR `id` != 'null' OR `last_id` != 'null' OR `fb_username` != 'null'";
			stm.executeUpdate(SQL);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}

	public static void resetSpecificUser(String user) {
		/* Reset 'inUse' and 'id' field in vose_users for a specific user */
		if (connexion == null || user == null)
			return;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "UPDATE `vose_users` SET `inUse` = 0, `id` = 'null' WHERE `pseudo` = '" + user + "'";
			stm.executeUpdate(SQL);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}

	public static String checkUserInDb(String login, String password) {
		/* Check if login and password are correct according to the DB */
		if (connexion == null || login == null || password == null)
			return "disconnected";
		try {
			Statement stm = connexion.createStatement();
			ResultSet res = stm.executeQuery("SELECT `Password` FROM `vose_adm` WHERE `Login` = '" + login + "'");
			if (res.next()) {
				if (res.getString("Password").equals("null") && password.equals("37a6259cc0c1dae299a7866489dff0bd")) //md5("null")
					return "defpass";
				if (password.equals(res.getString("Password")))
					return "ok";
			}
		} catch(Exception e) {
			System.err.println("Connexion à la base de donnée impossible.");
		}
		System.err.println("Login ou mot de passe incorrect.");
		return "ko";
	}

	public static String getUserRank(String username) {
		/* Get the user Rank value */
		if (connexion == null || username == null)
			return "disconnected";
		try {
			Statement stm = connexion.createStatement();
			String SQL = "SELECT `Rank` FROM `vose_adm` WHERE `Login` = '" + username + "'";
			ResultSet res = stm.executeQuery(SQL);
			if (res.next())
				return (res.getString("Rank"));
			SQL = "SELECT pseudo FROM `vose_users` WHERE `pseudo` = '" + username + "'";
			res = stm.executeQuery(SQL);
			if (res.next())
				return ("Guest");
		} catch(Exception e) {
			System.err.println("Connexion à la base de donnée impossible.");
		}
		return "unknown";
	}

	public static String getUserCity(String username) {
		/* Get the user city value */
		if (connexion == null || username == null)
			return "disconnected/unknown";
		try {
			Statement stm = connexion.createStatement();
			String SQL = "SELECT `City` FROM `vose_adm` WHERE `Login` = '" + username + "'";
			ResultSet res = stm.executeQuery(SQL);
			if (res.next())
				return (res.getString("City"));
			String id = getIdByName(username);
			SQL = "SELECT location FROM `facebook_users` WHERE `id` = '" + id + "'";
			res = stm.executeQuery(SQL);
			if (res.next())
				return res.getString("location");
		} catch(Exception e) {
			System.err.println("Connexion à la base de donnée impossible.");
		}
		return "unknown";
	}

	public static void setfb_username(String user) {
		/* set fb_username field in vose_users */
		if (connexion == null || user == null)
			return;
		try {
			String fb_username = getUserInfo(user).get("first_name") + '.' + getUserInfo(user).get("last_name");
			Statement stm = connexion.createStatement();
			String SQL = "UPDATE `vose_users` SET `fb_username` = '" + fb_username + "' WHERE `pseudo` = '" + user + "'";
			stm.executeUpdate(SQL);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}

	public static boolean hasNext(ResultSet res) throws SQLException {
		if (res.next()) {
			res.previous();
			return true;
		}
		return false;
	}

	public static List<HashMap<String, String>> resultSetToArrayListStrStr(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>(50);
		while (rs.next()) {
			LinkedHashMap<String, String> row = new LinkedHashMap<String, String>(columns);
			for(int i = 1; i <= columns; ++i) {           
				row.put(md.getColumnName(i), rs.getString(i));
			}
			list.add(row);
		}

		return list;
	}

	public static Map<String, String> getUserInfo(String username) {
		/* Get any user infos we can have in facebook db */
		Map<String, String>	infos = new HashMap<String, String>();
		if (connexion == null || username == null) {
			return null;
		}
		try {
			Statement stm = connexion.createStatement();
			String SQL = "SELECT * FROM `vose_users` LEFT JOIN `facebook_users` on `vose_users`.`id` = `facebook_users`.`id` WHERE `vose_users`.`pseudo` = '" + username + "'";
			ResultSet res = stm.executeQuery(SQL);
			if (hasNext(res)) {
				infos =  resultSetToArrayListStrStr(res).get(0);
			}else {
				infos.put("id", "NC");						infos.put("email", "NC");				infos.put("last_name", "NC");
				infos.put("first_name", "NC");				infos.put("username", "NC");			infos.put("birthday", "NC");
				infos.put("education", "NC");				infos.put("Candidature", "NC");			infos.put("Time", "NC");
				infos.put("Documentation", "NC");			infos.put("secret_code_1", "NC");		infos.put("secret_code_2", "NC");
				infos.put("secret_code_3", "NC");			infos.put("secret_code_4", "NC");		infos.put("secret_code_5", "NC");
				infos.put("hasConnectedAtLeastOnce", "NC");	infos.put("Condition1", "NC");			infos.put("Condition2", "NC");
				infos.put("Condition3", "NC");				infos.put("pseudo", username);
			}
			infos.put("rank", DataBase.getUserRank(username));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return infos;
	}

	public static Map<String, JpoAdmin> getAdmins() {
		/* Get all adm in a Map<String, JpoAdmin> */
		Map<String, JpoAdmin> admins = new HashMap<String, JpoAdmin>();
		if (connexion == null)
			return null;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "SELECT Login, Rank, City FROM `vose_adm`";
			ResultSet res = stm.executeQuery(SQL);
			while (res.next()) {
				admins.put(res.getString("Login"), new JpoAdmin(res.getString("Login"), res.getString("Rank"), res.getString("City")));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return admins;
	}

	public static boolean tutoPlayerDone(String id) {
		if (connexion == null)
			return false;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "SELECT `hasConnectedAtLeastOnce` FROM `facebook_users` WHERE `id` = '" + id + "'";
			ResultSet res = stm.executeQuery(SQL);
			if (res.next()) {
				if (res.getInt("hasConnectedAtLeastOnce") == 2)
					return true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void setHasConnectedAtLeastOnce(String id, int value) {
		if (connexion == null)
			return;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "UPDATE `facebook_users` SET `hasConnectedAtLeastOnce` = '" + value + "' WHERE `id` = '" + id + "'";
			stm.executeUpdate(SQL);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	/*********************************************************************************************/
	/******************************** Plugins info stockage **************************************/
	/*********************************************************************************************/

	public static void stockInfo(String table, String key, String value) {
		if (connexion == null || table == null || key == null || value == null)
			return;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "REPLACE INTO `" + table + "`(`Key`, `Value`) VALUES ('" + key + "','" + value + "')";
			stm.executeUpdate(SQL);
			return;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}

	public static String getInfo(String table, String key) {
		if (connexion == null || table == null || key == null)
			return "ko";
		try {
			Statement stm = connexion.createStatement();
			String SQL = "SELECT `" + table + "`.`Value` FROM `" + table + "` WHERE `" + table + "`.`Key` = '" + key + "'";
			ResultSet res = stm.executeQuery(SQL);
			if (res.next())
				return (res.getString("Value"));
		} catch(Exception e) {
			return "ko";
		}
		return "ko";
	}

	public static boolean valueExist(String table, String value) {
		if (connexion == null || table == null || value == null)
			return false;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "SELECT * FROM `" + table + "` WHERE `Value` = '" + value + "'";
			ResultSet res = stm.executeQuery(SQL);
			if (res.next())
				return true;
		} catch(Exception e) {
			return false;
		}
		return false;
	}

	public static int howManyInfoType(String table, String prefixKey) {
		if (connexion == null || table == null || prefixKey == null)
			return 0;
		try {
			int nElem = 0;
			Statement stm = connexion.createStatement();
			String SQL = "SELECT * FROM `" + table + "`";
			ResultSet res = stm.executeQuery(SQL);
			while (res.next()) {
				if (res.getString(1).startsWith(prefixKey))
					nElem++;
			}
			return nElem;
		} catch(Exception e) {
			return 0;
		}
	}

	public static boolean deleteInfo(String table, String value) {
		if (connexion == null || table == null || value == null)
			return false;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "DELETE FROM `" + table + "` WHERE `Value` = '" + value + "'";
			stm.executeUpdate(SQL);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	public static boolean CandidateToEpitech(String id) {
		/* Set the candidature boolean of the caller user to 1, means that he clicked on the comptuter */
		if (connexion == null || id == null)
			return false;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "UPDATE `facebook_users` SET `Candidature` = 1 WHERE `id` = '" + id + "'";
			stm.executeUpdate(SQL);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean hasAlreadyCandidate(String id) {
		/* Return true if the player has already candidate or false if not */
		if (connexion == null || id == null)
			return false;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "SELECT `Candidature` FROM `facebook_users` WHERE `id` = '" + id + "'";
			ResultSet res = stm.executeQuery(SQL);
			if (res.next() && res.getInt("Candidature") == 1)
				return true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Deprecated
	public static boolean validateTimeCondition(String username) {
		/* Set the value of the time condition (condition 1) to true */
		if (connexion == null || username == null)
			return false;
		try {
			String id = getIdByName(username);
			if (id == null)
				return false;
			Statement stm = connexion.createStatement();
			stm.executeUpdate("UPDATE `facebook_users` SET `Condition1` = 1 WHERE `id` = '" + id + "'");
			return true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Deprecated
	public static boolean validateBotsCondition(String username) {
		/* Set the value of the bots condition (condition 2) to true */
		if (connexion == null || username == null)
			return false;
		try {
			String id = getIdByName(username);
			if (id == null)
				return false;
			Statement stm = connexion.createStatement();
			stm.executeUpdate("UPDATE `facebook_users` SET `Condition2` = 1 WHERE `id` = '" + id + "'");
			return true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Deprecated
	public static boolean validateLibraryCondition(String username) {
		/* Set the value of the library condition (condition 3) to true */
		if (connexion == null || username == null)
			return false;
		try {
			String id = getIdByName(username);
			if (id == null)
				return false;
			Statement stm = connexion.createStatement();
			stm.executeUpdate("UPDATE `facebook_users` SET `Condition3` = 1 WHERE `id` = '" + id + "'");
			return true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean validateCondition(int condition, String username) {
		/* Set the value of the condition n°'condition' to true */
		if (connexion == null || username == null)
			return false;
		try {
			String id = getIdByName(username);
			if (id == null) {
				return false;
			}
			Statement stm = connexion.createStatement();
			String conditionField = "";
			if (condition == 1)
				conditionField = "Condition1";
			else if (condition == 2)
				conditionField = "Condition2";
			else if (condition == 3)
				conditionField = "Condition3";
			else if (condition == 4)
				conditionField = "Condition4";
			else if (condition == 5)
				conditionField = "Condition5";
			stm.executeUpdate("UPDATE `facebook_users` SET `" + conditionField + "` = 1 WHERE `id` = '" + id + "'");
			return true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean validateAllConditions(String username) {
		/* Set the value of all conditions (condition 1, 2, 3, 4 and 5) to true */
		if (connexion == null || username == null)
			return false;
		try {
			String id = getIdByName(username);
			if (id == null)
				return false;
			Statement stm = connexion.createStatement();
			stm.executeUpdate("UPDATE `facebook_users` SET `Condition1` = 1, `Condition2` = 1, `Condition3` = 1, `Condition4` = 1, `Condition5` = 1 WHERE `id` = '" + id + "'");
			return true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getIdByName(String username) {
		/* Get id by the player name */
		if (connexion == null || username == null)
			return null;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "SELECT `last_id` FROM `vose_users` WHERE `pseudo` = '" + username + "'";
			ResultSet res = stm.executeQuery(SQL);
			if (res.next())
				return res.getString("last_id");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void incrementStat(String field) {
		/* Increment field in stat table */
		if (connexion == null || field == null)
			return;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "UPDATE `stats` SET `" + field + "` = `" + field + "` + 1";
			stm.executeUpdate(SQL);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendPlayedTime(String id, int time_session) {
		/* Add the time_session to the time row in facebook_users */
		if (connexion == null || id == null)
			return;
		try {
			int time = time_session / 60;
			Statement stm = connexion.createStatement();
			String SQL = "UPDATE `facebook_users` SET `time` = `time` + '" + time + "' WHERE `id` = '" + id + "'";			stm.executeUpdate(SQL);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean saveCandidateComputer(String value) {
		/* Save the candidate computer in DB */
		if (connexion == null || value == null)
			return false;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "REPLACE INTO `epiplug_computer` SET `Location` = '" + value + "'";
			stm.executeUpdate(SQL);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String IsComputerSaved(Location location) {
		/* Check if the candidate computer is saved or not */
		if (connexion == null || location == null)
			return "ko";
		try {
			Statement stm = connexion.createStatement();
			String SQL = "SELECT `Location` FROM `epiplug_computer`";
			ResultSet res = stm.executeQuery(SQL);
			if (res.next()) {
				String[] splitted = res.getString("Location").split("-");
				if (splitted.length != 4)
					return "malformed_res";
				double x = Double.parseDouble(splitted[1]), y = Double.parseDouble(splitted[2]), z = Double.parseDouble(splitted[3]);
				if (x == location.getX() && y == location.getY() && z == location.getZ()) {
					return "ok";
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return "ko";
		}
		return "ko";
	}

	public static boolean saveLibrary(String value, String url) {
		/* Save a library block in DB */
		if (connexion == null || value == null || url == null)
			return false;
		try {
			Statement stm = connexion.createStatement();
			String SQL = "REPLACE INTO `epiplug_library` (`Location`, `URL`) VALUES ('" + value + "', '" + url + "')";
			stm.executeUpdate(SQL);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Map<String, String> getSavedLibrary() {
		if (connexion == null)
			return null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			Statement stm = connexion.createStatement();
			String SQL = "SELECT `Location`, `URL` FROM `epiplug_library`";
			ResultSet res = stm.executeQuery(SQL);
			while (res.next()) {
				map.put(res.getString("Location"), res.getString("URL"));
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}
}