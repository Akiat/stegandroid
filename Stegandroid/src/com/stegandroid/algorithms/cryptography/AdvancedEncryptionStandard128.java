package com.stegandroid.algorithms.cryptography;

import com.stegandroid.error.ErrorManager;


public class AdvancedEncryptionStandard128 extends AdvancedEncryptionStandard {

	private final int KEY_LENGTH = 16;
	
	public AdvancedEncryptionStandard128() {
	}

	@Override
	public byte[] encrypt(byte[] message, byte[] key) {
		boolean valid = true;
		
		if (message == null) {
			ErrorManager.getInstance().addErrorMessage("[AES 128]: Message to encrypt is null");
			valid = false;
		}
		if (key == null) {
			ErrorManager.getInstance().addErrorMessage("[AES 128]: Key is null");
			valid = false;
		} else if (!checkKeyLength(key.length)) {
			ErrorManager.getInstance().addErrorMessage("[AES 128]: Key must be " + KEY_LENGTH + " length");			
			valid = false;
		} 
		return (valid ? super.encryptionProcess(AESType.AES_128, message, key) : null);
	}

	@Override
	public byte[] decrypt(byte[] cipher, byte[] key) {
		boolean valid = true;
		
		if (cipher == null) {
			ErrorManager.getInstance().addErrorMessage("[AES 128]: Cipher to decrypt is null");
			valid = false;
		}
		if (key == null) {
			ErrorManager.getInstance().addErrorMessage("[AES 128]: Key is null");
			valid = false;
		} else if (!checkKeyLength(key.length)) {
			ErrorManager.getInstance().addErrorMessage("[AES 128]: Key must be " + KEY_LENGTH + " length");			
			valid = false;
		} 
		return (valid ? super.decryptionProcess(AESType.AES_128, cipher, key) : null);
	}

	private boolean checkKeyLength(int keylength) {
		return keylength == KEY_LENGTH;
	}
}
