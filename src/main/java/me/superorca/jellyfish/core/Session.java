package me.superorca.jellyfish.core;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.util.function.Consumer;

@UtilityClass
public class Session {
    public void get(String url, Consumer<HttpResponse<JsonNode>> consumer) {
        Unirest.get(url).asJsonAsync(new Callback<>() {
            @Override
            public void completed(HttpResponse<JsonNode> response) {
                consumer.accept(response);
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
    }

    public void getBinary(String url, Consumer<HttpResponse<InputStream>> consumer) {
        Unirest.get(url).asBinaryAsync(new Callback<>() {
            @Override
            public void completed(HttpResponse<InputStream> response) {
                consumer.accept(response);
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
    }
}
