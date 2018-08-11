package com.yuhao.sell.model;

import com.yuhao.sell.dto.OrderDTO;

import java.util.List;

public class TransData {
	public List<OrderMaster> list;
	public boolean flag;

	public void setList(List<OrderMaster> list) {
		this.list = list;
	}

	public void setFlag(boolean flag){
		this.flag = flag;
	}
}
