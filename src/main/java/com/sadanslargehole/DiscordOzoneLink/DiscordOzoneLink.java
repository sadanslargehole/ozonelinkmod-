package com.sadanslargehole.DiscordOzoneLink;

import club.minnced.discord.webhook.WebhookClient;
import com.google.gson.Gson;
import com.sadanslargehole.DiscordOzoneLink.discord.Discord;
import net.dv8tion.jda.api.entities.Activity;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.http.impl.client.DefaultUserTokenHandler;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;
import java.io.InputStreamReader;

@Mod(modid = DiscordOzoneLink.MODID, name = DiscordOzoneLink.NAME, version = DiscordOzoneLink.VERSION, acceptableRemoteVersions = "*")
public class DiscordOzoneLink
{
    public static final String MODID = "discordmodstest";
    public static Config config;
    public static final String NAME = "ozone link  Mod";
    public static final String VERSION = "0.0.1-ALPHA";
    public Discord d;
    public static WebhookClient webhook;
    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {

        try{
            Gson g = new Gson();
            config = g.fromJson(new InputStreamReader(ClassLoader.getSystemResourceAsStream("config.json")),
                    Config.class);
            webhook = WebhookClient.withUrl(config.webhook);
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
        webhook.send("server started");
        MinecraftForge.EVENT_BUS.register(new myEventBus());
        d = new Discord();
    }
    public class myEventBus{
        @SubscribeEvent
        public void toDiscord(ServerChatEvent chat){
            String user= chat.getUsername();
            String content = chat.getMessage();
            DiscordOzoneLink.webhook.send(String.format("%s > %s", user, content));

        }
        @SubscribeEvent
        public void updateStatus(EntityJoinWorldEvent event){
            try {
                int p= FMLCommonHandler.instance().getMinecraftServerInstance().getCurrentPlayerCount();
                if (p==0){
                    d.getShartManager().setActivity(Activity.playing("with nobody ;("));
                }else {
                    d.getShartManager().setActivity(Activity.playing(String.format("with %d players", FMLCommonHandler.instance().getMinecraftServerInstance().getCurrentPlayerCount())));
                }
            } catch (NullPointerException error) {
                d.getShartManager().setActivity(Activity.playing("with nobody ;("));
            }
        }
    }
}

