package com.hehua.order.dao;

import com.hehua.order.model.OrderTraceModel;
import org.apache.ibatis.annotations.*;

/**
 * Created by liuweiwei on 14-8-23.
 */
public interface OrderTraceDAO {

    @Select("select `id`,`orderid`,`deliveryCompanPinyin`,`deliveryNum`,`status`,`type`,`data`,`querytimes`,`lastqtime`,`addtime`,`modtime` from `ordertrace` where `orderid`=#{orderid} and `type`=#{type}")
    public OrderTraceModel getByOrderIdAndType(@Param("orderid") long orderid, @Param("type") int type);

    @Select("select `id`,`orderid`,`deliveryCompanPinyin`,`deliveryNum`,`status`,`type`,`data`,`querytimes`,`lastqtime`,`addtime`,`modtime` from `ordertrace` where `id`=#{id}")
    public OrderTraceModel getById(int id);

    @Insert("insert into `ordertrace` (`id`,`orderid`,`deliveryCompanPinyin`,`deliveryNum`,`status`,`type`,`data`,`querytimes`,`lastqtime`,`addtime`,`modtime`) values (#{id},#{orderid},#{deliveryCompanPinyin},#{deliveryNum},#{status},#{type},#{data},#{querytimes},#{lastqtime},#{addtime},#{modtime});")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int add(OrderTraceModel m);

    @Update("update `ordertrace` set `status`=#{status},`data`=#{data},`lastqtime`=#{lastqtime},`querytimes`=#{querytimes},`modtime`=#{modtime} where `id`=#{id}")
    public int queryKuaidiApi(OrderTraceModel m);

    @Update("update `ordertrace` set `status`=128 where `id`=#{id}")
    public int setDeleted(int id);

    @Update("update `ordertrace` set `deliveryCompanPinyin`=#{deliveryCompanPinyin},`deliveryNum`=#{deliveryNum},`modtime`=#{modtime} where `id`=#{id}")
    public int updateDeliveryInfo(OrderTraceModel m);
}
