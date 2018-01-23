package request;

public class BotAnswerRequest implements Request {

    private final String chatId;
    private final String message;
    private final String url;

    public BotAnswerRequest(String chatId,
                            String message,
                            String writeUrl,
                            String token) {
        this.chatId = chatId;
        this.message = message;
        this.url = writeUrl + chatId + "?access_token=" + token;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String getData() {
        throw new UnsupportedOperationException("there is no data");
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{")
               .append("\"recipient\":{\"chat_id\":\"")
               .append(chatId).append("\"},")
               .append("\"message\":{\"text\":")
               .append("\"")
               .append(message)
               .append("\"")
               .append("}}");

        return builder.toString();
    }
}
