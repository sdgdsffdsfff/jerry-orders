package com.hehua.order.dao;

import com.hehua.order.model.RefundItemsModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.inject.Named;
import java.util.List;

/**
 * Created by liuweiwei on 14-9-22.
 */
@Named
public interface RefundItemsDAO {

    @Insert("insert into `refunditems` (`refundid`,`userid`,`orderid`,`itemid`,`skuid`,`quantity`,`money`,`status`) values (#{refundid},#{userid},#{orderid},#{itemid},#{skuid},#{quantity},#{money},#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int add(RefundItemsModel refundItemsModel);

    @Select("select `id`,`refundid`,`userid`,`orderid`,`itemid`,`skuid`,`quantity`,`money`,`status` from `refunditems` where `orderid`=#{orderid} and `skuid`=#{skuid}")
    public List<RefundItemsModel> getListByOrderIdAndSkuid(@Param("orderid") long orderid, @Param("skuid") long skuid);

    @Select("select `id`,`refundid`,`userid`,`orderid`,`itemid`,`skuid`,`quantity`,`money`,`status` from `refunditems` where `orderid`=#{orderid} and `status`=#{status}")
    public List<RefundItemsModel> getListByOrderIdAndStatus(@Param("orderid") long orderid, @Param("status") int status);

    @Select("select `id`,`refundid`,`userid`,`orderid`,`itemid`,`skuid`,`quantity`,`money`,`status` from `refunditems` where `refundid`=#{refundid}")
    public List<RefundItemsModel> getListByRefundId(@Param("refundid") int refundid);
}
