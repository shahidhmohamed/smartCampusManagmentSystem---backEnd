package com.smartcampusmanagmentsystem.domain;

import java.util.UUID;

public class ChatTestSamples {

    public static Chat getChatSample1() {
        return new Chat()
            .id("id1")
            .contactId("contactId1")
            .contact("contact1")
            .unreadCount("unreadCount1")
            .title("title1")
            .type("type1")
            .createdAt("createdAt1")
            .owner("owner1")
            .ownerName("ownerName1")
            .binaryData("binaryData1");
    }

    public static Chat getChatSample2() {
        return new Chat()
            .id("id2")
            .contactId("contactId2")
            .contact("contact2")
            .unreadCount("unreadCount2")
            .title("title2")
            .type("type2")
            .createdAt("createdAt2")
            .owner("owner2")
            .ownerName("ownerName2")
            .binaryData("binaryData2");
    }

    public static Chat getChatRandomSampleGenerator() {
        return new Chat()
            .id(UUID.randomUUID().toString())
            .contactId(UUID.randomUUID().toString())
            .contact(UUID.randomUUID().toString())
            .unreadCount(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .createdAt(UUID.randomUUID().toString())
            .owner(UUID.randomUUID().toString())
            .ownerName(UUID.randomUUID().toString())
            .binaryData(UUID.randomUUID().toString());
    }
}
