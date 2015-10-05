package jp.co.saison.hulft.oauth20.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jp.co.saison.hulft.oauth20.OAuth2Details;
import jp.co.saison.hulft.oauth20.OAuthSystem;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Client Credencialsによる認可を Google Client Library によって行うクライアントクラス
 */
public class OAuth2Client_ClientCredentials_GoogleCLSAServlet extends HttpServlet {

	@Inject
	OAuthSystem oauthSystem;

	@Override
	public void doPost(HttpServletRequest req, 
						 HttpServletResponse resp) 
						 throws ServletException, IOException {

		Drive service = getDriveService();

		// ファイルアップロード処理
		String path = getServletContext().getRealPath("WEB-INF");
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		factory.setSizeThreshold(1024);
		upload.setSizeMax(-1);
		upload.setHeaderEncoding("Windows-31J");
		try {
			OAuth2Details oauthDetails = oauthSystem.getOauthDetails();

			List list = upload.parseRequest(req);
			
			Iterator iterator = list.iterator();
			while(iterator.hasNext()){
				FileItem fItem = (FileItem)iterator.next();
				
				if(!(fItem.isFormField())){
					String fileName = fItem.getName();
					if((fileName != null) && (!fileName.equals(""))){
						fileName = (new java.io.File(fileName)).getName();
						java.io.File fileContent = new java.io.File(path + "/" + fileName);
						fItem.write(fileContent);
						
						// ファイルを挿入
						File body = new File();
						body.setTitle(fileName);
						body.setDescription("A test document");
						body.setMimeType(fItem.getContentType());
						
						FileContent mediaContent = new FileContent(fItem.getContentType(), fileContent);
						try {
							
							File file = service.files().insert(body, mediaContent).execute();
							System.out.println("File ID: " + file.getId());
							
							// アップロードファイルを自分のアカウントに共有
							Permission newPermission = new Permission();
							newPermission.setValue(oauthDetails.getGoogleMyAccountEmail());
							newPermission.setType("user");
							newPermission.setRole("reader");
							service.permissions().insert(file.getId(), newPermission).execute();
							
							
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					}
				}
			}
			
		} catch (FileUploadException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		// ファイル一覧出力処理
		outputFileList(resp, GoogleDriveClientLibHelper.getFileList(service));
	}



	@Override
	public void doGet(HttpServletRequest req, 
						 HttpServletResponse resp) 
						 throws ServletException, IOException {

		Drive service = getDriveService();

		// ファイル一覧出力処理
		outputFileList(resp, GoogleDriveClientLibHelper.getFileList(service));
	}
		  
	private Drive getDriveService() {

		try {
			OAuth2Details oauthDetails = oauthSystem.getOauthDetails();

			// 新規認証APIクライアントを作成
			HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
			
			// プロパティファイルのパスを取得する
			ServletContext context = this.getServletContext();
			String keyfilePath = context.getRealPath(oauthDetails.getGoogleServiceKeyfilePath());
			
			// Build service account credential.
			GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
				.setJsonFactory(jsonFactory)
				.setServiceAccountId(oauthDetails.getGoogleServiceAccountEmail())
				.setServiceAccountScopes(Collections.singleton(DriveScopes.DRIVE))
				.setServiceAccountPrivateKeyFromP12File(new java.io.File(keyfilePath))
				.build();
			
			Drive service = new Drive.Builder(httpTransport, jsonFactory, null)
				.setApplicationName(oauthDetails.getGoogleServiceAppName())
				.setHttpRequestInitializer(credential)
				.build();
			
			return service;
		} catch (GeneralSecurityException ex) {
			Logger.getLogger(OAuth2Client_ClientCredentials_GoogleCLSAServlet.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(OAuth2Client_ClientCredentials_GoogleCLSAServlet.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
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
		pw.println("<form method=\"POST\" enctype=\"multipart/form-data\" action=\"clientcredencials_googlecl_sa\">");
		pw.println("<input type=\"file\" name=\"filefd\" />");
		pw.println("<input type=\"submit\" value=\"upload\" />");
		pw.println("</form>");
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