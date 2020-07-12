package miskyle.realsurvival.util;

public class RSEntry <R,S>{
	private R left;
	private S right;
	
	public RSEntry(R left,S right){
		this.left = left;
		this.right = right;
	}
	
	public void setLeft(R left) {
		this.left = left;
	}
	
	public void setRight(S right) {
		this.right = right;
	}
	
	public R getLeft() {
		return left;
	}
	
	public S getRight() {
		return right;
	}
}
