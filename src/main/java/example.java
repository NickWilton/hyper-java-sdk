import com.fasterxml.jackson.databind.JsonNode;
import com.guidedsoftware.hyper.Hyper;
import com.guidedsoftware.hyper.HyperException;
import com.guidedsoftware.hyper.Region;
import com.guidedsoftware.hyper.RequestType;

public class example {

	public static void main(String[] args) {

		// Initialize instance
		Hyper hyper = Hyper.getInstance("ACCESS-KEY-XXXXXXXXXXXXX", "SECRET-KEY-XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		hyper.setRegion(Region.US_WEST_1);  // Defaults to US-West-1

		try {
			// Fetch all containers from Hyper.sh
			JsonNode result = hyper.performRequest(RequestType.GET, "/containers/json?all=1", null);

			// List Containers
			for (final JsonNode container : result) {
				// Print Container Id
				System.out.println(container.at("/Id").asText());
			}

		} catch (HyperException ex) {
			ex.printStackTrace();
		}
	}

}
