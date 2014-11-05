package com.hehua.order.service.params;

import com.hehua.order.info.FeedbackInfo;

import java.util.List;

/**
 * Created by liuweiwei on 14-8-18.
 */
public class FeedbackServiceAddParam {

    private List<FeedbackInfo> feedbacks;

    public List<FeedbackInfo> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<FeedbackInfo> feedbacks) {
        this.feedbacks = feedbacks;
    }
}
