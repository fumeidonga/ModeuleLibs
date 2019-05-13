package com.rebeau.technology.android.dagger.activitys.one_mvp;

import android.support.annotation.Nullable;

/**
 * MVP
 * P
 * 持有view层 {@link OneActivity} Activity implements OneContract.View
 */
public class OnePresenter implements OneContract.Presenter {

    @Nullable
    private OneContract.View mOneContractView;

    public OnePresenter(@Nullable OneContract.View mOneContractView) {
        this.mOneContractView = mOneContractView;
        mOneContractView.setPresenter(this);
    }

    @Override
    public void editTask() {
        mOneContractView.editTask();
    }

    @Override
    public void deleteTask() {
        mOneContractView.deleteTask();
    }

    @Override
    public void completeTask() {
        mOneContractView.completeTask();
    }

    @Override
    public void takeView(OneContract.View view) {
        mOneContractView = view;
    }

    @Override
    public void dropView() {
        mOneContractView = null;
    }
}
