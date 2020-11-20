package com.garm.common.utils;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class CipherUtil {
    static private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    static private String method = "MD5";

    public static String encryptPassword(String password,String credentialsSalt) {
        String newPassword = new SimpleHash(
                method,           //加密算法
                password,      //密码
                ByteSource.Util.bytes(credentialsSalt),  //salt盐   username + salt
                2   //迭代次数
        ).toHex();

        return newPassword;
    }


    public static String getSalt(){
        return randomNumberGenerator.nextBytes().toHex();
    }

    /**
     * 校验密码是否正确
     */
    public static boolean verifyPassword(String password, String dbpassword,String credentialsSalt) {
        String newPassword = new SimpleHash(
                method,           //加密算法
                password,      //密码
                ByteSource.Util.bytes(credentialsSalt),  //salt盐   username + salt
                2   //迭代次数
        ).toHex();
        if(!dbpassword.equals(newPassword)){
            return false;
        }else{
            return true;
        }
    }
}
