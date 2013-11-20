
package cl.geoconflict.utils;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.util.ByteSource;

public class HashUtil{
	
	public static ByteSource getSalt(){
	    return new SecureRandomNumberGenerator().nextBytes();
	}
}
