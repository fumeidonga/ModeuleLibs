package com.rebeau.technology.java.testdesignmodel.template;


import com.rebeau.base.utils.RBLogUtil;

public abstract class ABSKuaiDi {
    /**
     * 派送流程
     * 这里申明为final，不希望子类覆盖这个方法，防止更改流程的执行顺序
     */
    public final void post() {
        prepare();
        call();
        if (isSign()){
            sign();
        } else {
            refuse();
        }
    }

    /** 准备操作，固定流程，父类实现 */
    protected void prepare() {
        RBLogUtil.d("快递已达到，准备派送");
    }
    /** 联系收货人，联系人不一样，所以为抽象方法，子类实现 */
    protected abstract void call();
    /** 是否签收,这个是钩子方法，用来控制流程的走向 */
    protected boolean isSign() {
        return true;
    }
    /** 签收，这个是固定流程，父类实现 */
    protected void sign() {
        RBLogUtil.d("客户已签收，上报系统");
    }
    /** 拒签，空实现，这个也是钩子方法，子类可以跟进实际来决定是否去实现这个方法 */
    protected void refuse() {
    }

}
