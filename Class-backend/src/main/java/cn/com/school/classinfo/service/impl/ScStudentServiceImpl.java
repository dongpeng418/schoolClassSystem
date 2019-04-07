/**
 *
 */
package cn.com.school.classinfo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.logging.log4j.util.Strings;
import org.apache.shiro.authc.credential.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.school.classinfo.mapper.ScBaseDictionaryMapper;
import cn.com.school.classinfo.mapper.ScStudentInfoMapper;
import cn.com.school.classinfo.mapper.ScUserMapper;
import cn.com.school.classinfo.model.ScBaseDictionary;
import cn.com.school.classinfo.model.ScStudentInfo;
import cn.com.school.classinfo.model.ScUser;
import cn.com.school.classinfo.service.ScStudentService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 禧泰_董鹏鹏
 *
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ScStudentServiceImpl  implements ScStudentService {

    @Autowired
    private ScStudentInfoMapper scStudentInfoMapper;

    @Autowired
    private ScUserMapper scUserMapper;

    @Autowired
    private ScBaseDictionaryMapper scBaseDictionaryMapper;

    @Autowired
    private PasswordService passwordService;

    @Override
    public List<ScUser> saveBatch(List<ScStudentInfo> itemList) {
        Set<String> classSets = new HashSet();
        String className = null;
        for (ScStudentInfo scStudentInfo : itemList) {
            className = scStudentInfo.getStuPubName();
            if(Strings.isNotEmpty(className)) {
                classSets.add(className);
            }
        }

        Map classMap = new HashMap();
        ScBaseDictionary scBaseDictionary = null;
        ScBaseDictionary dbBaseDictionary = null;
        int classId = 0;
        for (String classInfo : classSets) {
            scBaseDictionary = new ScBaseDictionary();
            scBaseDictionary.setBdName(classInfo);
            scBaseDictionary.setBdType("class");
            scBaseDictionary.setBdValue(classInfo);
            dbBaseDictionary = scBaseDictionaryMapper.queryByTypeAndName(classInfo, "class");
            if(dbBaseDictionary == null) {
                classId = scBaseDictionaryMapper.insert(scBaseDictionary);
            }
            classMap.put(classInfo, scBaseDictionary.getBdId());
        }

        List<ScStudentInfo> inertList = new ArrayList<ScStudentInfo>();
        int stuPubId = 0;
        for (ScStudentInfo scStudentInfo : itemList) {
            stuPubId = (int) classMap.get(scStudentInfo.getStuPubName());
            scStudentInfo.setStuPubId(stuPubId);
            inertList.add(scStudentInfo);
        }

        int result = scStudentInfoMapper.batchInsert(inertList);

        List<ScUser> inertUserList = new ArrayList<ScUser>();
        List<ScUser> reslutUserList = new ArrayList<ScUser>();
        ScUser scUser = null;
        String password = null;
        for (ScStudentInfo scStudentInfo : inertList) {
            scUser = new ScUser();
            scUser.setUserType(1);//代表学生
            scUser.setLoginUser(scStudentInfo.getStuNo());
            password = getStringRandom(6);//六位随机密码
            scUser.setEnablePassword(password);
            scUser.setPassword(passwordService.encryptPassword(password));
            scUser.setUserId(scStudentInfo.getStuId());
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
