package com.smartcampusmanagmentsystem.domain;

import java.util.UUID;

public class GroupChatTestSamples {

    public static GroupChat getGroupChatSample1() {
        return new GroupChat()
            .id("id1")
            .unreadCount("unreadCount1")
            .title("title1")
            .type("type1")
            .createdAt("createdAt1")
            .owner("owner1")
            .ownerName("ownerName1");
    }

    public static GroupChat getGroupChatSample2() {
        return new GroupChat()
            .id("id2")
            .unreadCount("unreadCount2")
            .title("title2")
            .type("type2")
            .createdAt("createdAt2")
            .owner("owner2")
            .ownerName("ownerName2");
    }

    public static GroupChat getGroupChatRandomSampleGenerator() {
        return new GroupChat()
            .id(UUID.randomUUID().toString())
            .unreadCount(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .createdAt(UUID.randomUUID().toString())
            .owner(UUID.randomUUID().toString())
            .ownerName(UUID.randomUUID().toString());
    }
}
