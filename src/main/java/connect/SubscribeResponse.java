package connect;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscribeResponse implements Serializable {

    private String success;

    public SubscribeResponse() {}

    public SubscribeResponse(String success) {
        this.success = success;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "SubscribeResponse{" +
                "success='" + success + "'"
                + "}";
    }
}
