package com.marketplace.api.exception;




import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;

/**
 * Created by Narasim Bayanaboina 
 */

@JsonRootName("error")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "errorCode",
        "message"
})
public class Error  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("errorCode")
    private String errorCode;
    @JsonProperty("message")
    private String message;

public Error(String errorCode , String message) {
	this.errorCode = errorCode;
	this.message = 	message;	
}
    @JsonProperty("errorCode")
    public String getErrorCode() {
        return errorCode;
    }

    @JsonProperty("errorCode")
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

}
