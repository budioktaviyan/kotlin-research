package id.jasoet.vertx

import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.core.impl.launcher.VertxCommandLauncher
import io.vertx.core.impl.launcher.VertxLifecycleHooks
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.core.logging.SLF4JLogDelegateFactory

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */


class VertxLauncher : VertxCommandLauncher(), VertxLifecycleHooks {


    companion object {
        /**
         * Main entry point.

         * @param args the user command line arguments.
         */
        @JvmStatic
        fun main(args: Array<String>) {
            System.setProperty(LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME, SLF4JLogDelegateFactory::class.java.name)
            VertxLauncher().dispatch(args)
        }

        /**
         * Utility method to execute a specific command.

         * @param cmd  the command
         * *
         * @param args the arguments
         */
        @JvmStatic
        fun executeCommand(cmd: String, vararg args: String) {
            System.setProperty(LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME, SLF4JLogDelegateFactory::class.java.name)
            VertxLauncher().execute(cmd, *args)
        }
    }

    override fun afterConfigParsed(config: JsonObject?) {
    }

    override fun afterStartingVertx(vertx: Vertx?) {
    }

    override fun handleDeployFailed(vertx: Vertx?, mainVerticle: String?, deploymentOptions: DeploymentOptions?, cause: Throwable?) {
        vertx?.close()
    }

    override fun beforeStartingVertx(options: VertxOptions?) {
    }

    override fun beforeDeployingVerticle(deploymentOptions: DeploymentOptions?) {
    }
}