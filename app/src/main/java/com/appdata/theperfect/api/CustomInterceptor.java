package com.appdata.theperfect.api;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;


import com.appdata.theperfect.utils.NetworkAlertUtility;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class CustomInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");
    private final Logger logger;
    private String version, lang;
    private Headers headers;
    private volatile Level level = Level.NONE;
    private Context context;
    private int timeout;


    public CustomInterceptor(Context context, String lang, String version, int timeout) {
        this(Logger.DEFAULT);
        setLevel();
        this.context = context;
        this.version = version;
        this.lang = lang;
        this.timeout = timeout;
    }


    private CustomInterceptor(Logger logger) {
        this.logger = logger;
    }

    /**
     * Change the level at which this interceptor logs.
     */
    private void setLevel() {
        this.level = Level.BODY;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Level level = this.level;
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder().addHeader("os", "android")
                .addHeader("version", version)
                .addHeader("language", lang);

        Request request = requestBuilder.build();
//        Response response = chain.proceed(request);
        Response response = chain
                .withConnectTimeout(timeout, TimeUnit.SECONDS)
                .withReadTimeout(timeout, TimeUnit.SECONDS)
                .withWriteTimeout(timeout, TimeUnit.SECONDS)
                .proceed(request);

        boolean logBody = level == Level.BODY;
        boolean logHeaders = logBody || level == Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;


        String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        logger.log(requestStartMessage);


        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    logger.log("Content-Type: " + requestBody.contentType());
                }
                if (requestBody.contentLength() != -1) {
                    logger.log("Content-Length: " + requestBody.contentLength());
                }
            }

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    logger.log(name + ": " + headers.value(i));
                }
            }

            if (!logBody || !hasRequestBody) {
                logger.log("--> END " + request.method());
            } else if (bodyEncoded(request.headers())) {
                logger.log("--> END " + request.method() + " (encoded body omitted)");
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                logger.log("");
                assert charset != null;
                logger.log(buffer.readString(charset));

                logger.log("--> END " + request.method()
                        + " (" + requestBody.contentLength() + "-byte body)");
            }
        }

        long startNs = System.nanoTime();
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        assert responseBody != null;
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        logger.log("<-- " + response.code() + ' ' + response.message() + ' '
                + response.request().url() + " (" + tookMs + "ms" + (!logHeaders ? ", "
                + bodySize + " body" : "") + ')');

        if (logHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                logger.log(headers.name(i) + ": " + headers.value(i));
            }

            if (!logBody /*|| !HttpEngine.hasBody(response)*/) {
                logger.log("<-- END HTTP");
            } else if (bodyEncoded(response.headers())) {
                logger.log("<-- END HTTP (encoded body omitted)");
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    try {
                        charset = contentType.charset(UTF8);
                    } catch (UnsupportedCharsetException e) {
                        logger.log("");
                        logger.log("Couldn't decode the response body; charset is likely malformed.");
                        logger.log("<-- END HTTP");

                        return response;
                    }
                }

                if (contentLength != 0) {
                    logger.log("");
                    assert charset != null;
                    logger.log(buffer.clone().readString(charset));
                }

                logger.log("<-- END HTTP (" + buffer.size() + "-byte body)");
            }
        }

        if (NetworkAlertUtility.isInternetConnection(context)) {
            int maxAge = 60; // read from cache for 1 minute
            return response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            return response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }

        // return response;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public enum Level {
        NONE,
        BASIC,
        HEADERS,
        BODY
    }


    public interface Logger {
        Logger DEFAULT = message -> {
            //Platform.get().logW(message);
            Log.e("Response", message);
            //AppUtils.showRoughLog("Response",message);
        };

        void log(String message);
    }


}
