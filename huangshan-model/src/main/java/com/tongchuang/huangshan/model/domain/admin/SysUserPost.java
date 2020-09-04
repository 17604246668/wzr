package com.tongchuang.huangshan.model.domain.admin;


import lombok.ToString;

/**
 * 用户和岗位关联 sys_user_post
 * 
 * @author fangwenzao
 */
@ToString
public class SysUserPost
{
    /** 用户ID */
    private Long userId;
    
    /** 岗位ID */
    private String postId;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
