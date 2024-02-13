import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class InternetConnection {
    //Check internet connection
    public static boolean isInternetConnected(){
        /*try{
            InetAddress address = InetAddress.getByName("www.google.com");
            return address.isReachable(6000);
        }catch (IOException e){
            return false;
        }*/
        try{
            URL url = new URL("http://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            return true;
            } catch(IOException e){
            return false;
        }
    }
}
