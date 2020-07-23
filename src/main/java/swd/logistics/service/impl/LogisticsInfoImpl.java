package swd.logistics.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import swd.logistics.bo.*;
import swd.logistics.dao.LogisticsInfoMapper;
import swd.logistics.dao.PlacingOrderTemplateMapper;
import swd.logistics.dao.TracesMapper;
import swd.logistics.logisticsUtils.CustomerUtil;
import swd.logistics.logisticsUtils.SendUtil;
import swd.logistics.po.LogisticsInfo;
import swd.logistics.po.Traces;
import swd.logistics.service.LogisticsInfoService;
import swd.logistics.utils.Empty;
import swd.logistics.utils.EnumUtils.ReturnCode;
import swd.logistics.utils.ReturnInfor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LogisticsInfoImpl implements LogisticsInfoService {

    @Autowired
    private TracesMapper tracesMapper;
    @Autowired
    private LogisticsInfoMapper logisticsInfoMapper;
    @Autowired
    private KdniaoConfig kdniaoConfig;
    @Autowired
    private SendUtil sendUtil;
    @Autowired
    private CustomerUtil customerUtil;


    Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 查询保存
     * @param logisticsInfo
     * @return
     */
    @Override
    public LogisticsInfo saveLogisticsInfo(LogisticsInfo logisticsInfo) {
        LogisticsInfo returnResult = null;
        try {
            LogisticsInfo isHas = logisticsInfoMapper.ByLogisticsCode(logisticsInfo.getLogisticCode());
            if (Empty.isNotEmpty(isHas)){//数据库里是否已有。（已推送过或者即时查询过）
                log.info("走更新逻辑");
                isHas.setState(logisticsInfo.getState());//更新物流信息状态
                isHas.setStateEx(logisticsInfo.getStateEx());
                int updateResult = logisticsInfoMapper.updateByLogisticsCode(isHas);
                if (updateResult > 0) {//没有很好的更新轨迹方法。所以先删除轨迹信息，再插入
                    int deleteResult = tracesMapper.deleteBylogisticsInfoId(isHas.getId());
                    List<Traces> traces = logisticsInfo.getTraces();
                    if(traces!= null && traces.size() > 0){
                        for (int i = 0; i < traces.size() ; i++) {//将物流和轨迹信息关联起来
                            traces.get(i).setLogisticsInfoId(isHas.getId());
                        }
                        int insertResult = tracesMapper.saveTracesByInfo(traces);
                        log.info("插入所有的轨迹信息:结果条数为："+insertResult);
                        if (insertResult == logisticsInfo.getTraces().size()){
                            returnResult = logisticsInfo;
                        }
                    }else {
                        log.error("暂无物流信息，结果集为："+traces);
                    }

                }else {
                    log.error("更新物流信息失败，updateResult结果集为："+updateResult);
                }
            }else { //插入逻辑
                log.info("走新增逻辑");
                int insertResult = logisticsInfoMapper.insert(logisticsInfo);
                if (insertResult > 0) {//先插入这条物流信息，成功后再去更新轨迹

                        List<Traces> traces = logisticsInfo.getTraces();
                    if(traces!= null && traces.size() > 0) {
                        for (int i = 0; i < traces.size(); i++) {//将物流和轨迹信息关联起来
                            traces.get(i).setLogisticsInfoId(logisticsInfo.getId());
                        }
                        int insertTraces = tracesMapper.saveTracesByInfo(traces);
                        log.info("插入所有的轨迹信息:结果条数为：" + insertTraces);
                        if (insertTraces == traces.size()) {
                            returnResult = logisticsInfo;
                            return returnResult;
                        }
                        log.error("轨迹信息插入不完整，结果集：" + insertTraces);
                    }else {
                        log.error("暂无物流信息，结果集为："+traces);
                    }
                }else {
                    log.error("物流信息logisticsInfo插入不成功，无法继续更新轨迹信息,结果集："+insertResult);
                }
            }
        }catch (Exception e){
            log.error("执行异常："+e.getMessage());
        }

        return returnResult;
    }

    /**
     * 实时查询
     * @param shipperCode
     * @param logisticCode
     * @return
     * @throws Exception
     */
    @Override
    public LogisticsInfo getLogisticsInfoByImmediate(String shipperCode, String logisticCode) throws Exception {//即时查询

        String requestData= "{'OrderCode':'','ShipperCode':'" + shipperCode + "','LogisticCode':'" + logisticCode + "'}";
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("RequestData", sendUtil.urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", kdniaoConfig.EBusinessID);
        params.put("RequestType", "8001");//8001是增值版（充钱），1002免费
        String dataSign=sendUtil.encrypt(requestData, kdniaoConfig.AppKey, "UTF-8");
        params.put("DataSign", sendUtil.urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");

        String result = sendUtil.sendPost(kdniaoConfig.cx, params);
        log.info("实时查询返回："+result);
        LogisticsInfo logisticsInfo = JSONObject.parseObject(result, LogisticsInfo.class);
        log.info(logisticsInfo.toString());
        LogisticsInfo info = saveLogisticsInfo(logisticsInfo);
        if (Empty.isNotEmpty(info)){
            return logisticsInfo;
        }
        return null;
    }

    /**
     * 下面单
     * @param placingOrderInfo
     * @return
     * @throws Exception
     */
    @Override
    public OrderReturnInfo placingOrder(PlacingOrderInfo placingOrderInfo) throws Exception {//下电子面单
        Customer customer = customerUtil.getCustomer(placingOrderInfo.getShipperCode());
        if(placingOrderInfo.getReceiver().getPostCode()==null){
            placingOrderInfo.getReceiver().setPostCode("");
        }
        log.info("下单接口物流客户信息："+customer.toString());
       // String oldOrderCode = placingOrderInfo.getCommodity().getOrderCode();
       // placingOrderInfo.getCommodity().setOrderCode(oldOrderCode.substring(2));
        String requestData= "{'OrderCode': '"+placingOrderInfo.getCommodity().getOrderCode()+"'," +
                "'ShipperCode':'"+placingOrderInfo.getShipperCode()+"'," +
                "'Callback':'"+placingOrderInfo.getCommodity().getOrderCode()+"'," +
                "'Cost':"+placingOrderInfo.getCommodity().getCost()+"," +
                "'OtherCost':"+placingOrderInfo.getOtherCost()+"," +
                "'CustomArea':'"+placingOrderInfo.getCommodity().getGoodsName()+"'," +
                "'Sender':" +
                "{" +
                "'Name':'"+placingOrderInfo.getSender().getName()+"','Mobile':'"+placingOrderInfo.getSender().getMobile()+"','ProvinceName':'"+placingOrderInfo.getSender().getProvinceName()+"','CityName':'"+placingOrderInfo.getSender().getCityName()+"','ExpAreaName':'"+placingOrderInfo.getSender().getExpAreaName()+"','Address':'"+placingOrderInfo.getSender().getAddress()+"','PostCode':'"+placingOrderInfo.getSender().getPostCode()+"'}," +
                "'Receiver':" +
                "{" +
                "'Name':'"+placingOrderInfo.getReceiver().getName()+"','Mobile':'"+placingOrderInfo.getReceiver().getTel()+"','ProvinceName':'"+placingOrderInfo.getReceiver().getProvince()+"','CityName':'"+placingOrderInfo.getReceiver().getCity()+"','ExpAreaName':'"+placingOrderInfo.getReceiver().getDistrict()+"','Address':'"+placingOrderInfo.getReceiver().getAddressDetail()+"','PostCode':'"+placingOrderInfo.getReceiver().getPostCode()+"'}," +
                "'Commodity':" +
                "[{" +
                "'GoodsName':'"+placingOrderInfo.getCommodity().getGoodsName()+"','Goodsquantity':"+placingOrderInfo.getCommodity().getGoodsquantity()+"}]," +
                /*"'Weight':1.0," +
                "'Quantity':1," +
                "'Volume':0.0," +*/
                "'Remark':'"+placingOrderInfo.getRemark()+"'," +
                "'CustomerName':'"+customer.getCustomerName()+"'," +
                "'CustomerPwd':'"+customer.getCustomerPwd()+"'," +
                "'SendSite':'"+customer.getSendSite()+"'," +
                "'TemplateSize':'"+customer.getTemplateSize()+"'," +
                "'PayType':"+customer.getPayType()+"," +
                "'ExpType':'"+customer.getExpType()+"'," +
                "'IsReturnPrintTemplate':"+placingOrderInfo.getIsReturnPrintTemplate()+"}";
                log.info(requestData);
        Map<String, String> params = new HashMap<String, String>();
        params.put("RequestData", sendUtil.urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", kdniaoConfig.EBusinessID);
        params.put("RequestType", "1007");
        String dataSign=sendUtil.encrypt(requestData, kdniaoConfig.AppKey, "UTF-8");
        params.put("DataSign", sendUtil.urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");

        String result=sendUtil.sendPost(kdniaoConfig.xd, params);

        //根据公司业务处理返回的信息......
        log.info("已返回******:"+result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        Boolean success = (Boolean)jsonObject.get("Success");
        OrderReturnInfo orderReturnInfo = null;
        if (success){
            log.info("下单成功，开始处理返回数据插入数据库");
            orderReturnInfo = JSONObject.parseObject(result, OrderReturnInfo.class);
            /*log.info("插入前查询数据库是不是已有数据，有的话，则更新模板");
            PlacingOrderTemplate template = placingOrderTemplateMapper.ByOrderId(oldOrderCode);
            if (Empty.isNotEmpty(template)){
                template.setPrinttemplate(orderReturnInfo.getPrintTemplate());
                template.setUpdateTime(new Date());
                int result1 = placingOrderTemplateMapper.updateByOrderId(template);
                if (result1 <= 0){
                    log.info("下单成功，但是保存数据失败，请排查");
                }
                log.info("更新数据成功");
            }else {
                log.info("该单号第一次下电子单，插入数据库");
                orderReturnInfo.getPrintTemplate().replace('\"', '"');
                template = new PlacingOrderTemplate();
                template.setOrderId(oldOrderCode);
                template.setPrinttemplate(orderReturnInfo.getPrintTemplate());
                template.setUpdateTime(new Date());
                placingOrderTemplateMapper.insert(template);
            }
                log.info("业务系统订单号:" + template.getOrderId() + ";;电子模板：" + template.getPrinttemplate());
*/
        }

        return orderReturnInfo;
    }

    /**
     * 取消面单
     * @param ShipperCode
     * @param OrderCode
     * @param ExpNo
     * @return
     * @throws Exception
     */
    @Override
    public ReturnInfor cancelOrder(String ShipperCode, String OrderCode, String ExpNo) throws Exception{//取消电子面单
        Customer customer = customerUtil.getCustomer(ShipperCode);
        log.info("取消下单接口物流客户信息："+customer.toString());
        String requestData= "'ShipperCode':'"+ShipperCode+"'," +
                "'OrderCode':'"+OrderCode+"',"+
                "'ExpNo':'"+ExpNo+"'," +
                "'CustomerName':"+customer.getCustomerName()+"',"+
                "'CustomerPwd':"+customer.getCustomerPwd()+"',";
        Map<String, String> params = new HashMap<String, String>();
        params.put("RequestData", sendUtil.urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", kdniaoConfig.EBusinessID);
        params.put("RequestType", "1147");
        String dataSign=sendUtil.encrypt(requestData, kdniaoConfig.AppKey, "UTF-8");
        params.put("DataSign", sendUtil.urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");

        String result=sendUtil.sendPost(kdniaoConfig.cancelXd, params);
        JSONObject jsonObject = JSONObject.parseObject(result);
        Boolean success = (Boolean)jsonObject.get("Success");
        if (success){
            return new ReturnInfor(Integer.parseInt(jsonObject.getString("ResultCode") ),"取消成功",null);
        }
        return new ReturnInfor(Integer.parseInt(jsonObject.getString("ResultCode") ),jsonObject.getString("Reason"),"取消失败");
    }

    /**
     * 重新订阅
     * @param placingOrderInfo
     * @return
     * @throws Exception
     */
    @Override
    public ReturnInfor logisticsSubscribe(PlacingOrderInfo placingOrderInfo) throws Exception {//物流信息订阅

        Customer customer = customerUtil.getCustomer(placingOrderInfo.getShipperCode());
        String MonthCode ="330052000043";//月结编码
        log.info("开始订阅");
        log.info("330052000043");
        log.info("订阅接口物流客户信息："+customer.toString());
        String requestData= "{'OrderCode': '"+placingOrderInfo.getCommodity().getOrderCode()+"'," +
                "'ShipperCode':'"+placingOrderInfo.getShipperCode()+"'," +
                "'LogisticCode':'"+placingOrderInfo.getLogisticCode()+"'," +
                "'Cost':"+placingOrderInfo.getCommodity().getCost()+"," +
                "'OtherCost':"+placingOrderInfo.getOtherCost()+"," +
               "'MonthCode':"+MonthCode+"," +
                "'Sender':" +
                "{" +
                "'Name':'"+placingOrderInfo.getSender().getName()+"','Mobile':'"+placingOrderInfo.getSender().getMobile()+"','ProvinceName':'"+placingOrderInfo.getSender().getProvinceName()+"','CityName':'"+placingOrderInfo.getSender().getCityName()+"','ExpAreaName':'"+placingOrderInfo.getSender().getExpAreaName()+"','Address':'"+placingOrderInfo.getSender().getAddress()+"'}," +
                "'Receiver':" +
                "{" +
                "'Name':'"+placingOrderInfo.getReceiver().getName()+"','Mobile':'"+placingOrderInfo.getReceiver().getTel()+"','ProvinceName':'"+placingOrderInfo.getReceiver().getProvince()+"','CityName':'"+placingOrderInfo.getReceiver().getCity()+"','ExpAreaName':'"+placingOrderInfo.getReceiver().getDistrict()+"','Address':'"+placingOrderInfo.getReceiver().getAddressDetail()+"'}," +
                "'Commodity':" +
                "[{" +
                "'GoodsName':'"+placingOrderInfo.getCommodity().getGoodsName()+"','Goodsquantity':"+placingOrderInfo.getCommodity().getGoodsquantity()+"}]," +
                /*"'Weight':1.0," +
                "'Quantity':1," +
                "'Volume':0.0," +*/
                "'Remark':'"+placingOrderInfo.getRemark()+"'," +
                "'CustomerName':'"+customer.getCustomerName()+"'," +
                "'CustomerPwd':'"+customer.getCustomerPwd()+"'," +
                "'SendSite':'"+customer.getSendSite()+"'," +
                "'TemplateSize':'"+customer.getTemplateSize()+"'," +
                "'PayType':"+customer.getPayType()+"," +
                "'ExpType':'1'," +
                "'IsReturnPrintTemplate':"+placingOrderInfo.getIsReturnPrintTemplate()+"}";
        Map<String, String> params = new HashMap<String, String>();
        params.put("RequestData", sendUtil.urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", kdniaoConfig.EBusinessID);
        params.put("RequestType", "1008");
        String dataSign=sendUtil.encrypt(requestData, kdniaoConfig.AppKey, "UTF-8");
        params.put("DataSign", sendUtil.urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");

        String result=sendUtil.sendPost(kdniaoConfig.dy, params);

        //根据公司业务处理返回的信息......
        log.info("已返回******:"+result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        Boolean success = (Boolean)jsonObject.get("Success");
        if (success){
            return new ReturnInfor(ReturnCode.SUCCESS.getCode(),jsonObject.getString("UpdateTime"),"预约成功");
        }
        return new ReturnInfor(ReturnCode.FAILED.getCode(),jsonObject.getString("UpdateTime"),"预约失败："+jsonObject.getString("Reason"));
    }


    /**
     * 用户查询
     * @param logisticCode
     * @param shipperCode
     * @return
     */
    @Override
    public LogisticsInfo userQuery(String logisticCode,String shipperCode){//专门为用户查询设计
        log.info("用户查询物流信息，参数：运单号："+logisticCode+";;快递编码："+shipperCode);
        LogisticsInfo info1 = null;
        try {
            info1 = logisticsInfoMapper.ByLogisticsCodeInfo(logisticCode);
            if (Empty.isNotEmpty(info1)){//如果不等于空，就返回数据库查询数据。
                log.info("运单号:"+logisticCode+"用户查询成功，返回信息如下："+info1.toString());
                return info1;
            }
            log.info("数据库里没有查询到，现走即时查询接口");
            info1 = this.getLogisticsInfoByImmediate(shipperCode, logisticCode);
            return info1;
        }catch (Exception e){
            info1 = new LogisticsInfo();
            info1.setReason("查询为空，暂无物流信息");
            info1.setState("-1");
            log.error("该运单号："+logisticCode+"用户查询出现异常，异常信息如下:"+e.getMessage());
            return info1;
        }
    }
}
