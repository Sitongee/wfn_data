package external;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class ParseIngreAPI {
	private static final String API_HOST = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/parseIngredients";
	private static final String API_KEY = "TEST";

	/**
	 * Creates and sends a request to the TicketMaster API by term and location.
	 */
	public JSONArray search(String ingreStr, String serving, boolean includeNutrition) {
		

		String query = String.format("includeNutrition=%s", includeNutrition);
		try {		
            String url = API_HOST + "?" + query;
            //create http client
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            //add header
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            post.addHeader("X-Mashape-Key", "RO3y3ThkDemshZuwFzrPPrfT8mXVp1E94FFjsnUy7WHBDccXBn");
            post.addHeader("Accept", "application/json");
            // add field
            List urlParameters = new ArrayList();
            urlParameters.add(new BasicNameValuePair("ingredientList", ingreStr));
            urlParameters.add(new BasicNameValuePair("servings", serving));
            post.setEntity(new UrlEncodedFormEntity(urlParameters, Consts.UTF_8));
            HttpResponse response = client.execute(post);
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + post.getEntity());
            System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
			//read response
			BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String inputLine;
			StringBuilder result = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				result.append(inputLine);
			}
			in.close();

			// Extract events array only.		
			JSONArray responseJson = new JSONArray(result.toString());
			return responseJson;
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
 
	private void queryAPI(String ingreStr, String serving, boolean includeNutrition) {
		JSONArray items = search(ingreStr, serving, includeNutrition);
		
		try {
			for (int i = 0; i < items.length(); i++) {
			    JSONObject item = items.getJSONObject(i);
				System.out.println(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    private String[] parseLines(String input){
    	String result[] = input.split("\\r?\\n");
    	return result;
    }
	/**
	 * Main entry for sample TicketMaster API requests.
	 */
	public static void main(String[] args) {
		final String DEFAULT_SERVING = "1";
		ParseIngreAPI tmApi = new ParseIngreAPI();
		String[] lines =tmApi.parseLines("3 oz pork shoulder\n 2 oz orange");
		for (int i = 0; i < lines.length; i++){
		    tmApi.queryAPI(lines[i], DEFAULT_SERVING, false);
		}
	}
}
