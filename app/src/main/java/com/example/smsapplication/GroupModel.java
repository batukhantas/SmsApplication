package com.example.smsapplication;

import java.util.List;

public class GroupModel {
     String groupName, groupDescp, groupImage, groupId;
     List<String> numbers;

     public GroupModel(String groupName, String groupDescp, String groupImage, List<String> numbers, String groupAdmin) {
          this.groupName = groupName;
          this.groupDescp = groupDescp;
          this.groupImage = groupImage;
          this.groupId = groupId;
          this.numbers= numbers;

     }

     public String getGroupName() {
          return groupName;
     }

     public void setGroupName(String groupName) {
          this.groupName = groupName;
     }

     public String getGroupDescp() {
          return groupDescp;
     }

     public void setGroupDescp(String groupDescp) {
          this.groupDescp = groupDescp;
     }

     public String getGroupImage() {
          return groupImage;
     }

     public void setGroupImage(String groupImage) {
          this.groupImage = groupImage;
     }

     public String getGroupId() {
          return groupId;
     }

     public void setGroupId(String groupId) {
          this.groupId = groupId;
     }

        public List<String> getNumbers() {
            return numbers;
        }

        public void setNumbers(List<String> numbers) {
            this.numbers = numbers;
        }


}
