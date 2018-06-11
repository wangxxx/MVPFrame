package com.wangxing.code.oss;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

/**
 * Created by WangXing on 2017/4/24.
 */

public interface OssCallBack {

    void onOssResult(PutObjectRequest request, PutObjectResult result);

    void onOssError(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException);

    void onOssProgress(PutObjectRequest request, long currentSize, long totalSize);

}
