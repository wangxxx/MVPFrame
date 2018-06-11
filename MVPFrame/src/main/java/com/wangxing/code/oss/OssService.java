package com.wangxing.code.oss;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.wangxing.code.FrameConst;
import com.wangxing.code.utils.GsonUtil;
import com.wangxing.code.utils.ValueUtil;

import java.util.Date;
import java.util.HashMap;

import wxc.android.logwriter.L;


/**
 * Created by WangXing on 2017/4/24.
 */

public class OssService {

    private static volatile OssService sInstance;
    private OssCallBack mOssCallBack;
    private final ClientConfiguration conf;

    public static OssService getInstance() {
        if (sInstance == null) {
            synchronized (OssService.class) {
                if (sInstance == null) {
                    sInstance = new OssService();
                }
            }
        }
        return sInstance;
    }

    private OssService() {

        conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认5秒
        conf.setMaxConcurrentRequest(9); // 最大并发请求数，默认9个
        conf.setMaxErrorRetry(2); // 失败后最大重1试次数，默认2次

    }


    /**
     * OSS上传文件
     *
     * @param certificate 密钥
     * @param localFile   本地文件路径
     * @param shopId      用户Id
     * @param type        下标
     * @param ossCallBack 回调
     */

    public void asyncPutFile(Context context, OSSCredentialProvider certificate, String localFile, String shopId, String type, String uuid, final String ossCallBackUrl, OssCallBack ossCallBack) {

        OSS oss = new OSSClient(context, FrameConst.OSS_END_POINT, certificate, conf);

        mOssCallBack = ossCallBack;
        String mFilePath = "";
        if (ossCallBackUrl.equals(FrameConst.OSS_ROOM_IMAGE_CALLBACK)) {
            mFilePath = "attached/rroomcategory/" + ValueUtil.formatSpecificDate(new Date()) + "/";
        } else {
            mFilePath = "attached/rshop/" + ValueUtil.formatSpecificDate(new Date()) + "/";

        }

        String mFileName = ValueUtil.formatStamp(new Date()) + "Android.jpg";
//        String urlType = cateFileName(fileType, localFile)[1];
        L.e("asyncPutFile filePath is --------------------> " + mFilePath + mFileName);

        // 构造上传请求
        final PutObjectRequest put = new PutObjectRequest(FrameConst.OSS_BUCKET, mFilePath + mFileName, localFile);
//        ObjectMetadata metadata = new ObjectMetadata();
        // 指定Content-Type
//        metadata.setContentType("audio/mp3");
//        put.setMetadata(metadata);

        final CallbackBodyParams mParams = new CallbackBodyParams();
        mParams.fileName = mFileName;
        mParams.filePath = mFilePath;
        mParams.type = type;
        mParams.shopId = shopId;
        mParams.uuid = uuid;

        L.e("Oss callbackBody is : " + GsonUtil.GsonString(mParams));

        // 传入对应的上传回调参数，这里默认使用OSS提供的公共测试回调服务器地址
        put.setCallbackParam(new HashMap<String, String>() {
            {
                put("callbackUrl", ossCallBackUrl);
                put("callbackBodyType", "application/json");
                //callbackBody可以自定义传入的信息
                put("callbackBody", GsonUtil.GsonString(mParams));
            }
        });

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {

            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                mOssCallBack.onOssProgress(request, currentSize, totalSize);
            }
        });

        oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
//                L.e("nCallback", result.getServerCallbackReturnBody());
//                StandardBean bean = GsonUtil.GsonToBean(result.getServerCallbackReturnBody(), StandardBean.class);
//
//                LogUtils.loge("oss 回调状态  status " + bean.getStatus());
//                if ("OK".equals(bean.getStatus())) {
//                    mOssCallBack.onOssResult(request, result);
//                } else {
//                    mOssCallBack.onOssError(request, null, null);
//                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {

                mOssCallBack.onOssError(request, clientExcepion, serviceException);

                String info = "";
                // 请求异常8
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    info = clientExcepion.toString();
//                    L.e("ClientException", info);
                }
                if (serviceException != null) {
                    // 服务异常
                    L.e("ErrorCode", serviceException.getErrorCode());
                    L.e("RequestId", serviceException.getRequestId());
                    L.e("HostId", serviceException.getHostId());
                    L.e("RawMessage", serviceException.getRawMessage());
                    info = serviceException.toString();
                }
            }
        });
    }


    /**
     * 根据文件类型设置文件名  及  urlType  0：图片  1：视频   2：音频
     *
     * @param fileType
     * @return
     */
//    private String[] cateFileName(String fileType, String localFile) {
//        String fileName = null;
//        String urlType = null;
//        switch (fileType) {
//            case ContentBean.TYPE_IMAGE:
//                if (localFile.endsWith(".gif")) {
//                    fileName = ValueUtil.formatStamp(new Date()) + "Android.gif";
//                } else {
//                    fileName = ValueUtil.formatStamp(new Date()) + "Android.jpg";
//                }
//                urlType = "0";
//                break;
//            case ContentBean.TYPE_AUDIO:
//                fileName = ValueUtil.formatStamp(new Date()) + "Android.mp3";
//                urlType = "2";
//                break;
//            case ContentBean.TYPE_VIDEO:
//                fileName = ValueUtil.formatStamp(new Date()) + "Android.mp4";
//                urlType = "1";
//                break;
//        }
//        return new String[]{fileName, urlType};
//    }

    /**
     * 区分头路径
     * @param type     上传type
     * @param fileType 文件类型
     * @param isCover  是否是封面
     * @return
     */
