package in.abhishek.LibraryModule;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class UtilFunctions {

    public static String encrypt(String password){

        StringBuilder passwordBuilder = new StringBuilder();
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] bt = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));

            for(byte b : bt)
                passwordBuilder.append(String.format("%02x", b));
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return passwordBuilder.toString();
    }

}
