package jp.co.saison.hulft.oauth20;

import java.io.FileNotFoundException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

/**
 * システム設定を行うクラス
 */
@ApplicationScoped
public class OAuthSystem {

	private OAuth2Details oauthDetails;

	@PostConstruct
	private void init() {
		try {
			//Load the properties file
			Properties config = OAuthUtils.getClientConfigProps(OAuthConstants.CONFIG_FILE_PATH);
			//Generate the OAuthDetails bean from the config properties file
			oauthDetails = OAuthUtils.createOAuthDetails(config);

			// Proxy Setting
			String proxyHost = getOauthDetails().getProxyHost();
			if (proxyHost != null && !proxyHost.isEmpty()) {
				System.setProperty("proxySet", "true");
				System.setProperty("proxyHost", getOauthDetails().getProxyHost());
				System.setProperty("proxyPort", getOauthDetails().getProxyPort());

				Authenticator.setDefault(new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(getOauthDetails().getProxyUserName(), getOauthDetails().getProxyUserPassword().toCharArray());
					}
				});

			}

		} catch (FileNotFoundException ex) {
			Logger.getLogger(OAuthSystem.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}

	/**
	 * @return the oauthDetails
	 */
	public OAuth2Details getOauthDetails() {
		return oauthDetails;
	}

}
