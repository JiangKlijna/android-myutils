package com.JUtil.mvp.model.impl;

import com.JUtil.mvp.model.IUserCj;
import com.JUtil.mvp.model.entity.User;
import com.JUtil.mvp.model.listener.OnLoginListener;

/**
 * Created by leil7 on 2016/6/11.
 */
public class UserCj implements IUserCj {

    @Override
    public void login(final String username, final String password, final OnLoginListener loginListener) {
        //模拟子线程耗时操作
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //模拟登录成功
                if ("cj".equals(username) && "123".equals(password)) {
                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    loginListener.loginSuccess(user);
                } else {
                    loginListener.loginFailed();
                }
            }
        }.start();
    }
}
