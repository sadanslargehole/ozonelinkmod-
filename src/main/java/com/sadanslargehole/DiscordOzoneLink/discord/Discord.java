package com.sadanslargehole.DiscordOzoneLink.discord;
import com.sadanslargehole.DiscordOzoneLink.DiscordOzoneLink;
import com.typesafe.config.ConfigException;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.annotation.Nullable;
import javax.security.auth.login.LoginException;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import scala.Int;

public class Discord {
    private static ShardManager shartManager;
    public Discord()throws LoginException{
        DefaultShardManagerBuilder b = DefaultShardManagerBuilder.createDefault(DiscordOzoneLink.config.token);
        b.setStatus(OnlineStatus.ONLINE);
        try {
            int p= FMLCommonHandler.instance().getMinecraftServerInstance().getCurrentPlayerCount();
            if (p==0){
                b.setActivity(Activity.playing("with nobody ;("));
            }else {
                b.setActivity(Activity.playing(String.format("with %d players", FMLCommonHandler.instance().getMinecraftServerInstance().getCurrentPlayerCount())));
            }
        } catch (NullPointerException e) {
            b.setActivity(Activity.playing("with nobody ;("));
        }


        shartManager = b.build();
    }
    public ShardManager getShartManager(){
        return shartManager;
    }
    public static void main(String[] args){
    }
}
