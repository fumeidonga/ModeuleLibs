package com.rebeau.technology.android.dagger.activitys.one_mvp;


import com.rebeau.commons.mvp.BasePresenter;
import com.rebeau.commons.mvp.BaseView;

public interface OneContract {

    interface View extends BaseView<Presenter> {

        void editTask();

        void deleteTask();

        void completeTask();
    }

    interface Presenter extends BasePresenter<View> {

        void editTask();

        void deleteTask();

        void completeTask();
    }
}
