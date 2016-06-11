package com.JUtil.mvp.presenter;

import android.os.Handler;

import com.JUtil.mvp.model.IUserCj;
import com.JUtil.mvp.model.entity.User;
import com.JUtil.mvp.model.impl.UserCj;
import com.JUtil.mvp.model.listener.OnLoginListener;
import com.JUtil.mvp.ui.IUserLoginView;

/**
 * Created by leil7 on 2016/6/11.
 */
public class UserLoginPresenter implements OnLoginListener {

    private final IUserLoginView userLoginView;
    private final IUserCj userCj = new UserCj();
    private final Handler mHandler = new Handler();


    public UserLoginPresenter(IUserLoginView userLoginView) {
        this.userLoginView = userLoginView;
    }

    public void login() {
        userLoginView.showLoading();
        userCj.login(userLoginView.getUserName().toString(), userLoginView.getPassword().toString(), this);
    }

    public void clear() {
        userLoginView.clearUserName();
        userLoginView.clearPassword();
    }

    @Override
    public void loginSuccess(final User user) {
        //需要在UI线程执行
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                userLoginView.toMainActivity(user);
                userLoginView.hideLoading();
            }
        });
    }

    @Override
    public void loginFailed() {
        //需要在UI线程执行
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                userLoginView.showFailedError();
                userLoginView.hideLoading();
            }
        });
    }
}

