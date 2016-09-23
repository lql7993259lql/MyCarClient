package com.mycar.nohttp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.JsonObjectRequest;
import com.yolanda.nohttp.rest.RestRequest;
import com.yolanda.nohttp.rest.StringRequest;
import com.yolanda.nohttp.tools.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>自定义请求对象.</p>
 * Created in Feb 1, 2016 8:53:17 AM.
 *
 * @author Yan Zhenjie.
 */
public class FastJsonRequest extends RestRequest<JSONObject> {

    public FastJsonRequest(String url) {
        super(url);
    }

    public FastJsonRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
    }

    @Override
    public void onPreExecute() {
        // TODO 这个方法会在真正请求前被调用，在这里可以做一些加密之类的工作。这个方法在子线程被调用。
        // 比如，我们做个模拟加密：
        MultiValueMap<String, Object> multiValueMap = getParamKeyValues();
        Set<String> keySet = multiValueMap.keySet();
            for (String key : keySet) {
            List<Object> values = multiValueMap.getValues(key);// POST, PUT, DELETE, PATCH请求方法传文件的时候，一个Key下允许有多个值。
            for (Object value : values) {
                // 这里就拿到所有的参数值了，你可以做加密啦。
            }
        }
    }

    @Override
    public JSONObject parseResponse(String url, Headers responseHeaders, byte[] responseBody) {
        String result = StringRequest.parseResponseString(responseHeaders, responseBody);
        JSONObject jsonObject;
        try {
            jsonObject = JSON.parseObject(result);
        } catch (Throwable e) {
            // 这里默认的错误可以定义为你们自己的协议
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("error", -1);
            map.put("url", url());
            map.put("data", "");
            map.put("method", getRequestMethod().toString());
            jsonObject = (JSONObject) JSON.toJSON(map);
        }
        return jsonObject;
    }

    @Override
    public String getAccept() {
        // 告诉服务器你接受什么类型的数据, 会添加到请求头的Accept中
        return JsonObjectRequest.ACCEPT;
    }

}
