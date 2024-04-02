package org.swiggy.launcher;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.swiggy.common.json.JsonFactory;
import org.swiggy.restaurant.internal.restcontroller.RestaurantController;
import org.swiggy.user.internal.restcontroller.CartController;
import org.swiggy.user.internal.restcontroller.OrderController;
import org.swiggy.user.internal.restcontroller.UserController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  Customizes the starting and stopping of a bundle and creates the JAX-RS server instance.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class Activator implements BundleActivator {

    private static final Logger LOGGER = LogManager.getLogger(Activator.class);
    private Server server;

    /**
     * <p>
     * Invoked when the bundle is started and creates the server instance.
     * </p>
     *
     * @param context The execution context of the bundle being started.
     */
    @Override
    public void start(final BundleContext context) {
        final JAXRSServerFactoryBean bean = new JAXRSServerFactoryBean();

        bean.setAddress("/swiggy");
        bean.setProvider(JsonFactory.getInstance().getJsonProvider());
        bean.setServiceBean(getBeans());
        server = bean.create();

        LOGGER.info("Launcher Bundle Is Started");
    }

    /**
     * <p>
     * Gets all the items in the user cart.
     * </p>
     *
     * @return The List of object n
     */
    private List<Object> getBeans() {
        final List<Object> beans = new ArrayList<>();

        beans.add(RestaurantController.getInstance());
        beans.add(UserController.getInstance());
        beans.add(CartController.getInstance());
        beans.add(OrderController.getInstance());

        return beans;
    }

    /**
     * <p>
     * Invoked when the bundle is stopped and destroys the sever instance.
     * </p>
     *
     * @param context The execution context of the bundle being stopped.
     */
    @Override
    public void stop(final BundleContext context) {
        LOGGER.info("Launcher Bundle Is Stopped");

        if (null != server) {
            server.destroy();
        }
    }
}
