package com.example.smsapplication;

public class MessageModel {
    String messageName, messageDescp, messageId;

    public MessageModel(String messageName, String messageDescp, String messageId) {
        this.messageName = messageName;
        this.messageDescp = messageDescp;
        this.messageId = messageId;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public String getMessageDescp() {
        return messageDescp;
    }

    public void setMessageDescp(String messageDescp) {
        this.messageDescp = messageDescp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }


}
