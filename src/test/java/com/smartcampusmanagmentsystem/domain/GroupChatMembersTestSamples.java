package com.smartcampusmanagmentsystem.domain;

import java.util.UUID;

public class GroupChatMembersTestSamples {

    public static GroupChatMembers getGroupChatMembersSample1() {
        return new GroupChatMembers().id("id1").groupChatId("groupChatId1").memberName("memberName1").memberUserId("memberUserId1");
    }

    public static GroupChatMembers getGroupChatMembersSample2() {
        return new GroupChatMembers().id("id2").groupChatId("groupChatId2").memberName("memberName2").memberUserId("memberUserId2");
    }

    public static GroupChatMembers getGroupChatMembersRandomSampleGenerator() {
        return new GroupChatMembers()
            .id(UUID.randomUUID().toString())
            .groupChatId(UUID.randomUUID().toString())
            .memberName(UUID.randomUUID().toString())
            .memberUserId(UUID.randomUUID().toString());
    }
}
