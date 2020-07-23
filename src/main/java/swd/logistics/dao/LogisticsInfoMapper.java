package swd.logistics.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import swd.logistics.po.LogisticsInfo;

public interface LogisticsInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(LogisticsInfo record);

    LogisticsInfo selectByPrimaryKey(Long id);

    List<LogisticsInfo> selectAll();

    int updateByPrimaryKey(LogisticsInfo record);

    @Select("select * from t_logistics_info where LogisticCode = #{LogisticCode}")
    LogisticsInfo ByLogisticsCode(String LogisticCode);

    int updateByLogisticsCode(LogisticsInfo logisticsInfo);

    LogisticsInfo ByLogisticsCodeInfo(String LogisticCode);
}