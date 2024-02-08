// Import the Cloudinary library
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.Map;

public class ImageUploader2 {
    public static void main(String[] args) throws IOException {
        // Configure Cloudinary with your credentials
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "ds6x3023p",
                "api_key", "966868569932862",
                "api_secret", "D7BSrzAaG0paHzz1xKZ4wJD1NH4"
        ));

        // Specify the local image path and desired public_id
        String localImagePath = "src/icons/bn_logo.jpg";
        String publicId = "42335154789562";

// Upload the image
        Map uploadResult = cloudinary.uploader().upload(localImagePath, ObjectUtils.asMap(
                "public_id", publicId
        ));

// Get the URL of the uploaded image
        String imageUrl = (String) uploadResult.get("url");

        System.out.println("Image uploaded successfully: " + imageUrl);
    }
}
