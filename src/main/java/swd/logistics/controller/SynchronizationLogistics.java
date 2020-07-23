package swd.logistics.controller;

import com.alibaba.fastjson.JSON;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swd.logistics.po.LogisticsInfo;
import swd.logistics.po.Traces;
import swd.logistics.service.LogisticsInfoService;
import swd.logistics.utils.ReturnInfor;

import java.util.*;

@RestController
@RequestMapping("Synchronization")
public class SynchronizationLogistics {

    @Autowired
    private LogisticsInfoService logisticsInfoService;

    @Value("${appShopNotifyUrl}")
    private String appShopNotifyUrl;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("logistics")
    public List<String> logistics(@RequestBody List<String> list){
        LogisticsInfo yd =null;

        Map<String,Date> logisticsMap = new HashMap<>();
        List<String> logistics = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            try {
               yd = logisticsInfoService.getLogisticsInfoByImmediate("YD", list.get(i));
                if (yd.getStateEx().equals("211") || yd.getStateEx().indexOf("3") == 0){
                    //logistics.add(yd.getLogisticCode());
                    Traces traces = yd.getTraces().get(yd.getTraces().size() - 1);
                    log.info("最后一次推送轨迹？"+traces.toString());
                    logisticsMap.put(yd.getLogisticCode(),traces.getAcceptTime());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
       if(logisticsMap.size() > 0){
           Map<String,Map<String,Date>> map1 = new HashMap<>();
           map1.put("logistics",logisticsMap);
           map1.put("orderCode",null);
            new Thread(){
                @Override
                public void run() {
                    HttpClient httpClient = new HttpClient();
                    PostMethod postMethod = new PostMethod(appShopNotifyUrl);

                    postMethod.addRequestHeader("accept", "application/json;charset=UTF-8");
                    postMethod.addRequestHeader("connection", "Keep-Alive");
                    //设置json格式传送
                    postMethod.addRequestHeader("Content-Type", "application/json;charset=UTF-8");
                    //必须设置下面这个Header
                    postMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
                    //postMethod.setParameter("map",JSON.toJSONString(map));
                    RequestEntity requestEntity = new StringRequestEntity(JSON.toJSONString(map1));
                    postMethod.setRequestEntity(requestEntity);
                    String res = "";
                    try {
                        log.info("请求的路径为："+postMethod.getURI());
                        int code = httpClient.executeMethod(postMethod);
                        log.info("code为："+code);
                        if (code == 200){
                            res = postMethod.getResponseBodyAsString();
                            log.info("推送状态成功,这些订单号{}已成功修改状态"+res);
                        }
                    }catch (Exception e){
                        log.error("推送状态消息异常");
                    }

                }
            }.start();
        }
        return list;
    }
}
