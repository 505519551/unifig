package com.unifig.im.handler;

import com.unifig.im.constant.Constant;
import com.unifig.im.entity.WebSocketMessageEntity;
import com.unifig.im.util.WebSocketUsers;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import org.msgpack.MessagePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * WebSocketServer端处理器
 * <p>
 *
 *   contact by kaixin254370777@163.com
 * @date 2018/6/29 - 上午9:07
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
    /**
     * WebSocketServerHandler 日志控制器
     * Create by maxl at 2018/5/11 上午11:50
     * Concat at kaixin254370777@163.com
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServerHandler.class);

    /**
     * webSocketUrl
     */
    private String webSocketUrl;
    /**
     * 用于打开关闭握手
     */
    private WebSocketServerHandshaker socketServerHandShaker;

    public WebSocketServerHandler(String webSocketUrl) {
        this.webSocketUrl = webSocketUrl;
    }

    /**
     * 异常
     *
     * @param channelHandlerContext channelHandlerContext
     * @param cause                 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) throws Exception {
        cause.printStackTrace();
        LOGGER.error("├ [服务端捕捉异常]: {}", cause.getMessage());
        channelHandlerContext.close();
    }

    /**
     * 当客户端主动链接服务端的链接后，调用此方法
     *
     * @param channelHandlerContext ChannelHandlerContext
     */
    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        LOGGER.info("├ 与客户端[{}]建立连接\n" +
                        "├ [服务器当前在线人数]: {}"
                , channelHandlerContext.channel().remoteAddress()
                , WebSocketUsers.getUSERS().size() + 1);
    }

    /**
     * 与客户端断开连接时 调用此方法
     *
     * @param channelHandlerContext channelHandlerContext
     */
    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) {
        Channel channel = channelHandlerContext.channel();
        LOGGER.info("├ 与客户端[{}]断开连接", channel.remoteAddress());
        // 从存储结构中移除通道，也可以用缓存来替代
        WebSocketUsers.remove(channel);
        // 关闭通道
        channel.close();
    }

    /**
     * 读完之后调用的方法
     *
     * @param channelHandlerContext ChannelHandlerContext
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
        channelHandlerContext.flush();
    }

    /**
     * 接收客户端发送的消息
     *
     * @param channelHandlerContext ChannelHandlerContext
     * @param receiveMessage        消息
     */
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Object receiveMessage) throws Exception {
        // 传统http接入 第一次需要使用http建立握手
        if (receiveMessage instanceof FullHttpRequest) {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) receiveMessage;
            LOGGER.info("├ [握手]: {}", fullHttpRequest.uri());
            // 握手
            handlerHttpRequest(channelHandlerContext, fullHttpRequest);
            // 发送连接成功给客户端
            channelHandlerContext.channel().write(new TextWebSocketFrame("连接成功"));
        }
        // WebSocket接入
        else if (receiveMessage instanceof WebSocketFrame) {
            WebSocketFrame webSocketFrame = (WebSocketFrame) receiveMessage;
            handlerWebSocketFrame(channelHandlerContext, webSocketFrame);
        }
    }

    /**
     * 第一次握手
     *
     * @param channelHandlerContext channelHandlerContext
     * @param req                   请求
     */
    private void handlerHttpRequest(ChannelHandlerContext channelHandlerContext, FullHttpRequest req) {
        //"ws://localhost:9999/oa/websocket/{uri}"
        // 构造握手响应返回，本机测试
        WebSocketServerHandshakerFactory wsFactory
                = new WebSocketServerHandshakerFactory(webSocketUrl, Constant.NULL, Constant.FALSE);
        // region 从连接路径中截取连接用户名
        String uri = req.uri();
        int i = uri.lastIndexOf("/");
        String userName = uri.substring(i + 1, uri.length());
        // endregion
        Channel connectChannel = channelHandlerContext.channel();
        // 加入在线用户
        WebSocketUsers.put(userName, connectChannel);
        socketServerHandShaker = wsFactory.newHandshaker(req);
        if (socketServerHandShaker == null) {
            // 发送版本错误
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(connectChannel);
        } else {
            // 握手响应
            socketServerHandShaker.handshake(connectChannel, req);
        }
    }

    /**
     * webSocket处理逻辑
     *
     * @param channelHandlerContext channelHandlerContext
     * @param frame                 webSocketFrame
     */
    private void handlerWebSocketFrame(ChannelHandlerContext channelHandlerContext, WebSocketFrame frame) throws IOException {
        Channel channel = channelHandlerContext.channel();
        // region 判断是否是关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            LOGGER.info("├ 关闭与客户端[{}]链接", channel.remoteAddress());
            socketServerHandShaker.close(channel, (CloseWebSocketFrame) frame.retain());
            return;
        }
        // endregion
        // region 判断是否是ping消息
        if (frame instanceof PingWebSocketFrame) {
            LOGGER.info("├ [Ping消息]");
            channel.write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // endregion
        // region 纯文本消息
        if (frame instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) frame).text();
            LOGGER.info("├ [接收到客户端的消息]: {} [channel]:{}", text,channel.id());
            channel.write(new TextWebSocketFrame("你发的消息是：" + text));
        }
        // endregion
        // region 二进制消息
        if (frame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) frame;
            ByteBuf content = binaryWebSocketFrame.content();
            LOGGER.info("├ [二进制数据]:{}", content);
            final int length = content.readableBytes();
            final byte[] array = new byte[length];
            content.getBytes(content.readerIndex(), array, 0, length);
            MessagePack messagePack = new MessagePack();
            WebSocketMessageEntity webSocketMessageEntity = messagePack.read(array, WebSocketMessageEntity.class);
            LOGGER.info("├ [解码数据]: {}", webSocketMessageEntity);
            WebSocketUsers.sendMessageToUser(webSocketMessageEntity.getAcceptName(), webSocketMessageEntity.getContent());
        }
        // endregion
    }
}
