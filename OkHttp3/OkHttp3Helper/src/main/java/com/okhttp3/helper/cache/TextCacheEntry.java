package com.okhttp3.helper.cache;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.CipherSuite;
import okhttp3.Handshake;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.TlsVersion;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.StatusLine;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class TextCacheEntry
{
	private static final String SENT_MILLIS = "OKHttp3-Sent-Millis";

	private static final String RECEIVED_MILLIS = "OKHttp3-Received-Millis";

	private final String url; // 链接

	private final String requestMethod; // 请求方法, GET、POST

	private final TextCacheHeader varyCacheMap;

	private final Protocol protocol; // http/1.1

	private final int protocolCode; // 200

	private final String protocolMessage; // ...

	private final TextCacheHeader textCacheMap;

	private final long sentRequestMillis; // send time, 149,277,174.7232

	private final long receivedResponseMillis; // received time, 149,277,174,7283

	private final Handshake handshake;

	public TextCacheEntry(Response response)
	{
		this.url = response.request().url().toString();
		this.requestMethod = response.request().method();
		if (null == response.headers())
		{
			this.varyCacheMap = new TextCacheHeader();
			this.textCacheMap = new TextCacheHeader();
		}
		else if (null == response.networkResponse())
		{
			this.varyCacheMap = new TextCacheHeader();
			this.textCacheMap = header2Map(response.headers());
		}
		else
		{
			this.varyCacheMap = header2Map(HttpHeaders.varyHeaders(response));
			this.textCacheMap = header2Map(response.headers());
		}
		this.protocol = response.protocol();
		this.protocolCode = response.code();
		this.protocolMessage = response.message();
		this.sentRequestMillis = response.sentRequestAtMillis();
		this.receivedResponseMillis = response.receivedResponseAtMillis();
		this.handshake = response.handshake();
	}

	// 这个构造方法，读取文件内容
	public TextCacheEntry(Source in) throws IOException
	{
		try
		{
			BufferedSource bufferedSource = Okio.buffer(in);

			this.url = bufferedSource.readUtf8LineStrict(); // 读取一行, url
			this.requestMethod = bufferedSource.readUtf8LineStrict(); // 读取一行, method
			int varyLineCount = readInt(bufferedSource); // 读取接下来连续的行数(指示接下来行数)
			this.varyCacheMap = new TextCacheHeader();
			for (int i = 0; i < varyLineCount; i++)
			{
				varyCacheMap.add(bufferedSource.readUtf8LineStrict());
			}

			String statusLineString = bufferedSource.readUtf8LineStrict();
			StatusLine statusLine = StatusLine.parse(statusLineString);
			this.protocol = statusLine.protocol;
			this.protocolCode = statusLine.code;
			this.protocolMessage = statusLine.message;

			int headerLineCount = readInt(bufferedSource);
			this.textCacheMap = new TextCacheHeader();
			for (int i = 0; i < headerLineCount; i++)
			{
				textCacheMap.add(bufferedSource.readUtf8LineStrict());
			}
			String sentRequestMillisString = textCacheMap.remove(SENT_MILLIS);
			String receivedResponseMillisString = textCacheMap.remove(RECEIVED_MILLIS);
			this.sentRequestMillis = null != sentRequestMillisString ? Long.parseLong(sentRequestMillisString) : 0L;
			this.receivedResponseMillis = null != receivedResponseMillisString ? Long.parseLong(receivedResponseMillisString) : 0L;

			if (isHttps())
			{
				String blank = bufferedSource.readUtf8LineStrict();
				if (blank.length() > 0)
				{
					throw new IOException("expected \"\" but was \"" + blank + "\"");
				}
				String cipherSuiteString = bufferedSource.readUtf8LineStrict();
				CipherSuite cipherSuite = CipherSuite.forJavaName(cipherSuiteString);
				List<Certificate> peerCertificates = readCertificateList(bufferedSource);
				List<Certificate> localCertificates = readCertificateList(bufferedSource);
				TlsVersion tlsVersion = !bufferedSource.exhausted() ? TlsVersion.forJavaName(bufferedSource.readUtf8LineStrict()) : null;
				this.handshake = Handshake.get(tlsVersion, cipherSuite, peerCertificates, localCertificates);
			}
			else
			{
				this.handshake = null;
			}
		} finally
		{
			in.close();
		}
	}

	public Response response(RequestBody requestBody, DiskLruCache.Snapshot snapshot)
	{
		String contentType = textCacheMap.get("Content-Type");
		String contentLength = textCacheMap.get("Content-Length");
		Request.Builder cacheRequestBuilder;
		if (requestMethod == "GET")
		{
			cacheRequestBuilder = new Request.Builder().url(url).method(requestMethod, null);
		}
		else
		{
			cacheRequestBuilder = new Request.Builder().url(url).method(requestMethod, requestBody);
		}
		for (String key : varyCacheMap.keySet())
		{
			cacheRequestBuilder.header(key, varyCacheMap.get(key));
		}
		Request cacheRequest = cacheRequestBuilder.build();

		Headers responseHeader = map2Header(textCacheMap);
		return new Response.Builder()
				.request(cacheRequest)
				.protocol(protocol)
				.code(protocolCode)
				.message(protocolMessage)
				.headers(responseHeader)
				.body(new CacheResponseBody(snapshot, contentType, contentLength))
				.handshake(handshake)
				.sentRequestAtMillis(sentRequestMillis)
				.receivedResponseAtMillis(receivedResponseMillis)
				.build();
	}

	public boolean matches(Request request, Response response)
	{
		return url.equals(request.url().toString())
				&& requestMethod.equals(request.method())
				&& HttpHeaders.varyMatches(response, request.headers(), request);
	}

	public void writeTo(DiskLruCache.Editor editor, Response response) throws IOException
	{
		try
		{
			writeToMetadata(editor);
			writeToMetaBody(editor, response);
		} finally
		{
			editor.commit();
		}
	}

	// 第一个文件
	private void writeToMetadata(DiskLruCache.Editor editor) throws IOException
	{
		BufferedSink bufferedSink = null;
		try
		{
			bufferedSink = Okio.buffer(editor.newSink(TextCache.ENTRY_METADATA));

			bufferedSink.writeUtf8(url).writeByte('\n');
			bufferedSink.writeUtf8(requestMethod).writeByte('\n');
			bufferedSink.writeDecimalLong(varyCacheMap.size()).writeByte('\n');
			for (String key : varyCacheMap.keySet())
			{
				bufferedSink.writeUtf8(key).writeUtf8(": ")
						.writeUtf8(varyCacheMap.get(key)).writeByte('\n');
			}

			bufferedSink.writeUtf8(new StatusLine(protocol, protocolCode, protocolMessage).toString()).writeByte('\n');
			bufferedSink.writeDecimalLong(textCacheMap.size() + 2).writeByte('\n');
			for (String key : textCacheMap.keySet())
			{
				bufferedSink.writeUtf8(key).writeUtf8(": ")
						.writeUtf8(textCacheMap.get(key)).writeByte('\n');
			}
			bufferedSink.writeUtf8(SENT_MILLIS).writeUtf8(": ")
					.writeDecimalLong(sentRequestMillis).writeByte('\n');
			bufferedSink.writeUtf8(SENT_MILLIS).writeUtf8(": ")
					.writeDecimalLong(sentRequestMillis).writeByte('\n');

			if (isHttps())
			{
				bufferedSink.writeByte('\n');
				bufferedSink.writeUtf8(handshake.cipherSuite().javaName()).writeByte('\n');
				writeCertList(bufferedSink, handshake.peerCertificates());
				writeCertList(bufferedSink, handshake.localCertificates());
				// The handshake’s TLS version is null on HttpsURLConnection and on older cached responses.
				if (handshake.tlsVersion() != null)
				{
					bufferedSink.writeUtf8(handshake.tlsVersion().javaName()).writeByte('\n');
				}
			}
		} finally
		{
			if (null != bufferedSink)
			{
				bufferedSink.close();
			}
		}
	}

	// 写入第二个文件
	private void writeToMetaBody(DiskLruCache.Editor editor, Response response) throws IOException
	{
		BufferedSink bufferedSink = null;
		try
		{
			bufferedSink = Okio.buffer(editor.newSink(TextCache.ENTRY_BODY));
			InputStream inputStream = response.body().byteStream();
			int len;
			byte[] buffer = new byte[4096];
			while ((len = inputStream.read(buffer)) != -1)
			{
				bufferedSink.write(buffer, 0, len);
			}
		} finally
		{
			if (null != bufferedSink)
			{
				bufferedSink.close();
			}
		}
	}

	private void writeCertList(BufferedSink sink, List<Certificate> certificates) throws IOException
	{
		try
		{
			sink.writeDecimalLong(certificates.size()).writeByte('\n');
			for (int i = 0, size = certificates.size(); i < size; i++)
			{
				byte[] bytes = certificates.get(i).getEncoded();
				String line = ByteString.of(bytes).base64();
				sink.writeUtf8(line).writeByte('\n');
			}
		} catch (CertificateEncodingException e)
		{
			throw new IOException(e.getMessage());
		}
	}

	private TextCacheHeader header2Map(Headers headers)
	{
		TextCacheHeader cacheMap = new TextCacheHeader();
		for (String name : headers.names())
		{
			cacheMap.add(name, headers.get(name));
		}
		return cacheMap;
	}

	private Headers map2Header(TextCacheHeader textCacheMap)
	{
		Headers.Builder responseHeaderBuilder = new Headers.Builder();
		for (String key : textCacheMap.getMap().keySet())
		{
			responseHeaderBuilder.add(key, textCacheMap.getMap().get(key));
		}

		return responseHeaderBuilder.build();
	}

	private int readInt(BufferedSource source) throws IOException
	{
		try
		{
			long result = source.readDecimalLong();
			String line = source.readUtf8LineStrict();
			if (result < 0 || result > Integer.MAX_VALUE || !line.isEmpty())
			{
				throw new IOException("expected an int but was \"" + result + line + "\"");
			}
			return (int) result;
		} catch (NumberFormatException e)
		{
			throw new IOException(e.getMessage());
		}
	}

	private boolean isHttps()
	{
		return url.startsWith("https://");
	}

	private List<Certificate> readCertificateList(BufferedSource source) throws IOException
	{
		int length = readInt(source);
		if (length == -1)
		{
			return Collections.emptyList(); // OkHttp v1.2 used -1 to indicate null.
		}

		try
		{
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			List<Certificate> result = new ArrayList<>(length);
			for (int i = 0; i < length; i++)
			{
				String line = source.readUtf8LineStrict();
				Buffer bytes = new Buffer();
				bytes.write(ByteString.decodeBase64(line));
				result.add(certificateFactory.generateCertificate(bytes.inputStream()));
			}
			return result;
		} catch (CertificateException e)
		{
			throw new IOException(e.getMessage());
		}
	}

	private static class CacheResponseBody extends ResponseBody
	{
		final DiskLruCache.Snapshot snapshot;

		private final BufferedSource bodySource;

		private final String contentType;

		private final String contentLength;

		public CacheResponseBody(final DiskLruCache.Snapshot snapshot, String contentType, String contentLength)
		{
			this.snapshot = snapshot;
			this.contentType = contentType;
			this.contentLength = contentLength;

			Source source = snapshot.getSource(TextCache.ENTRY_BODY);
			bodySource = Okio.buffer(new ForwardingSource(source)
			{
				@Override
				public void close() throws IOException
				{
					snapshot.close();
					super.close();
				}
			});
		}

		@Override
		public MediaType contentType()
		{
			return contentType != null ? MediaType.parse(contentType) : null;
		}

		@Override
		public long contentLength()
		{
			try
			{
				return contentLength != null ? Long.parseLong(contentLength) : -1;
			} catch (NumberFormatException e)
			{
				return -1;
			}
		}

		@Override
		public BufferedSource source()
		{
			return bodySource;
		}
	}
}
