package com.hehua.order.dao;

import com.hehua.order.model.CreditModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import javax.inject.Named;

/**
 * Created by liuweiwei on 14-8-15.
 */
@Named
public interface CreditDAO {

    @Insert("insert into `credit` (`userid`,`value`,`rebate`,`giftcard`,`recharge`,`direct`,`modtime`) values (#{userid},#{value},#{rebate},#{giftcard},#{recharge},#{direct},#{modtime})")
    public int add(CreditModel m);

    @Select("select `userid`,`value`,`rebate`,`giftcard`,`recharge`,`direct`,`modtime` from `credit` where `userid`=#{userid}")
    public CreditModel getByUserid(long userid);

    @Update("update `credit` set `value`=#{value},`rebate`=#{rebate},`giftcard`=#{giftcard},`recharge`=#{recharge},`direct`=#{direct},`modtime`=#{modtime} where `userid`=#{userid}")
    public int update(CreditModel m);
}
