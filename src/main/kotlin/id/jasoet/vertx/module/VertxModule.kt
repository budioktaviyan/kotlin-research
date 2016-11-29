package id.jasoet.vertx.module

import dagger.Module
import dagger.Provides
import io.vertx.core.Vertx
import io.vertx.core.file.FileSystem
import io.vertx.ext.web.Router

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */


@Module
class VertxModule(val vertx: Vertx) {

    @Provides
    fun provideFileSystem(): FileSystem {
        return vertx.fileSystem()
    }

    @Provides
    fun provideRouter(): Router {
        return Router.router(vertx)
    }
}