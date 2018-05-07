package external;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;


public class ExactWebAPI {
	private static final String API_HOST = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/extract";
	//private static final String SEARCH_PATH = "forceExtraction=false&";
	private static final String INPUT_TERM = "url=";  // no restriction
	private static final String API_KEY = "TSET";

	/**
	 * Creates and sends a request to the TicketMaster API by term and location.
	 */
	public JSONArray search(String inputUrl) {
		String url =  API_HOST ;
		//String url = "https://" + API_HOST + SEARCH_PATH;
	
		//term = urlEncodeHelper(term);
		String query = String.format("forceExtraction=false&url=%s", inputUrl);
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url + "?" + query).openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("X-Mashape-Key", "RO3y3ThkDemshZuwFzrPPrfT8mXVp1E94FFjsnUy7WHBDccXBn");
			System.out.println("--------------------------- "+url + "?" + query);
			int responseCode = connection.getResponseCode();
			
			System.out.println("\nSending 'GET' request to URL : " + url + "?" + query);
			System.out.println("Response Code : " + responseCode);
 
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// Extract events array only.
			JSONObject responseJson = new JSONObject(response.toString());
			//JSONObject embedded = (JSONObject) responseJson.get("_embedded");
			//JSONArray events = (JSONArray) embedded.get("extendedIngredients");
			System.out.println("========================");
			System.out.println(responseJson);
			JSONArray ingredients = (JSONArray) responseJson.get("extendedIngredients");
			return ingredients;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
 
	private String urlEncodeHelper(String term) {
		try {
			term = java.net.URLEncoder.encode(term, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return term;
	}
 
	private void queryAPI(String inputUrl) {
		JSONArray items = search(inputUrl);
		System.out.println("-------------" + items.length());
		try {
			for (int i = 0; i < items.length(); i++) {
			    JSONObject item = items.getJSONObject(i);
				System.out.println(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	/**
	 * Main entry for sample TicketMaster API requests.
	 */
	public static void main(String[] args) {
		ExactWebAPI tmApi = new ExactWebAPI();
		tmApi.queryAPI("http://www.melskitchencafe.com/the-best-fudgy-brownies/");
	}
}
