package com.hehua.order.dao;

import com.hehua.order.model.CartModel;
import org.apache.ibatis.annotations.*;

import javax.inject.Named;
import java.util.List;

/**
 * Created by liuweiwei on 14-7-27.
 */
@Named
public interface CartDAO {
    @Insert("insert into `cart` (`userid`,`itemid`,`skuid`,`quantity`,`tid`,`addtime`,`modtime`) values (#{userid},#{itemid},#{skuid},#{quantity},#{tid},#{addtime},#{modtime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int add2Cart(CartModel cartitem);

    @Select("select `id`,`userid`,`itemid`,`skuid`,`quantity`,`tid`,`addtime`,`modtime` from `cart` where `userid`=#{userid}")
    public List<CartModel> getListByUserId(long userid);

    @Select("select `id`,`userid`,`itemid`,`skuid`,`quantity`,`tid`,`addtime`,`modtime` from `cart` where `userid`=#{userid} and `skuid`=#{skuid}")
    public CartModel getByUserIdAndSkuId(@Param("userid")long userid, @Param("skuid")long skuid);

    @Delete("delete from `cart` where `id`=#{id}")
    public  int deleteById(long id);

    @Update("update `cart` set `quantity`=#{quantity},`modtime`=#{modtime} where id=#{id}")
    public int updateById(CartModel cart);

    @Select("select `id`,`userid`,`itemid`,`skuid`,`quantity`,`tid`,`addtime`,`modtime` from `cart` where `id`=#{id}")
    public CartModel getById(long id);
    
    @Select("select sum(`quantity`) from `cart` where `userid`=#{userid} and `itemid`=#{itemid}")
    public Integer getQuantityCountByUserIdAndItemId(@Param("userid") long userid, @Param("itemid") long itemid);
}
