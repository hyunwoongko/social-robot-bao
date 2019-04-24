package com.welfarerobotics.welfareapplcation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Builder @Data class Server {
    private String url;
    private String cssid;
    private String csssecret;
    private String youtubekey;
}
