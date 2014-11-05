package com.hehua.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.hehua.commons.model.CommonMetaCode;
import com.hehua.commons.model.MetaCodeImpl;
import com.hehua.commons.model.ResultView;
import com.hehua.commons.time.DateUtils;
import com.hehua.framework.image.ImageService;
import com.hehua.framework.image.domain.Image;
import com.hehua.item.domain.Item;
import com.hehua.item.domain.ItemSku;
import com.hehua.item.purchase.restriction.PurchaseOperation;
import com.hehua.item.purchase.restriction.PurchaseRestrictionPolicy;
import com.hehua.item.purchase.restriction.PurchaseRestrictionPolicyCheckResult;
import com.hehua.item.purchase.restriction.PurchaseRestrictionPolicyManager;
import com.hehua.item.purchase.restriction.PurchaseRestrictionService;
import com.hehua.item.service.ItemService;
import com.hehua.item.service.ItemSkuService;
import com.hehua.order.dao.CartDAO;
import com.hehua.order.enums.CartErrorEnum;
import com.hehua.order.info.CartSummaryInfo;
import com.hehua.order.info.CartsInfo;
import com.hehua.order.model.CartModel;
import com.hehua.order.utils.DecimalUtil;

/**
 * Created by liuweiwei on 14-7-27.
 */
@Named
public class CartService {

    @Inject
    private CartDAO cartDAO;

    @Inject
    private ItemSkuService itemSkuService;

    @Inject
    private ItemService itemService;

    @Inject
    private OrdersService ordersService;

    @Inject
    private ImageService imageService;

    @Inject
    private PurchaseRestrictionService purchaseRestrictionService;

    @Inject
    private PurchaseRestrictionPolicyManager purchaseRestrictionPolicyManager;

    static Logger log = Logger.getLogger(CartService.class.getName());

    /**
     * 加入购物车接口
     * 
     * @param userid
     * @param skuid
     * @param quantity
     * @return
     */
    public ResultView<CartSummaryInfo> add2Cart(long userid, int skuid, int quantity) {

        int time_now = DateUtils.unix_timestamp();

        ItemSku itemSku = itemSkuService.getItemSkuById(skuid);
        if (itemSku == null) {
            return new ResultView<>(CartErrorEnum.SKU_NOT_EXIST);
        }
        if (quantity < 0 || (itemSku.getQuantity() - quantity < 0)) {
            return new ResultView<>(CartErrorEnum.QUANTITY_ERROR);
        }
        if (!itemService.isSellable(itemSku.getItemid())) {
            if (log.isDebugEnabled()) {
                System.out.println("item can not buy:" + itemSku.getItemid());
            }
            return new ResultView<>(CartErrorEnum.ITEM_CAN_NOT_BUY);
        }

        PurchaseRestrictionPolicyCheckResult checkResult = purchaseRestrictionService
                .checkRestrictionPolicies((int) userid, 0, (int) itemSku.getItemid(), skuid,
                        quantity, PurchaseOperation.AddToCart);
        if (!checkResult.isAccept()) {
            return new ResultView<CartSummaryInfo>(new MetaCodeImpl(
                    CartErrorEnum.QUANTITY_RESTRICTION.getCode(), checkResult.getRejectMessage()));
            //            return new ResultView<CartSummaryInfo>(CartErrorEnum.QUANTITY_RESTRICTION);
        }

        CartModel cart = cartDAO.getByUserIdAndSkuId(userid, skuid);
        if (cart != null) {
            if (log.isDebugEnabled()) {
                System.out.println("update cart quantity,cartid:" + cart.getId() + ",quantity:"
                        + cart.getQuantity());
            }
            log.info("update cart quantity,cartid:" + cart.getId() + ",quantity:"
                    + cart.getQuantity());
            cart.setQuantity(quantity);
            cart.setModtime(time_now);
            cartDAO.updateById(cart);
        } else {
            cart = new CartModel();
            cart.setItemid(itemSku.getItemid());
            cart.setSkuid(skuid);
            cart.setQuantity(quantity);
            cart.setTid("");
            cart.setUserid(userid);
            cart.setAddtime(time_now);
            cart.setModtime(time_now);
            cartDAO.add2Cart(cart);
        }

        return new ResultView<>(CommonMetaCode.Success, this.getCartSummaryInfoByUserId(userid));
    }

