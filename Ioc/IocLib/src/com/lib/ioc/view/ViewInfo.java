package com.lib.ioc.view;

/**
 * 父类是否和子类相同
 * @author fatenliyer
 * @date 2016-1-2
 */
final class ViewInfo {
	public int value;
	public int parentId;
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (null == o || getClass() != o.getClass()) {
			return false;
		}
		
		ViewInfo viewInfo = (ViewInfo) o;
		if (value != viewInfo.value) {
			return false;
		}
		return parentId == viewInfo.parentId;
	}
	
	@Override
	public int hashCode() {
		int result = value;
		result = 31 * result + parentId;
		return result;
	}
}
