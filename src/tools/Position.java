/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: tools/Position.java 2015-03-11 buixuan.
 * ******************************************************/
package tools;

public class Position {
  public double x,y;
  public Position(double x, double y){
    this.x=x;
    this.y=y;
  }

	//Return	> 0 if the x of object in parameter is greater than this position
	//		< 0 if the x of object in parameter is less than this position
	//		= 0 if x are equals
	public double compareX (Position position) {
		return position.x - this.x;
	}


	//Return	> 0 if the y of object in parameter is greater than this position
	//		< 0 if the y of object in parameter is less than this position
	//		= 0 if y are equals
	public double compareY (Position position) {
		return position.y - this.y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Position other = (Position) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}
}
