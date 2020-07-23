package swd.logistics.service;

import swd.logistics.bo.PushLogisticsInfo;
import swd.logistics.bo.PushReturn;
import swd.logistics.po.PushLogistics;

import java.util.List;

public interface PushLogisticsService {

    int deleteByPrimaryKey(Long id);

    int insert(PushLogistics record);

    PushLogistics selectByPrimaryKey(Long id);

    List<PushLogistics> selectAll();

    int updateByPrimaryKey(PushLogistics record);

    public PushReturn savePushData(PushLogisticsInfo pushLogisticsInfo);

    public PushReturn savePushDataJxH5(PushLogisticsInfo pushLogisticsInfo);
}
