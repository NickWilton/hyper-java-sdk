# hyper-java-sdk

**The unofficial Hyper.sh SDK for Java**

This SDK can be used to connect to the Hyper.sh API interface to execute various commands documented at [https://docs.hyper.sh/](https://docs.hyper.sh/).

The a simple usage example is as follows:
```java
// Initialize instance
Hyper hyper = Hyper.getInstance("ACCESS-KEY-XXXXXXXXXXXXX", "SECRET-KEY-XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
hyper.setRegion(Region.US_WEST_1);  // Defaults to US-West-1

try {
	// Fetch all containers from Hyper.sh
	JsonNode result = hyper.performRequest(RequestType.GET, "/containers/json?all=1", null);
	
	// List Containers
	for (final JsonNode container : result) {
		System.out.println(container.at("/Id").asText());
	}

} catch (HyperException ex) {
	ex.printStackTrace();
}
```

You can also use `setRegistryAuth(String registryUsername, String registryPassword, String registryEmail)` to set the registry authentication details.

### Dependencies
The project has the following maven dependencies.
```xml
<dependency>
    <groupId>uk.co.lucasweb</groupId>
    <artifactId>aws-v4-signer-java</artifactId>
    <version>1.2</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.5.4</version>
</dependency>
<dependency>
    <groupId>com.sun.jersey</groupId>
    <artifactId>jersey-client</artifactId>
    <version>1.8</version>
</dependency>
```

Sorry, I haven't got this project up on Maven yet (it's a frustratingly difficult progress).

Let me know if you have any requests, or want to contribute.
