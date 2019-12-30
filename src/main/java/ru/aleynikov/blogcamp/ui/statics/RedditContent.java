package ru.aleynikov.blogcamp.ui.statics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RedditContent {

    private static final List<String> redditAvatarsSources = new ArrayList<>() {
        {
            add("https://www.redditstatic.com/avatars/avatar_default_01_46A508.png");
            add("https://www.redditstatic.com/avatars/avatar_default_02_46D160.png");
            add("https://www.redditstatic.com/avatars/avatar_default_03_46D160.png");
            add("https://www.redditstatic.com/avatars/avatar_default_04_25B79F.png");
            add("https://www.redditstatic.com/avatars/avatar_default_05_24A0ED.png");
            add("https://www.redditstatic.com/avatars/avatar_default_06_7193FF.png");
            add("https://www.redditstatic.com/avatars/avatar_default_07_FF66AC.png");
            add("https://www.redditstatic.com/avatars/avatar_default_08_A06A42.png");
            add("https://www.redditstatic.com/avatars/avatar_default_09_FF4500.png");
            add("https://www.redditstatic.com/avatars/avatar_default_10_FFB000.png");
            add("https://www.redditstatic.com/avatars/avatar_default_13_DDBD37.png");
            add("https://www.redditstatic.com/avatars/avatar_default_16_25B79F.png");
            add("https://www.redditstatic.com/avatars/avatar_default_10_FF66AC.png");
            add("https://www.redditstatic.com/avatars/avatar_default_15_7E53C1.png");
            add("https://www.redditstatic.com/avatars/avatar_default_17_FF585B.png");
            add("https://www.redditstatic.com/avatars/avatar_default_12_FF66AC.png");
            add("https://www.redditstatic.com/avatars/avatar_default_14_D4E815.png");
        }
    };

    public static String getRandomAvatar() {
        return redditAvatarsSources.get(ThreadLocalRandom.current().nextInt(redditAvatarsSources.size()));
    }
}
