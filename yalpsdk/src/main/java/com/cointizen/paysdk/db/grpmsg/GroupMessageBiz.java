package com.cointizen.paysdk.db.grpmsg;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Administrator
 *
 */
public class GroupMessageBiz {
	public int ID = -1;
	public String type;//消息类型
	public String account;//用户名
    public String message;	
    public int ico;//目前只能用系统自带头像
    public long time;//添加时间 
    public List<GroupMessageBiz> list=new ArrayList<GroupMessageBiz>();
}
