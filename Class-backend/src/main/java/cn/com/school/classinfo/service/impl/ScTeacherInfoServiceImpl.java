/**
 *
 */
package cn.com.school.classinfo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.shiro.authc.credential.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.school.classinfo.mapper.ScTeacherInfoMapper;
import cn.com.school.classinfo.mapper.ScUserMapper;
import cn.com.school.classinfo.model.ScTeacherInfo;
import cn.com.school.classinfo.model.ScUser;
import cn.com.school.classinfo.service.ScTeacherInfoService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dongpp
 *
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ScTeacherInfoServiceImpl  implements ScTeacherInfoService {

    @Autowired
    private ScTeacherInfoMapper scTeacherInfoMapper;

    @Autowired
    private ScUserMapper scUserMapper;

    @Autowired
    private PasswordService passwordService;

    @Override
    public List<ScUser> saveBatch(List<ScTeacherInfo> itemList) {
        int result = scTeacherInfoMapper.batchInsert(itemList);

        List<ScUser> reslutUserList = new ArrayList<ScUser>();
        ScUser scUser = null;
        String password = null;
        for (ScTeacherInfo scTeacherInfo : itemList) {
            scUser = new ScUser();
            scUser.setUserType(0);//代表教师
            scUser.setLoginUser(scTeacherInfo.gettTeacherNo());
            password = getStringRandom(6);//六位随机密码
            scUser.setEnablePassword(password);
            scUser.setPassword(passwordService.encryptPassword(password));
            scUser.setUserId(scTeacherInfo.gettId());
            reslutUserList.add(scUser);
        }

        scUserMapper.batchInsert(reslutUserList);
        return reslutUserList;
    }

    //生成随机数字和字母
    private String getStringRandom(int length) {
        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

}
