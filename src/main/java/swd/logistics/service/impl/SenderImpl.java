package swd.logistics.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swd.logistics.dao.SenderMapper;
import swd.logistics.po.Sender;
import swd.logistics.service.SenderService;
import swd.logistics.utils.Empty;
import swd.logistics.utils.EnumUtils.ReturnCode;
import swd.logistics.utils.ReturnInfor;

import java.util.List;

@Service
public class SenderImpl implements SenderService {
    
    @Autowired
    private SenderMapper senderMapper;
    @Override
    public ReturnInfor insert(Sender record) {
        int result = senderMapper.insert(record);
        if (result > 0 ){
            return new ReturnInfor(ReturnCode.SUCCESS,"新增成功");
        }
        return new ReturnInfor(ReturnCode.FAILED,"新增失败");
    }

    @Override
    public ReturnInfor selectByPrimaryKey(Integer senderId) {
        Sender sender = senderMapper.selectByPrimaryKey(senderId);
        if (Empty.isNotEmpty(sender)){
            return new ReturnInfor(ReturnCode.SUCCESS,sender);
        }
        return new ReturnInfor(ReturnCode.NOT_DATA,"查询为空");
    }

    @Override
    public ReturnInfor selectAll() {
        List<Sender> senders = senderMapper.selectAll();
        return new ReturnInfor(senders);
    }

    @Override
    public ReturnInfor updateByPrimaryKey(Sender record) {
        int result = senderMapper.updateByPrimaryKey(record);
        if (result > 0 ){
            Sender sender = senderMapper.selectByPrimaryKey(record.getSenderId());
            return new ReturnInfor(ReturnCode.SUCCESS,sender);
        }
        return new ReturnInfor(ReturnCode.FAILED,"新增失败");
    }

    @Override
    public Sender byIsDefault(Integer is_default) {
        return senderMapper.byIsDefault(is_default);
    }
}
