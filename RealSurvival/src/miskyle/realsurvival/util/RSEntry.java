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
	
	public void set(R left,S right) {
		this.left = left;
		this.right = right;
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

	@Override
	public String toString() {
		return "RSEntry [left=" + left + ", right=" + right + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RSEntry<?, ?> other = (RSEntry<?, ?>) obj;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (right == null) {
			if (other.right != null)
				return false;
		} else if (!right.equals(other.right))
			return false;
		return true;
	}
}
