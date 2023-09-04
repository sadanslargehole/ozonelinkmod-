package com.sadanslargehole.DiscordOzoneLink.util;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.minecraft.entity.player.EntityPlayer;

public class webhookWrapper {
    private static WebhookClient webhook;
    private static  String  serverName = "server";
    private static  String serverPicture = "https://cdn.discordapp.com/avatars/1112732934546395227/ef515f7441c85ce55a81eaccd4667872.webp?size=4096";
    public webhookWrapper(String web, String serverName, String serverPicture) {
        webhook = WebhookClient.withUrl(web);
//        serverName = serverName;
//        serverPicture = serverPicture;
    }
    private static final String API = "https://minotar.net/helm/%USER%/128.png";
    public void sendJoin(EntityPlayer player){
        sendPlayer(player, String.format("%s Has Joined The Server", player.getName()));
    }
    public void sendLeave(EntityPlayer player){
        sendPlayer(player, String.format("%s Has Left The Server", player.getName()));
    }
    public void sendPlayer(EntityPlayer player, String str){
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setAvatarUrl(API.replace("%USER%", player.getUniqueID().toString()));
        builder.setUsername(player.getName());
        builder.setContent(str);
        getWebhook().send(builder.build());
    }
    public void sendServer(String str){
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setContent(str);
        builder.setUsername(serverName);
        builder.setAvatarUrl(serverPicture);
    }
    private WebhookClient getWebhook(){
        return webhook;
    }
}
