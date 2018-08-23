package net.epitech.other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HttpRequest {

	private final static String USER_AGENT = "Mozilla/5.0";

	public static String sendPost(String adress, LinkedHashMap<String, String> parameters) throws IOException {
		String result = "";
		BufferedReader reader = null;
		try {
			//Encodage des paramètres de la requête
			String data = "?";
			Set<String> cles = parameters.keySet();
			Iterator<String> it = cles.iterator();
			while (it.hasNext()) {
				String cle = it.next();
				String valeur = parameters.get(cle);
				data += URLEncoder.encode(cle, "UTF-8") + "=" + URLEncoder.encode(valeur, "UTF-8");
				//data += cle + "=" + valeur;
				if (it.hasNext())
					data += "&";
			}
			adress += data;
			//Création de la connection
			URL url = new URL(adress);
			System.out.println("[CANDIDATURE] " + adress);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			conn.setRequestProperty("Accept-Language", "fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Host", "candidature.epitech.net");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
			
			conn.setDoOutput(true);

			/*			
			//Envoi de la requête
			writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(data);
			writer.flush();
			 */

			//Lecture de la réponse
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String ligne = "";
			while ((ligne = reader.readLine()) != null) {
				result += ligne;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ("ReadError");
		} finally {
			try {
				reader.close();
			} catch(Exception e) {
			}
		}
		return result;
	}

	public static boolean sendPostCheckEpitechLogin(String format, String login, String password) throws IOException {

		//http://ws.paysdu42.fr/FORMAT/?action=ACTION&auth_login=LOGIN&auth_password=PASSWORD&login=TARGET
		String data = format + "/?action=login&auth_login=" + login + "&auth_password=" + password;
		//System.out.println("Data : " + data);

		//Création de la connection
		URL url = new URL("http://ws.paysdu42.fr/" + data);
		// Sending GET request
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);

		conn.setRequestMethod("GET");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		// On s'en fou : toujours égal a 200
		//int responseCode = conn.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		// On s'en fou : toujours égal a 200
		//response.append("Response code : " + Integer.toString(responseCode) + '\n');
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine + '\n');
		}
		in.close();

		// TODO Checker si une erreur un peu mieux que ça quand meme, parce que c'est dégeu la | Genre comme ça ? :p

		/*if (response.toString().startsWith("{\"error\":\"auth_fail\""))
			return "error";*/
		try {
			JSONParser parser = new JSONParser();
			Object obj;
			obj = parser.parse(response.toString());
			JSONObject jsonObject = (JSONObject) obj;
			if (!jsonObject.get("error").toString().equals("none")) {
				System.err.println("HttpRequest error.");
				System.err.println("Error ID: " + jsonObject.get("error").toString());
				return (false);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static String getInetIp() throws IOException {
		URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					whatismyip.openStream()));
			String ip = in.readLine();
			return ip;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}