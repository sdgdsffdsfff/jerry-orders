package com.hehua.order.dao;

import com.hehua.order.model.NotifyModel;
import org.apache.ibatis.annotations.*;

import javax.inject.Named;
import java.util.List;

/**
 * Created by liuweiwei on 14-8-1.
 */
@Named
public interface NotifyDAO {

    @Insert("insert into `notifylog` (`id`,`objid`,`outno`,`type`,`paytype`,`money`,`tradeno`,`buyer`,`paytime`,`result`,`sign`,`status`,`error`,`addtime`,`modtime`,`data`) values (#{id},#{objid},#{outno},#{type},#{paytype},#{money},#{tradeno},#{buyer},#{paytime},#{result},#{sign},#{status},#{error},#{addtime},#{modtime},#{data})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn="id")
    public int add(NotifyModel n);

    @Select("select * from `notifylog` where `id`=#{id}")
    public NotifyModel getById(long id);

    @Update("update `notifylog` set `status`=64 where `id`=#{id}")
    public int setSuccess(long id);

    @Update("update `notifylog` set `status`=128,`error`=#{error} where `id`=#{id}")
    public int setFail(@Param("id") long id, @Param("error") String error);

    @Select("select * from `notifylog` where `status`=0")
    public List<NotifyModel> getUnprocessedList();

}
