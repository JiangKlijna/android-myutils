package com.JUtil.mvp.model.listener;

import com.JUtil.mvp.model.entity.User;

/**
 * Created by leil7 on 2016/6/11.
 */
public interface OnLoginListener {

    void loginSuccess(User user);

    void loginFailed();
}
