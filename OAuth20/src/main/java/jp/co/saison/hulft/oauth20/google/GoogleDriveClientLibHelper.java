package jp.co.saison.hulft.oauth20.google;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Google Drive Client Library のヘルパークラス
 *
 */
public class GoogleDriveClientLibHelper {
	
	static public List<File> getFileList(Drive service) {

		try {
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
			
			return fileList;
		} catch (IOException ex) {
			Logger.getLogger(OAuth2Client_ClientCredentials_GoogleCLSAServlet.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}

}
