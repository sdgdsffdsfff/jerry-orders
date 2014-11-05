package com.hehua.order.dao;

import com.hehua.order.model.RefundLogModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import javax.inject.Named;
import java.util.List;

/**
 * Created by liuweiwei on 14-8-9.
 */
@Named
public interface RefundLogDAO {

    @Insert("insert into refundlog (`refundid`,`opid`,`type`,`comment`,`addtime`) values (#{refundid},#{opid},#{type},#{comment},#{addtime});")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int add(RefundLogModel refundLog);

    @Select("select `id`,`refundid`,`opid`,`type`,`comment`,`addtime` from `refundlog` where `refundid`=#{refundid} order by id desc")
    public List<RefundLogModel> getListByRefundId(int refundid);
}
