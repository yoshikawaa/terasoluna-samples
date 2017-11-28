package io.github.yoshikawaa.sample.test.util;

import org.terasoluna.gfw.common.message.StandardResultMessageType;

public class TestUtils {

    public static String resultMessage(StandardResultMessageType type, String text) {
        return "ResultMessages [type=" + type + ", list=[ResultMessage [code=null, args=[], text=" + text + "]]]";
    }

}
