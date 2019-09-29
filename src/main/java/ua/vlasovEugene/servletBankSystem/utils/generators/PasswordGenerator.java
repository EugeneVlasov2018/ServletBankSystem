package ua.vlasovEugene.servletBankSystem.utils.generators;

import org.apache.log4j.Logger;
import ua.vlasovEugene.servletBankSystem.entity.User;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.PasswordException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class PasswordGenerator {

    private static final Logger LOG = Logger.getLogger(PasswordGenerator.class);
    public static Map<String, String> createPassword(String password) {
        Map<String, String> result = new HashMap<>();
        String salt = generateSalt();

        result.put("salt", salt);
        result.put("password", workWithPassAndSalt(password, salt));

        LOG.info("gave the password and salt up");
        return result;
    }

    private static String generateSalt() {
        LOG.info("return random 16x String as key");
        return UUID.randomUUID().toString().substring(0, 16);
    }

    public static boolean checkPassword(User user, String innerPassword) {
        String userPassword = user.getUserPassword();
        String salt = user.getSalt();

        return Objects.equals(workWithPassAndSalt(innerPassword, salt), userPassword);
    }

    private static String workWithPassAndSalt(String password, String salt) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
            SecretKeySpec key = new SecretKeySpec(salt.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            return Arrays.toString(cipher.doFinal(password.getBytes()));


        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException |
                BadPaddingException |
                IllegalBlockSizeException |
                InvalidKeyException e) {
            LOG.error("get Exception in PasswordGenerator!!!", e);
            throw new PasswordException(e);
        }
    }
}
