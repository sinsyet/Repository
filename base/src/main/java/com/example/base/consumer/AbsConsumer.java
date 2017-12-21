package com.example.base.consumer;


/**
 * @author YGX
 *         <p>
 *         消费者基类
 *
 *         Callback, handler, 处理者,
 *         Data, data, 消费的数据,
 *         Returner, return value, 返回值
 *
 *  本类是基于链式消费模式,
 *  可以通过{@link #setNextConsumer(AbsConsumer)}设置消费链的下一个节点,
 *  当有数据可被消费的时候, 通过{@link #onConsume(Object)}方法将数据传入消费链
 *  消费者通过{@link #isCanHandle(Object)} 判断自己是否可以消费掉该事件;
 *      如果可以, 则调用该消费者的{@link #handle(Object)}方法将数据消费掉, 数据不再往下传
 *      如果不可以, 则判断下一个消费节点是否为null,
 *           不为null则将数据传递给下一个节点进行消费
 *           为null则消费结束, 没有消费者;
 *
 *  消费后可以通过{@link #handler}通过接口反馈,
 *  也可以通过直接return 的方式将消费结果反馈给调用者;
 *
 *  通过Callback的方式进行接口回调反馈消费结果, 可以自由的选择回调在当前线程, 还是到主线程
 */

public abstract class AbsConsumer<Handler, Data, Returner> {

    private Handler handler;
    private AbsConsumer<Handler, Data, Returner> mNext;

    public AbsConsumer(Handler handler) {
        this.handler = handler;
    }

    protected Handler getCallbackHandler() {
        return handler;
    }

    public void setNextConsumer(AbsConsumer<Handler, Data, Returner> next) {
        if (mNext != null) {
            mNext.setNextConsumer(next);
        } else {
            this.mNext = next;
        }
    }

    public Returner onConsume(Data data) {

        if (isCanHandle(data)) {
            return handle(data);
        }

        if (mNext != null) {
            return mNext.onConsume(data);
        }

        return null;
    }

    protected abstract boolean isCanHandle(Data data);

    protected abstract Returner handle(Data data);
}
