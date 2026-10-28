package in.lms.cca.exceptions;

public class AccountInactiveException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AccountInactiveException(String message) {
        super(message);
    }

}
