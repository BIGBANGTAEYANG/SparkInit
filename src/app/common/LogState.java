package app.common;

/**
 * 设置日志状态只能为成功或者失败
 * @author DEV031681
 *
 */
public enum LogState {
	成功(1), 失败(2);
	
	private int state;
	private LogState(int state) {
		this.state = state;
	}
	
	public LogState getLogState(int state) {
		for (LogState tt : LogState.values()) {
			if (state == tt.state) {
				return tt;
			}
		}
		return null;
	}
	
	public int getState() {
		return this.state;
	}

}
