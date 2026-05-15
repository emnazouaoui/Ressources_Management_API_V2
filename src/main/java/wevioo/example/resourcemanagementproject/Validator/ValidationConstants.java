package wevioo.example.resourcemanagementproject.Validator;

import java.util.regex.Pattern;

public class ValidationConstants {

    // ─── Error codes ───────────────────────────────────────
    public static final String ERROR_CODE_REQUIRED = "REQUIRED";
    public static final String ERROR_CODE_INVALID  = "INVALID";

    // ─── Error messages ────────────────────────────────────
    public static final String ERROR_MISSING_REQUIRED_DATA = "This field is required";
    public static final String ERROR_EMAIL_INVALID         = "Invalid email format";
    public static final String ERROR_PHONE_INVALID         = "Invalid phone number (8-15 digits, optional + prefix)";
    public static final String ERROR_PASSWORD_INVALID      = "Password must be at least 8 chars with 1 uppercase, 1 lowercase, 1 digit and 1 special character";
    public static final String ERROR_USERNAME_MIN_LENGTH   = "Username must be at least 3 characters";
    public static final String ERROR_USERNAME_MAX_LENGTH   = "Username must not exceed 50 characters";
    public static final String ERROR_NAME_INVALID          = "Name can only contain letters, numbers, spaces, hyphens and apostrophes";

    // ─── Field names ───────────────────────────────────────
    public static final String FIELD_EMAIL      = "email";
    public static final String FIELD_PHONE      = "phone";
    public static final String FIELD_PASSWORD   = "password";
    public static final String FIELD_USERNAME   = "username";
    public static final String FIELD_FIRST_NAME = "firstName";
    public static final String FIELD_LAST_NAME  = "lastName";

    // ─── Patterns ──────────────────────────────────────────
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"
            //✅ user@gmail.com ✅ test.user@company.tn
            //❌ user@ ❌ @gmail.com ❌ user@gmail
            //email format
    );

    public static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[+]?[0-9]{8,15}$"
            //✅ 12345678 ✅ +21612345678
            //❌ 123 (trop court) ❌ +216abc (lettres interdites)
            // phone number (8-15 digits, optional + prefix)
    );

    public static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
            //Lookahead (?=...) = condition qui vérifie sans consommer les caractères
            //
            //✅ Password1@ ✅ Secure#99
            //❌ password ❌ PASSWORD1 ❌ Pass1
            //Password must be at least 8 chars with 1 uppercase, 1 lowercase, 1 digit and 1 special character
    );

    public static final Pattern NAME_PATTERN = Pattern.compile(
            "^[a-zA-ZÀ-ÿ0-9\\s'\\-_.]{1,255}$"
            //✅ Mohamed Ali ✅ Société Générale ✅ O'Brien
            //❌ Name@123 (@ interdit) ❌ `` (vide interdit)
            //Name can only contain letters, numbers, spaces, hyphens and apostrophes
    );

    // ─── Taille ────────────────────────────────────────────
    public static final int USERNAME_MIN_LENGTH = 3;
    public static final int USERNAME_MAX_LENGTH = 50;
    public static final int NAME_MAX_LENGTH     = 255;
    public static final int PASSWORD_MIN_LENGTH = 8;

}
