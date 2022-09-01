package shule.one.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthorityMixing {

	@JsonCreator
	public SimpleGrantedAuthorityMixing( @JsonProperty("authority") String role) {
		
	}
	
	

}
