package com.heropicker.facts.ddos;

import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;

@Role(Role.Type.EVENT)
@Expires("5m")
public class IpAccessFact {

	private String ipAddress;
	private boolean analysed = false;

	public IpAccessFact() {

	}

	public IpAccessFact(String ipAddress) {
		super();
		this.ipAddress = ipAddress;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public boolean isAnalysed() {
		return analysed;
	}

	public void setAnalysed(boolean analysed) {
		this.analysed = analysed;
	}

}
