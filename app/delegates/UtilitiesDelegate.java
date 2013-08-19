package delegates;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.joda.time.DateTime;

import akka.event.slf4j.Logger;

import play.Play;
import play.mvc.Http.MultipartFormData.FilePart;
import pojos.CityBean;
import pojos.FileBean;
import utils.FileUtilities;
import utils.PlayDozerMapper;

public class UtilitiesDelegate {
	public static UtilitiesDelegate getInstance() {
		return new UtilitiesDelegate();
	}

	public List<CityBean> getCities () {
		List<models.City> 
			modelCities = 
				models.City.all();
		List<CityBean> pojosCities = new ArrayList<CityBean>();
		for (models.City city : modelCities) {
			CityBean cityBean = PlayDozerMapper.getInstance().map(
					city, CityBean.class);
			pojosCities.add(cityBean);
		}
		return pojosCities;
	}
	
	public List<CityBean> getCitiesByCountryId(Long countryId) {
		List<models.City> modelCities = models.City.readByCountry(countryId);
		List<CityBean> pojosCities = new ArrayList<CityBean>();
		for (models.City city : modelCities) {
			CityBean cityBean = PlayDozerMapper.getInstance().map(
					city, CityBean.class);
			pojosCities.add(cityBean);
		}
		return pojosCities;
	}

	public List<CityBean> getCitiesByCountryName(String countryName) {
		List<models.City> modelCities = models.City.readByCountryName(countryName);
		List<CityBean> pojosCities = new ArrayList<CityBean>();
		for (models.City city : modelCities) {
			CityBean cityBean = PlayDozerMapper.getInstance().map(
					city, CityBean.class);
			pojosCities.add(cityBean);
		}
		return pojosCities;
	}

	public List<CityBean> getCitiesByCountryNameAndRegion(String country,
			String region) {
		List<models.City> modelCities = models.City.readByCountryNameAndRegion(country, region);
		List<CityBean> pojosCities = new ArrayList<CityBean>();
		for (models.City city : modelCities) {
			CityBean cityBean = PlayDozerMapper.getInstance().map(
					city, CityBean.class);
			pojosCities.add(cityBean);
		}
		return pojosCities;	
	}

	
	public List<CityBean> getNewCities (Long lastCityId) {
		List<models.City> modelCities = models.City.readNewById(lastCityId);
		List<CityBean> pojosCities = new ArrayList<CityBean>();
		for (models.City city : modelCities) {
			CityBean cityBean = PlayDozerMapper.getInstance().map(
					city, CityBean.class);
			pojosCities.add(cityBean);
		}
		return pojosCities;
	}
	
	public List<CityBean> getCityByName (String cityName) {
		List<models.City> modelCities = models.City.findByName(cityName);
		List<CityBean> pojosCities = new ArrayList<CityBean>();
		for (models.City city : modelCities) {
			CityBean cityBean = PlayDozerMapper.getInstance().map(
					city, CityBean.class);
			pojosCities.add(cityBean);
		}
		return pojosCities;
	}

	public CityBean getCitiesById(Long cityId) {
		models.City city = models.City.read(cityId);
		CityBean cityBean = PlayDozerMapper.getInstance().map(city, CityBean.class);
		return cityBean;
	}	

	public FileBean saveFile(FilePart file, FileBean fileBean) throws IOException {
		/*
		 * 1. Prepare file metadata before saving the file in the final destination
		 */
		String uploadDir = Play.application().configuration().getString("files.home");
		String filesBaseURL = Play.application().configuration().getString("files.baseurl");
		
		String fileName = "U_"+fileBean.getOwner().toString()+"_"+"D"+DateTime.now()+"_"+file.getFilename();
		// TODO check how to make uploadDir also cross-platform
		String fullPath = uploadDir+ FileUtilities.slash + fileName;
		String contentType = file.getContentType();
		File uploadFile = file.getFile();

		String [] parts = file.getFilename().split(".");
		String fileExtension = "";
		
		if (parts != null && parts.length > 0) {
			fileExtension = parts[parts.length-1];	
		}
		
		
		Logger.root().debug("Saving File....");
		Logger.root().debug("--> fileName=" + fileName);
		Logger.root().debug("--> contentType=" + contentType);
		Logger.root().debug("--> uploadFile=" + uploadFile);

		/*
		 * 2. Save the file in the final destination
		 */
		File localFile = new File(fullPath);
		uploadFile.renameTo(localFile);		
		Logger.root().debug("--> localFile=" + localFile);
		File thumbnailFile = null;
		
		if (contentType.contains("image"))
			thumbnailFile = scalePicture(localFile);
		
		String thumbnailURI = "";
		if (thumbnailFile != null) {
			thumbnailURI = filesBaseURL + "/" + thumbnailFile.getName();
		}
		/*
		 * 3. Generate Hashcode to use as new name
		 */
		String hashcode = UUID.nameUUIDFromBytes(fullPath.getBytes()).toString();
		/* 
		 * 4. Save File metadata in Database
		 */
		fileBean.setFilename(fileName);
		fileBean.setURI(filesBaseURL + "/" + fileName);
		fileBean.setThumbnailURI(thumbnailURI);
		fileBean.setContentType(contentType);
		fileBean.setCreationDate(DateTime.now());
		fileBean.setHashcode(hashcode);
		fileBean.setExtension(fileExtension);
		Logger.root().debug("--> creationDate=" + fileBean.getCreationDate());
		Logger.root().debug("--> hashcode=" + fileBean.getHashcode());
		
		models.File f = PlayDozerMapper.getInstance().map(fileBean, models.File.class);
		f.save();
		fileBean.setFileId(f.getFileId());
		return fileBean;
	}
	
	
	private File scalePicture(File localFile) throws IOException {
		BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		img.createGraphics().drawImage(ImageIO.read(localFile), null, null);
		BufferedImage scaled = scale(img,0.5);
		File thumbFile = new File(localFile.getAbsolutePath()+"_thumb");
		ImageIO.write(scaled, "jpg", thumbFile);
		return thumbFile;
	}

	public File getFile(String hashcode, Long userId) {
		models.File f = models.File.readByHashCodeAndUserId(hashcode, userId);
		
		String uploadDir = Play.application().configuration().getString("files.home");
		String fileName = f == null ? "" : f.getFilename();
		String fullPath = f == null ? "" : uploadDir+ FileUtilities.slash + fileName;
	
		return new File(fullPath);
	}
	
	private BufferedImage scale(BufferedImage source,double ratio) {
		  int w = (int) (source.getWidth() * ratio);
		  int h = (int) (source.getHeight() * ratio);
		  BufferedImage bi = getCompatibleImage(w, h);
		  Graphics2D g2d = bi.createGraphics();
		  double xScale = (double) w / source.getWidth();
		  double yScale = (double) h / source.getHeight();
		  AffineTransform at = AffineTransform.getScaleInstance(xScale,yScale);
		  g2d.drawRenderedImage(source, at);
		  g2d.dispose();
		  return bi;
		}

	private BufferedImage getCompatibleImage(int w, int h) {
		  GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		  GraphicsDevice gd = ge.getDefaultScreenDevice();
		  GraphicsConfiguration gc = gd.getDefaultConfiguration();
		  BufferedImage image = gc.createCompatibleImage(w, h);
		  return image;
		}
}
