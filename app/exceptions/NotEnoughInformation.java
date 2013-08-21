package exceptions;

public class NotEnoughInformation extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7978166321444260519L;

	public NotEnoughInformation(String msg) {
		super(msg);
	}

	public NotEnoughInformation() {
	}

}
