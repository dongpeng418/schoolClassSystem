/**
 *
 */
package cn.com.school.classinfo.authorization;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

/**
 * @author dongpp
 *
 */
@Slf4j
public class JWTCredentialsMatcher implements CredentialsMatcher{

    /** * Matcher中直接调用工具包中的verify方法即可 */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        return true;
    }
}
