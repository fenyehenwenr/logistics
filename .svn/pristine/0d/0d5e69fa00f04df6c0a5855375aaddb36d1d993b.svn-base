package swd.logistics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import swd.logistics.po.Sender;
import swd.logistics.service.SenderService;
import swd.logistics.utils.Empty;
import swd.logistics.utils.EnumUtils.ReturnCode;
import swd.logistics.utils.ReturnInfor;

@RestController
@RequestMapping("sender")
public class SenderController {

    @Autowired
    private SenderService senderService;

    @RequestMapping(value = "all",method = RequestMethod.GET)
    public ReturnInfor queryAll(){
        ReturnInfor returnInfor = senderService.selectAll();
        return returnInfor;
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    public ReturnInfor insert(@RequestBody Sender sender){
        if (Empty.isNotEmpty(sender.getMobile())){
            ReturnInfor returnInfor = senderService.insert(sender);
            return returnInfor;
        }
        return new ReturnInfor(ReturnCode.PARAMETER_ERROR,"参数校验错误");
    }

    @RequestMapping(value = "update",method = RequestMethod.POST)
    public ReturnInfor update(@RequestBody Sender sender){
        if (Empty.isNotEmpty(sender.getMobile())){
            ReturnInfor returnInfor = senderService.updateByPrimaryKey(sender);
            return returnInfor;
        }
        return new ReturnInfor(ReturnCode.PARAMETER_ERROR,"参数校验错误");
    }

    @RequestMapping(value = "",method = RequestMethod.GET)
    public ReturnInfor queryById(@RequestParam Integer senderId){
        if (Empty.isNotEmpty(senderId)){
            return senderService.selectByPrimaryKey(senderId);
        }
        return new ReturnInfor(ReturnCode.PARAMETER_ERROR,"参数校验失败");
    }
}
