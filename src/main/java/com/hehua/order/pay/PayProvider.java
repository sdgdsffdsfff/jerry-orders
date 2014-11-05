package com.hehua.order.pay;

import com.hehua.order.model.NotifyModel;
import com.hehua.order.model.OrdersModel;
import com.hehua.order.model.RefundModel;

/**
 * Created by liuweiwei on 14-8-14.
 */
public interface PayProvider {

    /**
     * 拼接第三方支付URL
     * @param order
     * @return 若成功则返回url否则返回字符串“fail"
     */
    public String getPayUrl(OrdersModel order);

    /**
     * 验证第三方签名是否正确
     * @param content 待验证内容，包含签名类型和签名字符串
     * @return
     */
    public boolean verifySign(String content);

    /**
     * 保存第三方通知
     * @param content 第三方通知内容
     * @param notifyid 自增ID
     * @return
     */
    public boolean saveNotify(String content, NotifyId notifyid);

    /**
     * 检查支付结果状态
     * @return
     */
    public boolean checkTradeStatus(NotifyModel notify);

    public String refund(RefundModel refund);

}
