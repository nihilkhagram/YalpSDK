package com.cointizen.paysdk.db;
/**
 * 留言信息
 * @author Administrator
 *
 */
public class MessageInfo {
	public int ID = -1;
	public String type;//消息类型
	public String account;//用户名
    public String message;	
    public int ico;//目前只能用系统自带头像
    public long time;//添加时间 
}
