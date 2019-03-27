/**
 *
 */
package cn.com.school.classinfo.authorization;

import lombok.Data;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @author dongpp
 * 加解密相关
 *
 */
@Data
public class ShiroUtils {

    /**
     * 随机生成 salt 需要指定 它的字符串的长度
     *
     * @param len 字符串的长度
     * @return salt
     */
    public static String generateSalt(int len) {
        //一个Byte占两个字节
        int byteLen = len >> 1;
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        return secureRandom.nextBytes(byteLen).toHex();
    }
    /**
     * 获取加密后的密码，使用默认hash迭代的次数 1 次
     *
     * @param hashAlgorithm hash算法名称 MD2、MD5、SHA-1、SHA-256、SHA-384、SHA-512、etc。
     * @param password      需要加密的密码
     * @param salt          盐
     * @return 加密后的密码
     */
    public static String encryptPassword(
                    String hashAlgorithm, String password, String salt) {
        return encryptPassword(hashAlgorithm, password, salt, 1);
    }
    /**
     * 获取加密后的密码，需要指定 hash迭代的次数
     *
     * @param hashAlgorithm  hash算法名称 MD2、MD5、SHA-1、SHA-256、SHA-384、SHA-512、etc。
     * @param password       需要加密的密码
     * @param salt           盐
     * @param hashIterations hash迭代的次数
     * @return 加密后的密码
     */
    public static String encryptPassword(String hashAlgorithm, String password, String salt, int hashIterations) {
        return new SimpleHash(hashAlgorithm, password, ByteSource.Util.bytes(salt), hashIterations).toHex();
    }

    /**
     * 比较密码是否相等
     * @param current 当前已加密的密码
     * @param target 需要比较的未加密密码
     * @param hashAlgorithm 加密算法
     * @param salt 盐
     * @return 比较结果
     */
    public static boolean checkPassword(String current, String target, String hashAlgorithm, String salt){
        String encryptTarget = encryptPassword(hashAlgorithm, target, salt);
        return current.equals(encryptTarget);
    }

}
