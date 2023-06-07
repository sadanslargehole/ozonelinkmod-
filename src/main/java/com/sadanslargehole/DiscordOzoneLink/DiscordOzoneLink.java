package com.sadanslargehole.DiscordOzoneLink;

import club.minnced.discord.webhook.WebhookClient;
import com.google.gson.Gson;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import java.io.InputStreamReader;

@Mod(modid = DiscordOzoneLink.MODID, name = DiscordOzoneLink.NAME, version = DiscordOzoneLink.VERSION)
public class DiscordOzoneLink
{
    public static final String MODID = "discordmodstest";
    public Config config;
    public static final String NAME = "Example Mod";
    public static final String VERSION = "1.0";

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
    public void postinit(FMLPostInitializationEvent e) {
        logger.info("server started; sending discord webhook message");
        webhook.send("server started");
        MinecraftForge.EVENT_BUS.register(new myEventBus());
    }
    public class myEventBus{
        @SubscribeEvent
        public void toDiscord(ServerChatEvent c){
            String user= c.getUsername();
            String content = c.getMessage();
            DiscordOzoneLink.webhook.send(String.format("%s > %s", user, content));

        }

    }
}

