package jp.co.saison.hulft.oauth20.google;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jp.co.saison.hulft.oauth20.OAuth2Details;
import jp.co.saison.hulft.oauth20.OAuthConstants;
import jp.co.saison.hulft.oauth20.OAuthUtils;

/**
 * Authorization Code による認可を REST API によって行うクライアントクラス
 */
public class OAuth2Client_AuthCode_RestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public OAuth2Client_AuthCode_RestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String caller = request.getParameter(OAuthConstants.CALLER);
		String code = request.getParameter(OAuthConstants.CODE);

		//Load the properties file
		Properties config = OAuthUtils.getClientConfigProps(OAuthConstants.CONFIG_FILE_PATH);

		//Generate the OAuthDetails bean from the config properties file
		OAuth2Details oauthDetails = OAuthUtils.createOAuthDetails(config);

		//Validate Input
		List<String> invalidProps = OAuthUtils.validateInput(oauthDetails);
		if(invalidProps!=null && invalidProps.size() == 0){
			//Validation successful

			if(OAuthUtils.isValid(caller)){
				//Request was sent from web application.
				//Check type of request
				if(caller.equalsIgnoreCase(OAuthConstants.TOKEN)){
					//Request for Access token
					oauthDetails.setAccessTokenRequest(true);
					String location = OAuthUtils.getAuthorizationCode(oauthDetails);

					//Send redirect to location returned by endpoint
					response.sendRedirect(location);
					return;
				}
				else{
					//Request for accessing protected resource
					if(!OAuthUtils.isValid(oauthDetails.getResourceServerUrl())){
						invalidProps.add(OAuthConstants.RESOURCE_SERVER_URL);

					}

					if(!OAuthUtils.isValid(oauthDetails.getAccessToken())){
						if(!OAuthUtils.isValid(oauthDetails.getRefreshToken())){
							invalidProps.add(OAuthConstants.REFRESH_TOKEN);
						}


					}


					if(invalidProps.size() > 0){
						sendError(response, invalidProps);
						return;
					}

					Map<String,String> map = OAuthUtils.getProtectedResource(oauthDetails);
					response.getWriter().println(new Gson().toJson(map));
					return;
				}
			}
			else if(OAuthUtils.isValid(code)){
				//Callback from endpoint with code.
				Map<String,String> map = OAuthUtils.getAccessToken(oauthDetails, code);
//				response.getWriter().println(new Gson().toJson(map));

				String accessToken = map.get(OAuthConstants.ACCESS_TOKEN);
				if (OAuthUtils.isValid(accessToken)) {
					// update the access token
					System.out.println("access token: " + accessToken);
					oauthDetails.setAccessToken(accessToken);
				}
//				Map<String,String> map2 = OAuthUtils.getJsonParser(oauthDetails);
				JsonParser parser = OAuthUtils.getJsonParser(oauthDetails);
				outputFileList(response, parser);
//				response.getWriter().println(new Gson().toJson(map2));
				return;
			}
			else{
				//Invalid request/error response
				String queryString = request.getQueryString();
				String error = "Invalid request";
				if(OAuthUtils.isValid(queryString)){
					//Endpoint returned an error
					error = queryString;
				}
				response.getWriter().println(error);
				return;

			}
		}
		else{
			//Input is not valid. Send error
			sendError(response, invalidProps);
			return;

		}




	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private void sendError(HttpServletResponse response, List<String> invalidProps) throws IOException{
		PrintWriter pw = response.getWriter();

		pw.println("<HTML>");
		pw.println("<HEAD>");
		pw.println("<H1>");
		pw.println("Invalid Input in config file Oauth2Client.config");
		pw.println("</H1>");
		pw.println("</HEAD>");
		pw.println("<BODY>");
		pw.println("</BODY>");
		pw.println("<P>");
		pw.println("Please provide valid values for the following properties");
		pw.println(new Gson().toJson(invalidProps));
		pw.println("</P>");
		pw.println("</HTML>");

		pw.flush();
		pw.close();

		return;
	}

	private void outputFileList(HttpServletResponse response, JsonParser parser) throws IOException{
		PrintWriter pw = response.getWriter();


		pw.println("<HTML lang=\"ja\">");
		pw.println("<HEAD>");
		pw.println("<meta http-equiv=\"Content-Type\" Content=\"text/html;charset=UTF-8\">");
		pw.println("<title>Google Drive API Sample</title>");
		pw.println("<link rel=\"stylesheet\" href=\"./stylesheets/base.css\" type=\"text/css\">");
		pw.println("</HEAD>");

		pw.println("<BODY>");

		pw.println("<div id=\"wrapper\">");

		pw.println("<div id=\"header\" height=\"10%\">");
		pw.println("<p>Google Drive API Sample</p>");
		pw.println("</div>");

		pw.println("<div id=\"contents\" height=\"80%\">");
		pw.println("<p>file list.</p>");

        Event event;
		if ((event = parser.next()) == Event.START_OBJECT) {
			while ((event = parser.next()) != Event.END_OBJECT) {
				if (event == Event.START_ARRAY) {
					while ((event = parser.next()) != Event.END_ARRAY) {
						if (event == Event.START_OBJECT) {
							pw.println("<div class=\"pic\">");
							while ((event = parser.next()) != Event.END_OBJECT) {
								if (event == Event.KEY_NAME) {
									System.out.print(parser.getString() + " : ");
									if (parser.getString().equals("webContentLink")) {
										event =parser.next();
										System.out.println(parser.getString());
										StringBuilder builder = new StringBuilder();
										builder.append("<a href=\"");
										builder.append(parser.getString());
										builder.append("\">");
										pw.println(builder.toString());
									} else if (parser.getString().equals("thumbnailLink")) {
										event =parser.next();
										System.out.println(parser.getString());
										StringBuilder builder = new StringBuilder();
										builder.append("<img src=\"");
										builder.append(parser.getString());
										builder.append("\">");
										pw.println(builder.toString());
									} else {
										event =parser.next();
										if (event == Event.VALUE_STRING) {
											System.out.println(parser.getString());
										} else if (event == Event.START_OBJECT) {
											while ((event = parser.next()) != Event.END_OBJECT) {
												if (event == Event.KEY_NAME) {
													System.out.print(parser.getString() + " : ");
													event =parser.next();
													if (event == Event.VALUE_STRING) {
														System.out.println(parser.getString());
													}
												}
											}
										} else if (event == Event.START_ARRAY) {
											while ((event = parser.next()) != Event.END_ARRAY) {
												if (event == Event.START_OBJECT) {
													if (event == Event.KEY_NAME) {
														System.out.print(parser.getString() + " : ");
														event =parser.next();
														if (event == Event.VALUE_STRING) {
															System.out.println(parser.getString());
														}
													}
												}
											}
										} else if (event == Event.START_OBJECT) {
											while ((event = parser.next()) != Event.END_OBJECT) {
												if (event == Event.KEY_NAME) {
													System.out.print(parser.getString() + " : ");
													event =parser.next();
													if (event == Event.VALUE_STRING) {
														System.out.println(parser.getString());
													}
												}
											}
										}
									}
								}
							}
							pw.println("</div>");
						}
					}
				}

		    }
		}

		pw.println("</div>");

		pw.println("<div id=\"footer\" height=\"10%\">");
		pw.println("<p></p>");
		pw.println("</div>");

		pw.println("</div>");

		pw.println("</BODY>");
		pw.println("</HTML>");

		pw.flush();
		pw.close();

		return;
	}
}
