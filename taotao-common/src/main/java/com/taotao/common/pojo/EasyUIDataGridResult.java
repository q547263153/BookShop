package com.taotao.common.pojo;
import java.io.Serializable;
import java.util.List;
/**
 * @author myPc
 *  total为总记录数
 *  rows为一页的展示的行数
 */
public class EasyUIDataGridResult implements Serializable {
	private Integer total;
	private List rows;
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	@Override
	public String toString() {
		return "EasyUIDataGridResult [total=" + total + ", rows=" + rows + "]";
	}
}