    /**
     * 拼装购物车信息
     * 
     * @param userid
     * @return
     */
    public CartSummaryInfo getCartSummaryInfoByUserId(long userid) {
        List<CartsInfo> cartList = this.getListByUserId(userid);
        BigDecimal totalfee = new BigDecimal(0);
        for (CartsInfo info : cartList) {
            totalfee = totalfee.add(info.getPrice().multiply(new BigDecimal(info.getQuantity())));
        }
        CartSummaryInfo cartSummaryInfo = new CartSummaryInfo();
        cartSummaryInfo.setTotalFee(DecimalUtil.toStandard(totalfee));
        cartSummaryInfo.setDeliveryFeeTips("不限数量，不限种类，全场包邮！");
        //cartSummaryInfo.setDeliveryFeeTips("满158元包邮，尿不湿和湿巾可不算哦~");
        cartSummaryInfo.setCarts(cartList);
        return cartSummaryInfo;
    }

    /**
     * 获取购物车商品信息列表
     * 
     * @param userid
     * @return
     */
    public List<CartsInfo> getListByUserId(long userid) {
        List<CartsInfo> cartList = new ArrayList<CartsInfo>();
        List<CartModel> carts = cartDAO.getListByUserId(userid);

        for (CartModel cart : carts) {
            CartsInfo info = new CartsInfo();
            ItemSku itemSku = itemSkuService.getItemSkuById(cart.getSkuid());
            Item item = itemService.getItemById(itemSku.getItemid());
            info.setId(cart.getId());
            info.setSkuid(itemSku.getId());
            info.setTid(cart.getTid());
            info.setQuantity(cart.getQuantity());
            info.setItemid(item.getId());
            info.setTitle(item.getName());
            info.setPrice(new BigDecimal(itemSku.getRealprice()));

            Image image = imageService.getImageById(Long.parseLong(item.getImage()));
            if (image != null) {
                info.setImage(image.getUrl());
            }

            PurchaseRestrictionPolicy purchaseRestrictionPolicy = purchaseRestrictionPolicyManager
                    .getItemPurchaseRestrictionPolicy((int) itemSku.getItemid());
            if (purchaseRestrictionPolicy == null) {
                info.setLimitPerUser(-1);
            } else {
                info.setLimitPerUser(1);
            }
            info.setRemainAmount(itemSku.getQuantity());
            info.setCanBuy(itemService.isSellable(item.getId()));
            info.setTypes(ordersService.genItemTypeInfo(itemSku.getId()));
            cartList.add(info);
        }
        return cartList;
    }

    /**
     * 删除购物车商品
     * 
     * @param userid
     * @param cartids
     * @return
     */
    public ResultView<CartSummaryInfo> deleteByIds(long userid, String cartids) {

        if (StringUtils.isBlank(cartids)) {
            return new ResultView<>(CartErrorEnum.ITEMID_NOT_PRESENT);
        }
        Set<Integer> ids = com.hehua.commons.lang.StringUtils.getIntegerSet(cartids);
        Iterator<Integer> iter = ids.iterator();
        while (iter.hasNext()) {
            int cartid = iter.next();
            CartModel old = cartDAO.getById(cartid);
            if (old == null) {
                return new ResultView<>(CartErrorEnum.ITEM_NOT_EXIST);
            }
            if (old.getUserid() != userid) {
                return new ResultView<>(CartErrorEnum.ITEM_NOT_YOURS);
            }
            cartDAO.deleteById(cartid);
        }

        return new ResultView<>(CommonMetaCode.Success, getCartSummaryInfoByUserId(userid));
    }

    /**
     * @param cartId
     * @return
     */
    public CartModel getById(long cartId) {
        return cartDAO.getById(cartId);
    }

    public int getItemCountInCart(long userId, long itemId) {
        Integer quantityCount = cartDAO.getQuantityCountByUserIdAndItemId(userId, itemId);
        if (quantityCount == null) {
            return 0;
        }
        return quantityCount;
    }
}
