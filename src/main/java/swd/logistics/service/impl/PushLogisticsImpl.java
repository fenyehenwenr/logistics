package swd.logistics.service.impl;

import com.alibaba.fastjson.JSON;
import jdk.nashorn.internal.runtime.ECMAException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import swd.logistics.bo.KdniaoConfig;
import swd.logistics.bo.PushLogisticsInfo;
import swd.logistics.bo.PushReturn;
import swd.logistics.dao.LogisticsInfoMapper;
import swd.logistics.dao.PushLogisticsMapper;
import swd.logistics.dao.TracesMapper;
import swd.logistics.po.LogisticsInfo;
import swd.logistics.po.PushLogistics;
import swd.logistics.po.Traces;
import swd.logistics.service.LogisticsInfoService;
import swd.logistics.service.PushLogisticsService;
import swd.logistics.utils.Empty;

import java.util.*;

@Service
public class PushLogisticsImpl implements PushLogisticsService {

    @Autowired
    private LogisticsInfoService logisticsInfoService;
    @Autowired
    private PushLogisticsMapper pushLogisticsMapper;

    @Autowired
    private LogisticsInfoMapper logisticsInfoMapper;

    @Autowired
    private TracesMapper tracesMapper;

    @Autowired
    private KdniaoConfig kdniaoConfig;

    @Value("${appShopNotifyUrl}")
    private String appShopNotifyUrl;

    @Value("${jxH5NotifyUrl}")
    private String jxH5NotifyUrl;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public int deleteByPrimaryKey(Long id) {
        return 0;
    }

    @Override
    public int insert(PushLogistics record) {
        return pushLogisticsMapper.insert(record);
    }

    @Override
    public PushLogistics selectByPrimaryKey(Long id) {
        return pushLogisticsMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<PushLogistics> selectAll() {
        return pushLogisticsMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(PushLogistics record) {
        return pushLogisticsMapper.updateByPrimaryKey(record);
    }

    @Override
    public PushReturn savePushData(PushLogisticsInfo pushLogisticsInfo) {

        List<String> logisticsList = new ArrayList<>();
        List<String> orderCodeList = new ArrayList<>();

        Map<String,Date> logisticsMap = new HashMap<>();

        Map<String,Date> orderCodeMap = new HashMap<>();

        PushReturn pushReturn = new PushReturn();
        pushReturn.setUpdateTime(new Date());
        pushReturn.setEBusinessID(kdniaoConfig.EBusinessID);

        PushLogistics pushLogistics = new PushLogistics();
        pushLogistics.setCount(pushLogisticsInfo.getCount());
        pushLogistics.setPushTime(pushLogisticsInfo.getPushTime());
        int insertResult = pushLogisticsMapper.insert(pushLogistics);
        log.info("pushId为："+pushLogistics.getId());
        if (insertResult > 0) {
            log.info("(深圳)插入推送消息成功。");
            int successNum = 0;
            List<LogisticsInfo> data = pushLogisticsInfo.getData();
            for (int i = 0; i < data.size(); i++) {
                LogisticsInfo logisticsInfo = data.get(i);

                logisticsInfo.setStateEx(logisticsInfo.getStateEx() == null ? "" : logisticsInfo.getStateEx());
                logisticsInfo.setPushId(pushLogistics.getId());
                Boolean isSuccess = saveLogisticsInfo(logisticsInfo);
                if (isSuccess){
                    successNum ++;
                }
                if (logisticsInfo.getStateEx().equals("211") || logisticsInfo.getStateEx().indexOf("3") == 0){
                    if (logisticsInfo.getOrderCode() == null || logisticsInfo.getOrderCode().equals("")){
                        //logisticsList.add(logisticsInfo.getLogisticCode());
                        Traces traces = logisticsInfo.getTraces().get(logisticsInfo.getTraces().size() - 1);
                        log.info("(深圳)最后一次推送轨迹？"+traces.toString());
                        logisticsMap.put(logisticsInfo.getLogisticCode(),traces.getAcceptTime());
                    }else {
                       // orderCodeList.add(logisticsInfo.getOrderCode());
                        Traces traces = logisticsInfo.getTraces().get(logisticsInfo.getTraces().size() - 1);
                        log.info("(深圳)最后一次推送轨迹？"+traces.toString());
                        orderCodeMap.put(logisticsInfo.getOrderCode(),traces.getAcceptTime());
                    }

                }
            }
            if (logisticsMap.size() > 0 || orderCodeMap.size() > 0) {
               // Map<String,List<String>> map = new HashMap<>();
                Map<String,Map<String,Date>> map1 = new HashMap<>();
                map1.put("logistics",logisticsMap);
                map1.put("orderCode",orderCodeMap);
                log.info("(深圳)发送的logistics："+map1.get("logistics").toString());
                log.info("(深圳)发送的orderCode："+map1.get("orderCode").toString());
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
                        RequestEntity requestEntity = new StringRequestEntity(JSON.toJSONString(map1));
                        postMethod.setRequestEntity(requestEntity);
                        String res = "";
                        try {
                            log.info("(深圳)请求的路径为："+postMethod.getURI());
                            int code = httpClient.executeMethod(postMethod);
                            log.info("code："+code);
                            if (code == 200){
                                res = postMethod.getResponseBodyAsString();
                                log.info("(深圳)推送状态成功,这些订单号已成功修改状态"+res);
                            }
                        }catch (Exception e){
                            log.error("(深圳)推送状态消息异常");
                        }

                    }
                }.start();
            }

            if (successNum == data.size()){//推送的数据已经全部接收，可以返回成功消息
                pushReturn.setSuccess(true);
                pushReturn.setReason("已全部接收，感谢推送！");
                log.info("(深圳)已全部接收，感谢推送！");
                return pushReturn;
            }
            pushReturn.setSuccess(true);
            pushReturn.setReason("推送信息只接收成功部分。请重试");
            log.error("(深圳)推送信息只接收成功部分。请重试");
            return pushReturn;
        }
        pushReturn.setSuccess(false);
        pushReturn.setReason("推送信息接收失败。请重试");
        log.error("(深圳)推送信息接收失败。请重试");
        return pushReturn;
    }


