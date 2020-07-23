package swd.logistics.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import swd.logistics.po.Sender;

public interface SenderMapper {
    int deleteByPrimaryKey(Integer senderId);

    int insert(Sender record);

    Sender selectByPrimaryKey(Integer senderId);

    List<Sender> selectAll();

    int updateByPrimaryKey(Sender record);

    @Select("select * from t_sender where is_default=#{is_default}")
    Sender byIsDefault(Integer is_default);
}