package com.unifig.im.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * <p>
 * create by maxl at 2018/5/18-下午4:12
 * contact by kaixin254370777@163.com
 */
@Message
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessageEntity implements Serializable {
    /**
     * 序列化字段
     */
    private static final long serialVersionUID = 1579258906605843062L;

    /**
     * 接收人
     */
    private String acceptName;

    /**
     * 内容
     */
    private String content;

    /**
     * 方式
     * client -> webClient
     * client -> client
     */
    private String method;
}
