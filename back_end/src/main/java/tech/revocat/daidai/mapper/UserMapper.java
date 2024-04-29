package tech.revocat.daidai.mapper;

import org.apache.ibatis.annotations.*;
import tech.revocat.daidai.entity.User;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE open_id = #{open_id}")
    User findByOpenID(@Param("open_id") String openID);

    @Update("UPDATE user SET username = #{username}, avatar = #{avatar} WHERE open_id = #{open_id}")
    void updateInfo(@Param("open_id") String openID, @Param("username") String username, @Param("avatar") String avatar);


    @Insert("INSERT INTO user (open_id) values(#{open_id}) ")
    void saveUser(@Param("open_id") String openID);
}
