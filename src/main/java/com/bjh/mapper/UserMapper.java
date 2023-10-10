package com.bjh.mapper;

import com.bjh.entity.User;
import com.bjh.page.Page;
import org.apache.ibatis.annotations.Param;
import sun.tracing.dtrace.DTraceProviderFactory;

import java.util.List;

//user_info表的mapper接口
public interface UserMapper {

    //根据账号查询用户信息

    public User selectUserByCode(String userCode);

    //查询用户行数
    public Integer selectUserRowCount(User user);
    //分页查询用户
    public List<User> selectUserByPage(@Param("page") Page page,@Param("user") User user);
    //添加用户
    public int insertUser(User user);
    //启用禁用用户状态
    public int updateUserState(String userState,Integer userId);
    //删除用户
    public int setIsDeleteUserByUid(List<Integer> userIdList);
    //修改用户
    public int updateUserNameByUid(User user);
    //重置密码
    public int updateUserPwdByUid(String pwd,Integer userId);

}
