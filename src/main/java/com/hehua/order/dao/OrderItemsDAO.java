package com.hehua.order.dao;

import com.hehua.order.model.OrderItemsModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.inject.Named;
import java.util.Collection;
import java.util.List;

/**
 * Created by liuweiwei on 14-7-26.
 */
@Named
public interface OrderItemsDAO {

    @Insert("insert into `orderitems` (`userid`,`orderid`,`itemid`,`skuid`,`quantity`,`buyprice`,`saleprice`) values (#{userid},#{orderid},#{itemid},#{skuid},#{quantity},#{buyprice},#{saleprice})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int createOrderItems(OrderItemsModel orderitems);

    @Select("select `id`,`userid`,`orderid`,`itemid`,`skuid`,`quantity`,`buyprice`,`saleprice` from `orderitems` where `orderid`=#{orderid}")
    public List<OrderItemsModel> getListByOrderId(long orderid);

    @Select("select `id`,`userid`,`orderid`,`itemid`,`skuid`,`quantity`,`buyprice`,`saleprice` from `orderitems` where `orderid`=#{orderid} and `skuid`=#{skuid}")
    public OrderItemsModel getByOrderIdAndSkuId(@Param("orderid") long orderid, @Param("skuid") long skuid);
    
    @Select("select `id`,`userid`,`orderid`,`itemid`,`skuid`,`quantity`,`buyprice`,`saleprice` from `orderitems` where `userid` =#{userid} and `itemid`=#{itemid}")
    public List<OrderItemsModel> getListByUserIdAndItemId(@Param("userid")long userid, @Param("itemid")long itemid);

    public List<Long> getListByItemIds(@Param("ids")Collection<Long> ids);

    public List<OrderItemsModel> getListByOrderIds(@Param("ids")Collection<Long> ids);

}
