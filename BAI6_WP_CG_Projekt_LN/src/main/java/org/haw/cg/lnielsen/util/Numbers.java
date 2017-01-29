package org.haw.cg.lnielsen.util;

public final class Numbers {
	private Numbers() {}
	
	public static IntRequirement require(int x) {
		return new IntRequirement(x);
	}
	
	public static LongRequirement require(long x) {
		return new LongRequirement(x);
	}
	
	public static DoubleRequirement require(double x) {
		return new DoubleRequirement(x);
	}
	
	public static class IntRequirement {
		private int _number;
		
		private IntRequirement(int number) {
			_number = number;
		}
		
		public int equal(int number, String errorMessage) {
			if(_number != number) {
				throw new IllegalArgumentException(errorMessage);
			}
			return _number;
		}
		
		public int notEqual(int number, String errorMessage) {
			if(_number == number) {
				throw new IllegalArgumentException(errorMessage);
			}
			return _number;
		}
		
		public int greaterThan(int number, String errorMessage) {
			if(_number <= number) {
				throw new IllegalArgumentException(errorMessage);
			}
			return _number;
		}
		
		public int greaterThanOrEqual(int number, String errorMessage) {
			if(_number < number) {
				throw new IllegalArgumentException(errorMessage);
			}
			return _number;
		}
		
		public int lessThan(int number, String errorMessage) {
			if(_number >= number) {
				throw new IllegalArgumentException(errorMessage);
			}
			return _number;
		}
		
		public int lessThanOrEqual(int number, String errorMessage) {
			if(_number > number) {
				throw new IllegalArgumentException(errorMessage);
			}
			return _number;
		}
	}
	
	public static class LongRequirement {
		private long _number;
		
		private LongRequirement(long number) {
			_number = number;
		}
		
		public long equal(long number, String errorMessage) {
			if(_number != number) {
				throw new IllegalArgumentException(errorMessage);
			}
			return _number;
		}
		
		public long notEqual(long number, String errorMessage) {
			if(_number == number) {
				throw new IllegalArgumentException(errorMessage);
			}
			return _number;
		}
		
		public long greaterThan(long number, String errorMessage) {
			if(_number <= number) {
				throw new IllegalArgumentException(errorMessage);
			}
			return _number;
		}
		
		public long greaterThanOrEqual(long number, String errorMessage) {
			if(_number < number) {
				throw new IllegalArgumentException(errorMessage);
			}
			return _number;
		}
		
		public long lessThan(long number, String errorMessage) {
			if(_number >= number) {
				throw new IllegalArgumentException(errorMessage);
			}
			return _number;
		}
		
		public long lessThanOrEqual(long number, String errorMessage) {
			if(_number > number) {
				throw new IllegalArgumentException(errorMessage);
			}
			return _number;
		}
	}
	
	public static class DoubleRequirement {
		private double _number;
		
		private DoubleRequirement(double number) {
			_number = number;
		}
		
		public double equal(double number, String errorMessage) {
			if(_number != number) {
				throw new IllegalArgumentException(errorMessage);
			}
			return _number;
		}
		
		public double notEqual(double number, String errorMessage) {
			if(_number == number) {
				throw new IllegalArgumentException(errorMessage);
			}
			return _number;
		}
		
		public double greaterThan(double number, String errorMessage) {
			if(_number <= number) {
				throw new IllegalArgumentException(errorMessage);
			}
			return _number;
		}
		
		public double greaterThanOrEqual(double number, String errorMessage) {
			if(_number < number) {
				throw new IllegalArgumentException(errorMessage);
			}
			return _number;
		}
		
		public double lessThan(double number, String errorMessage) {
			if(_number >= number) {
				throw new IllegalArgumentException(errorMessage);
			}
			return _number;
		}
		
		public double lessThanOrEqual(double number, String errorMessage) {
			if(_number > number) {
				throw new IllegalArgumentException(errorMessage);
			}
			return _number;
		}
	}
}
