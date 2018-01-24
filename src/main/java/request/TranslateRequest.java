package request;

public class TranslateRequest implements Request {

    private final String TR_URL =
            "/api/v1.5/tr.json/translate?lang=en-ru&key=";

    private final String url;
    private final String text;

    public TranslateRequest(String token, String text) {
        this.text = text;
        this.url = TR_URL + token;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getData() {
        return "text=" + text;
    }

    @Override
    public String toString() {
        return url + " " + text;
    }
}
