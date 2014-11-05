package com.hehua.order.refund.params;

import com.hehua.commons.model.MetaCode;

/**
 * Created by liuweiwei on 14-9-23.
 */
public class ResultParam<T> {

    private T data;
    private MetaCode metaCode;

    public ResultParam(MetaCode code) {
        this.metaCode = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public MetaCode getMetaCode() {
        return metaCode;
    }

    public void setMetaCode(MetaCode metaCode) {
        this.metaCode = metaCode;
    }
}
