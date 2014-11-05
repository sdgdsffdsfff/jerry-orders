package com.hehua.order.dao;

import com.hehua.order.model.NotifySuccessLogModel;
import org.apache.ibatis.annotations.*;

import javax.inject.Named;
import java.util.List;

/**
 * Created by liuweiwei on 14-8-14.
 */
@Named
public interface NotifySuccessLogDAO {

    @Insert("insert into `notifysuccesslog` (`id`,`userid`,`orderid`,`outno`,`type`,`paytype`,`money`,`tradeno`,`buyer`,`paytime`,`addtime`,`modtime`,`data`,`refund2third`,`refund2credit`) values (#{id},#{userid},#{orderid},#{outno},#{type},#{paytype},#{money},#{tradeno},#{buyer},#{paytime},#{addtime},#{modtime},#{data},#{refund2third},#{refund2credit})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn="id")
    public int add(NotifySuccessLogModel n);

    @Select("select `id`,`userid`,`orderid`,`outno`,`type`,`paytype`,`money`,`tradeno`,`buyer`,`paytime`,`addtime`,`modtime`,`data`,`refund2third`,`refund2credit` from `notifysuccesslog` where `outno`=#{outno} and `paytype`=#{paytype}")
    public NotifySuccessLogModel getByOutnoAndPaytype(@Param("outno") String outno, @Param("paytype") int paytype);

    @Select("select `id`,`userid`,`orderid`,`outno`,`type`,`paytype`,`money`,`tradeno`,`buyer`,`paytime`,`addtime`,`modtime`,`data`,`refund2third`,`refund2credit` from `notifysuccesslog` where `orderid`=#{orderid} order by id desc")
    public List<NotifySuccessLogModel> getByOrderId(@Param("orderid") long orderid);

    @Update("update `notifysuccesslog` set `refund2third`=#{refund2third}, `modtime`=#{modtime} where `id`=#{id}")
    public int refund2third(NotifySuccessLogModel m);
}
