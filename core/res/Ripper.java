package stega.core.res;

import stega.core.res.ByteHandlingException;

//Riiiiiiiip!

/* Jorge Huete - Ripper
 * 
 * Esta clase contiene metodos para cortar, recortar e introducir bytes dentro de bytes
 * Esta es la base del recorte del archivo origen
 * 
 */


public class Ripper {

	static byte ripByte(byte original, int S, int F) { //"Corta" los bytes entre S y F de un byte TODO que compruebe que no es mayor de 8
		
		byte res = original;
		
			for(int i = S; i>=F; i--) {
				res = (byte) (res & ~ (1 << i)); //bit i a 0
			}
		
		return res;
	}
	
	static byte insertBits(byte bigBoss, byte smol, int N) { //Inserta los ultimos N bit de smol en bigBoss
		
		bigBoss = ripByte(bigBoss, N-1, 0);
		smol = ripByte(smol, 7 ,N);
		
		bigBoss = (byte) (bigBoss | smol);
		
		return bigBoss;
	}
	
	public static byte[] insertBytes(byte[] vessel, byte payload, int N) throws ByteHandlingException { 	//Inserta el byte payload recortado en vessel
																						//N es el tamaño de recorte
		if (N != 1 && N != 2 && N != 4) {
			throw new ByteHandlingException("Non divisible number");
		}else if(8/N != vessel.length) {
			throw new ByteHandlingException("Wrong byte array length");
		}else {
			for (int i=0; i< vessel.length; i++) {
				vessel[i]=insertBits(vessel[i], (byte)(payload >> (8-(i+1)*N)), N);
			}
		}
		
		return vessel;
	}
	
	public static byte[] recoverBytes(byte[] vessel) {
		byte[] message = new byte[(int) Math.floor((float)vessel.length/4)];
		
		byte[] cVessel = new byte[ vessel.length-vessel.length%4]; //TODO añadir if para evitar hacer esto si ya sale perfecto
		for(int i = 0; i < vessel.length-vessel.length%4; i++) {
			cVessel[i] = (byte) (vessel[i] & (byte) 3);  // 3 = 0000 0011, con & ponemos todos los bits a 0 salvo los 2 menos sign
		}
		vessel = cVessel;
		
		//System.out.println(Core.toBinary(vessel));
		//System.out.println(vessel.length);
		
		for(int i = 0; i < vessel.length; i++) {
			message[i/4]=(byte) (message[i/4]<<2); 			//Desplazamos 2 a la izquierda XXXXXXYY -> XXXXYY00
			message[i/4]=(byte) (message[i/4] | vessel[i]); //XXXXYY00 | 000000AA -> XXXXYYAA "magia negra"
		}
		
		return message;
	}

	public static byte[] insertByteArray(byte[] matOrigen, byte[] paquete) throws ByteHandlingException{
		try{
			for(int i = 0; i< paquete.length; i++) {
				byte[] hiddenBytes = Ripper.insertBytes(new byte[] {matOrigen[i*4], matOrigen[i*4+1], matOrigen[i*4+2], matOrigen[i*4+3]}, paquete[i], 2);
				matOrigen[i*4]=hiddenBytes[0];
				matOrigen[i*4+1]=hiddenBytes[1];
				matOrigen[i*4+2]=hiddenBytes[2];
				matOrigen[i*4+3]=hiddenBytes[3];
			}
		}catch (NullPointerException e1) {
			throw new ByteHandlingException("File to big, Out of bounds");
		}
		return matOrigen;

	}
	

}
