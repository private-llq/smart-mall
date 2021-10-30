package com.jsy.listener;
import com.jsy.config.FlashSaleRabbitConfig;
import com.jsy.handle.MessageHandle;
import com.jsy.service.impl.GoodsFlashSaleServiceImpl;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import java.io.IOException;
@Component
public class FlashSaleListener {
    private static final Logger log = LoggerFactory.getLogger(FlashSaleListener.class);

    @Autowired
    GoodsFlashSaleServiceImpl GoodsFlashSaleServiceImpl;
    /**
     * @RabbitListener 可以标注在类上面，需配合 @RabbitHandler 注解一起使用
     * @RabbitListener 标注在类上面表示当有收到消息的时候，就交给 @RabbitHandler 的方法处理，具体使用哪个方法处理，
     * 根据 MessageConverter 转换后的参数类型
     * 通过 ACK 确认是否被正确接收，每个 Message 都要被确认（acknowledged），可以手动去 ACK 或自动 ACK
     */
    @RabbitListener(queues = {FlashSaleRabbitConfig.DIRECT_QUEUE}) //指定监听的队列名
    public void receiver(MessageHandle messageHandle, @Headers Channel channel, Message message) throws IOException {
        log.info("用户{}开始抢单", messageHandle.getUserUuid());
        try {
            //处理消息
            GoodsFlashSaleServiceImpl.robbingProduct(messageHandle.getUserUuid(),messageHandle.getGoodsUuid(), messageHandle.getShopUuid());
            //  确认消息已经消费成功
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e){
            // 拒绝当前消息，并把消息返回原队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
}