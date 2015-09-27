package jp.co.saison.hulft.oauth20.google;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jp.co.saison.hulft.oauth20.OAuth2Details;
import jp.co.saison.hulft.oauth20.OAuthConstants;
import jp.co.saison.hulft.oauth20.OAuthUtils;

/**
 * Authorization Code による認可を Google Client Library によって行うクライアントクラス
 */
public class OAuth2Client_AuthCode_GoogleCLWebServlet extends AbstractAuthorizationCodeServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

  }

  @Override
  protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
    GenericUrl url = new GenericUrl(req.getRequestURL().toString());
    url.setRawPath("/OAuth20/authcode_googlecl_webcb");
    return url.build();
  }

  @Override
  protected AuthorizationCodeFlow initializeFlow() throws IOException {
	//Load the properties file
	Properties config = OAuthUtils.getClientConfigProps(OAuthConstants.CONFIG_FILE_PATH);
	//Generate the OAuthDetails bean from the config properties file
	OAuth2Details oauthDetails = OAuthUtils.createOAuthDetails(config);
	//Validate Input
	List<String> invalidProps = OAuthUtils.validateInput(oauthDetails);
	if(invalidProps!=null && invalidProps.size() == 0){
		//Validation successful
		return new GoogleAuthorizationCodeFlow.Builder(
			new NetHttpTransport(), JacksonFactory.getDefaultInstance(),
			oauthDetails.getClientId(), oauthDetails.getClientSecret(),
			Arrays.asList(DriveScopes.DRIVE)).setDataStoreFactory(new MemoryDataStoreFactory())
			.setAccessType("offline").build();
	}
	return null;
  }

  @Override
  protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
	  return req.getSession(true).getId();
  }

}