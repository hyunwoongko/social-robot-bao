package com.welfarerobotics.welfareapplcation.entity.cache;

import com.welfarerobotics.welfareapplcation.entity.Server;
import lombok.Data;

/**
 * @Author : Hyunwoong
 * @When : 4/24/2019 12:18 AM
 * @Homepage : https://github.com/gusdnd852
 */
public @Data class ServerCache {

    private static ServerCache instance;

    private ServerCache() {
    }

    public synchronized static ServerCache getInstance() {
        if (instance == null)
            instance = new ServerCache();
        return instance;
    }

    public static void setInstance(Server server) {
        ServerCache cache = ServerCache.getInstance();
        cache.setCssid(server.getCssid());
        cache.setCsssecret(server.getCsssecret());
        cache.setYoutubekey(server.getYoutubekey());
        cache.setUrl(server.getUrl());
        cache.setState(server.isState());
    }

    private String url;
    private String cssid;
    private String csssecret;
    private String youtubekey;
    private boolean state;
}
