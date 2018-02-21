package stega;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/* 
 * Jorge Huete
 * 
 * Esta clase contiene los metodos necesarios para encriptar arrays de bytes
 * No puedo asegurar la integridad de la informacion o la seguridad de la encriptacion
 * 
 */

public class Crypto {

	private String formattedKey;
	private SecretKeySpec aesKey;
	private SecretKeySpec userKey;
	
	public byte[] encrypt(byte[] entrada, String clave){ //Encripta los bytes entrantes con AES usando el string recibido
	      
		 byte[] salida = new byte[0];
		 
		 userKey = this.getKey(clave);

		 
		 try {
			 
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.ENCRYPT_MODE, userKey);
	        salida = cipher.doFinal(entrada);
	        
		 }catch(Exception e) {
			 
			 throw new IllegalStateException("something is wrong", e);
			 
		 }
		 
		 return salida;
	        
	}
	 
	public byte[] decrypt(byte[] entrada, String clave) { //Desencripta los bytes entrantes con AES usando el string recibido
		
		byte[] salida = new byte[0];
		
		userKey = this.getKey(clave);
		
		try {
			
			Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.DECRYPT_MODE, userKey);
	        salida = cipher.doFinal(entrada);
			
		}catch(Exception e) {
			
			throw new IllegalStateException("something went wrong", e);
			
		}
		
		return salida;
		
	}
	
	public SecretKeySpec getKey(String clave) { //Recibe un string de menos de 64 ch y devuelve la clave AES resultante
		
		formattedKey = formatKey(clave);
		
		try {
			
	        aesKey = new SecretKeySpec(formattedKey.getBytes(), "AES");
			
		}catch(Exception e) {
			
			System.out.println("Exception in getKey");
			
		}
		
		return aesKey;
		
	}
	
	public String formatKey(String clave) { //Devuelve la clave con 1s detras para que mida 16, 32 o 64 ch
		
		if (clave.length() == 0) {
			System.out.println("Password length must be greater than 0 \n");
		}else if (clave.length() <= 16) {
			formattedKey = clave;
			for (int i=0; i< 16-clave.length(); i++) {
				formattedKey = formattedKey + "1";
				
			}
			//System.out.println(formattedKey + " " + formattedKey.length());
			
		}else if (clave.length() <= 32) {
			formattedKey = clave;
			for (int i=0; i< 32-clave.length(); i++) {
				formattedKey = formattedKey + "1";
				
			}
			//System.out.println(formattedKey + " " + formattedKey.length());
			
		}else if (clave.length() <= 64) {
			formattedKey = clave;
			for (int i=0; i< 64-clave.length(); i++) {
				formattedKey = formattedKey + "1";
				
			}
			//System.out.println(formattedKey + " " + formattedKey.length());
			
		}else if (clave.length() > 64) {
			System.out.println("Password lenght must be lower than 64 \n");
		}else {
			System.out.println("Unknown error \n");
		}
		
		return formattedKey;
		
	}
	
}
