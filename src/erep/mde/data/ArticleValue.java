package erep.mde.data;

public enum ArticleValue
{

	NONE(0), POPULAR(0), RARE(0), VERY_RARE(0);

	private int	value;

	private ArticleValue(int value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public static Integer getValue(String string) {
		try {
			return valueOf(string).value;
		} catch (Exception e) {
			return NONE.getValue();
		}
	}

}
