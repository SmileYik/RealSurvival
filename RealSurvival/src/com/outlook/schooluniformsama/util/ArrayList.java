package com.outlook.schooluniformsama.util;

import java.util.Iterator;

public class ArrayList<E> extends java.util.ArrayList<E>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public E remove(int index) {
		int index2=0;
		Iterator<E> ite=iterator();
		while(ite.hasNext()){
			ite.next();
			if(index==index2++)
				ite.remove();
		}
		// TODO Auto-generated method stub
		return super.remove(index);
	}
	
	@Override
	public boolean remove(Object o) {
		Iterator<E> ite=iterator();
		while(ite.hasNext()){
			if(o.equals(ite.next()))ite.remove();
		}
		return super.remove(o);
	}
}