//    private String cateHeadPath(String type, String fileType, boolean isCover) {
//        String headPath = null;
//        switch (type) {
//            //圈子type
//            case FrameConst.OSS_CIRCLE_UPLOAD_TYPE:
//                headPath = FrameConst.OSS_CIRCLE_COVER_PATH + ValueUtil.formatSpecificDate(new Date()) + "/";
//                break;
//
//            //圈子帖子type
//            case FrameConst.OSS_CIRCLE_POST_UPLOAD_TYPE:
//                if (fileType.equals(ContentBean.TYPE_IMAGE) && isCover) {
//                    headPath = FrameConst.OSS_CIRCLE_POST_PATH + "coverpath/" + ValueUtil.formatSpecificDate(new Date()) + "/";
//                } else if (fileType.equals(ContentBean.TYPE_AUDIO)) {
//                    headPath = FrameConst.OSS_CIRCLE_POST_PATH + "audio/" + ValueUtil.formatSpecificDate(new Date()) + "/";
//                } else if (fileType.equals(ContentBean.TYPE_VIDEO)) {
//                    headPath = FrameConst.OSS_CIRCLE_POST_PATH + "video/" + ValueUtil.formatSpecificDate(new Date()) + "/";
//                } else {
//                    headPath = FrameConst.OSS_CIRCLE_POST_PATH + "image/" + ValueUtil.formatSpecificDate(new Date()) + "/";
//                }
//                break;
//
//            //自媒体帖子type
//            case FrameConst.OSS_MEDIA_POST_UPLOAD_TYPE:
//                if (fileType.equals(ContentBean.TYPE_IMAGE) && isCover) {
//                    headPath = FrameConst.OSS_MEDIA_POST_PATH + "coverpath/" + ValueUtil.formatSpecificDate(new Date()) + "/";
//                } else if (fileType.equals(ContentBean.TYPE_AUDIO)) {
//                    headPath = FrameConst.OSS_MEDIA_POST_PATH + "audio/" + ValueUtil.formatSpecificDate(new Date()) + "/";
//                } else if (fileType.equals(ContentBean.TYPE_VIDEO)) {
//                    headPath = FrameConst.OSS_MEDIA_POST_PATH + "video/" + ValueUtil.formatSpecificDate(new Date()) + "/";
//                } else {
//                    headPath = FrameConst.OSS_MEDIA_POST_PATH + "image/" + ValueUtil.formatSpecificDate(new Date()) + "/";
//                }
//                break;
//
//            //问答上传
//            case FrameConst.OSS_ASK_ANSWER_POST_UPLOAD_TYPE:
//                if (fileType.equals(ContentBean.TYPE_IMAGE) && isCover) {
//                    headPath = FrameConst.OSS_ASK_ANSWER_POST_PATH + "coverpath/" + ValueUtil.formatSpecificDate(new Date()) + "/";
//                } else if (fileType.equals(ContentBean.TYPE_AUDIO)) {
//                    headPath = FrameConst.OSS_ASK_ANSWER_POST_PATH + "audio/" + ValueUtil.formatSpecificDate(new Date()) + "/";
//                } else if (fileType.equals(ContentBean.TYPE_VIDEO)) {
//                    headPath = FrameConst.OSS_ASK_ANSWER_POST_PATH + "video/" + ValueUtil.formatSpecificDate(new Date()) + "/";
//                } else {
//                    headPath = FrameConst.OSS_ASK_ANSWER_POST_PATH + "image/" + ValueUtil.formatSpecificDate(new Date()) + "/";
//                }
//                break;
//
//            //圈子帖子评论
//            case FrameConst.OSS_CIRCLE_POST_COMMENT_UPLOAD_TYPE:
//                headPath = FrameConst.OSS_CIRCLE_SUBMIT_COMMENT_IMAGE_PATH + ValueUtil.formatSpecificDate(new Date()) + "/";
//                break;
//            //自媒体帖子评论
//            case FrameConst.OSS_MEDIA_POST_COMMENT_UPLOAD_TYPE:
//                headPath = FrameConst.OSS_MEDIA_SUBMIT_COMMENT_IMAGE_PATH + ValueUtil.formatSpecificDate(new Date()) + "/";
//                break;
//            //商品封面
//            case FrameConst.OSS_PRODUCT_POST_IMAGE_TYPE:
//                headPath = FrameConst.OSS_SUBMIT_PRODUCT_COVER_PATH + ValueUtil.formatSpecificDate(new Date()) + "/";
//                break;
//            //上传头像
//            case FrameConst.OSS_USER_ICON_POST_IMAGE_TYPE:
//                headPath = FrameConst.OSS_SUBMIT_USER_ICON_PATH + ValueUtil.formatSpecificDate(new Date()) + "/";
//                break;
//
//            //实名认证
//            case FrameConst.OSS_CERTIFIED_POST_IMAGE_TYPE:
//                headPath = FrameConst.OSS_SUBMIT_USER_TICATION_PATH + ValueUtil.formatSpecificDate(new Date()) + "/";
//                break;
//            //实体店认证
//            case FrameConst.OSS_PHYSICAL_STORE_TYPE:
//                headPath = FrameConst.OSS_SUBMIT_USER_PHYSICALSTORE_PATH + ValueUtil.formatSpecificDate(new Date()) + "/";
//                break;
//
//        }
//        return headPath;
//    }


}
