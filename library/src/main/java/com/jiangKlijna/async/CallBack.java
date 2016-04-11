package com.jiangKlijna.async;

/**
 * 回调函数
 * Author: jiangKlijna
 */
public abstract class CallBack extends android.os.Handler {
    public static final int SUCCESS = -1;
    public static final int FAILURE = -2;

    /**
     * 执行成功时调用
     */
    public abstract void onSuccess(int arg1, int arg2, Object object);


    /**
     * 执行失败时调用
     */
    public abstract void onFailure(int arg1, int arg2, Object object);

    /**
     * msg.what为其他情况下的调用,需要重写此方法
     */
    public void onOther(int arg1, int arg2, Object obj) {
    }

    /**
     *
     * @param msg
     */
    @Override
    public final void handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case SUCCESS:
                onSuccess(msg.arg1, msg.arg2, msg.obj);
                break;
            case FAILURE:
                onFailure(msg.arg1, msg.arg2, msg.obj);
                break;
            default:
                onOther(msg.arg1, msg.arg2, msg.obj);
        }
        msg.recycle();
    }

    /**
     * 发送消息函数
     */

    public final void sendMsg() {
        obtainMessage().sendToTarget();
    }

    public final void sendMsg(int what) {
        obtainMessage(what).sendToTarget();
    }

    public final void sendMsg(int what, Object object) {
        obtainMessage(what, object).sendToTarget();
    }

    public final void sendMsg(int what, int arg1, int arg2) {
        obtainMessage(what, arg1, arg2).sendToTarget();
    }

    public final void sendMsg(int what, int arg1, int arg2, Object object) {
        obtainMessage(what, arg1, arg2, object).sendToTarget();
    }
}
