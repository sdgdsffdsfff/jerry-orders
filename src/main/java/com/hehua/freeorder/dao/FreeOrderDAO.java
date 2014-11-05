package com.hehua.freeorder.dao;

import com.hehua.freeorder.model.FreeOrderModel;
import org.apache.ibatis.annotations.*;

import javax.inject.Named;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by liuweiwei on 14-10-6.
 */
@Named
public interface FreeOrderDAO {

    @Insert("insert into `freeorder` (`freeFlashId`,`userid`,`itemid`,`skuid`,`status`,`applyip`,`deliveryInfo`,`addressInfo`,`addtime`) values (#{freeFlashId},#{userid},#{itemid},#{skuid},#{status},#{applyip},#{deliveryInfo},#{addressInfo},#{addtime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int add(FreeOrderModel model);

    @Select("select `id`,`freeFlashId`,`userid`,`itemid`,`skuid`,`status`,`applyip`,`deliveryInfo`,`addressInfo`,`addtime` from `freeorder` where `id`=#{id}")
    public FreeOrderModel getById(int id);

    @Select("select 1 from `freeorder` where `id`=#{id}")
    public Integer hasWithId(long id);

    /* 注意：mybatis特殊符号如 >、< 等需要转义 */
    @Select("select `id`,`freeFlashId`,`userid`,`itemid`,`skuid`,`status`,`applyip`,`deliveryInfo`,`addressInfo`,`addtime` from `freeorder` where `userid`=#{userid} and `status`!=4 and `status`&lt;14")
    public FreeOrderModel existGoing(long userid);

    @Select("select `id`,`freeFlashId`,`userid`,`itemid`,`skuid`,`status`,`applyip`,`deliveryInfo`,`addressInfo`,`addtime` from `freeorder` where `userid`=#{userid} and `status`=8")
    public FreeOrderModel getCanWriteAppraiseBy(long userid);

    @Select("select `id`,`freeFlashId`,`userid`,`itemid`,`skuid`,`status`,`applyip`,`deliveryInfo`,`addressInfo`,`addtime` from `freeorder` where `userid`=#{userid} and `status`=#{status} order by id desc limit #{offset}, #{limit}")
    public List<FreeOrderModel> getListByUserId(@Param("userid") long userid, @Param("status") int status, @Param("offset") int offset, @Param("limit") int limit);

    @Select("select `id`,`freeFlashId`,`userid`,`itemid`,`skuid`,`status`,`applyip`,`deliveryInfo`,`addressInfo`,`addtime` from `freeorder` where  `itemid`=#{itemid} order by id desc limit #{offset}, #{limit}")
    public List<FreeOrderModel> getListByItemIdByPage(@Param("itemid") long itemid, @Param("offset") int offset, @Param("limit") int limit);

    @Select("select count(1) from `freeorder` where  `itemid`=#{itemid}")
    public Integer getCountByItemId(@Param("itemid") long itemid);

    @Select("select count(1) from `freeorder` where  `itemid`=#{itemid} and `status`=#{status}")
    public Integer getCountByStatusAndItemId(@Param("itemid") long itemid, @Param("status") int status);

    @Select("select `id`,`freeFlashId`,`userid`,`itemid`,`skuid`,`status`,`applyip`,`deliveryInfo`,`addressInfo`,`addtime` from `freeorder` where `status`!=#{status} and `status`!=0 and `itemid`=#{itemid} order by id desc")
    public List<FreeOrderModel> getListByItemIdNotContaionReject(@Param("itemid") long itemid, @Param("status") int status);

    @Select("select `id`,`freeFlashId`,`userid`,`itemid`,`skuid`,`status`,`applyip`,`deliveryInfo`,`addressInfo`,`addtime` from `freeorder` where `userid`=#{userid} order by id desc limit #{offset}, #{limit}")
    public List<FreeOrderModel> getAllByUserId(@Param("userid") long userid, @Param("offset") int offset, @Param("limit") int limit);

    @Update("update `freeorder` set `status`=#{status} where `id`=#{id}")
    public int updateStatus(FreeOrderModel m);

    @Select("select userid from `freeorder` where `status`= 0")
    public List<Long> getUseridOfNewApply();

    @Update("update `freeorder` set `status`=#{status},`deliveryInfo`=#{deliveryInfo} where `id`=#{id}")
    public int setDeliveried(FreeOrderModel m);

    public List<Integer> getsByItemIdsAndUserId(@Param("itemIds") Collection<Integer> itemIds, @Param("userid") long userid);

    @Select("select count(1) from `freeorder` where `freeFlashId`=#{freeFlashId} and `userid`=#{userid}")
    public Integer isExsitApplyByUserIdAndFreeId(@Param("freeFlashId")int freeFlashId, @Param("userid") long userid);
}
