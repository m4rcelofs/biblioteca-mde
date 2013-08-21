package erep.mde.erpk;

import java.net.MalformedURLException;
import java.net.URL;

public enum FakeApiURL
{

	ARTICLE("{host}/article?id={id}"),

	;

	private static final String	apiHost	= "http://dev.erpkapi.appspot.com";

	private final String		url;

	private FakeApiURL(String url) {
		this.url = url;
	}

	public URL getUrl(String id) {
		try {
			return new URL(url.replace("{host}", apiHost).replace("{id}", id));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
