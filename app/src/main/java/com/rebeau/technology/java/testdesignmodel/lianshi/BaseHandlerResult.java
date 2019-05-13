package com.rebeau.technology.java.testdesignmodel.lianshi;

/**
 * date: 2019/4/12
 * description: ${Desc} .
 */
public abstract class BaseHandlerResult {

    BaseHandlerResult mNextHandler;

    public abstract boolean handleResult(String url);

    public boolean handleNext(String url){
        if(getmNextHandler() != null) {
            return getmNextHandler().handleResult(url);
        } else {
            return false;
        }
    }

    public BaseHandlerResult getmNextHandler() {
        return mNextHandler;
    }

    public void setmNextHandler(BaseHandlerResult mNextHandler) {
        this.mNextHandler = mNextHandler;
    }
}
