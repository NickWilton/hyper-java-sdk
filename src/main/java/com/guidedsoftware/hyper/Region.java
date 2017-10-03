package com.guidedsoftware.hyper;

public enum Region {
	US_WEST_1("us-west-1", "https://us-west-1.hyper.sh/v1.23"),
	EU_CENTRAL_1("eu-central-1", "https://eu-central-1.hyper.sh/v1.23");

	private String endpoint;
	private String name;

	Region(String name, String endpoint) {
		this.name = name;
		this.endpoint = endpoint;
	}

	public String getName() {
		return name;
	}

	public String getEndpoint() {
		return endpoint;
	}
}