    @Override
    public PushReturn savePushDataJxH5(PushLogisticsInfo pushLogisticsInfo) {

        List<String> logisticsList = new ArrayList<>();
        List<String> orderCodeList = new ArrayList<>();

        Map<String,Date> logisticsMap = new HashMap<>();

        Map<String,Date> orderCodeMap = new HashMap<>();

        Map<String,Date> orderMap = new HashMap<>();

        PushReturn pushReturn = new PushReturn();
        pushReturn.setUpdateTime(new Date());
        pushReturn.setEBusinessID(kdniaoConfig.EBusinessID);

        PushLogistics pushLogistics = new PushLogistics();
        pushLogistics.setCount(pushLogisticsInfo.getCount());
        pushLogistics.setPushTime(pushLogisticsInfo.getPushTime());
        int insertResult = pushLogisticsMapper.insert(pushLogistics);
        log.info("pushId为："+pushLogistics.getId());
        if (insertResult > 0) {
            log.info("(江西)插入推送消息成功。");
            int successNum = 0;
            List<LogisticsInfo> data = pushLogisticsInfo.getData();
            for (int i = 0; i < data.size(); i++) {
                LogisticsInfo logisticsInfo = data.get(i);

                logisticsInfo.setStateEx(logisticsInfo.getStateEx() == null ? "" : logisticsInfo.getStateEx());
                logisticsInfo.setPushId(pushLogistics.getId());
                Boolean isSuccess = saveLogisticsInfo(logisticsInfo);
                if (isSuccess){
                    successNum ++;
                }
                if (logisticsInfo.getStateEx().equals("211") || logisticsInfo.getStateEx().indexOf("3") == 0){
                    if (logisticsInfo.getOrderCode() == null || logisticsInfo.getOrderCode().equals("")){
                        //logisticsList.add(logisticsInfo.getLogisticCode());
                        Traces traces = logisticsInfo.getTraces().get(logisticsInfo.getTraces().size() - 1);
                        log.info("（江西）最后一次推送轨迹？"+traces.toString());
                        logisticsMap.put(logisticsInfo.getLogisticCode(),traces.getAcceptTime());
                    }else {
                        // orderCodeList.add(logisticsInfo.getOrderCode());
                        Traces traces = logisticsInfo.getTraces().get(logisticsInfo.getTraces().size() - 1);
                        log.info("（江西，走订单号）最后一次推送轨迹？"+traces.toString());
                        orderCodeMap.put(logisticsInfo.getOrderCode(),traces.getAcceptTime());
                    }

                } else if (logisticsInfo.getStateEx().equals("201")){
                    if (logisticsInfo.getOrderCode() == null || logisticsInfo.getOrderCode().equals("")){
                        //logisticsList.add(logisticsInfo.getLogisticCode());
                        Traces traces = logisticsInfo.getTraces().get(logisticsInfo.getTraces().size() - 1);
                        log.info("（江西）改为已发货？"+traces.toString());
                        logisticsMap.put(logisticsInfo.getLogisticCode(),traces.getAcceptTime());
                    }else {
                        // orderCodeList.add(logisticsInfo.getOrderCode());
                        Traces traces = logisticsInfo.getTraces().get(logisticsInfo.getTraces().size() - 1);
                        log.info("（江西，走订单号）改为已发货？"+traces.toString());
                        orderCodeMap.put(logisticsInfo.getOrderCode(),traces.getAcceptTime());
                    }
                }else if (logisticsInfo.getReason().indexOf("轨迹")!=-1){//重新订阅
                    log.info("（江西，走订单号）无物流轨迹请重新订阅");
                    orderMap.put(logisticsInfo.getOrderCode(), new Date());

                }
            }
            if (logisticsMap.size() > 0 || orderCodeMap.size() > 0 || orderMap.size()>0) {
                // Map<String,List<String>> map = new HashMap<>();
                Map<String,Map<String,Date>> map1 = new HashMap<>();
                map1.put("logistics",logisticsMap);
                map1.put("orderCode",orderCodeMap);
                map1.put("order", orderMap);
                log.info("(江西)发送的logistics："+map1.get("logistics").toString());
                log.info("(江西)发送的orderCode："+map1.get("orderCode").toString());
                new Thread(){
                    @Override
                    public void run() {
                        HttpClient httpClient = new HttpClient();
                        PostMethod postMethod = new PostMethod(jxH5NotifyUrl);

                        postMethod.addRequestHeader("accept", "application/json;charset=UTF-8");
                        postMethod.addRequestHeader("connection", "Keep-Alive");
                        //设置json格式传送
                        postMethod.addRequestHeader("Content-Type", "application/json;charset=UTF-8");
                        //必须设置下面这个Header
                        postMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
                        RequestEntity requestEntity = new StringRequestEntity(JSON.toJSONString(map1));
                        postMethod.setRequestEntity(requestEntity);
                        String res = "";
                        try {
                            log.info("(江西)请求的路径为："+postMethod.getURI());
                            int code = httpClient.executeMethod(postMethod);
                            log.info("code："+code);
                            if (code == 200){
                                res = postMethod.getResponseBodyAsString();
                                log.info("(江西)推送状态成功,这些订单号已成功修改状态"+res);
                            }
                        }catch (Exception e){
                            log.error("(江西)推送状态消息异常");
                        }

                    }
                }.start();
            }

            if (successNum == data.size()){//推送的数据已经全部接收，可以返回成功消息
                pushReturn.setSuccess(true);
                pushReturn.setReason("已全部接收，感谢推送！");
                log.info("已全部接收，感谢推送！");
                return pushReturn;
            }
            pushReturn.setSuccess(true);
            pushReturn.setReason("推送信息只接收成功部分。请重试");
            log.error("推送信息只接收成功部分。请重试");
            return pushReturn;
        }
        pushReturn.setSuccess(false);
        pushReturn.setReason("推送信息接收失败。请重试");
        log.error("推送信息接收失败。请重试");
        return pushReturn;
    }

