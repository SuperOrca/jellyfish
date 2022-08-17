package me.superorca.jellyfish.core;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.experimental.UtilityClass;

import java.io.InputStream;

@UtilityClass
public class Session {
    public HttpResponse<JsonNode> get(String url) {
        final HttpResponse<JsonNode>[] response = new HttpResponse[]{null};
        Unirest.get(url).asJsonAsync(new Callback<>() {
            @Override
            public void completed(HttpResponse<JsonNode> result) {
                response[0] = result;
            }

            @Override
            public void failed(UnirestException e) {
                e.printStackTrace();
            }

            @Override
            public void cancelled() {
                new UnirestException("Cancelled").printStackTrace();
            }
        });
        return response[0];
    }

    public HttpResponse<InputStream> getBinary(String url) {
        final HttpResponse<InputStream>[] response = new HttpResponse[]{null};
        Unirest.get(url).asBinaryAsync(new Callback<>() {
            @Override
            public void completed(HttpResponse<InputStream> result) {
                response[0] = result;
            }

            @Override
            public void failed(UnirestException e) {
                e.printStackTrace();
            }

            @Override
            public void cancelled() {
                new UnirestException("Cancelled").printStackTrace();
            }
        });
        return response[0];
    }
}
