package com.smartcampusmanagmentsystem.domain;

import java.util.UUID;

public class MessageTestSamples {

    public static Message getMessageSample1() {
        return new Message()
            .id("id1")
            .content("content1")
            .createdAt("createdAt1")
            .senderId("senderId1")
            .contactId("contactId1")
            .chatId("chatId1")
            .groupChatId("groupChatId1")
            .binaryData("binaryData1")
            .senderName("senderName1");
    }

    public static Message getMessageSample2() {
        return new Message()
            .id("id2")
            .content("content2")
            .createdAt("createdAt2")
            .senderId("senderId2")
            .contactId("contactId2")
            .chatId("chatId2")
            .groupChatId("groupChatId2")
            .binaryData("binaryData2")
            .senderName("senderName2");
    }

    public static Message getMessageRandomSampleGenerator() {
        return new Message()
            .id(UUID.randomUUID().toString())
            .content(UUID.randomUUID().toString())
            .createdAt(UUID.randomUUID().toString())
            .senderId(UUID.randomUUID().toString())
            .contactId(UUID.randomUUID().toString())
            .chatId(UUID.randomUUID().toString())
            .groupChatId(UUID.randomUUID().toString())
            .binaryData(UUID.randomUUID().toString())
            .senderName(UUID.randomUUID().toString());
    }
}
