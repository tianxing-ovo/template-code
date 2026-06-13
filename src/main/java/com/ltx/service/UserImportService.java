package com.ltx.service;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.ltx.entity.po.User;
import com.ltx.exception.CustomException;
import com.ltx.listener.UserListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 用户导入服务类
 *
 * @author tianxing
 */
@Service
@Slf4j
public class UserImportService {

    /**
     * 导入用户数据并保存至数据库
     *
     * @param file 文件
     * @return 用户列表
     */
    @Transactional(rollbackFor = Exception.class)
    public List<User> importUsers(MultipartFile file) {
        UserListener userListener = new UserListener();
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
            EasyExcel.read(inputStream, User.class, userListener).sheet().doRead();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new CustomException(500, e.getMessage());
        }
        List<User> userList = userListener.getUserList();
        if (!userList.isEmpty()) {
            log.info("开始批量导入 {} 条用户数据至数据库", userList.size());
            Db.saveBatch(userList);
            log.info("用户数据成功批量保存至数据库");
        }
        return userList;
    }
}
