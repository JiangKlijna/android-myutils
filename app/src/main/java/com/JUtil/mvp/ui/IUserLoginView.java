package com.JUtil.mvp.ui;

import com.JUtil.mvp.model.entity.User;

/**
 * Created by leil7 on 2016/6/11.
 */
public interface IUserLoginView {

    CharSequence getUserName();

    CharSequence getPassword();

    void clearUserName();

    void clearPassword();

    void showLoading();

    void hideLoading();

    void toMainActivity(User user);

    void showFailedError();
}
