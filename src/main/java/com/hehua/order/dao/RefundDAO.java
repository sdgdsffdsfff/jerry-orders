package com.hehua.order.dao;

import com.hehua.order.model.RefundModel;
import org.apache.ibatis.annotations.*;

import javax.inject.Named;
import java.util.List;

/**
 * Created by liuweiwei on 14-8-9.
 */
@Named
public interface RefundDAO {

    @Insert("insert into `refund` (`userid`,`orderid`,`paytype`,`money`,`recharge`,`direct`,`tradeno`,`outno`,`type`,`method`,`reason`,`status`,`comment`,`deliveryinfo`,`sendtime`,`succtime`,`failtime`,`addtime`,`modtime`,`attributes`) values (#{userid},#{orderid},#{paytype},#{money},#{recharge},#{direct},#{tradeno},#{outno},#{type},#{method},#{reason},#{status},#{comment},#{deliveryinfo},#{sendtime},#{succtime},#{failtime},#{addtime},#{modtime},#{attributes});")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int add(RefundModel refund);

    @Select("select `id`,`userid`,`orderid`,`paytype`,`money`,`recharge`,`direct`,`tradeno`,`outno`,`type`,`method`,`reason`,`status`,`comment`,`deliveryinfo`,`sendtime`,`succtime`,`failtime`,`addtime`,`modtime`,`attributes` from `refund` where `orderid`=#{orderid} order by id desc")
    public List<RefundModel> getListByOrderId(long orderd);

    @Select("select `id`,`userid`,`orderid`,`paytype`,`money`,`recharge`,`direct`,`tradeno`,`outno`,`type`,`method`,`reason`,`status`,`comment`,`deliveryinfo`,`sendtime`,`succtime`,`failtime`,`addtime`,`modtime`,`attributes` from `refund` where `id`=#{id}")
    public RefundModel getById(int id);

    @Update("update `refund` set `status`=#{status} where `id`=#{id}")
    public int updateStatus(@Param("status") int status, @Param("id") int id);

    @Update("update `refund` set `paytype`=#{paytype},`direct`=#{direct},`sendtime`=#{sendtime},`tradeno`=#{tradeno},`outno`=#{outno},`status`=#{status} where `id`=#{id}")
    public int refund2third(RefundModel refund);

    @Select("select `id`,`userid`,`orderid`,`paytype`,`money`,`recharge`,`direct`,`tradeno`,`outno`,`type`,`method`,`reason`,`status`,`comment`,`deliveryinfo`,`sendtime`,`succtime`,`failtime`,`addtime`,`modtime` from `refund` where `tradeno`=#{tradeno} and `outno`=#{outno}")
    public RefundModel getByTradenoAndOutno(@Param("tradeno") String tradeno, @Param("outno") String outno);

    @Update("update `refund` set `status`=#{status},`succtime`=#{succtime},`failtime`=#{failtime},`modtime`=#{modtime} where `id`=#{id}")
    public int refundNotify(RefundModel refund);

    @Update("update `refund` set `attributes`=#{attributes} where `id`=#{id}")
    public int setSended2Paiu(RefundModel m);

    @Update("update `refund` set `deliveryinfo`=#{deliveryinfo} where `id`=#{id}")
    public int updateDeliveryInfo(RefundModel m);

}
