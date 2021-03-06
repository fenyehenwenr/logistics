package swd.logistics.service;

import swd.logistics.bo.PlacingOrderInfo;
import swd.logistics.po.LogisticsInfo;
import swd.logistics.bo.OrderReturnInfo;
import swd.logistics.utils.ReturnInfor;

public interface LogisticsInfoService {

    LogisticsInfo saveLogisticsInfo(LogisticsInfo logisticsInfo);

    LogisticsInfo getLogisticsInfoByImmediate(String shipperCode, String logisticCode) throws Exception;//即时查询

    OrderReturnInfo placingOrder(PlacingOrderInfo placingOrderInfo)throws Exception;//电子下单

    ReturnInfor cancelOrder(String ShipperCode, String OrderCode, String ExpNo)throws Exception;

    ReturnInfor logisticsSubscribe(PlacingOrderInfo placingOrderInfo)throws Exception;//物流轨迹订阅

    LogisticsInfo userQuery(String logisticCode,String shipperCode);//用户查询
}
