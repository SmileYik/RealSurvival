package com.outlook.schooluniformsama.util;

import java.util.Iterator;

public class HashMap<K, V> extends java.util.HashMap<K, V>{

	/**
	 * Override remove method and replace method
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public V remove(Object key) {
		Iterator<Entry<K, V>> entry=this.entrySet().iterator();
		while(entry.hasNext()){
			Entry<K,V> e=entry.next();
			if(e.getKey()==key){
				entry.remove();
				break;
			}
		}
		return super.remove(key);
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		Iterator<Entry<K, V>> entry=this.entrySet().iterator();
		while(entry.hasNext()){
			Entry<K,V> e=entry.next();
			if(e.getKey()==key&&e.getValue()==value){
				entry.remove();
				break;
			}
		}
		return super.remove(key, value);
	}
}
