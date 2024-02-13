import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FTPFileUploader3 {

    public static void main(String[] args) {
        String localFilePath = "src/files/photo_files/passport.jpg";
        String remoteDirectory = "/uploads/passports/";
        String server = "ftp.reavemsglobal.com";
        int port = 21; // Default FTP port

        String username = "reavems1";
        String password = "8ai7g:KY[h82OX";

        try {
            uploadFileToFTP(localFilePath, remoteDirectory, server, port, username, password);
            System.out.println("File uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void uploadFileToFTP(String localFilePath, String remoteDirectory, String server, int port,
                                        String username, String password) throws IOException {
        FTPClient ftpClient = new FTPClient();

        try {
            // Set passive mode
            //ftpClient.enterLocalPassiveMode();
            ftpClient.enterLocalActiveMode();

            ftpClient.connect(server, port);
            ftpClient.login(username, password);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            File localFile = new File(localFilePath);
            FileInputStream inputStream = new FileInputStream(localFile);

            // Change working directory on the FTP server
            if (!ftpClient.changeWorkingDirectory(remoteDirectory)) {
                // Directory does not exist, create it
                ftpClient.makeDirectory(remoteDirectory);
                ftpClient.changeWorkingDirectory(remoteDirectory);
            }

            // Upload the file
            boolean uploaded = ftpClient.storeFile(localFile.getName(), inputStream);
            inputStream.close();

            if (uploaded) {
                System.out.println("File uploaded successfully.");
            } else {
                System.err.println("File upload failed. Reply code: " + ftpClient.getReplyCode());
            }
        } finally {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        }
    }
}