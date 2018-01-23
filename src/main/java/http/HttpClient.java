package http;

import java.io.IOException;

import org.apache.http.HttpResponse;

import com.fasterxml.jackson.core.JsonProcessingException;

import request.Request;

public interface HttpClient {
    HttpResponse sendPost(Request req) throws IOException;
    HttpResponse sendGet(Request req);
    HttpResponse sendPostTranslate(Request req) throws IOException;
}
