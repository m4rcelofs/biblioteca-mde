package erep.mde.erpk;

import static com.google.appengine.api.urlfetch.FetchOptions.Builder.withDeadline;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class Fetcher {

	private URLFetchService		fetchService	= URLFetchServiceFactory
														.getURLFetchService();
	private JSONParser			parser			= new JSONParser();

	private static final Logger	log				= Logger.getLogger(Fetcher.class
														.getName());

	public JSONObject fetchUser(String id) throws IOException, ParseException {
		String fetchResponse = fetchUrl(ErpkApiURL.CITIZEN.getUrl(id));
		JSONObject jsonObj = (JSONObject) parser.parse(fetchResponse);
		return jsonObj;
	}

	public JSONArray searchUser(String name) throws IOException, ParseException {
		String fetchString = fetchUrl(ErpkApiURL.CITIZEN_SEARCH.getUrl(name));
		JSONArray jsonArray = (JSONArray) parser.parse(fetchString);
		return jsonArray;
	}

	public JSONObject fetchArticle(String id) throws IOException,
			ParseException {
		String fetchString = fetchUrl(FakeApiURL.ARTICLE.getUrl(id));
		log.warning(fetchString);
		JSONObject jsonArray = (JSONObject) parser.parse(fetchString);
		return jsonArray;
	}

	private String fetchUrl(URL url) throws IOException {

		HTTPResponse response = fetchService.fetch(new HTTPRequest(url,
				HTTPMethod.GET, withDeadline(60)));

		if (response.getResponseCode() != 200)
			throw new IllegalStateException("Invalid HTTP Response Code from "
					+ url.toString() + " : " + response.getResponseCode());

		return new String(response.getContent(), "UTF-8");
	}

}
