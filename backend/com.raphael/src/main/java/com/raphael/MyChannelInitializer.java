package com.raphael;

import javax.net.ssl.SSLContext;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * MyChannelInitializer
 */
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    public MyChannelInitializer(SSLContext context) { }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        System.out.println("init channel ...");
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new HandlerInbound());
    }

    
}