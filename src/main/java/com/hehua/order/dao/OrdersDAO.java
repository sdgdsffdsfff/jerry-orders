package com.hehua.order.dao;

import com.hehua.order.info.OrderStatusCountInfo;
import com.hehua.order.model.OrdersModel;
import org.apache.ibatis.annotations.*;

import javax.inject.Named;
import java.util.Collection;
import java.util.List;

/**
 * Created by liuweiwei on 14-7-23.
 */
@Named
public interface OrdersDAO {

    @Insert("insert into `orders` (`userid`,`totalfee`,`orderfee`,`deliveryfee`,`rebate`,`giftcard`,`recharge`,`direct`,`payed`,`reduce`,`credit`,`outmoney`,`mobile`,`ordertime`,`orderip`,`paytime`,`paytype`,`status`,`attributes`) values (#{userid},#{totalfee},#{orderfee},#{deliveryfee},#{rebate},#{giftcard},#{recharge},#{direct},#{payed},#{reduce},#{credit},#{outmoney},#{mobile},#{ordertime},#{orderip},#{paytime},#{paytype},#{status},#{attributes})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int createOrder(OrdersModel order);

    @Select("select `id`,`userid`,`totalfee`,`orderfee`,`deliveryfee`,`rebate`,`giftcard`,`recharge`,`direct`,`payed`,`reduce`,`credit`,`outmoney`,`mobile`,`ordertime`,`orderip`,`paytime`,`paytype`,`status`,`attributes` from `orders` where `id`=#{id}")
    public OrdersModel getById(long id);

    @Select("select 1 from `orders` where `id`=#{id}")
    public Integer hasWithId(long id);

    @Select("select `id`,`userid`,`totalfee`,`orderfee`,`deliveryfee`,`rebate`,`giftcard`,`recharge`,`direct`,`payed`,`reduce`,`credit`,`outmoney`,`mobile`,`ordertime`,`orderip`,`paytime`,`paytype`,`status`,`attributes` from `orders` where `userid`=#{id} and `status`=#{status} order by `id` desc limit #{offset}, #{limit}")
    public List<OrdersModel> getListByUserId(@Param("id")long userid, @Param("status") int status, @Param("offset") int offset, @Param("limit") int limit);

    @Select("select `id`,`userid`,`totalfee`,`orderfee`,`deliveryfee`,`rebate`,`giftcard`,`recharge`,`direct`,`payed`,`reduce`,`credit`,`outmoney`,`mobile`,`ordertime`,`orderip`,`paytime`,`paytype`,`status`,`attributes` from `orders` where `userid`=#{id} order by `id` desc limit #{offset}, #{limit}")
    public List<OrdersModel> getAllByUserId(@Param("id")long userid, @Param("offset") int offset, @Param("limit") int limit);

    @Update("update `orders` set `status`=#{status} where `id`=#{id}")
    public int updateStatus(@Param("id") long orderid, @Param("status") int status);

    @Select("select count(`id`) as `count`,`status` from `orders` where userid=#{userid} group by `status`")
    public List<OrderStatusCountInfo> getCountByStatus(long userid);

    @Update("update `orders` set `rebate`=#{rebate},`giftcard`=#{giftcard},`recharge`=#{recharge},`direct`=#{direct},`payed`=#{payed},`reduce`=#{reduce},`credit`=#{credit},`outmoney`=#{outmoney},`paytime`=#{paytime},`paytype`=#{paytype},`status`=32 where `id`=#{id}")
    public int setPayed(OrdersModel order);

    @Update("update `orders` set `direct`=#{direct},`payed`=#{payed},`outmoney`=#{outmoney},`paytime`=#{paytime},`paytype`=#{paytype} where `id`=#{id}")
    public int updatePayed(OrdersModel order);

    @Update("update `orders` set `status`=#{status}, `attributes`=#{attributes} where `id`=#{id}")
    public int setDeliveried(OrdersModel order);

    @Update("update `orders` set `attributes`=#{attributes} where `id`=#{id}")
    public int updateAttr(OrdersModel order);

    @Select("select `id`,`userid`,`totalfee`,`orderfee`,`deliveryfee`,`rebate`,`giftcard`,`recharge`,`direct`,`payed`,`reduce`,`credit`,`outmoney`,`mobile`,`ordertime`,`orderip`,`paytime`,`paytype`,`status`,`attributes` from `orders` where `status`=0 and (unix_timestamp() - `ordertime`>#{sec})")
    public List<OrdersModel> getToBeClosedOrders(@Param("sec") int sec);

    @Select("select `id`,`userid`,`totalfee`,`orderfee`,`deliveryfee`,`rebate`,`giftcard`,`recharge`,`direct`,`payed`,`reduce`,`credit`,`outmoney`,`mobile`,`ordertime`,`orderip`,`paytime`,`paytype`,`status`,`attributes` from `orders` where `ordertime` between #{begin} and #{end}")
    public List<OrdersModel> getOrdersByOrdertime(@Param("begin") int begin, @Param("end") int end);

    @Select("select `id`,`userid`,`totalfee`,`orderfee`,`deliveryfee`,`rebate`,`giftcard`,`recharge`,`direct`,`payed`,`reduce`,`credit`,`outmoney`,`mobile`,`ordertime`,`orderip`,`paytime`,`paytype`,`status`,`attributes` from `orders` order by id desc limit #{offset}, #{limit}")
    public List<OrdersModel> getAll(@Param("offset") int offset, @Param("limit") int limit);

    @Select("select count(`id`) from `orders`")
    public Integer getAllCount();

    @Select("select count(`id`) from `orders` where `status`=#{status}")
    public Integer getCountByOrderStatus(@Param("status") int status);

    @Select("select `id`,`userid`,`totalfee`,`orderfee`,`deliveryfee`,`rebate`,`giftcard`,`recharge`,`direct`,`payed`,`reduce`,`credit`,`outmoney`,`mobile`,`ordertime`,`orderip`,`paytime`,`paytype`,`status`,`attributes` from `orders` where `status`=#{status} order by id desc limit #{offset}, #{limit}")
    public List<OrdersModel> getByStatus(@Param("status") int status, @Param("offset") int offset, @Param("limit") int limit);
    
    public List<OrdersModel> getsByIds(@Param("ids") Collection<Long> orderIds);

    @Select("select `id`,`userid`,`totalfee`,`orderfee`,`deliveryfee`,`rebate`,`giftcard`,`recharge`,`direct`,`payed`,`reduce`,`credit`,`outmoney`,`mobile`,`ordertime`,`orderip`,`paytime`,`paytype`,`status`,`attributes` from `orders` where `id` between #{startPos} and #{endPos} and `status`=#{status} limit 2000")
    public List<OrdersModel> getByStatusAndOrderIdRange(@Param("status") int status, @Param("startPos") int startPos, @Param("endPos") int endPos);

    public List<OrdersModel> getByParamsBy(@Param("startTime") int startTime, @Param("endTime") int endTime,
                                           @Param("minPrice") int minPrice, @Param("maxPrice") int maxPrice,
                                           @Param("startPos") int startPos, @Param("endPos") int endPos);

    public Integer getCountParmsBy(@Param("startTime") int startTime, @Param("endTime") int endTime,
                                   @Param("minPrice") int minPrice, @Param("maxPrice") int maxPrice);


}
