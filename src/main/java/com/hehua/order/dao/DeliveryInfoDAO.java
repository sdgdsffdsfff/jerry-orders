package com.hehua.order.dao;

import com.hehua.order.info.DeliveryInfo;
import com.hehua.order.model.DeliveryInfoModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import javax.inject.Named;
import java.util.Collection;
import java.util.List;

/**
 * Created by liuweiwei on 14-8-4.
 */
@Named
public interface DeliveryInfoDAO {
    @Insert("insert into `deliveryinfo` (`orderid`, `address`, `invoicetype`, `invoicecomment`, `deliverycomment`) values (#{orderid},#{address},#{invoicetype},#{invoicecomment},#{deliverycomment})")
    public int add(DeliveryInfoModel deliveryinfo);

    @Select("select `orderid`, `address`, `invoicetype`, `invoicecomment`, `deliverycomment` from `deliveryinfo` where `orderid`=#{orderid}")
    public DeliveryInfoModel getByOrderId(long orderid);

    @Update("update `deliveryinfo` set `address`=#{address},`invoicetype`=#{invoicetype},`invoicecomment`=#{invoicecomment} where `orderid`=#{orderid}")
    public int update(DeliveryInfoModel deliveryInfoModel);

    public List<DeliveryInfoModel> getListByOrderIds(@Param("ids") Collection<Long> ids);
}
