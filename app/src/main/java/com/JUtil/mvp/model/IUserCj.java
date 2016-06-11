package com.JUtil.mvp.model;

import com.JUtil.mvp.model.listener.OnLoginListener;

/**
 * Created by leil7 on 2016/6/11.
 */
public interface IUserCj {
    void login(String username, String password, OnLoginListener loginListener);
}
