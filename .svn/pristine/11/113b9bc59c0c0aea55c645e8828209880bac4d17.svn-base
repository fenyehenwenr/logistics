package swd.logistics.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import swd.logistics.po.Traces;

public interface TracesMapper {
    int deleteByPrimaryKey(Long tracesId);

    int insert(Traces record);

    Traces selectByPrimaryKey(Long tracesId);

    List<Traces> selectAll();

    int updateByPrimaryKey(Traces record);

    int saveTracesByInfo(List<Traces> tracesList);

    @Delete("delete from t_traces where logistics_info_id=#{logisticsInfoId}")
    int deleteBylogisticsInfoId(Long logisticsInfoId);

}