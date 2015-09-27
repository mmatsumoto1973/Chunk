
package jp.co.saison.hulft.oauth20.google;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jp.co.saison.hulft.oauth20.OAuth2Details;
import jp.co.saison.hulft.oauth20.OAuthSystem;
import jp.co.saison.hulft.oauth20.OAuthUtils;

/**
 * Authorization Code による認可後に呼び出されるコールバッククラス
 */
public class OAuth2Client_AuthCode_GoogleCLWebCallbackServlet extends AbstractAuthorizationCodeCallbackServlet {

	@Inject
	OAuthSystem oauthSystem;

	@Override
	protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential)
      throws ServletException, IOException {

		// 新規認証APIクライアントを作成
		Drive service = new Drive.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential).build();

		// ファイル一覧取得処理
		List<File> fileList = new ArrayList<File>();
		Files.List request = service.files().list();
		do {
			try {
				 FileList files = request.execute();
				 fileList.addAll(files.getItems());
				 request.setPageToken(files.getNextPageToken());
			} catch (IOException e) {
				System.out.println("An error occurred: " + e);
				request.setPageToken(null);
			}
		} while (request.getPageToken() != null && request.getPageToken().length() > 0);

		// ファイル一覧出力処理
		outputFileList(resp, GoogleDriveClientLibHelper.getFileList(service));
  	}

	@Override
	protected void onError(
		HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse)
		throws ServletException, IOException {
		// handle error
  	}

	@Override
	protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
		GenericUrl url = new GenericUrl(req.getRequestURL().toString());
    	url.setRawPath("/OAuth20/authcode_googlecl_webcb");
		return url.build();
  	}

	@Override
	protected AuthorizationCodeFlow initializeFlow() throws IOException {

		OAuth2Details oauthDetails = oauthSystem.getOauthDetails();
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

	static void outputFileList(HttpServletResponse response, List<File> fileList) throws IOException{
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

		for (File file : fileList) {

			pw.println("<div class=\"pic\">");

			StringBuilder builder = new StringBuilder();

			// コンテンツリンク
			builder.append("<a href=\"");
			builder.append(file.getWebContentLink());
			builder.append("\">");
			pw.println(builder.toString());

			// イメージ
			builder = new StringBuilder();
			builder.append("<img src=\"");
			builder.append(file.getThumbnailLink());
			builder.append("\">");
			pw.println(builder.toString());

			pw.println("</div>");
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

	}
}