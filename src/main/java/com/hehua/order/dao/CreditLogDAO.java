package com.hehua.order.dao;

import com.hehua.order.model.CreditLogModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.inject.Named;
import java.util.List;

/**
 * Created by liuweiwei on 14-8-15.
 */
@Named
public interface CreditLogDAO {

    @Insert("insert into `creditlog` (`userid`,`type`,`objid`,`value`,`balance`,`rebate`,`giftcard`,`recharge`,`direct`,`addtime`) values (#{userid},#{type},#{objid},#{value},#{balance},#{rebate},#{giftcard},#{recharge},#{direct},#{addtime})")
    public int add(CreditLogModel m);

    @Select("select `userid`,`type`,`objid`,`value`,`balance`,`rebate`,`giftcard`,`recharge`,`direct`,`addtime` from `creditlog` where `userid`=#{userid} order by id desc limit #{offset},#{limit};")
    public List<CreditLogModel> getListByUserId(@Param("userid") long userid, @Param("offset") int offset, @Param("limit") int limit);
}
