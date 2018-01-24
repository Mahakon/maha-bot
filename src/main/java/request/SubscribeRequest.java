package request;

import java.io.Serializable;

public class SubscribeRequest implements Request, Serializable {
    private final String baseUrl;
    private final String token;
    private final String url;


    public SubscribeRequest(String baseUrl,
                            String token,
                            String myAppHandler) {
        this.baseUrl = baseUrl;
        this.token = token;

        url = myAppHandler;
    }

    @Override
    public String getUrl() {
        return baseUrl + token;
    }

    @Override
    public String getData() {
        throw new UnsupportedOperationException("There is not data");
    }

    @Override
    public String toString() {
        return "{ \"url\": " + "\"" + url + "\" }";
    }
}

