import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import static play.test.Helpers.*;

import models.User;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonNode;
import org.junit.*;

import play.Logger;
import play.test.*;
import static play.libs.Json.toJson;
//import play.api.libs.json.Json;
import play.libs.F.*;
import play.mvc.Result;
import pojos.FileBean;
import pojos.UserBean;

import static org.fest.assertions.Assertions.*;

public class IntegrationTest extends BaseApplicationTest {

	/**
	 * add your integration test here in this example we just check if the
	 * welcome page is being shown
	 */
	@Test
	public void test() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333");
						assertThat(browser.pageSource()).contains(
								"Reminiscens RESTful API");
					}
				});
	}

	/**
	 * Test file upload
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 */
	@Test
	public void testFileUpload() {

		running(fakeApplication(), new Runnable() {
			public void run() {
				File f = new File("test.txt");
				Writer writer = null;

				try {
					writer = new BufferedWriter(new OutputStreamWriter(
							new FileOutputStream(f), "utf-8"));
					writer.write("Something");
				} catch (IOException ex) {
					// report
				} finally {
					try {
						writer.close();
					} catch (Exception ex) {
					}
				}

				// File file =
				// VirtualFile.fromRelativePath("/test/imagetest.jpg").getRealFile();

				User user = User.read(new Long(2));
				FileBean fileBean = new FileBean();
				fileBean.setFilename("test.txt");
				fileBean.setOwner(user.getUserId());

				JsonNode jn = toJson(fileBean);

				// Map<String,String> params = new HashMap<String,String>();
				// params.put("owner", user.getUserId().toString());

				Map<String, Object> files = new HashMap<String, Object>();
				files.put("file", f);

				// Response response = POST(routes.Utilities.upload(), params,
				// files);

				Result result = route(fakeRequest(POST, "/upload")
						.withHeader("Content-Type", "multipart/form-data")
						.withHeader(
								"PLAY_SESSION",
								"618b4ad9aa13ff7bd4b785e71eb8d9bbd937d561-pa.u.exp%3A1377189637632%00pa.p.id%3Apassword%00pa.u.id%3Acdparra%40gmail.com")
						.withJsonBody(jn)
				// .withFormUrlEncodedBody(files)
				);

				Logger.debug("result is " + contentAsString(result));
				System.out.println("result is " + contentAsString(result));

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://localhost/lifeapi/secupload");

				FileBody imageFile = new FileBody(f);
				StringBody stringBody = null;
				try {
					stringBody = new StringBody(jn.toString());
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				FormBodyPart body = new FormBodyPart("form", stringBody);

				MultipartEntity reqEntity = new MultipartEntity();
				reqEntity.addPart("file", imageFile);
				reqEntity.addPart(body);

				httppost.setEntity(reqEntity);

				HttpResponse response;
				try {
					response = httpclient.execute(httppost);
//					HttpEntity resEntity = response.getEntity();

					assertThat(response.getStatusLine().getStatusCode())
							.isEqualTo(200);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				assertThat(result).isNotNull();

				f.delete();
			}
		});
	}
	
	
	@Test
	public void testLogin() {

		running(app, new Runnable() {
			public void run() {
				UserBean userBean = new UserBean();
				userBean.setEmail("cdparra@gmail.com");
				userBean.setEmail("testing-password");
				Result result = route(fakeRequest(POST, "/user/login")
						.withHeader("Content-Type", "application/json")
						.withJsonBody(toJson(userBean))
				);

				Logger.debug("result is " + contentAsString(result));
				System.out.println("result is " + contentAsString(result));
				assertThat(status(result)).isEqualTo(OK);
				

				userBean = new UserBean();
				userBean.setEmail("cdparra@gmail.com");
				userBean.setEmail("testing-password-lalala");
				result = route(fakeRequest(POST, "/user/login")
						.withHeader("Content-Type", "application/json")
						.withJsonBody(toJson(userBean))
				);

				Logger.debug("result is " + contentAsString(result));
				System.out.println("result is " + contentAsString(result));
				assertThat(status(result)).isEqualTo(UNAUTHORIZED);
			}
		});
	}

}
