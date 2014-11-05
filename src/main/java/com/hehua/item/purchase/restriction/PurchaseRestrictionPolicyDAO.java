/**
 * 
 */
package com.hehua.item.purchase.restriction;

import java.util.List;

import javax.inject.Named;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author zhihua
 *
 */
@Named
public interface PurchaseRestrictionPolicyDAO {

    @Insert("insert into `purchase_restriction_policy`(`itemid`, `type`, `title`, `desc`, `data`) values (#{itemid}, #{type}, #{title}, #{desc}, #{data})")
    @Options(keyColumn = "id", keyProperty = "id", useGeneratedKeys = true)
    public int insert(PurchaseRestrictionPolicy policy);

    @Select("select `id`, `itemid`, `type`, `title`, `desc`, `data` from `purchase_restriction_policy` where `itemid` = #{itemid}")
    public List<PurchaseRestrictionPolicy> getsByItemId(@Param("itemid") int itemId);

    @Select("select `id`, `itemid`, `type`, `title`, `desc`, `data` from `purchase_restriction_policy`")
    public List<PurchaseRestrictionPolicy> getAll();

    @Delete("delete from `purchase_restriction_policy` where `itemid` = #{itemid}")
    public int del(@Param("itemid") int itemId);
}
