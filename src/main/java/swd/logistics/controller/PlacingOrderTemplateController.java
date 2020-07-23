package swd.logistics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swd.logistics.po.PlacingOrderTemplate;
import swd.logistics.service.PlacingOrderTemplateService;
import swd.logistics.utils.Empty;
import swd.logistics.utils.EnumUtils.ReturnCode;
import swd.logistics.utils.ReturnInfor;

import java.util.List;

@RestController
@RequestMapping("template")
public class PlacingOrderTemplateController {
    @Autowired
    private PlacingOrderTemplateService templateService;


    @RequestMapping(value = "byOrderId",method = RequestMethod.GET)
    public ReturnInfor selectByOrderId(@RequestParam("orderId") String orderId){
        if (Empty.isNotEmpty(orderId)){
            PlacingOrderTemplate template = templateService.ByOrderId(orderId);
            if (Empty.isNotEmpty(template)){
                return new ReturnInfor(ReturnCode.SUCCESS,template);
            }
            return new ReturnInfor(ReturnCode.NOT_DATA,"没有查询到下单信息，请核查是否下单");
        }
        return new ReturnInfor(ReturnCode.PARAMETER_ERROR,null);
    }

    @RequestMapping(value = "all",method = RequestMethod.GET)
    public ReturnInfor selectAll(){
        List<PlacingOrderTemplate> placingOrderTemplates = templateService.selectAll();
        if (Empty.isNotEmpty(placingOrderTemplates)){
                return new ReturnInfor(ReturnCode.SUCCESS,placingOrderTemplates);
            }
            return new ReturnInfor(ReturnCode.NOT_DATA,"没有查询到下单信息，请核查是否下单");
        }
}
