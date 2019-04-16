package com.raphael;

import javax.net.ssl.SSLContext;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public final class NettyServer {

	int PORT = 8080;
	SSLContext context = null;

	public static void main(String[] args) {
		try {
			new NettyServer().createServer();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    public void createServer() throws InterruptedException{
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
	try {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup)
		.channel( NioServerSocketChannel.class)
		.handler(new LoggingHandler(LogLevel.INFO))
		.childHandler(new MyChannelInitializer(context));
		Channel channel = bootstrap.bind(PORT).sync().channel();
		channel.closeFuture().sync();
		}
	catch(Error e) {
		System.out.println(e.getMessage());
	}
	finally {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
		}
	}
}