    public Boolean saveLogisticsInfo(LogisticsInfo logisticsInfo){
        Boolean returnResult = false;
        LogisticsInfo isHas = logisticsInfoMapper.ByLogisticsCode(logisticsInfo.getLogisticCode());
        if (Empty.isNotEmpty(isHas)){//数据库里是否已有。（已推送过或者即时查询过）
            log.info("走更新逻辑");
            isHas.setState(logisticsInfo.getState());//更新物流信息状态
            isHas.setPushId(logisticsInfo.getPushId());
            isHas.setStateEx(logisticsInfo.getStateEx());
            isHas.setLocation(logisticsInfo.getLocation());
            int updateResult = logisticsInfoMapper.updateByLogisticsCode(isHas);
            if (updateResult > 0) {//没有很好的更新轨迹方法。所以先删除轨迹信息，再插入
                tracesMapper.deleteBylogisticsInfoId(isHas.getId());
                List<Traces> traces = logisticsInfo.getTraces();
                if (traces != null && traces.size() >0) {
                    for (int i = 0; i < traces.size() ; i++) {//将物流和轨迹信息关联起来
                        traces.get(i).setLogisticsInfoId(isHas.getId());
                    }
                    int insertResult = tracesMapper.saveTracesByInfo(traces);
                    log.info("插入所有的轨迹信息:结果条数为："+insertResult);
                    if (insertResult == logisticsInfo.getTraces().size()){
                        returnResult = true;
                        return returnResult;
                    }
                    log.error("轨迹信息插入不完整，结果集："+insertResult);
                }else {
                    log.error("暂无物流信息，结果集为"+traces);
                }

            } else {
                log.error("更新物流信息失败，updateResult结果集为："+updateResult);
            }

        }else { //插入逻辑
            log.info("走新增逻辑");
            int insertResult = logisticsInfoMapper.insert(logisticsInfo);
            log.info("logisticsInfo Idw为："+logisticsInfo.getId());
            if (insertResult > 0) {//先插入这条物流信息，成功后再去更新轨迹
                List<Traces> traces = logisticsInfo.getTraces();
                if (traces != null && traces.size() > 0){
                    for (int i = 0; i < traces.size() ; i++) {//将物流和轨迹信息关联起来
                        traces.get(i).setLogisticsInfoId(logisticsInfo.getId());
                    }
                    int insertTraces = tracesMapper.saveTracesByInfo(traces);
                    log.info("插入所有的轨迹信息:结果条数为："+insertTraces);
                    if (insertTraces == traces.size()){
                        returnResult = true;
                        return returnResult;
                    }
                    log.error("轨迹信息插入不完整，结果集："+insertTraces);
                }else {
                    log.error("暂无物流信息，结果集为"+traces);
                }

            }else {
                log.error("物流信息logisticsInfo插入不成功，无法继续更新轨迹信息,结果集："+insertResult);
            }

        }
        return returnResult;
    }
}
