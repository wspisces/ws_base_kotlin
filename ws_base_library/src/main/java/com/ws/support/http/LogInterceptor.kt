package com.ws.support.http

import android.util.Log
import com.ws.support.http.LogInterceptor.Logger
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * @Desc :
 * @Author : curry
 * @Date : 2018/9/17
 */
class LogInterceptor @JvmOverloads constructor(private val logger: Logger = Logger.DEFAULT) : Interceptor {
    @Volatile
    private var level = Level.NONE

    enum class Level {
        /**
         * No logs.
         */
        NONE,

        /**
         * Logs request and response lines.
         *
         *
         * Example:
         * <pre>`--> POST /greeting HTTP/1.1 (3-byte body)
         *
         * <-- HTTP/1.1 200 OK (22ms, 6-byte body)
        `</pre> *
         */
        BASIC,

        /**
         * Logs request and response lines and their respective headers.
         *
         *
         * Example:
         * <pre>`--> POST /greeting HTTP/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- HTTP/1.1 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
        `</pre> *
         */
        HEADERS,

        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         *
         *
         * Example:
         * <pre>`--> POST /greeting HTTP/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END GET
         *
         * <-- HTTP/1.1 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
        `</pre> *
         */
        BODY
    }

    interface Logger {
        fun log(message: String?)

        companion object {
            /**
             * A [Logger] defaults output appropriate for the current platform.
             */
            val DEFAULT: Logger = object : Logger {
                override fun log(message: String?) {
                    Log.e("reuest", message)
                }
            }
        }
    }

    /**
     * Change the level at which this interceptor logs.
     */
    fun setLevel(level: Level?): LogInterceptor {
        if (level == null) throw NullPointerException("level == null. Use Level.NONE instead.")
        this.level = level
        return this
    }

    fun getLevel(): Level {
        return level
    }

    @kotlin.Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val level = level
        val request = chain.request()
        if (level == Level.NONE) {
            return chain.proceed(request)
        }
        val logBody = level == Level.BODY
        val logHeaders = logBody || level == Level.HEADERS
        val requestBody = request.body()
        val hasRequestBody = requestBody != null
        val connection = chain.connection()
        //        Protocol protocol = connection != null ? connection.getProtocol() : Protocol.HTTP_1_1;
        val requestStartMessage = StringBuilder()
        requestStartMessage.append("--> " + request.method() + ' ' + request.url() + ' ') //+ protocol(protocol)
        if (!logHeaders && hasRequestBody) {
            requestStartMessage.append(" (" + requestBody!!.contentLength() + BYTE_BODY)
        }
        logger.log(requestStartMessage.toString())
        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody!!.contentType() != null) {
                    logger.log("Content-Type: " + requestBody.contentType())
                }
                if (requestBody.contentLength() != -1L) {
                    logger.log("Content-Length: " + requestBody.contentLength())
                }
            }
            val headers = request.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                val name = headers.name(i)
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equals(name, ignoreCase = true) && !"Content-Length".equals(name, ignoreCase = true)) {
                    logger.log(name + ": " + headers.value(i))
                }
                i++
            }
            if (!logBody || !hasRequestBody) {
                logger.log(END + request.method())
            } else if (bodyEncoded(request.headers())) {
                logger.log(END + request.method() + " (encoded body omitted)")
            } else {
                val buffer = Buffer()
                requestBody!!.writeTo(buffer)
                val charset = UTF8
                val contentType = requestBody.contentType()
                contentType?.charset(UTF8)
                logger.log("")
                logger.log(buffer.readString(charset))
                logger.log(END + request.method()
                        + " (" + requestBody.contentLength() + BYTE_BODY)
            }
        }
        val startNs = System.nanoTime()
        val response = chain.proceed(request)
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        val responseBody = response.body()
        logger.log("<-- " + response.code() + ' ' + response.message() + ' '
                + response.request().url() + " (" + tookMs + "ms" + (if (!logHeaders) ", "
                + responseBody!!.contentLength() + "-byte body" else "") + ')')
        if (logHeaders) {
            val headers = response.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                logger.log(headers.name(i) + ": " + headers.value(i))
                i++
            }
            if (!logBody) { //|| !HttpEngine.hasBody(response)
                logger.log("<-- END HTTP")
            } else if (bodyEncoded(response.headers())) {
                logger.log("<-- END HTTP (encoded body omitted)")
            } else {
                val source = responseBody!!.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source.buffer()
                var charset = UTF8
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }
                if (responseBody.contentLength() != 0L) {
                    logger.log("")
                    logger.log(buffer.clone().readString(charset!!))
                }
                logger.log("<-- END HTTP (" + buffer.size + BYTE_BODY)
            }
        }
        return response
    }

    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"]
        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
    }

    companion object {
        private const val BYTE_BODY = "-byte body)"
        private const val END = "--> END "
        private val UTF8 = Charset.forName("UTF-8")
        private fun protocol(protocol: Protocol): String {
            return if (protocol == Protocol.HTTP_1_0) "HTTP/1.0" else "HTTP/1.1"
        }
    }
}