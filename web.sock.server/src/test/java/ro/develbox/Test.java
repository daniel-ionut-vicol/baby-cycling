package ro.develbox;

import javax.servlet.ServletException;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainer;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;

public class Test {

	public static void main(String[] args) {
//		PathHandler path = Handlers.path();
//
//
//        Undertow server = Undertow.builder()
//                .addHttpListener(8080, "0.0.0.0")
//                .setHandler(path)
//                .build();
//        server.start();
//
//        final ServletContainer container = ServletContainer.Factory.newInstance();
//
//        DeploymentInfo builder = new DeploymentInfo()
//                .setClassLoader(WebSocketEndPoint.class.getClassLoader())
//                .setContextPath("/web.sock.server")
//                .addWelcomePage("index.html")
//                .setResourceManager(new ClassPathResourceManager(WebSocketEndPoint.class.getClassLoader(), WebSocketEndPoint.class.getPackage()))
//                .addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME,
//                        new WebSocketDeploymentInfo()
//                                .setBuffers(new DefaultByteBufferPool(true, 100))
//                                .addEndpoint(WebSocketEndPoint.class)
//                )
//                .setDeploymentName("web.sock.server.war");
//
//
//        DeploymentManager manager = container.addDeployment(builder);
//        manager.deploy();
//        try {
//            path.addPrefixPath("/web.sock.server", manager.start());
//        } catch (ServletException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("");
    }

	
}
