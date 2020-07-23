package swd.logistics.pushInterface;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import swd.logistics.bo.KdniaoConfig;
import swd.logistics.bo.PushLogisticsInfo;
import swd.logistics.bo.PushReturn;
import swd.logistics.po.LogisticsInfo;
import swd.logistics.po.PushLogistics;
import swd.logistics.service.PushLogisticsService;
import swd.logistics.utils.Empty;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("push")
public class PushController {

    @Autowired
    private PushLogisticsService pushLogisticsService;

    @Autowired
    private KdniaoConfig kdniaoConfig;

    Logger log = LoggerFactory.getLogger(this.getClass());

    /////////@RequestBody接json转对象，字段一定不能用private!!!!!
    @RequestMapping(value = "data",method = RequestMethod.POST)
    public PushReturn getPushData(@RequestParam Map<String, String> params){
        log.info("开始接收快递鸟推送数据："+params.toString());
        String data1 = params.get("RequestData");
        log.info("接收到的推送数据为："+data1);
        PushLogisticsInfo pushLogisticsInfo = JSONObject.parseObject(data1, PushLogisticsInfo.class);
        log.info("转为PushLogisticsInfo对象："+pushLogisticsInfo.toString());
        log.info(pushLogisticsInfo.toString());
        PushReturn pushReturn = null;
        PushReturn jxReturn = null;
        if (Empty.isNotEmpty(pushLogisticsInfo.getCount())){
            List<LogisticsInfo> data = pushLogisticsInfo.getData();

            List<LogisticsInfo> jxData = new ArrayList<>();

            List<LogisticsInfo> szData = new ArrayList<>();
            if (Empty.isNotEmpty(data)){
                    for (int i = 0; i <data.size() ; i++) {
                        if (data.get(i).getShipperCode().equals("STO")){
                            jxData.add(data.get(i));
                        }else if (data.get(i).getShipperCode().equals("YD")){
                            szData.add(data.get(i));
                        }
                }
                if (jxData.size() > 0){
                    PushLogisticsInfo jx = new PushLogisticsInfo();
                    jx.setPushTime(pushLogisticsInfo.getPushTime());
                    if (jxData.size() == data.size()){
                        jx.setCount(pushLogisticsInfo.getCount());
                    }else {
                        jx.setCount("同时间，同一次推送拆为了两条");
                    }
                    jx.setData(jxData);
                    jxReturn = pushLogisticsService.savePushDataJxH5(jx);//江西接口
                }
                if (szData.size() > 0){
                    pushLogisticsInfo.setData(szData);
                    pushReturn = pushLogisticsService.savePushData(pushLogisticsInfo);//深圳接口
                }
                String result = "";
                if (pushReturn != null  && !pushReturn.getSuccess()){
                    result = JSON.toJSONString(pushReturn);//深圳失败
                }else if (jxReturn != null && !jxReturn.getSuccess()){
                    result = JSON.toJSONString(jxReturn);//江西失败
                }else {
                    result = JSON.toJSONString(pushReturn == null ? jxReturn : pushReturn);//两个都接收完成，随便选一个结果推回去
                }

                System.out.println(result);
                return pushReturn == null ? jxReturn : pushReturn;
            }
        }
            pushReturn = new PushReturn();
            pushReturn.setUpdateTime(new Date());
            pushReturn.setSuccess(false);
            pushReturn.setReason("推送的数据为空？？？");
            pushReturn.setEBusinessID(kdniaoConfig.EBusinessID);
        String result = JSON.toJSONString(pushReturn);
        System.out.println(result);
        return pushReturn;
    }
}
