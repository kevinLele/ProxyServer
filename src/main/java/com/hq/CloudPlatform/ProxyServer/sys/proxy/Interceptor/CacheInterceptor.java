package com.hq.CloudPlatform.ProxyServer.sys.proxy.Interceptor;

import com.hq.CloudPlatform.ProxyServer.utils.ConfigHelper;
import com.predic8.membrane.core.exchange.Exchange;
import com.predic8.membrane.core.http.Body;
import com.predic8.membrane.core.http.Header;
import com.predic8.membrane.core.http.Response;
import com.predic8.membrane.core.interceptor.AbstractInterceptor;
import com.predic8.membrane.core.interceptor.Outcome;
import com.predic8.membrane.core.util.EndOfStreamException;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 9/13/2016.
 * 用于缓存响应结果的拦截器
 */
public class CacheInterceptor extends AbstractInterceptor {

    /*private static final Logger log = LoggerFactory.getLogger(CacheInterceptor.class);

    public static final String CACHE_CLUSTER_NAME = ConfigHelper.getValue("ProxyServer.cache.clusterServerName");

    public static final String CACHE_NAME = ConfigHelper.getValue("ProxyServer.cache.name");

    private static final String serDirPath = ConfigHelper.getValue("ProxyServer.cache.ser.dir.path");

    private static final boolean isCacheFile = Boolean.valueOf(ConfigHelper.getValue("ProxyServer.cache.toFile"));

    public CacheInterceptor() {

        super();
        setFlow(Flow.Set.REQUEST_RESPONSE);
    }

    @Override
    public Outcome handleRequest(Exchange exc) throws Exception {
        String bodyMd5 = "";
        Resource resource = (Resource)exc.getProperty("resource");

        //如果资源停用缓存则直接返回
        if (!"1".equals(resource.getCacheStatus())) {
            return Outcome.CONTINUE;
        }

        //如果是post请求则可能URL都是相同的只是请求消息体不同，所以需要根据消息体的内容来进行MD5值的计算从而区分不同的请求
        if (exc.getRequest().isPOSTRequest()) {
            String content = new String(exc.getRequest().getBody().getContent());
            bodyMd5 = DigestUtils.md5Hex(content);
        }

        //根据url生成md5值，作为缓存的key
        String pathMd5 = DigestUtils.md5Hex(String.valueOf(exc.getProperty("sortedUrl")) + bodyMd5);
        Cache cache = DataGridTool.getInstance(CACHE_CLUSTER_NAME).getCache(
                String.valueOf(exc.getProperty("serviceId")), true);
        SerializableResponse serializableRsp = (SerializableResponse) cache.get(pathMd5);

        //如果内存级缓存无法找到就尝试从磁盘级缓存中读取
        if (isCacheFile && null == serializableRsp) {
            File serFile = getSerFile(exc, pathMd5);
            ObjectInputStream in = null;

            if (serFile.exists()) {
                try {
                    in = new ObjectInputStream(new FileInputStream(serFile));
                    Object obj = in.readObject();

                    if (obj instanceof SerializableResponse) {
                        serializableRsp = (SerializableResponse) obj;
                    } else {
                        new Exception("反序列化失败！");
                    }
                } finally {
                    if (null != in) {
                        in.close();
                    }
                }
            }
        }

        if (null != serializableRsp) {
            Response rsp = getNormalResponse(serializableRsp);
            exc.setResponse(rsp);

            //直接返回RETURN， 这样就不需要执行其它拦截器
            return Outcome.RETURN;
        }

        return Outcome.CONTINUE;
    }

    @Override
    public Outcome handleResponse(Exchange exc) throws Exception {

        Resource resource = (Resource)exc.getProperty("resource");

        //如果资源停用缓存则直接返回
        if (!"1".equals(resource.getCacheStatus())) {
            return Outcome.CONTINUE;
        }

        Response rsp = exc.getResponse();

        *//**
         * 只缓存返回码为200的正常响应结果，防止缓存了无效的数据，例如304，302，404等等。
         *//*
        if (200 != rsp.getStatusCode()) {
            return Outcome.CONTINUE;
        }

        String bodyMd5 = "";

        //如果是post请求则可能URL都是相同的只是请求消息体不同，所以需要根据消息体的内容来进行MD5值的计算从而区分不同的请求
        if (exc.getRequest().isPOSTRequest()) {
            String content = new String(exc.getRequest().getBody().getContent());
            bodyMd5 = DigestUtils.md5Hex(content);
        }

        SerializableResponse serializableRsp = getSerializableResponse(rsp);

        String pathMd5 = DigestUtils.md5Hex(String.valueOf(exc.getProperty("sortedUrl")) + bodyMd5);
        Cache cache = DataGridTool.getInstance(CACHE_CLUSTER_NAME).getCache(
                String.valueOf(exc.getProperty("serviceId")), true);
        cache.put(pathMd5, serializableRsp);

        //如果需要将响应结果缓存到文件中
        if (isCacheFile) {
            File serFile = getSerFile(exc, pathMd5);
            ObjectOutputStream out = null;

            try {
                if (!serFile.getParentFile().exists()) {
                    serFile.getParentFile().mkdirs();
                }

                out = new ObjectOutputStream(new FileOutputStream(serFile));
                out.writeObject(serializableRsp);
            } finally {
                if (null != out) {
                    out.flush();
                    out.close();
                }
            }
        }

        return Outcome.CONTINUE;
    }

    private File getSerFile(Exchange exc, String pathMd5) throws MalformedURLException {

        try {
            URL url = new URL(
                    "http",
                    exc.getOriginalHostHeaderHost(),
                    Integer.parseInt(exc.getOriginalHostHeaderPort()),
                    exc.getOriginalRequestUri());

            String path = url.getPath().substring(ProxyServer.PROXY_PATH.length());
            path.replace('\\', '/');
            File serFile = new File(serDirPath + path + "/" + pathMd5);
            log.debug("Stored File Path : " + serFile.getAbsolutePath());

            return serFile;
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);

            throw e;
        }
    }

    private SerializableResponse getSerializableResponse(Response rsp) throws IOException {

        SerializableResponse serializableRsp = new SerializableResponse();
        serializableRsp.setStatusCode(rsp.getStatusCode());
        serializableRsp.setStatusMessage(rsp.getStatusMessage());
        serializableRsp.setErrorMessage(rsp.getErrorMessage());
        serializableRsp.setVersion(rsp.getVersion());
        serializableRsp.setHeader(rsp.getHeader().toString());
        serializableRsp.setBodyContentBytes(rsp.getBody().getContent());

        return serializableRsp;
    }

    private Response getNormalResponse(SerializableResponse serializableRsp) throws IOException, EndOfStreamException {

        Response rsp = new Response.ResponseBuilder().header(new Header(serializableRsp.getHeader())).build();
        rsp.setStatusCode(serializableRsp.getStatusCode());
        rsp.setStatusMessage(serializableRsp.getStatusMessage());
        rsp.setErrorMessage(serializableRsp.getErrorMessage());
        rsp.setVersion(serializableRsp.getVersion());
        rsp.setBody(new Body(serializableRsp.getBodyContentBytes()));

        return rsp;
    }*/
}
