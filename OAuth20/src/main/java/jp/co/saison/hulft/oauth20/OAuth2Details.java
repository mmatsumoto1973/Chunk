package jp.co.saison.hulft.oauth20;
public class OAuth2Details {

	private String scope;
	private String state;
	private String grantType;
	private String clientId;
	private String clientSecret;
	private String accessToken;
	private String refreshToken;
	private String approvalPromptKey;
	private String approvalPromptValue;
	private String accessTypeKey;
	private String accessTypeValue;
	private String redirectURI;
	private String username;
	private String password;
	private String authenticationServerUrl;
	private String tokenEndpointUrl;
	private String resourceServerUrl;
	private String requestServerUrl;
	private boolean isAccessTokenRequest;

	// proxy setting
	private String proxyHost;
	private String proxyPort;
	private String proxyUserName;
	private String proxyUserPassword;


	public String getTokenEndpointUrl() {
		return tokenEndpointUrl;
	}
	public void setTokenEndpointUrl(String tokenEndpointUrl) {
		this.tokenEndpointUrl = tokenEndpointUrl;
	}
	public String getRedirectURI() {
		return redirectURI;
	}
	public void setRedirectURI(String redirectURI) {
		this.redirectURI = redirectURI;
	}
	public String getAccessTypeKey() {
		return accessTypeKey;
	}
	public void setAccessTypeKey(String accessTypeKey) {
		this.accessTypeKey = accessTypeKey;
	}
	public String getAccessTypeValue() {
		return accessTypeValue;
	}
	public void setAccessTypeValue(String accessTypeValue) {
		this.accessTypeValue = accessTypeValue;
	}
	public String getApprovalPromptKey() {
		return approvalPromptKey;
	}
	public void setApprovalPromptKey(String approvalPromptKey) {
		this.approvalPromptKey = approvalPromptKey;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getApprovalPromptValue() {
		return approvalPromptValue;
	}
	public void setApprovalPromptValue(String approvalPromptValue) {
		this.approvalPromptValue = approvalPromptValue;
	}

	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getGrantType() {
		return grantType;
	}
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getAuthenticationServerUrl() {
		return authenticationServerUrl;
	}
	public void setAuthenticationServerUrl(String authenticationServerUrl) {
		this.authenticationServerUrl = authenticationServerUrl;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isAccessTokenRequest() {
		return isAccessTokenRequest;
	}
	public void setAccessTokenRequest(boolean isAccessTokenRequest) {
		this.isAccessTokenRequest = isAccessTokenRequest;
	}
	public String getResourceServerUrl() {
		return resourceServerUrl;
	}
	public void setResourceServerUrl(String resourceServerUrl) {
		this.resourceServerUrl = resourceServerUrl;
	}
	public String getRequestServerUrl() {
		return requestServerUrl;
	}
	public void setRequestServerUrl(String requestServerUrl) {
		this.requestServerUrl = requestServerUrl;
	}

	/**
	 * @return the proxyHost
	 */
	public String getProxyHost() {
		return proxyHost;
	}

	/**
	 * @param proxyHost the proxyHost to set
	 */
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	/**
	 * @return the proxyPort
	 */
	public String getProxyPort() {
		return proxyPort;
	}

	/**
	 * @param proxyPort the proxyPort to set
	 */
	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}

	/**
	 * @return the proxyUserName
	 */
	public String getProxyUserName() {
		return proxyUserName;
	}

	/**
	 * @param proxyUsername the proxyUserName to set
	 */
	public void setProxyUserName(String proxyUsername) {
		this.proxyUserName = proxyUsername;
	}

	/**
	 * @return the proxyUserPassword
	 */
	public String getProxyUserPassword() {
		return proxyUserPassword;
	}

	/**
	 * @param proxyPassword the proxyUserPassword to set
	 */
	public void setProxyUserPassword(String proxyPassword) {
		this.proxyUserPassword = proxyPassword;
	}
}
