package tech.thatgravyboat.skycubed.config

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigScreen
import com.teamresourceful.resourcefulconfig.api.loader.Configurator
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.screens.Screen
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.misc.RegisterCommandsEvent
import tech.thatgravyboat.skyblockapi.helpers.McClient
import java.nio.file.Files

object ConfigManager {

    private val configurator = Configurator("skycubed")

    init {
        Files.createDirectories(FabricLoader.getInstance().configDir.resolve("skycubed"))
        configurator.register(Config::class.java)
    }

    @Subscription
    fun onRegisterCommands(event: RegisterCommandsEvent) {
        event.register("skycubed") {
            callback {
                McClient.setScreen(getScreen(null))
            }
        }
    }

    fun getScreen(parent: Screen?) = ResourcefulConfigScreen.get(parent, configurator, Config::class.java)

    fun save() {
        configurator.saveConfig(Config::class.java)
    }
}