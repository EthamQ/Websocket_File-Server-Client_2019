package com.raphael;

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

public class HandlerInbound extends SimpleChannelInboundHandler<Object> {

    static final String URL = "ws://127.0.0.1:8080/websocket";
    WebSocketServerHandshaker handshaker;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object req) throws Exception {
        System.out.println("Check request ...");
        if (req instanceof FullHttpRequest) {
            System.out.println("Received FullHttpRequest ...");
            handleHttpRequest(ctx, (FullHttpRequest) req);
        } else if (req instanceof WebSocketFrame) {
            System.out.println("Received WebSocketFrame ...");
            handleWebSocketFrame(ctx, (WebSocketFrame) req);
        }
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        // Handshake
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(URL, null, true);
        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
        this.handshaker = handshaker;
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // Check for closing frame
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if (frame instanceof TextWebSocketFrame) {
            String fileNameClient = ((TextWebSocketFrame) frame).text();
            try {
                FileStorage fileStorage = new FileStorage();
                if(fileStorage.fileExists(fileNameClient)) {
                    ctx.channel().writeAndFlush(new BinaryWebSocketFrame(fileStorage.getFile(fileNameClient)));
                } else {
                    ctx.channel().writeAndFlush(new TextWebSocketFrame("The requested file doesn't exist on the server"));
                }
            } catch (Exception e) {
                ctx.channel().writeAndFlush(new TextWebSocketFrame("An error occured when trying to access the file"));
                e.printStackTrace();
            }
            return;
        }
        }

}