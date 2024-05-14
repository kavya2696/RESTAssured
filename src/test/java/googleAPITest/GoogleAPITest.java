package googleAPITest;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.Requests.PostAddressRequest;
import com.qa.Requests.UpdatePlaceRequest;
import com.qa.Responses.PostAddressResponse;
import com.qa.Responses.PostPlaceResponse;
import com.qa.RestUtils.JSONUtils;
import com.qa.ServiceAPI.GoogleAPIHTTPMethods;

import io.restassured.response.Response;

public class GoogleAPITest extends BaseTest {
	
	public static final String testdata1 =  System.getProperty("user.dir")+"//TestDataJSONFiles//CreatPlace.json";
	public static final String testdata2 =  System.getProperty("user.dir")+"//TestDataJSONFiles//UpdatePlace.json";
	
	GoogleAPIHTTPMethods apimethods =  new GoogleAPIHTTPMethods();
	public static PostPlaceResponse response1;
	
	@Test(priority=1)
	public void createPlaceTest() throws IOException {
		setTestDesc("Creating the place using post API");
		PostAddressRequest request = JSONUtils.createPOJOfomJSON(testdata1, PostAddressRequest.class);
		request.setName("Kavya S");
		Response response = apimethods.createAddress(request);
		apimethods.validateStatusCode(response, 200);
		response1 = response.as(PostPlaceResponse.class);
		AssertJUnit.assertNotNull(response1.getPlaceId());
	}
	
	@Test(priority=2)
	public void getPlaceTest() throws IOException{
		setTestDesc("Get created place using get API");
		Response response = apimethods.getPlaceAPI(response1.getPlaceId().toString());
		apimethods.validateStatusCode(response, 200);
		PostAddressResponse response1 = response.as(PostAddressResponse.class);
		AssertJUnit.assertEquals(response1.getName().toString(), "Kavya S");
	}
	
	@Test(priority=3)
	public void updatePlaceTest() throws IOException{
		setTestDesc("Update the created place using put API");
		UpdatePlaceRequest request = JSONUtils.createPOJOfomJSON(testdata2, UpdatePlaceRequest.class);
		request.setPlaceId(response1.getPlaceId().toString());
		request.setAddress("Kodavathi");
		Response response = apimethods.updatePlaceAPI(response1.getPlaceId().toString(), request);
		apimethods.validateStatusCode(response, 200);
		AssertJUnit.assertEquals(response.jsonPath().get("msg").toString(), "Address successfully updated");
	}
	
	@Test(priority=4)
	public void deletePlaceTest() throws IOException{
		setTestDesc("Delete the place using delete API");
		UpdatePlaceRequest request = JSONUtils.createPOJOfomJSON(testdata2, UpdatePlaceRequest.class);
		request.setPlaceId(response1.getPlaceId().toString());
		request.setAddress(null);
		request.setKey(null);
		Response response = apimethods.deletePlaceAPI(request);
		apimethods.validateStatusCode(response, 200);
		AssertJUnit.assertEquals(response.jsonPath().get("status").toString(), "OK");
	}
	
	@Test(priority=5)
	public void getPlaceAfterdeletTest() throws IOException{
		setTestDesc("Get place after deleting the place");
		Response response = apimethods.getPlaceAPIusingRequestSpecificattion(response1.getPlaceId().toString());
		apimethods.validateStatusCode(response, 404);
		AssertJUnit.assertEquals(response.jsonPath().get("msg").toString(), "Get operation failed, looks like place_id  doesn't exists");
		
	}
	

}
