package com.sadanslargehole.DiscordOzoneLink;

import com.google.gson.Gson;
import com.sadanslargehole.DiscordOzoneLink.Config;
import com.sadanslargehole.DiscordOzoneLink.discord.Discord;
import com.sadanslargehole.DiscordOzoneLink.util.webhookWrapper;
import net.dv8tion.jda.api.entities.Activity;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;
import java.io.InputStreamReader;
import java.util.Objects;

@Mod(modid = DiscordOzoneLink.MODID, name = DiscordOzoneLink.NAME, version = DiscordOzoneLink.VERSION, acceptableRemoteVersions = "*")
public class DiscordOzoneLink
{
    public static final String MODID = "discordmodstest";
    public static Config config;
    public static final String NAME = "ozone link  Mod";
    public static final String VERSION = "0.0.1-ALPHA";
    private static Discord discordBot;
    private static webhookWrapper wrapper;
    private static Logger logger;

    public static Discord getDiscordBot(){
        return discordBot;
    }
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        try{
            Gson g = new Gson();
            config = g.fromJson(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("config.json"))),
                    Config.class);
            wrapper = new webhookWrapper(config.webhook, config.serverName, config.serverPicture);
        } catch (Exception e){
            e.printStackTrace();
        }
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

    }
    @EventHandler
    public void postinit(FMLPostInitializationEvent e)throws LoginException {
        logger.info("server started; sending discord webhook message");
        MinecraftForge.EVENT_BUS.register(new myEventBus());
        discordBot = new Discord();
    }
    @EventHandler
    public void starting(FMLServerStartingEvent e){
        com.sadanslargehole.DiscordOzoneLink.commands.registerCommands.starting(e);

    }
    @EventHandler
    public void startServer(FMLServerStartedEvent event){

        wrapper.sendServer("server started");
    }
    @EventHandler
    public void stopserver(FMLServerStoppedEvent e){
        wrapper.sendServer("Server Stopped");
    }
    public class myEventBus{
        @SubscribeEvent
        public void toDiscord( ServerChatEvent chat){
            wrapper.sendPlayer(chat.getPlayer(), String.format("%s Has Joined The Server", chat.getPlayer().getName()));
        }
        @SubscribeEvent
        public void updateStatus(PlayerEvent.PlayerLoggedInEvent event){
            wrapper.sendServer(String.format("%s Joined the server", event.player.getName()));
            discordBot.getShartManager().setActivity(Activity.playing("With %C% Players".replace("%C%", String.valueOf(FMLCommonHandler.instance().getMinecraftServerInstance().getCurrentPlayerCount()))));
        }
        @SubscribeEvent
        public void updatestatus1(PlayerEvent.PlayerLoggedOutEvent event){
            wrapper.sendServer(String.format("%s Left the server", event.player.getName()));
            if (FMLCommonHandler.instance().getMinecraftServerInstance().getCurrentPlayerCount()-1==0){
                discordBot.getShartManager().setActivity(Activity.playing("With Nobody"));
            }
            discordBot.getShartManager().setActivity(Activity.playing("With %C% Players!".replace("%C%", String.valueOf(FMLCommonHandler.instance().getMinecraftServerInstance().getCurrentPlayerCount()-1))));
        }

    }
}

