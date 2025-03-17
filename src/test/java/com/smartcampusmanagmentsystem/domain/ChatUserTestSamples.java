package com.smartcampusmanagmentsystem.domain;

import java.util.UUID;

public class ChatUserTestSamples {

    public static ChatUser getChatUserSample1() {
        return new ChatUser()
            .id("id1")
            .userId("userId1")
            .avatar("avatar1")
            .name("name1")
            .about("about1")
            .title("title1")
            .birthday("birthday1")
            .address("address1")
            .phoneNumber("phoneNumber1");
    }

    public static ChatUser getChatUserSample2() {
        return new ChatUser()
            .id("id2")
            .userId("userId2")
            .avatar("avatar2")
            .name("name2")
            .about("about2")
            .title("title2")
            .birthday("birthday2")
            .address("address2")
            .phoneNumber("phoneNumber2");
    }

    public static ChatUser getChatUserRandomSampleGenerator() {
        return new ChatUser()
            .id(UUID.randomUUID().toString())
            .userId(UUID.randomUUID().toString())
            .avatar(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .about(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .birthday(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString());
    }
}
