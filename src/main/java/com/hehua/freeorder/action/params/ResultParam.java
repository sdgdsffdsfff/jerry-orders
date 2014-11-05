package com.hehua.freeorder.action.params;

import com.hehua.commons.model.MetaCode;

/**
 * Created by liuweiwei on 14-10-7.
 * 处理结果参数
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
