package com.hehua.order.dao;

import com.hehua.order.model.FeedbackModel;
import org.apache.ibatis.annotations.*;

import javax.inject.Named;
import java.util.List;

/**
 * Created by liuweiwei on 14-8-18.
 */
@Named
public interface FeedbackDAO {

    @Insert("insert into `feedback` (`id`,`userid`,`itemid`,`orderid`,`skuid`,`score`,`comment`,`status`,`attributes`,`addtime`,`modtime`) values (#{id},#{userid},#{itemid},#{orderid},#{skuid},#{score},#{comment},#{status},#{attributes},#{addtime},#{modtime});")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public int add(FeedbackModel feedbackModel);

    @Select("select `id`,`userid`,`itemid`,`orderid`,`skuid`,`score`,`comment`,`status`,`attributes`,`addtime`,`modtime` from `feedback` where userid=#{userid}")
    public List<FeedbackModel> getListByUseridId(long userid);

    @Select("select `id`,`userid`,`itemid`,`orderid`,`skuid`,`score`,`comment`,`status`,`attributes`,`addtime`,`modtime` from `feedback` where itemid=#{itemid}")
    public List<FeedbackModel> getListByItemId(long itemid);

    @Select("select `id`,`userid`,`itemid`,`orderid`,`skuid`,`score`,`comment`,`status`,`attributes`,`addtime`,`modtime` from `feedback` where `orderid`=#{orderid} and `skuid`=#{skuid}")
    public FeedbackModel getByOrderIdAndSkuid(@Param("orderid") long orderid, @Param("skuid") long skuid);

    @Select("select `id`,`userid`,`itemid`,`orderid`,`skuid`,`score`,`comment`,`status`,`attributes`,`addtime`,`modtime` from `feedback` where `orderid`=#{orderid}")
    public List<FeedbackModel> getListByOrderId(long orderid);

    @Update("update `feedback` set `score`=#{score},`comment`=#{comment},`attributes`=#{attributes},`modtime`=#{modtime} where id=#{id}")
    public int updateById(FeedbackModel feedbackModel);

    @Select("select count(`id`) from `feedback` where `itemid`=#{itemid}")
    public int getCountByItemId(@Param("itemid") long itemid);
}
