package utils;

import static org.imgscalr.Scalr.OP_ANTIALIAS;
import static org.imgscalr.Scalr.OP_BRIGHTER;
import static org.imgscalr.Scalr.resize;

import java.awt.image.BufferedImage;
import java.io.File;

import org.imgscalr.Scalr.Method;

public class FileUtilities {

	/**
	 * This corresponds to the delimiter of the file path for the current
	 * operating system.
	 */
	public static String slash = (System.getProperty("os.name")
		.indexOf("Windows") != -1) ? "\\" : "/";
    
    public static File readFile(String fullPath) {
    	return new File(fullPath);
    }
    
	public static BufferedImage createThumbnail(BufferedImage img) {
		// Create quickly, then smooth and brighten it.
		img = resize(img, Method.SPEED, 125, OP_ANTIALIAS, OP_BRIGHTER);

		// Let's add a little border before we return result.
		return img;
	}
	
	public static BufferedImage createLarge(BufferedImage img) {
		// Create quickly, then smooth and brighten it.
		img = resize(img, Method.SPEED, 1024, OP_ANTIALIAS, OP_BRIGHTER);

		// Let's add a little border before we return result.
		return img;
	}
	
	public static BufferedImage createMedium(BufferedImage img) {
		// Create quickly, then smooth and brighten it.
		img = resize(img, Method.SPEED, 500, OP_ANTIALIAS, OP_BRIGHTER);

		// Let's add a little border before we return result.
		return img;
	}	

	public static BufferedImage createSmall(BufferedImage img) {
		// Create quickly, then smooth and brighten it.
		img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_BRIGHTER);

		// Let's add a little border before we return result.
		return img;
	}	

    
    public static String generateHashCode(String fullPath, Long userId, String contentType) {
    	// TODO implement hash code for files generation
    	return ""; 
    }
    
    // TODO implement file supporting utilities
}
