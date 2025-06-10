package org.wj.letsrock.interfaces.api.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wj.letsrock.infrastructure.config.RabbitmqConfig;
import org.wj.letsrock.enums.notify.NotifyTypeEnum;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.infrastructure.event.NotifyMsgEvent;
import org.wj.letsrock.domain.user.model.entity.UserFootDO;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-15:18
 **/
//@RequestMapping("/test")
//@RestController
//public class TestController {
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//    @GetMapping("/rabbitmq")
//    ResultVo<String> testRabbitmq(){
//        UserFootDO foot=new UserFootDO();
//        NotifyTypeEnum notifyType=NotifyTypeEnum.PRAISE;
//        rabbitTemplate.convertAndSend(
//                RabbitmqConfig.OPERATE_EXCHANGE,
//                RabbitmqConfig.OPERATE_ROUTING_KEY,
//                new NotifyMsgEvent<>(this, notifyType, foot)
//        );
//        return ResultVo.ok();
//    }
//}
