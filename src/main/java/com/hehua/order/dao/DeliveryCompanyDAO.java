package com.hehua.order.dao;

import com.hehua.order.info.DeliveryCompanyInfo;
import com.hehua.order.model.DeliveryCompanyModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import javax.inject.Named;
import java.util.List;

/**
 * Created by liuweiwei on 14-8-13.
 */
@Named
public interface DeliveryCompanyDAO {

    @Insert("insert into `deliverycompany` (`id`,`name`,`code`,`website`,`phone`,`status`,`sort`) values (#{id},#{name},#{code},#{website},#{phone},#{status},#{sort});")
    public int add(DeliveryCompanyModel m);

    @Select("select `name` as deliveryCompany, `code` as deliveryComPinyin from `deliverycompany` where `status`=0 order by `sort` desc")
    public List<DeliveryCompanyInfo> getSupportedList();

    @Select("select `id`,`name`,`code`,`website`,`phone`,`status`,`sort` from `deliverycompany` order by `sort` desc")
    public List<DeliveryCompanyModel> getAllBySort();

    @Update("update `deliverycompany` set `sort`=#{sort} where `id`=#{id}")
    public int updateSortById(DeliveryCompanyModel m);

    @Select("select `id`,`name`,`code`,`website`,`phone`,`status`,`sort` from `deliverycompany` where `id`=#{id}")
    public DeliveryCompanyModel getById(int id);

    @Select("select `id`,`name`,`code`,`website`,`phone`,`status`,`sort` from `deliverycompany` where `code`=#{pinyin}")
    public DeliveryCompanyModel getByPinyin(@Param("pinyin") String pinyin);
}
