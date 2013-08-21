package erep.mde.erpk;

import java.net.MalformedURLException;
import java.net.URL;

public enum ErpkApiURL
{

	CITIZEN("{host}/citizen/profile/{id}.json?key={key}"), CITIZEN_SEARCH(
			"{host}/citizen/search/{id}/1.json?key={key}"),

	;

	// also erpk-api.com?
	private static final String	apiHost		= "http://api.erpk.org";
	private static final String	apiKey		= "PtXZtnza";
	private static final String	countryCode	= "BR";

	private final String		url;

	private ErpkApiURL(String url) {
		this.url = url;
	}

	public URL getUrl() {
		try {
			return new URL(url.replace("{host}", apiHost)
					.replace("{country}", countryCode).replace("{key}", apiKey));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public URL getUrl(String id) {
		try {
			String link = url.replace("{host}", apiHost)
					.replace("{country}", countryCode).replace("{key}", apiKey)
					.replace("{id}", id.replace(" ", "%20"));
			return new URL(link);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static URL getUrl(ErpkApiURL url) {
		return url.getUrl();
	}

}
