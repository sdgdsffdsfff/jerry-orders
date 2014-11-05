package com.hehua.order.service;

import com.hehua.commons.model.CommonMetaCode;
import com.hehua.commons.model.ResultView;
import com.hehua.commons.time.DateUtils;
import com.hehua.framework.web.HehuaRequestContext;
import com.hehua.item.domain.ItemSku;
import com.hehua.item.service.ItemSkuService;
import com.hehua.order.dao.FeedbackDAO;
import com.hehua.order.dao.OrderItemsDAO;
import com.hehua.order.dao.OrdersDAO;
import com.hehua.order.enums.OrdersErrorEnum;
import com.hehua.order.info.FeedbackInfo;
import com.hehua.order.model.FeedbackModel;
import com.hehua.order.model.OrderItemsModel;
import com.hehua.order.model.OrdersModel;
import com.hehua.order.service.params.FeedbackServiceAddParam;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liuweiwei on 14-8-18.
 */
@Named
public class FeedbackService {

    @Inject
    private FeedbackDAO feedbackDAO;

    @Inject
    private OrdersDAO ordersDAO;

    @Inject
    private OrderItemsDAO orderItemsDAO;

    @Inject
    private ItemSkuService itemSkuService;

    /**
     * 新增评价
     * @param param
     * @return
     */
    public ResultView<String> updateAdd(FeedbackServiceAddParam param) {
        long userid = HehuaRequestContext.getUserId();
        if (param.getFeedbacks() == null) {
            return new ResultView<>(OrdersErrorEnum.PARAM_ERROR);
        }
        for (FeedbackInfo feedbackInfo : param.getFeedbacks()) {
            if (!(!StringUtils.isBlank(feedbackInfo.getComment()) && feedbackInfo.getSkuid() != 0 && feedbackInfo.getOrderid() != 0)) {
                return new ResultView<>(OrdersErrorEnum.PARAM_ERROR);
            }
            OrdersModel order = ordersDAO.getById(feedbackInfo.getOrderid());
            if (order == null) {
                return new ResultView<>(OrdersErrorEnum.ORDER_NOT_EXIST);
            }
            if (order.getUserid() != userid) {
                return new ResultView<>(OrdersErrorEnum.ORDER_NOT_YOURS);
            }
            //验证订单和商品是否关联
            ItemSku itemSku = itemSkuService.getItemSkuById(feedbackInfo.getSkuid());
            if (itemSku == null) {
                return new ResultView<>(OrdersErrorEnum.SKU_NOT_EXIST);
            }
            OrderItemsModel orderItemsModel = orderItemsDAO.getByOrderIdAndSkuId(order.getId(), itemSku.getId());
            if (orderItemsModel == null) {
                return new ResultView<>(OrdersErrorEnum.ORDER_FEEDBACK_ERROR);
            }
            FeedbackModel feedback = feedbackDAO.getByOrderIdAndSkuid(order.getId(), feedbackInfo.getSkuid());
            if (feedback != null) {
                if (feedbackInfo.getComment().equalsIgnoreCase(feedback.getComment())) {
                    continue; //评价内容与上次一致，什么都不做
                    //return new ResultView<>(OrdersErrorEnum.FEEDBACK_SAME_WITH_LAST);
                }
                feedback.setAppend(FeedbackModel.FEEDBACK_APPEND);
                feedback.setComment(feedbackInfo.getComment());
                feedback.setModtime(DateUtils.unix_timestamp());
                feedbackDAO.updateById(feedback);
                continue;
            }

            feedback = new FeedbackModel();
            feedback.setUserid(userid);
            feedback.setItemid(itemSku.getItemid());
            feedback.setOrderid(order.getId());
            feedback.setComment(feedbackInfo.getComment());
            feedback.setSkuid(feedbackInfo.getSkuid());
            feedback.initWhenAdd();
            feedbackDAO.add(feedback);
        }
        return new ResultView<>(CommonMetaCode.Success, "success");
    }

    /**
     * 获取订单评价列表
     * @param orderid
     * @return
     */
    public HashMap<Long, FeedbackModel> getListByOrderId(long orderid) {
        HashMap<Long, FeedbackModel> result = new HashMap<>();
        List<FeedbackModel> feedbacks = feedbackDAO.getListByOrderId(orderid);
        for (FeedbackModel feedback : feedbacks) {
            result.put(feedback.getSkuid(), feedback);
        }
        return result;
    }

    public List<FeedbackModel> getListByItemId(long itemId) {
        return feedbackDAO.getListByItemId(itemId);
    }

    public int getCountByItemId(long itemid) {
        return feedbackDAO.getCountByItemId(itemid);
    }
}
