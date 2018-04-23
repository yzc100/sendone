import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.client.utils.URIUtils;

import com.kx.util.HttpClient;


public class TTS {
	public static void main(String[] args) {
		HttpClient httpClient = new HttpClient();
		//System.out.println(httpClient.sendPost("https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials&client_id=RrImKcADG1aCr4IO4eG6DjpI&client_secret=258b82a7dac381db071f41237a52e41b", ""));
		String param = "tex="+URLEncoder.encode("123")+"&lan=zh&cuid="+URLEncoder.encode("C0-38-96-10-56-3D")+"&cpt=1&tok="+URLEncoder.encode("24.f8fd1aa95fafdce3b5592d26681d2616.2592000.1462527219.282335-7966063");
				try {
					URI url = URIUtils.createURI("http", "tsn.baidu.com", 80, "/text2audio", param, null);
					System.out.println(url);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		//System.out.println(httpClient.sendPost("http://tsn.baidu.com/text2audio?","" ));
	}
}
