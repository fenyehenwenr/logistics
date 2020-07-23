package swd.logistics.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swd.logistics.dao.PlacingOrderTemplateMapper;
import swd.logistics.po.PlacingOrderTemplate;
import swd.logistics.service.PlacingOrderTemplateService;

import java.util.List;

@Service
public class PlacingOrderTemplateImpl implements PlacingOrderTemplateService {

    @Autowired
    private PlacingOrderTemplateMapper placingOrderTemplateMapper;
    @Override
    public int insert(PlacingOrderTemplate record) {
        return 0;
    }

    @Override
    public PlacingOrderTemplate selectByPrimaryKey(Long id) {
        return placingOrderTemplateMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<PlacingOrderTemplate> selectAll() {
        return placingOrderTemplateMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(PlacingOrderTemplate record) {
        return placingOrderTemplateMapper.updateByPrimaryKey(record);
    }

    @Override
    public PlacingOrderTemplate ByOrderId(String orderId) {
        return placingOrderTemplateMapper.ByOrderId(orderId);
    }

    @Override
    public int updateByOrderId(PlacingOrderTemplate record) {
        return placingOrderTemplateMapper.updateByOrderId(record);
    }
}
