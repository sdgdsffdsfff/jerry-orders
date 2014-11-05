package com.hehua.freeorder.dao;

import com.hehua.freeorder.model.FreeOrderLogModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import javax.inject.Named;
import java.util.List;

/**
 * Created by liuweiwei on 14-10-7.
 */
@Named
public interface FreeOrderLogDAO {

    @Insert("insert into `freeorderlog` (`freeOrderId`,`opid`,`type`,`comment`,`addtime`) values (#{freeOrderId},#{opid},#{type},#{comment},#{addtime});")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int add(FreeOrderLogModel refundLog);

    @Select("select `id`,`freeOrderId`,`opid`,`type`,`comment`,`addtime` from `freeorderlog` where `freeOrderId`=#{freeOrderId} order by id desc")
    public List<FreeOrderLogModel> getListByFreeOrderId(int freeOrderId);
}
