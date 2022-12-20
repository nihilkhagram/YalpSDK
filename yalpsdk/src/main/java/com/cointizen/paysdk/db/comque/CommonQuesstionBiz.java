package com.cointizen.paysdk.db.comque;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Administrator
 *
 */
public class CommonQuesstionBiz {
 public String CQID;
 public String TITLE;
 public String DESCRIPTION;
 public List<CommonQuesstionBiz> list=new ArrayList<CommonQuesstionBiz>();
@Override
public String toString() {
	return "CommonQuesstionBiz [CQID=" + CQID + ", TITLE=" + TITLE
			+ ", DESCRIPTION=" + DESCRIPTION + ", list=" + list + "]";
}
 
}
